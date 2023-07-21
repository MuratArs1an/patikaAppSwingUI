package com.patikaDev.Model;

import com.patikaDev.Helper.DbConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentCourse {
    private int studentId;
    private int courseId;
    private Course course;
    private User user;

    public StudentCourse(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.user=User.getFetch(studentId);
        this.course=Course.getFetch(courseId);
    }

    public static ArrayList<StudentCourse> getList(int studentId){
        ArrayList<StudentCourse> courseList=new ArrayList<>();
        StudentCourse stCourse;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM course_join WHERE student_id="+studentId);
            while(rs.next()){
                stCourse=new StudentCourse(rs.getInt("student_id"),rs.getInt("course_id") );
                courseList.add(stCourse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static ArrayList<StudentCourse> getList(){
        ArrayList<StudentCourse> courseList=new ArrayList<>();
        StudentCourse stCourse;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM course_join");
            while(rs.next()){
                stCourse=new StudentCourse(rs.getInt("student_id"),rs.getInt("course_id") );
                courseList.add(stCourse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
