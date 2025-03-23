package homework01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connectpool {
    LinkedList<Connection> lists = new LinkedList<>();
    String database;

    public Connectpool(String database){
        this.database = database;
        System.out.println("获取数据库");
        try {
            for(int i = 0; i < 5; i++){
                Connection connection = start();
                lists.add(connection);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //创造连接
    public Connection start() throws ClassNotFoundException, SQLException {
        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //获取用户信息
        String url = "jdbc:mysql://localhost:3306/" + database + "?useUnicode=true&characterEncoding=utf8&useSSL=true";;
        String username = "root";
        String password = "5115848wr";
        //连接获取数据库对象
        return DriverManager.getConnection(url, username, password);
    }


    //在队列里面获取连接
    public Connection getConnection(){
        if(lists.isEmpty()){
            System.out.println("没有连接可用了");
            return null;
        }
        return lists.removeFirst();
    }

    //返回连接给队列
    public void backConnection(Connection connection){
        if(lists.size() == 5){
            System.out.println("队列已满");
            return;
        }
        lists.add(connection);
    }

    //关闭所有的链接
    public void closeALL() throws SQLException {
        if(lists.size() != 5){
            System.out.println("还有连接未归还");
            return;
        }
        for(int i = 0; i < 5; i++){
            Connection connection = lists.remove();
            connection.close();
        }
    }


}
