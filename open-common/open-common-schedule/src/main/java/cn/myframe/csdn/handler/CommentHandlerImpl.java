package cn.myframe.csdn.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author huanghuapeng create at 2019/5/5 19:00
 * @version 1.0.0
 */
public class CommentHandlerImpl extends AbsRequestHandler<Object, Object> {
    @Override
    public Object parse(String html, String reqUrl) {

        JSONObject value = (JSONObject) JSON.parse(html);

        if (String.valueOf(value.get("result")).equals("1")) {
            System.out.println("Send comment to: " + reqUrl + " is success");
        } else {
            String message = String.valueOf(value.get("content"));
            System.out.println("Oops,we can't send comment to: " + reqUrl + "  because of: " + message);
        }
        return null;
    }
}
