package Scoer;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
public class Student {
    String name;
    String password;
    int id;
    int age;
    String phonenumber;
    int classnum = 0;
    String classes = null;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public int getClassnum() {
        return classnum;
    }

    public String getClasses() {
        return classes;
    }
    public Student(){

    }

    public Student(String name, String password, int id, int age, String phonenumber, int classnum){
        this.name = name;
        this.password = password;
        this.id = id;
        this.age = age;
        this.phonenumber = phonenumber;
        this.classnum = classnum;
    }

    //登录功能
    public boolean login(String password){
        if(password.equals(this.password)){
            System.out.println("login successful");
            return true;
        }else {
            System.out.println("login false");
            return false;
        }
    }

    //输出数据
    public void showimfor() throws SQLException {
        System.out.println("姓名" + this.name);
        System.out.println("年龄" + this.age);
        System.out.println("学号" + this.id);
        System.out.println("电话号码" + this.phonenumber);
        System.out.println("选课信息是");
        //提取数据
        String sql = "select name, type from user_class where student_id = ?";
        Box box = JDBCUtil.search(sql, id);
        ResultSet results = box.getResult();
        String name;
        int t;
        while (results.next()){
            String is;
            name = results.getString("name");
            t = results.getInt("type");
            if(t == 0){
                is = "未开课";
            }else {
                is = "已连接";
            }
            System.out.println(name + " " + is);
        }
        box.close();
    }

    //完善学生信息
    public void getimfor() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入年龄");
        while (true) {
            try {
                this.age = sc.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("输入的格式不对，请重新输入");
                sc.nextLine();
            }
        }
        System.out.println("请输入电话号码");
        this.phonenumber = sc.nextLine();
        add();
        System.out.println("保存信息成功");
    }

    //改变电话号码
    public void changephone(){
        System.out.println("请输入要改变的电话号码");
        Scanner sc = new Scanner(System.in);
        String num = sc.nextLine();
        this.phonenumber = num;
    }

    //选课
    public void chooseclass() throws SQLException {
        System.out.println("请输入你选课的名称");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        String sql1 = "select uc.name from user_class uc join students s on uc.student_id = s.id where s.name = ? && uc.name = ?";
        Box box = JDBCUtil.search(sql1, this.name, name);
        ResultSet resultSet = box.getResult();
        int t = 0;
        if(resultSet.next()){
            t = 1;
        }
        if(t == 1){
            System.out.println("已选择该课程");
        }else if(classnum >= 5){
            System.out.println("选择的课程已满");
        }else{
            //classes = classes + name + ",";
            classnum++;
            //添加到数据库
            String sql = "insert into user_class(name, type, student_id) value (?, 0, ?)";
            JDBCUtil.update(sql, name, id);
            System.out.println("选课成功");
        }
    }

    //删除选课
    public void delectclass() throws SQLException {
        System.out.println("请输入要删除的课程的名称");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        if(classes.contains(name)){
            //获取课程状态
            String sqlfind = "select name, type from user_class where name = ? && student_id = ?";
            Box box = JDBCUtil.search(sqlfind, name, id);
            ResultSet result = box.getResult();
            int flag = result.getInt("type");
            if(flag == 1){
                System.out.println("该课程已开课，无法退课");
            }else {
                //删除数据
                String sql = "delete from user_class where name = ? && student_id = ?;";
                JDBCUtil.update(sql, name, id);
                classnum--;
                System.out.println("删除成功");
            }
            box.close();
        }else {
            System.out.println("你没有选该课程");
        }
    }

    //显示的已选课程
    public LinkedList<StudentClass> showhaschooseclass() throws SQLException {
        LinkedList<StudentClass> list = new LinkedList<>();
        String sql = "select uc.name, uc.type from user_class uc where uc.student_id = ?";
        Box box = null;
        try {
            box = JDBCUtil.search(sql, id);
            ResultSet resultSet = box.getResult();
            while (resultSet.next()){
                int t = resultSet.getInt("type");
                //获取课程对象
                StudentClass sc = new StudentClass(resultSet.getString("name"), t);
                list.add(sc);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            box.close();
        }
        return list;
    }

    //可以选择的课程
    public void shownochoose() throws SQLException {
        String sql = "select c.name  from classes c where c.name not in(select uc.name from user_class uc where uc.student_id = ?)";
        Box box = JDBCUtil.search(sql, id);
        ResultSet re = box.getResult();
        while (re.next()){
            System.out.println(re.getString("name"));
        }
    }

    public int getid() throws SQLException {
        String sql = "select id from students s where s.name = '?' and s.password = '?'; ";
        Box box = JDBCUtil.search(sql, name, password);
        ResultSet re = box.getResult();
        int id = re.getInt("id");
        return id;
    }

    //把学生的数据添加导数据库
    public void add() throws SQLException {
        String sql = "insert into students(name, age, password, phonenumber, classnum, classes) values (?, ?, ?, ?, ?)";
        JDBCUtil.update(sql, name, age, password, phonenumber, classnum);
    }

    //保存学生数据
    public void save() throws SQLException {
        String sql = "update students set phonenumber = ?, classnum = ?, classes = ? where id = ?";
        JDBCUtil.update(sql, phonenumber, classnum, classes, id);
    }

}
