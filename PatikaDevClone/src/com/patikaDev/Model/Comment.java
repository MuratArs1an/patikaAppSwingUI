package com.patikaDev.Model;

import com.patikaDev.Helper.DbConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Comment {
    private int id;
    private String comment;
    private int studentId;
    private int topicId;
    private String point;
    private Topics topics;

    public Comment(int id, String comment, int studentId, int topicId, String point) {
        this.id = id;
        this.comment = comment;
        this.studentId = studentId;
        this.topicId = topicId;
        this.point = point;
        this.topics=Topics.getFetch(topicId);
    }

    public static ArrayList<Comment> getList(int topicId){
        ArrayList<Comment> comments=new ArrayList<>();
        Comment comment;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM comments WHERE topic_id="+topicId);
            while(rs.next()){
                comment=new Comment(rs.getInt("id"),rs.getString("comments"),rs.getInt("student_id"),rs.getInt("topic_id"),rs.getString("point"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    public static boolean add(String comment, int studentId, int topicId, String point){
        String query="INSERT INTO comments (comments, student_id, topic_id, point) VALUES(?,?,?,?)";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,comment);
            pr.setInt(2,studentId);
            pr.setInt(3,topicId);
            pr.setString(4,point);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }


    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }
}
