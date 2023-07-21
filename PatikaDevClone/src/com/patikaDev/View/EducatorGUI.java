package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Helper.Item;
import com.patikaDev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_welcome;
    private JTabbedPane tab_courseList;
    private JTable tbl_courseList;
    private JButton btn_exit;
    private JPanel fld_topics;
    private JTable tbl_topics;
    private JTextField fld_topicTitle;
    private JTextField fld_topicExp;
    private JTextField fld_topicUrl;
    private JComboBox cmb_courseList;
    private JButton btn_add;
    private JTable tbl_quiz;
    private JTextField fld_quiz;
    private JComboBox cmb_quiz;
    private JButton btn_quizAdd;
    private JTextField fld_searchTitle;
    private JTextField fld_searchCourse;
    private JButton btn_topicFilter;
    private JPanel fld_topicsAdd;
    private DefaultTableModel mdl_courseList;
    private DefaultTableModel mdl_topics;
    private DefaultTableModel mdl_quizs;
    private Object[] row_courseList;
    private Object[] row_topics;
    private JPopupMenu topicMenu;
    private JPopupMenu quizMenu;
    private Object [] row_quizs;

    public EducatorGUI(Educator educator) {
        add(wrapper);
        setSize(1000,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(Helper.screenCenter("x",getSize()), Helper.screenCenter("y",getSize()));
        setTitle(Config.PROJECT_TİTLE);
        setResizable(false);
        setVisible(true);
        lbl_welcome.setText(educator.getName());

        //Courselarımızın table modelını olusturuyoruz
        mdl_courseList=new DefaultTableModel();
        Object [] col_courseList= {"ID","Ders Adı","Patika","Programla Dili"};
        mdl_courseList.setColumnIdentifiers(col_courseList);
        row_courseList=new Object[col_courseList.length];
        loadCourseModel(educator);
        tbl_courseList.setModel(mdl_courseList);
        tbl_courseList.getTableHeader().setReorderingAllowed(false);
        loadCourseCombo();

        //popup menu olusturma (sag tık )
        topicMenu=new JPopupMenu();
        // sag tıkta guncelle ve sil komutları acılacak
        JMenuItem update=new JMenuItem("Güncelle");
        JMenuItem delete=new JMenuItem("Sil");
        topicMenu.add(update);
        topicMenu.add(delete);

        //update Menusunu listener ekşeyerek hangi row üzerinden işlem yapılacagını diğer menuye gecişi yapacagız
        update.addActionListener(e -> {
            //seçilen satır ve 0 ıncı kolun bize secilen patikanın idsini verecek
            int selectedId=Integer.parseInt(tbl_topics.getValueAt(tbl_topics.getSelectedRow(),0).toString());
            //UpdateGUI mızı cagırıyoruz
             UpdateTopicsGUI updateGUI=new UpdateTopicsGUI(Topics.getFetch(selectedId));
            //guncelle penceresi kapandıgı zaman listemizi yenilemek için pencerenin kapanma olayına listener ekliyoruz
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadTopicModel();
                }
            });
        });

        delete.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int selectedId=Integer.parseInt(tbl_topics.getValueAt(tbl_topics.getSelectedRow(),0).toString());
                Topics.delete(selectedId);
                Helper.showMsg("success");
                loadTopicModel();
            }
        });

        //Konularımızın modelini olusturuyoruz
        mdl_topics=new DefaultTableModel();
        Object [] col_topics={"ID","Title","Explanation","Youtube URL","Dersi"};
        mdl_topics.setColumnIdentifiers(col_topics);
        row_topics=new Object[col_topics.length];
        loadTopicModel();
        tbl_topics.setModel(mdl_topics);
        tbl_topics.getTableHeader().setReorderingAllowed(false);
        tbl_topics.setComponentPopupMenu(topicMenu);


        //Exit butonu
        btn_exit.addActionListener(e -> {
            dispose();
            LoginGUI login=new LoginGUI();
        });

        //Ekleme Butonu
        btn_add.addActionListener(e -> {
            Item courseCombo=(Item) cmb_courseList.getSelectedItem();
            if(Helper.isFieldEmpty(fld_topicTitle) || Helper.isFieldEmpty(fld_topicExp) || Helper.isFieldEmpty(fld_topicUrl)){
                Helper.showMsg("fill");
            }else{
                Topics.add(fld_topicTitle.getText(),fld_topicExp.getText(),fld_topicUrl.getText(), courseCombo.getKey());
                Helper.showMsg("success");
                loadTopicModel();
                fld_topicTitle.setText(null);
                fld_topicExp.setText(null);
                fld_topicUrl.setText(null);
            }

        });

        //Arama butonu
        btn_topicFilter.addActionListener(e -> {
            String title=fld_searchTitle.getText();
            String courseName=fld_searchCourse.getText();
            String topicSearsQuery=Topics.searchQuery(title,courseName);
            ArrayList<Topics> searchingTopics=Topics.searchTopics(topicSearsQuery);
            loadTopicModel(searchingTopics);
        });

        //QUİZ

        quizMenu=new JPopupMenu();
        JMenuItem deleteQuiz=new JMenuItem("Sil");
        quizMenu.add(deleteQuiz);

        deleteQuiz.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int selectedId=Integer.parseInt(tbl_quiz.getValueAt(tbl_quiz.getSelectedRow(),0).toString());
                Quiz.delete(selectedId);
                Helper.showMsg("success");
                loadQuizModel();
            }
        });

        mdl_quizs=new DefaultTableModel();
        Object [] col_quiz={"ID","QUİZ SORUSU","KONUSU"};
        mdl_quizs.setColumnIdentifiers(col_quiz);
        row_quizs=new Object[col_quiz.length];
        loadTopicCombo();
        loadQuizModel();
        tbl_quiz.setModel(mdl_quizs);
        tbl_quiz.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_quiz.getColumnModel().getColumn(2).setMaxWidth(400);
        tbl_quiz.getTableHeader().setReorderingAllowed(false);
        tbl_quiz.setComponentPopupMenu(quizMenu);

        //Quiz add button
        btn_quizAdd.addActionListener(e -> {
            Item topicCombo=(Item) cmb_quiz.getSelectedItem();
            if(Helper.isFieldEmpty(fld_quiz)){
                Helper.showMsg("fill");
            }else{
                Quiz.add(fld_quiz.getText(), topicCombo.getKey());
                Helper.showMsg("success");
                loadQuizModel();
                fld_quiz.setText(null);
            }
        });

    }






    private void loadCourseModel(Educator educator) {
        DefaultTableModel clearModel= (DefaultTableModel) tbl_courseList.getModel();
        clearModel.setRowCount(0);
        for(Course obj: Course.getList()){
            if(obj.getEducator().getId()==educator.getId()){
                row_courseList[0]=obj.getId();
                row_courseList[1]=obj.getName();
                row_courseList[2]=obj.getPatika().getName();
                row_courseList[3]=obj.getLang();
                mdl_courseList.addRow(row_courseList);
            }
        }
    }

    private void loadTopicModel(){
        DefaultTableModel clearModel=(DefaultTableModel) tbl_topics.getModel();
        clearModel.setRowCount(0);
        for(Topics topic: Topics.getList()){
            row_topics[0]=topic.getId();
            row_topics[1]=topic.getTitle();
            row_topics[2]=topic.getExplanation();
            row_topics[3]=topic.getYoutubeUrl();
            row_topics[4]=topic.getCourse().getName();
            mdl_topics.addRow(row_topics);
        }
    }

    private void loadTopicModel(ArrayList<Topics> topics){
        DefaultTableModel clearModel=(DefaultTableModel) tbl_topics.getModel();
        clearModel.setRowCount(0);
        for(Topics topic: topics){
            row_topics[0]=topic.getTitle();
            row_topics[1]=topic.getExplanation();
            row_topics[2]=topic.getYoutubeUrl();
            row_topics[3]=topic.getCourse().getName();
            mdl_topics.addRow(row_topics);
        }
    }

    private void loadQuizModel(){
        DefaultTableModel clearModel=(DefaultTableModel) tbl_quiz.getModel();
        clearModel.setRowCount(0);
        for(Quiz quiz: Quiz.getList()){
            row_quizs[0]=quiz.getId();
            row_quizs[1]=quiz.getQuestion();
            row_quizs[2]=quiz.getTopic().getTitle();
            mdl_quizs.addRow(row_quizs);
        }
    }

    private void loadCourseCombo(){
        //combo boxın ıcerısını bosaltıyoruz
        cmb_courseList.removeAllItems();
        for(Course obj: Course.getList()){
            cmb_courseList.addItem(new Item(obj.getId(),obj.getName()));
        }
    }

    private void loadTopicCombo(){
        cmb_quiz.removeAllItems();
        for(Topics topic: Topics.getList()){
            cmb_quiz.addItem(new Item(topic.getId(),topic.getTitle()));
        }
    }



}
