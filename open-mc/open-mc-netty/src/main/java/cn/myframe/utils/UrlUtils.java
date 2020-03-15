package cn.myframe.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 处理URL地址工具类
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月26日 上午9:06:01
 */
public class UrlUtils {
	
	/**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param strUrlParam url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String strUrlParam) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

}
