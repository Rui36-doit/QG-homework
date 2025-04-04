package Scoer;

import java.sql.*;

public class JDBCUtil {
    static Connectpool connectpool;

    //更新数据进数据库
    public static int update(String sql, Object... parms) throws SQLException {
        Connection connection = connectpool.getConnection();
        PreparedStatement pres = connection.prepareStatement(sql);
        int t = 0;
        try {
            connection.setAutoCommit(false);
            for(int i = 0; i < parms.length; i++){
                if(parms[i].getClass() == Integer.class){
                    int n = (int)parms[i];
                    pres.setInt(i + 1, n);
                }else{
                    pres.setString(i + 1, (String)parms[i]);
                }
            }
            t = pres.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("操作有误");
            connection.rollback();
        } finally {
            pres.close();
            connectpool.backConnection(connection);
            //connection.close();
            return t;
        }
    }

    //查询数据
    public static Box search(String sql, Object... parms) throws SQLException {
        Connection connection = connectpool.getConnection();
        //连接获取数据库对象
        PreparedStatement pres = connection.prepareStatement(sql);
        ResultSet result = null;
            connection.setAutoCommit(false);
            if(parms.length != 0) {
                for (int i = 0; i < parms.length; i++) {
                    if (parms[i].getClass() == Integer.class) {
                        pres.setInt(i + 1, (int) parms[i]);
                    } else {
                        pres.setString(i + 1, parms[i].toString());
                    }
                }
            }
            result = pres.executeQuery();
            connection.commit();
            Box box = new Box(result, pres, connection);
            //pres.close();
            connectpool.backConnection(connection);
            return box;
    }
}
