package cn.myframe.support;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: ynz
 * @Date: 2019/6/28/028 9:38
 * @Version 1.0
 */
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        //MD5Util.encode((String) charSequence);
        System.out.println(charSequence.toString());
        return null;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        System.out.println(charSequence);
        System.out.println(s);
        return true;
    }
}
