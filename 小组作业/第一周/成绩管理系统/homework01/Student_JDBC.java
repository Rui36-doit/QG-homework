package homework01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student_JDBC {
    static Connectpool connectpool;

    //添加数据进数据库
    public static void add(String sql, Student stu) throws SQLException {
        Connection connection = connectpool.getConnection();
        PreparedStatement prs = connection.prepareStatement(sql);
        try {
            connection.setAutoCommit(false);
            prs.setString(1, stu.name);
            prs.setInt(2, stu.age);
            prs.setString(3, stu.password);
            prs.setString(4, stu.phonenumber);
            prs.setInt(5, stu.classnum);
            prs.executeUpdate();
            connection.commit();
        }catch (Exception e) {
            //报错
            System.out.println("操作有误");
            connection.rollback();
        }finally {
            //归还连接
            prs.close();
            connectpool.backConnection(connection);
        }
    }
}
