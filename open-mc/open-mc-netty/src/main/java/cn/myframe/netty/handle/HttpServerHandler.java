package cn.myframe.netty.handle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.myframe.mvc.annotation.ActionBody;
import cn.myframe.netty.wrap.Action;
import cn.myframe.netty.wrap.HttpRequestWrapper;
import cn.myframe.netty.wrap.HttpResponseWrapper;
import cn.myframe.utils.ClassLoaderUtil;
import cn.myframe.utils.UrlUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ynz
 * @version 创建时间：2018/6/21
 * @email ynz@myframe.cn
 */
@Component
@Slf4j
@ChannelHandler.Sharable //@Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用
public class HttpServerHandler  extends ChannelInboundHandlerAdapter {

    private static Map<String, Action> actionMapping = null;

    public Map<String, Action> getActionMapping(){
        if(actionMapping == null){
            return actionMapping = ClassLoaderUtil.buildActionMapping();
        }
        return actionMapping;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest req = (FullHttpRequest) msg;
                if (HttpUtil.is100ContinueExpected(req)) {
                    ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
                }
                //封装请求和响应
                HttpRequestWrapper httpRequestWrapper = buildRequestWraper(req);
                //建造netty响应
                HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
                Action action = getActionMapping().get(httpRequestWrapper.getUri());
                if(action != null){
                	Object responseBody = null;
                    Object object = action.getMethod().invoke(action.getBean(),buildArgs(
                            action, httpRequestWrapper.getParams(),httpRequestWrapper));

                    if(StringUtils.isNotEmpty(action.getResponseType()) &&
                    		action.getResponseType().equals("JSON")){
                    	responseBody = JSON.toJSONString(object);
                    }else{
                    	responseBody = object;
                    }
                    httpResponseWrapper.write(object.toString().getBytes("UTF-8"));
                }
                FullHttpResponse response = buildResponse(httpResponseWrapper);
                boolean keepAlive = HttpUtil.isKeepAlive(req);
                if (!keepAlive) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                }
            }
            //负责显式释放与池的ByteBuf实例相关联的内存,SimpleChannelInboundHandler会自动释放资源，因此无需显式释放
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
        	log.error("system exception:{}", e);
        }
    }

    /**
     * 构建请求对象
     * @param req
     * @return
     */
    private HttpRequestWrapper buildRequestWraper(FullHttpRequest req) {
        HashMap<String, String> headersMap = new HashMap<String, String>(16);
        for (Map.Entry<String, String> entry : req.headers()) {
            headersMap.put(entry.getKey(), entry.getValue());
        }
        byte[] content = new byte[req.content().readableBytes()];
        req.content().readBytes(content);
        String url = req.uri();
        String params = "";
        if(url.indexOf("?")>0){
            String[] urls = url.split("\\?");
            url = urls[0];
            params = urls[1];
        }
        return new HttpRequestWrapper(req.method().name(), url, headersMap, content, params);
    }

    /**
     * 构建响应对象
     * @param httpResponseWrapper
     * @return
     */
    private FullHttpResponse buildResponse(HttpResponseWrapper httpResponseWrapper) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.valueOf(httpResponseWrapper.getStatusCode()), Unpooled.wrappedBuffer(httpResponseWrapper.content()));
        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        headers.set(HttpHeaderNames.CONTENT_TYPE, new AsciiString("application/json; charset=utf-8"));
        for (Map.Entry<String, String> entry : httpResponseWrapper.headers().entrySet()) {
            headers.set(entry.getKey(), entry.getValue());
        }
        return response;
    }


    /**
     * 构建请求参数
     * @param action
     * @param urlParams
     * @param httpRequestWrapper
     * @return
     */
    public Object[] buildArgs(Action action,String urlParams,HttpRequestWrapper httpRequestWrapper){
        if(action == null){
            return null;
        }
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        //获取处理方法的参数
        String[] params = u.getParameterNames(action.getMethod());
        Object[] objects = new Object[params.length];
        Map<String,String> paramMap = new HashMap<>();
        try{
            if(StringUtils.isNotEmpty(urlParams)){
                paramMap = UrlUtils.URLRequest(urlParams);
            }
            if( httpRequestWrapper.content()!=null){
                Parameter[] parameters = action.getMethod().getParameters();
                for(Parameter parameter : parameters){
                    Annotation annotation = parameter.getAnnotation(ActionBody.class);
                    if(annotation == null){
                        continue;
                    }
                    int index = Integer.parseInt(parameter.getName().substring(3));
                    paramMap.put(params[index],new String(httpRequestWrapper.content(),"UTF-8"));
                }
            }

            for( int i = 0 ;i<params.length; i++){
                final int flag = i;
                paramMap.forEach((k,v)->{
                    if(k.equals(params[flag])){
                        objects[flag] = v;
                    }
                });
            }
        }catch(Exception e){
        	log.error(e.getMessage());
        }
        return objects;
    }

    @Override
    @SentinelResource("hello")
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	ctx.flush();
    }
    
    /**
	 * 当客户端断开连接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
	}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
