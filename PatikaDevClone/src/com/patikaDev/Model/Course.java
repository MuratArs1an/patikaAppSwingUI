package com.patikaDev.Model;

import com.patikaDev.Helper.DbConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int id;
    private int userId;
    private int patikaId;
    private String name;
    private String lang;

    private Patika patika;
    private User educator;

    public Course(int id, int userId, int patikaId, String name, String lang) {
        this.id = id;
        this.userId = userId;
        this.patikaId = patikaId;
        this.name = name;
        this.lang = lang;
        this.patika=Patika.getFetch(patikaId);
        this.educator=User.getFetch(userId);
    }

    public Course(){}

    public static ArrayList<Course> getList(){
        ArrayList<Course> courseList=new ArrayList<>();
        Course obj;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM course");
            while(rs.next()){
                obj=new Course(rs.getInt("id"),rs.getInt("user_id"), rs.getInt("patika_id"), rs.getString("name"),rs.getString("lang") );
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static boolean add(int userId, int patikaId, String name, String lang){
        String query="INSERT INTO course (user_id, patika_id, name, lang) VALUES(?,?,?,?)";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,userId);
            pr.setInt(2,patikaId);
            pr.setString(3,name);
            pr.setString(4,lang);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(int id){
        String query="DELETE FROM course WHERE id= ?";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //user id ye gore listelenerek user silindiğinde bu id ye baglı courselarda silinmesi saglandı
    public static ArrayList<Course> getListById(int userId){
        ArrayList<Course> courseList=new ArrayList<>();
        Course obj;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM course WHERE user_id="+userId);
            while(rs.next()){
                obj=new Course(rs.getInt("id"),rs.getInt("user_id"), rs.getInt("patika_id"), rs.getString("name"),rs.getString("lang") );
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static ArrayList<Course> getListByCourseId(int id){
        ArrayList<Course> courseList=new ArrayList<>();
        Course obj;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM course WHERE id="+id);
            while(rs.next()){
                obj=new Course(rs.getInt("id"),rs.getInt("user_id"), rs.getInt("patika_id"), rs.getString("name"),rs.getString("lang") );
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static Course getFetch(int id){
        Course obj=null;
        String query="SELECT * FROM course WHERE id=?";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                obj=new Course(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("patika_id"),rs.getString("name"), rs.getString("lang"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPatikaId() {
        return patikaId;
    }

    public void setPatikaId(int patikaId) {
        this.patikaId = patikaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }
}
