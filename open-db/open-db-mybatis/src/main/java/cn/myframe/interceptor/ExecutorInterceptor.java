package cn.myframe.interceptor;

import cn.myframe.utils.ExecutorPluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 10:45
 */
@Intercepts({
        @Signature(
        type= Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
})
@Slf4j
/*@Component*/
public class ExecutorInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String sql = ExecutorPluginUtils.getSqlByInvocation(invocation);
        //可以对sql重写
        log.error("拦截器ExecutorInterceptor:"+sql);
        //sql = "SELECT id from BUS_RECEIVER where id = ? ";
        ExecutorPluginUtils.resetSql2Invocation( invocation,  sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
