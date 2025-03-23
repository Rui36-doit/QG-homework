package homework01;

import java.sql.SQLException;
import java.util.Scanner;

public class ShowUI {
    Manager manager;

    public void manue_user(){
        System.out.println("------学生信息管理系统------");
        System.out.println("1.登录");
        System.out.println("2.注册");
        System.out.println("3.退出");
    }
    //主页面功能实现
    public void getchoose(Manager manager) throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true){
            manue_user();
            int n = sc.nextInt();
            if(n == 1){
                System.out.println("请输入id号");
                int id = sc.nextInt();
                if(id == 0){
                    if(manager.login()){
                        choosemawork(manager);
                    }
                }else if(id >= 0){
                    Student stu = manager.loginstu(id);
                    if(stu != null){
                        choosestuwork(stu);
                    }
                }else {
                    System.out.println("数据有误");
                }
            } else if (n == 2) {
                //注册学生
                try {
                    manager.register();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                break;
            }
        }
    }

    //学生的菜单
    public void showstuUI(){
        System.out.println("学生菜单(输入1~6)");
        System.out.println("1.查看可选课程");
        System.out.println("2.选择课程");
        System.out.println("3.退选课程");
        System.out.println("4.查看已选课程");
        System.out.println("5.修改手机号");
        System.out.println("6.退出");
    }

    //学生的功能实现
    public void choosestuwork(Student stu) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int t;
        while (true) {
            showstuUI();
            t = sc.nextInt();
            if (t == 1) {
                stu.shownochoose();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 2) {
                stu.chooseclass();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 3) {
                stu.delectclass();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 4) {
                stu.showhaschooseclass();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 5) {
                stu.changephone();
                sc.nextLine();
                sc.nextLine();
            } else {
                stu.save();
                break;
            }
        }

    }

    //管理员菜单
    public void showmaUI(){
        System.out.println("管理员菜单");
        System.out.println("1.查询所有学生");
        System.out.println("2.修改学生的手机号");
        System.out.println("3.查询所有的课程");
        System.out.println("4.查询某课程的学生名单");
        System.out.println("5.查询某学生的选课");
        System.out.println("6.退出");
    }

    //管理员功能实现
    public void choosemawork(Manager manager) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int t;
        while (true) {
            showmaUI();
            t = sc.nextInt();
            if (t == 1) {
                manager.showallstu();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 2) {
                manager.changestuphonenum();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 3) {
                manager.showclasses();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 4) {
                manager.showclassstu();
                sc.nextLine();
                sc.nextLine();
            } else if (t == 5) {
                manager.showstuclass();
                sc.nextLine();
                sc.nextLine();
            } else {
                break;
            }
        }

    }

    public static void main(String[] args) throws SQLException {
        ShowUI showUI = new ShowUI();
        Connectpool connectpool = new Connectpool("homework01");
        JDBCUtil.connectpool = connectpool;
        Manager manager = new Manager();
        showUI.manager = manager;
        showUI.getchoose(manager);

    }
}
