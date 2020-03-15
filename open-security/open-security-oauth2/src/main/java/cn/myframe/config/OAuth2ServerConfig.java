package cn.myframe.config;

import cn.myframe.exception.CustomOAuth2WebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @Author: ynz
 * @Date: 2019/6/28/028 14:52
 * @Version 1.0
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomOAuth2WebResponseExceptionTranslator customOAuth2WebResponseExceptionTranslator;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)  {
        // 自定义异常处理端口
        oauthServer.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        oauthServer.realm("oauth2-resources") // code授权添加
                .tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()") // allow check token
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                // 允许 GET、POST 请求获取 token，即访问端点：oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        // 要使用refresh_token的话，需要额外配置userDetailsService
       // endpoints.userDetailsService(userDetailsService);
        // 处理 ExceptionTranslationFilter 抛出的异常
        endpoints.exceptionTranslator(customOAuth2WebResponseExceptionTranslator);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)  {
        try {
            clients.inMemory().withClient("demoApp").secret(bCryptPasswordEncoder.encode("demoAppSecret"))
                    .redirectUris("http://baidu.com")// code授权添加
                    .authorizedGrantTypes("authorization_code", "client_credentials", "password", "refresh_token")
                    .scopes("all").resourceIds("oauth2-resource").accessTokenValiditySeconds(1200)
                    .refreshTokenValiditySeconds(50000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
