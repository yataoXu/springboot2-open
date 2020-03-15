package cn.myframe.service.callback;

/**
 * @Author: ynz
 * @Date: 2019/1/3/003 15:13
 * @Version 1.0
 */
public interface CallbackService {
    void addListener(String key, CallbackListener listener);
}
