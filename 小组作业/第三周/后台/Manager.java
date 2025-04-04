package Scoer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class Manager {

    String password = "123456";
    /*
    public Manager(){
        Connectpool connectpool = new Connectpool("homework01");
        JDBCUtil.connectpool = connectpool;
    }*/
    //注册新学生
    public Student register(String name, String password) throws SQLException {
        Student stu;
        stu = new Student();
        stu.name = name;
        stu.password = password;
        return stu;
    }

    //学生登录
    /*
    public Student loginstu(int id) throws SQLException {
        Student stu = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入你的密码");
        String password = sc.nextLine();
        String sql = "select name, age, phonenumber, classnum, password from students where id = ?";
        Box box = JDBCUtil.search(sql, id);
        ResultSet resultSet = box.getResult();
        if(resultSet.next()){
            if(password.equals(resultSet.getString("password"))){
                stu = new Student(resultSet.getString("name"), password, id,
                        resultSet.getInt("age"), resultSet.getString("phonenumber"),
                        resultSet.getInt("classnum"));
            }else{
                System.out.println("密码不正确");
            }
        }else {
            System.out.println("没有该用户");
        }
        return stu;
    }*/
    public Student loginstu(int id, String password) throws SQLException {
        Student stu = null;
        String sql = "select name, age, phonenumber, classnum, password from students where id = ?";
        Box box = JDBCUtil.search(sql, id);
        ResultSet resultSet = box.getResult();
        if(resultSet.next()){
            if(password.equals(resultSet.getString("password"))){
                stu = new Student(resultSet.getString("name"), password, id,
                        resultSet.getInt("age"), resultSet.getString("phonenumber"),
                        resultSet.getInt("classnum"));
            }else{
                System.out.println("密码不正确");
            }
        }else {
            System.out.println("没有该用户");
        }
        box.close();
        return stu;
    }

    //自己登录
    public boolean login(){
        System.out.println("请输入密码");
        Scanner sc = new Scanner(System.in);
        String password = sc.nextLine();
        if(password.equals(this.password)){
            System.out.println("登录成功");
            return true;
        }
        return false;
    }


    //查找对应的学生的对象。
    public int findstu(int id) throws SQLException {
        String sql = "select count(*) from students where id = ?";
        Box box = JDBCUtil.search(sql, id);
        ResultSet resultSet = box.getResult();
        int t = resultSet.getInt(1);
        box.close();
        if(t == 0) {
            return -1;
        }else{
            return 1;
        }
    }

    //展示所有学生的信息
    public void showallstu() throws SQLException {
        String sql = "select name, age, phonenumber, classnum from students;";
        Box box = JDBCUtil.search(sql);
        ResultSet results = box.getResult();
        while (results.next()){
            System.out.println("姓名：" + results.getString("name") + "年龄：" + results.getInt("age")
                    + "电话号码" + results.getString("phonenumber"));
        }
        box.close();
    }

    //改变学生的电话号码
    public void changestuphonenum() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要更改的学生的姓名");
        String name = sc.nextLine();
        System.out.println("请输入更改的电话号码");
        String phonenum = sc.nextLine();
        String sql = "update students set phonenumber = ? where name = ?";
        int t = JDBCUtil.update(sql, phonenum, name);
        if(t == 0){
            System.out.println("没有该学生修改失败");
        }
    }

    //展示所有的课程信息
    public void showclasses() throws SQLException {
        String sql = "select name, stunum from classes";
        Box box = JDBCUtil.search(sql);
        ResultSet resultSet = box.resultSet;
        int i = 0;
        while (resultSet.next()){
            System.out.println("课程名字：" + resultSet.getString("name") + "选课人数：" + resultSet.getInt("stunum"));
            i++;
        }
        if(i == 0){
            System.out.println("没有课程");
        }
    }

    //删除课程信息
    public void delectclass() throws SQLException {
        System.out.println("请输入要删除的课程名称");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        //修改对应学生的信息
        String sql1 = "update students s set s.classnum = s.classnum + 1 where s.id in(select uc.student_id from user_class uc where uc.name = ?)";
        JDBCUtil.update(sql1, name);
        //删除数据
        String sql = "delete from classes where name = ?";
        int i = JDBCUtil.update(sql, name);
        if(i == 0){
            System.out.println("没有该课程");
        }
    }

    //查找某课程的学生
    public void showclassstu() throws SQLException {
        System.out.println("请输入课程名称");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        String sql = "select s.name from students s join user_class uc on s.id = uc.student_id where uc.name = ?";
        Box box = JDBCUtil.search(sql, name);
        ResultSet resultSet = box.getResult();
        System.out.println("该课程选的人数有");
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

    //某学生的选课情况
    public void showstuclass() throws SQLException {
        System.out.println("请输入查询的学生");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        String sql = "select uc.name from user_class uc join students s on uc.student_id = s.id where s.name = ?";
        Box box = JDBCUtil.search(sql);
        ResultSet resultSet = box.getResult();
        int i = 0;
        System.out.println("该学生选的课程是");
        while (resultSet.next()){
            System.out.println();
            i++;
        }
        if(i == 0){
            System.out.println("没有学生选该课程");
        }
    }

    public int getid() throws SQLException {
        String sql = "select count(*) from students";
        Box box = JDBCUtil.search(sql);
        ResultSet resultSet = box.getResult();
        resultSet.next();
        int t = resultSet.getInt(1);
        box.close();
        return t;
    }
}
