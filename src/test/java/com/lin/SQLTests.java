package com.lin;

import com.lin.mapper.UserMapper;
import com.lin.pojo.User;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;

@SpringBootTest
class SQLTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testXSQL3333() {
        String username = "admin' or '1' = '1' limit 1 #";           //admin' or '1' = '1' limit 1 #
        String password = "123456";           //123456

        //select ... from user where username = 'admin' or '1' = '1' # and password = '123456'
        //select * from user where username = '${username}' AND password = '${password}'
        User user1 = userMapper.selectUser2(username, password);
        if(user1 != null){
            System.out.println("【提示】登录成功！");
        }else{
            System.out.println("【错误】登录失败！");
        }
        System.out.println(user1);
    }

    @Test
    void testUtils() throws Exception{
        String username = "admin' or '1' = '1' limit 1 #";
        String password = "123456";

        //1、注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2、获取连接对象
        String url = "jdbc:mysql://127.0.0.1:3306/app_safe";
        String dbuser = "root";
        String pssword = "root";
        Connection connection = DriverManager.getConnection(url, dbuser, pssword);
        //3、获取发送SQL语句的对象
        Statement statement =connection.createStatement();
        //编写SQL语句
        String sql = "SELECT * FROM user WHERE username='"+username+"' AND pssword = '"+password;

//        String s = StringEscapeUtils.escapeSql(sql);

        //4、执行SQL语句
        ResultSet resultSet=statement.executeQuery(sql);

        if(resultSet.next()){
            System.out.println("【提示】登录成功！");
        }else{
            System.out.println("【错误】登录失败！");
        }
    }

    @Test
    void testPrepareStatement() throws Exception{
        String username = "admin' or '1' = '1' limit 1 #";
        String password = "123456";

        //1、注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2、获取连接对象
        String url = "jdbc:mysql://127.0.0.1:3306/app_safe";
        String dbuser = "root";
        String pssword = "root";
        Connection connection = DriverManager.getConnection(url, dbuser, pssword);
        //3、获取发送SQL语句的对象
        String sql = "select * from user where username = ? and password=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 4、绑定参数，有多少个?绑定多少个参数值
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        // 5、执行SQL语句，并处理结果
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            System.out.println("【提示】登录成功！");
        }else{
            System.out.println("【错误】登录失败！");
        }
    }


}
