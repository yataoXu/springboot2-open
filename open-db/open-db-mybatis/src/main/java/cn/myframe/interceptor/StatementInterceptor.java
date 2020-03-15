package cn.myframe.interceptor;

import cn.myframe.utils.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 14:22
 */
@Intercepts(
        {@Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
                )
        })
/*@Component*/
@Slf4j
public class StatementInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler =
                (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement =
                (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        //只拦截select方法
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        //获取到sql
        String originalSql = boundSql.getSql();
        //可以对originalSql进行改写
        log.error("拦截器StatementInterceptor:"+originalSql);
        metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
        Object parameterObject = boundSql.getParameterObject();
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
