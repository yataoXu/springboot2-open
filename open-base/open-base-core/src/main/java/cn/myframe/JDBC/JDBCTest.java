package cn.myframe.JDBC;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author: ynz
 * @Date: 2019/1/19/019 14:14
 * @Version 1.0
 */
public class JDBCTest {

    /**
     * 数据库相关参数
     */
    //这是驱动名称，此例子中我们加载的是mysql的驱动，在之前需要导入mysql的驱动jar包
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    //连接数据库的url，各个数据库厂商不一样，此处为mysql的;后面是创建的数据库名称
    public static final String JDBC_URL = "jdbc:mysql://10.10.2.139:3306/dbshared";

    //连接数据库所需账户名
    public static final String JDBC_USERNAME = "slave";

    //用户名对应的密码，我的mysql密码是123456
    public static final String JDBC_PASSWORD = "Rojao@123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        //加载Driver类，注册数据库驱动
        Class.forName(JDBC_DRIVER);
        //通过DriverManager,使用url,username,password建立连接
        Connection connection = DriverManager.getConnection(JDBC_URL,JDBC_USERNAME,JDBC_PASSWORD);
        //通过connection使用sql语句打开PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement("insert into bus_group(id,group_name) value (?,?)");
        //执行sql语句返回resultSet
        preparedStatement.setLong(1,1000);
        preparedStatement.setString(2,"group1000");
        int count = preparedStatement.executeUpdate();
        //对结果resultSet进行处理；
        System.out.println(count);
        //释放资源
        preparedStatement.close();
        connection.close();

    }
}
