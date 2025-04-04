package Scoer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Deal {
    HttpServletRequest req;
    HttpServletResponse resp;
    Manager manager;

    public Deal(HttpServletRequest req, HttpServletResponse resp, Manager manager){
        this.req = req;
        this.resp = resp;
        this.manager = manager;
    }
    
    //登录代码
    public Student login(String str) throws SQLException, IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        Infor infor = objectMapper.readValue(str, Infor.class);
        int id = Integer.valueOf(infor.username);
        //查找学生
        Student stu = manager.loginstu(id, infor.password);
        //转换成json字符串
        String json = objectMapper.writeValueAsString(stu);
        System.out.println(json);
        //回响数据
        PrintWriter writer = resp.getWriter();
        System.out.println(json);
        writer.print(json);
        return stu;
    }

    //学生查找课程的代码
    public void findclass(Student stu) throws SQLException, IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        //获取课程信息
        LinkedList<StudentClass> list = stu.showhaschooseclass();
        //转成JSON数据
        String json = objectMapper.writeValueAsString(list);
        //回响数据
        PrintWriter writer = resp.getWriter();
        writer.print(json);
        System.out.println(json);
    }

    //学生注册
    public void register(String str) throws SQLException, IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = objectMapper.readValue(str, Student.class);
        System.out.println(student.id + " " + student.name);
        //添加学生信息
        student.add();
        int t = manager.getid();
        //回想信息
        PrintWriter writer = resp.getWriter();
        writer.write(t);
    }
}
