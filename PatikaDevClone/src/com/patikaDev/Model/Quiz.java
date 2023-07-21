package com.patikaDev.Model;

import com.patikaDev.Helper.DbConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Quiz {
    private int id;
    private String question;
    private Topics topic;

    private int topicId;

    public Quiz(int id, String question, int topicId) {
        this.id = id;
        this.question = question;
        this.topicId = topicId;
        this.topic=Topics.getFetch(topicId);
    }

    public Quiz(){}

    public static ArrayList<Quiz> getList(){
        ArrayList<Quiz> quizzes=new ArrayList<>();
        Quiz quiz;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM quiz");
            while(rs.next()){
                quiz=new Quiz(rs.getInt("id"),rs.getString("question"),rs.getInt("topic_id"));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quizzes;
    }

    public static boolean add(String question, int topicId ){
        String query="INSERT INTO quiz (question, topic_id) VALUES(?,?)";
        try {
            PreparedStatement pr= DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,question);
            pr.setInt(2,topicId);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(int id){
        String query="DELETE FROM quiz WHERE id=?";

        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Topics getTopic() {
        return topic;
    }

    public void setTopic(Topics topic) {
        this.topic = topic;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
