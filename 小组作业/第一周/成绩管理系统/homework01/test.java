package homework01;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws SQLException {
        Connectpool connectpool = new Connectpool("homework01");
        JDBCUtil.connectpool = connectpool;
        String sql = "select s.name from students s join user_class uc on s.id = uc.student_id where uc.name = ?";
        Box box = JDBCUtil.search(sql, "高数");
        System.out.println("该课程选的人数有");
        ResultSet resultSet = box.getResult();
        int i = 0;
        while (resultSet.next()){
            System.out.println(resultSet.getString("name"));
            i++;
        }
        if(i == 0){
            System.out.println("该学生没有选课");
        }
        box.close();
    }

}
