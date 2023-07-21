package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.DbConnector;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Helper.Item;
import com.patikaDev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentGUI extends JFrame {
    private JPanel wrapper;
    private JTable tbl_courses;
    private JComboBox cmb_patika;
    private JButton btn_courseSign;
    private JTabbedPane tabbedPane1;
    private JComboBox cmb_courses;
    private JTable tbl_topics;
    private JComboBox cmb_topics;
    private JTable tbl_quiz;
    private JButton btn_comment;
    private DefaultTableModel mdl_courses;
    private Object [] row_courses;
    private ArrayList<Course> studentCourses;
    private DefaultTableModel mdl_topics;
    private Object [] row_topics;
    private DefaultTableModel mdl_quizs;
    private Object [] row_quizs;
    public StudentGUI(Student student) {
        add(wrapper);
        setSize(600,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(Helper.screenCenter("x",getSize()), Helper.screenCenter("y",getSize()));
        setTitle(Config.PROJECT_TİTLE);
        setResizable(false);
        setVisible(true);


        //Course Tablosu
        mdl_courses=new DefaultTableModel();
        Object[] col_corses={"ID","Ders Adı","Programlama Dili","Eğitmen"};
        mdl_courses.setColumnIdentifiers(col_corses);
        row_courses=new Object[col_corses.length];
        loadPatikaCombo();
        Item patikaCombo=(Item) cmb_patika.getSelectedItem();
        loadCourseModel(patikaCombo.getKey());
        tbl_courses.setModel(mdl_courses);
        tbl_courses.getTableHeader().setReorderingAllowed(false);
        loadCourseCombo(student.getId());


        //comboBOX listener combobox her degıstırıldıgınde patikaya baglı olan kurslar listelenecek
        cmb_patika.addActionListener(e -> {
            Item comboPatika=(Item) cmb_patika.getSelectedItem();
            loadCourseModel(comboPatika.getKey());
        });


        tbl_courses.addMouseListener(new MouseAdapter() {
            @Override
            //mouse tıklandıgında basıldıgında ne olacak
            public void mousePressed(MouseEvent e) {
                // point sınıfı bizim için x ve y yani kordinat tutan bir sınıf
                //getPoint ile tıklanınlan yerin kordinatlarını alıyoruz
                Point point=e.getPoint();
                // rowAtpoint ile kordinatlardaki rowu buluyoruz
                int selected_row=tbl_courses.rowAtPoint(point);
                //sag tıklandıgında da rowun secılmesı ıcın
                tbl_courses.setRowSelectionInterval(selected_row,selected_row);
            }
        });

        //Kayıt ol butonu listener
        btn_courseSign.addActionListener(e -> {
            int selectedId=Integer.parseInt(tbl_courses.getValueAt(tbl_courses.getSelectedRow(),0).toString());
            System.out.println(selectedId);
            if(joinCourse(student.getId(),selectedId)){
                Helper.showMsg("success");
                loadCourseCombo(student.getId());
            }else{
                Helper.showMsg("repeat");
            }

        });

        //topics tablosu
        mdl_topics=new DefaultTableModel();
        Object[] col_topics={"ID","Başlık","Açıklama","Youtube Linki"};
        mdl_topics.setColumnIdentifiers(col_topics);
        row_topics=new Object[col_topics.length];
        Item courseCombo=(Item) cmb_courses.getSelectedItem();
        loadTopicModel(courseCombo.getKey());
        tbl_topics.setModel(mdl_topics);
        tbl_topics.getTableHeader().setReorderingAllowed(false);


        //Quiz tablosu
        mdl_quizs=new DefaultTableModel();
        Object [] col_quizs={"Id","Question","Konusu"};
        mdl_quizs.setColumnIdentifiers(col_quizs);
        row_quizs=new Object[col_quizs.length];
        loadTopicCombo(student);
        Item topicCombo=(Item) cmb_topics.getSelectedItem();
        loadQuizModel(topicCombo.getKey());
        tbl_quiz.setModel(mdl_quizs);
        tbl_quiz.getTableHeader().setReorderingAllowed(false);




        //comboboxta kurs secıldıkce konuların ona yuklenmesını sağladık
        cmb_courses.addActionListener(e -> {
            Item comboCourse=(Item) cmb_courses.getSelectedItem();
            loadTopicModel(comboCourse.getKey());
        });

        //Konuları degerlndırme ekranı
        btn_comment.addActionListener(e -> {
            int selectedId=Integer.parseInt(tbl_topics.getValueAt(tbl_topics.getSelectedRow(),0).toString());
            CommentGUI comment=new CommentGUI(Topics.getFetch(selectedId),student);
        });



    }

    private void loadQuizModel(int topicId){
        DefaultTableModel clearModel=(DefaultTableModel) tbl_quiz.getModel();
        clearModel.setRowCount(0);
        for(Quiz quiz: Quiz.getList()){
            if(quiz.getTopicId()==topicId){
                row_quizs[0]=quiz.getId();
                row_quizs[1]=quiz.getQuestion();
                row_quizs[2]=quiz.getTopic().getTitle();
                mdl_quizs.addRow(row_quizs);
            }
        }
    }

    private void loadTopicCombo(Student student){
        cmb_topics.removeAllItems();
        for(StudentCourse stcourse: StudentCourse.getList(student.getId())){
            for(Topics topic: Topics.getListByCourseId(stcourse.getCourseId())){
                cmb_topics.addItem(new Item(topic.getId(), topic.getTitle()));
            }
        }
    }

    private void loadPatikaCombo(){
        cmb_patika.removeAllItems();
        for(Patika patika:Patika.getList()){
            cmb_patika.addItem(new Item(patika.getId(),patika.getName()));
        }
    }

    private void loadCourseCombo(int id){
        cmb_courses.removeAllItems();
        for(StudentCourse stCourse: StudentCourse.getList(id)){
            cmb_courses.addItem(new Item(Course.getFetch(stCourse.getCourseId()).getId(),Course.getFetch(stCourse.getCourseId()).getName()));
        }
    }
    private void loadCourseModel(int id){
        DefaultTableModel clearModel= (DefaultTableModel) tbl_courses.getModel();
        clearModel.setRowCount(0);
        for(Course obj: Course.getList()){
            if(obj.getPatika().getId()==id){
                row_courses[0]=obj.getId();
                row_courses[1]=obj.getName();
                row_courses[2]=obj.getLang();
                row_courses[3]=obj.getEducator().getName();
                mdl_courses.addRow(row_courses);
            }
        }
    }

    private void loadTopicModel(int id){
        DefaultTableModel clearModel=(DefaultTableModel) tbl_topics.getModel();
        clearModel.setRowCount(0);
        for(Topics topic: Topics.getList()){
            if(topic.getCourse().getId()==id){
                row_topics[0]=topic.getId();
                row_topics[1]=topic.getTitle();
                row_topics[2]=topic.getExplanation();
                row_topics[3]=topic.getYoutubeUrl();
                mdl_topics.addRow(row_topics);
            }
        }

    }
    public boolean joinCourse(int studentId, int courseId){
        for(StudentCourse studentCourse: StudentCourse.getList()){
            if(studentCourse.getStudentId()==studentId && studentCourse.getCourseId()==courseId){
                return false;
            }
        }
        String query="INSERT INTO course_join (course_id, student_id) VALUES(?,?)";
        try {
            PreparedStatement pr= DbConnector.getInstance().prepareStatement(query);
            pr.setInt(1,courseId);
            pr.setInt(2,studentId);
            return pr.executeUpdate()!=-1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
