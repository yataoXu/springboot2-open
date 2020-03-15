package cn.myframe.csdn.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 点赞
 *
 * @author huanghuapeng create at 2019/5/5 17:07
 * @version 1.0.0
 */
public class VoteHandlerImpl extends AbsRequestHandler<Object, Object> {

    @Override
    public Object parse(String html, String reqUrl) {

        JSONObject value = (JSONObject) JSON.parse(html);
        System.out.println(html + " ，点赞成功: " + String.valueOf(value.get("status")).equals("true"));

        return html;
    }
}
