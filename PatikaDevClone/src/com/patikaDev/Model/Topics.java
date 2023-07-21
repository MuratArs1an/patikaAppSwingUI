package com.patikaDev.Model;

import com.patikaDev.Helper.DbConnector;

import java.sql.*;
import java.util.ArrayList;

public class Topics {
    private int id;
    private String title;
    private String explanation;
    private String youtubeUrl;
    private int courseID;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    private Course course;

    public Topics(int id,String title, String explanation, String youtubeUrl, int courseID) {
        this.id=id;
        this.title = title;
        this.explanation = explanation;
        this.youtubeUrl = youtubeUrl;
        this.courseID = courseID;
        this.course=Course.getFetch(courseID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topics(){}

    public static boolean add(String title, String explanation, String youtubeUrl, int courseID ){
        String query="INSERT INTO topics (title, explanation, url, course_id) VALUES(?,?,?,?)";
        try {
            PreparedStatement pr= DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,title);
            pr.setString(2,explanation);
            pr.setString(3,youtubeUrl);
            pr.setInt(4,courseID);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public static ArrayList<Topics> getList(){
        ArrayList<Topics> topics=new ArrayList<>();
        Topics topic;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM topics");
            while(rs.next()){
                topic=new Topics(rs.getInt("id"),rs.getString("title"),rs.getString("explanation"),rs.getString("url"),rs.getInt("course_id"));
                topics.add(topic);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topics;
    }

    //Arama yapacak querymizi kullanıcıdan gelen bilgilerle doldurduk
    // ve searchTopics isimli metodumuz ile yeni bir liste dondurduk
    public static String searchQuery(String title, String courseName){
        int courseID=getCourseIdFromName(courseName);

        String query="SELECT * FROM topics WHERE title ILIKE '%{{title}}%' AND course_id="+courseID;
        query=query.replace("{{title}}",title);
        System.out.println(query);
        return query;

    }

    public static int getCourseIdFromName(String courseName){

        String query="SELECT * FROM course WHERE name ILIKE '%{{name}}%' ";
        query=query.replace("{{name}}",courseName);
        Course course=null;
        try {
            Statement st=DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery(query);
            if(rs.next()){
                course=new Course(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("patika_id"),rs.getString("name"), rs.getString("lang"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return course.getId();

    }

    public static ArrayList<Topics> searchTopics(String query){
        ArrayList<Topics> topics=new ArrayList<>();
        Topics topic;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                topic=new Topics();
                topic.setId(rs.getInt("id"));
                topic.setTitle(rs.getString("title"));
                topic.setExplanation(rs.getString("explanation"));
                topic.setYoutubeUrl(rs.getString("url"));
                topic.setCourseID(rs.getInt("course_id"));
                topic.setCourse(Course.getFetch(topic.courseID));
                topics.add(topic);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topics;
    }

    public static Topics getFetch(int id){
        Topics topic=null;
        String query="SELECT * FROM topics WHERE id=?";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs=pr.executeQuery();
            if (rs.next()){
                topic=new Topics(rs.getInt("id"),rs.getString("title"),rs.getString("explanation"), rs.getString("url"),rs.getInt("course_id") );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topic;
    }

    public static boolean update(int id, String title){
        String query="UPDATE topics SET title=? WHERE id=?";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,title);
            pr.setInt(2,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(int id){
        String query="DELETE FROM topics WHERE id=?";

        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Topics> getListByCourseId(int courseID){
        ArrayList<Topics> topics=new ArrayList<>();
        Topics topic;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM topics WHERE course_id="+courseID);
            while(rs.next()){
                topic=new Topics(rs.getInt("id"),rs.getString("title"),rs.getString("explanation"),rs.getString("url"),rs.getInt("course_id"));
                topics.add(topic);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

}
