package Scoer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/myservelt")
public class MyServelt extends HttpServlet {
    Manager manager;
    Student student;
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("驱动加载失败", e);
        }
        this.manager = new Manager();
        Connectpool connectpool = new Connectpool("homework01");
        JDBCUtil.connectpool = connectpool;
        System.out.println("初始化成功");
    }

    //get请求方式
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String action = req.getHeader("X-Action");
        String json = JSONUtils.getJSONstr(req);
        try {
            Deal deal = new Deal(req, resp, manager);
            System.out.println(action);
            if(action.equals("login")){
                System.out.println("login" + json);
                student = deal.login(json);
            }else if(action.equals("register")){
                System.out.println(json);
                deal.register(json);
            }else if(action.equals("seeclasses")){
                deal.findclass(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //post请求方式
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    /*
    public void login(String str, HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
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
    }

     */

    @Override
    public void destroy() {
        super.destroy();
        JDBCUtil.connectpool.closeALL();
        System.out.println("连接池关闭");
    }
}

