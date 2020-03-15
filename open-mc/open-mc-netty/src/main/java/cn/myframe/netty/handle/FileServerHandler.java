package cn.myframe.netty.handle;

import cn.myframe.properties.NettyFileProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author ynz
 * @version 创建时间：2018/6/26
 * @email ynz@myframe.cn
 */
@Component
@Slf4j
@ChannelHandler.Sharable //@Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用
public class FileServerHandler extends ChannelInboundHandlerAdapter {

   // private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    //文件存放路径
    @Value("${netty.file.path:}")
    String path;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try{
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest req = (FullHttpRequest) msg;
                if(req.method() != HttpMethod.GET) {
                    sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
                    return;
                }
                String url = req.uri();
                File file = new File(path + url);
                if(file.exists()){
                    if(file.isDirectory()){
                        if(url.endsWith("/")) {
                            sendListing(ctx, file);
                        }else{
                            sendRedirect(ctx, url + "/");
                        }
                        return;
                    }else {
                        transferFile( file,  ctx);
                    }
                }else{
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                }
            }
        }catch(Exception e){
            log.error("Exception:{}",e);
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
        }
    }

    /**
     * 传输文件
     * @param file
     * @param ctx
     */
    private void transferFile(File file, ChannelHandlerContext ctx){
        try{
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            long fileLength = randomAccessFile.length();
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, fileLength);
            ctx.write(response);
            ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
            addListener( sendFileFuture);
            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e){
            log.error("Exception:{}",e);
        }
    }

    /**
     * 监听传输状态
     * @param sendFileFuture
     */
    private void addListener( ChannelFuture sendFileFuture){
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                @Override
                public void operationComplete(ChannelProgressiveFuture future)
                        throws Exception {
                    log.debug("Transfer complete.");
                }
                @Override
                public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                    if(total < 0){
                        log.debug("Transfer progress: " + progress);
                    }else{
                        log.debug("Transfer progress: " + progress + "/" + total);
                    }
                }
        });
    }


    /**
     * 请求为目录时，显示文件列表
     * @param ctx
     * @param dir
     */
    private static void sendListing(ChannelHandlerContext ctx, File dir){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");

        String dirPath = dir.getPath();
        StringBuilder buf = new StringBuilder();

        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append("目录:");
        buf.append("</title></head><body>\r\n");

        buf.append("<h3>");
        buf.append(dirPath).append(" 目录：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li>链接：<a href=\" ../\")..</a></li>\r\n");
        for (File f : dir.listFiles()) {
            if(f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            /*if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }*/
            buf.append("<li>链接：<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf,CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 跳转链接
     * @param ctx
     * @param newUri
     */
    private static void sendRedirect(ChannelHandlerContext ctx, String newUri){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 失败响应
     * @param ctx
     * @param status
     */
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


}
