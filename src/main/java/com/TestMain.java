package com;

import com.entity.College;
import com.entity.Student;
import com.util.JDBCTool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TestMain {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = JDBCTool.getConnection();
            System.out.println("====数据库连接成功====");
            int opt;
            while(true){
                printMenu();
                System.out.print("请输入功能编号：");
                opt = Integer.parseInt(scanner.nextLine());
                switch (opt){
                    case 1:
                        addStudent(conn);
                        break;
                    case 2:
                        addCollege(conn);
                        break;
                    case 3:
                        getStudentById(conn);
                        break;
                    case 4:
                        getCollegeById(conn);
                        break;
                    case 5:
                        updateStudent(conn);
                        break;
                    case 6:
                        updateCollege(conn);
                        break;
                    case 7:
                        deleteStudent(conn);
                        break;
                    case 8:
                        deleteCollege(conn);
                        break;
                    case 9:
                        listAllStudent(conn);
                        break;
                    case 10:
                        listAllCollege(conn);
                        break;
                    case 0:
                        System.out.println("程序退出");
                        return;
                    default:
                        System.out.println("输入错误，请重新选择！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //打印菜单
    private static void printMenu(){
        System.out.println("\n=========简易ORM测试菜单========");
        System.out.println("1.新增学生");
        System.out.println("2.新增学院");
        System.out.println("3.按ID查询学生");
        System.out.println("4.按ID查询学院");
        System.out.println("5.修改学生");
        System.out.println("6.修改学院");
        System.out.println("7.删除学生");
        System.out.println("8.删除学院");
        System.out.println("9.查询全部学生");
        System.out.println("10.查询全部学院");
        System.out.println("0.退出");
        System.out.println("================================");
    }

    //新增学生
    private static void addStudent(Connection conn) throws Exception{
        Student stu = new Student();
        System.out.print("输入姓名：");
        stu.setName(scanner.nextLine());
        System.out.print("输入专业：");
        stu.setMajor(scanner.nextLine());
        System.out.print("输入年龄：");
        stu.setAge(Integer.parseInt(scanner.nextLine()));
        stu.setEnter_time(new Date());
        System.out.print("是否毕业(0否/1是)：");
        stu.setIs_graduated(Short.parseShort(scanner.nextLine()));
        System.out.print("输入学费：");
        stu.setTuition(new BigDecimal(scanner.nextLine()));
        int rows = JDBCTool.save(stu,conn);
        System.out.println("新增完成，影响行数："+rows);
    }

    //新增学院
    private static void addCollege(Connection conn) throws Exception{
        College col = new College();
        System.out.print("输入学院名称：");
        col.setName(scanner.nextLine());
        System.out.print("输入学院编码：");
        col.setCode(scanner.nextLine());
        int rows = JDBCTool.save(col,conn);
        System.out.println("新增完成，影响行数："+rows);
    }

    //按id查询学生
    private static void getStudentById(Connection conn) throws Exception{
        System.out.print("输入学生【数字ID】：");
        String id = scanner.nextLine();
        Student stu = JDBCTool.getOneById(id,Student.class,conn);
        if(stu == null){
            System.out.println("未找到该学生");
        }else{
            String graduateText = stu.getIs_graduated() == 1 ? "已毕业" : "未毕业";
            System.out.println("【学生信息】");
            System.out.println("编号：" + stu.getId());
            System.out.println("姓名：" + stu.getName());
            System.out.println("专业：" + stu.getMajor());
            System.out.println("年龄：" + stu.getAge());
            System.out.println("入学时间：" + stu.getEnter_time());
            System.out.println("毕业状态：" + graduateText);
            System.out.println("学费：" + stu.getTuition());
        }
    }

    //按id查询学院
    private static void getCollegeById(Connection conn) throws Exception{
        System.out.print("输入学院【数字ID】：");
        String id = scanner.nextLine();
        College col = JDBCTool.getOneById(id,College.class,conn);
        if(col == null){
            System.out.println("未找到该学院");
        }else{
            System.out.println("【学院信息】");
            System.out.println("编号：" + col.getId());
            System.out.println("学院名称：" + col.getName());
            System.out.println("学院编码：" + col.getCode());
        }
    }

    //修改学生
    private static void updateStudent(Connection conn) throws Exception{
        Student stu = new Student();
        System.out.print("输入待修改学生【数字ID】：");
        stu.setId(Integer.parseInt(scanner.nextLine()));
        System.out.print("输入新姓名：");
        stu.setName(scanner.nextLine());
        System.out.print("输入新专业：");
        stu.setMajor(scanner.nextLine());
        System.out.print("输入新年龄：");
        stu.setAge(Integer.parseInt(scanner.nextLine()));
        stu.setEnter_time(new Date());
        System.out.print("是否毕业(0否/1是)：");
        stu.setIs_graduated(Short.parseShort(scanner.nextLine()));
        System.out.print("输入新学费：");
        stu.setTuition(new BigDecimal(scanner.nextLine()));
        int rows = JDBCTool.update(stu,conn);
        System.out.println("修改完成，影响行数："+rows);
    }

    //修改学院
    private static void updateCollege(Connection conn) throws Exception{
        College col = new College();
        System.out.print("输入待修改学院【数字ID】：");
        col.setId(Integer.parseInt(scanner.nextLine()));
        System.out.print("输入新学院名称：");
        col.setName(scanner.nextLine());
        System.out.print("输入新学院编码：");
        col.setCode(scanner.nextLine());
        int rows = JDBCTool.update(col,conn);
        System.out.println("修改完成，影响行数："+rows);
    }

    //删除学生
    private static void deleteStudent(Connection conn) throws Exception{
        Student stu = new Student();
        System.out.print("输入要删除学生【数字ID】：");
        stu.setId(Integer.parseInt(scanner.nextLine()));
        int rows = JDBCTool.delete(stu,conn);
        System.out.println("删除完成，影响行数："+rows);
    }

    //删除学院
    private static void deleteCollege(Connection conn) throws Exception{
        College col = new College();
        System.out.print("输入要删除学院【数字ID】：");
        col.setId(Integer.parseInt(scanner.nextLine()));
        int rows = JDBCTool.delete(col,conn);
        System.out.println("删除完成，影响行数："+rows);
    }

    //查询全部学生（中文美化输出）
    private static void listAllStudent(Connection conn) throws Exception{
        String sql = "select * from student";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Student> studentList = JDBCTool.resultSetToList(rs,Student.class);
        System.out.println("====全部学生列表====");
        if(studentList.isEmpty()){
            System.out.println("暂无学生数据");
        }else {
            for(Student s : studentList){
                String graduateText = s.getIs_graduated() == 1 ? "已毕业" : "未毕业";
                System.out.println("学生编号："+s.getId()+"，姓名："+s.getName()+"，专业："+s.getMajor()
                        +"，年龄："+s.getAge()+"，入学日期："+s.getEnter_time()
                        +"，毕业状态："+graduateText+"，学费："+s.getTuition());
            }
        }
        JDBCTool.close(pstmt,rs);
    }

    //查询全部学院（中文美化输出）
    private static void listAllCollege(Connection conn) throws Exception{
        String sql = "select * from college";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<College> collegeList = JDBCTool.resultSetToList(rs,College.class);
        System.out.println("====全部学院列表====");
        if(collegeList.isEmpty()){
            System.out.println("暂无学院数据");
        }else {
            for(College c : collegeList){
                System.out.println("学院编号："+c.getId()+"，学院名称："+c.getName()+"，学院编码："+c.getCode());
            }
        }
        JDBCTool.close(pstmt,rs);
    }
}
