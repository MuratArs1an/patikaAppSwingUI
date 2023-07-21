package com.patikaDev.Model;

import com.patikaDev.Helper.DbConnector;
import com.patikaDev.Helper.Helper;

import java.sql.*;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String userName;
    private String password;
    private String userType;

    public User(){}

    public User(int id, String name, String userName, String password, String userType) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public static ArrayList<User> getList(){
        ArrayList<User> userList=new ArrayList<>();
        User obj;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM users");
            while(rs.next()){
                obj=new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("type"));
                userList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    //Database Kullanıcı ekleme metodu
    public static boolean add(String name, String userName, String password, String userType){

        String query="INSERT INTO users (name, user_name, password, type) VALUES (?,?,?,?)";
        User findUser=User.getFetch(userName);
        if(findUser!=null){
            Helper.showMsg("Bu Kullanıcı Adı Daha Once Alınmıs!");
            return false;
        }
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,userName);
            pr.setString(3,password);
            pr.setString(4,userType);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Kullanıcı adının daha once alınıp alınmadıgını kontrol ediyoruz
    public static User getFetch(String userName){
        User obj=null;
        String sql="SELECT * FROM users WHERE user_name=?";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(sql);
            pr.setString(1,userName);
            ResultSet rs=pr.executeQuery();
            //eger sql sorgusunda sonuc alırsak ılk deger obj ye atıyoruz
            // eger aynı kullanıcı adında varsa bıze user donecek yoksa null donecek
            if(rs.next()){
                obj=new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean delete(int id){
        String query="DELETE FROM users WHERE id=?";

        //user silindiğinde buna baglı kurslarında silinmesi için
        //user_id ye baglı olarak courselar listelendi ve silindi
        ArrayList<Course> courseList=Course.getListById(id);
        for(Course c: courseList){
            Course.delete(c.getId());
        }


        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean update(int id,String name, String userName, String password, String userType){
        String query="UPDATE users SET name=?, user_name=?, password=?, type=?  WHERE id=?";
        User findUser=User.getFetch(userName);
        if(findUser!=null && findUser.getId()!=id){
            Helper.showMsg("Bu Kullanıcı Adı Daha Once Alınmıs!");
            return false;
        }
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,userName);
            pr.setString(3,password);
            pr.setString(4,userType);
            pr.setInt(5,id);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<User> searchUserlist(String query){
        ArrayList<User> userList=new ArrayList<>();
        User obj;
        try {
            Statement st= DbConnector.getInstance().createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                obj=new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("type"));
                userList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    //ARAMA YAPMAK için query yazdık
    public static String searchQuery(String name, String userName, String userType){
        String query="SELECT * FROM users WHERE name LIKE '%{{name}}%' AND user_name LIKE '%{{user_name}}%'";
        query=query.replace("{{name}}",name);
        query=query.replace("{{user_name}}",userName);
        if(!userType.isEmpty()){
            query+=" AND type = '{{type}}'";
            query=query.replace("{{type}}", userType);
        }
        return query;
    }

    //id ye gore user donduren bir fetch overloading yaptık

    public static User getFetch(int id){
        User obj=null;
        String sql="SELECT * FROM users WHERE id=?";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(sql);
            pr.setInt(1,id);
            ResultSet rs=pr.executeQuery();
            //eger sql sorgusunda sonuc alırsak ılk deger obj ye atıyoruz
            // eger aynı kullanıcı adında varsa bıze user donecek yoksa null donecek
            if(rs.next()){
                obj=new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static User getFetch(String userName, String password){
        User obj=null;
        String sql="SELECT * FROM users WHERE user_name=? AND password=?";
        try {
            PreparedStatement pr=DbConnector.getInstance().prepareStatement(sql);
            pr.setString(1,userName);
            pr.setString(2,password);
            ResultSet rs=pr.executeQuery();
            //eger sql sorgusunda sonuc alırsak ılk deger obj ye atıyoruz
            // eger aynı kullanıcı adında varsa bıze user donecek yoksa null donecek
            if(rs.next()){
                switch (rs.getString("type")){
                    case "operator":
                        obj=new Operator();
                        break;
                    case "educator":
                        obj=new Educator();
                        break;
                    case "student":
                        obj=new Student();
                        break;
                    default:
                        obj=new User();
                }
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("user_name"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}
