package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Helper.Item;
import com.patikaDev.Model.Comment;
import com.patikaDev.Model.Course;
import com.patikaDev.Model.Student;
import com.patikaDev.Model.Topics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommentGUI extends JFrame {
    private JPanel wrapper;
    private JComboBox cmb_star;
    private JButton btn_save;
    private JButton btn_exit;
    private JTextField fld_comment;
    private JTable tbl_commnets;
    private DefaultTableModel mdl_comments;
    private Object [] row_comments;
    private Topics topics;
    private Student student;

    public CommentGUI(Topics topics, Student student){
        this.student=student;
        this.topics=topics;
        add(wrapper);
        setSize(500,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(Helper.screenCenter("x",getSize()), Helper.screenCenter("y",getSize()));
        setTitle(Config.PROJECT_TİTLE);
        setResizable(false);
        setVisible(true);

        mdl_comments=new DefaultTableModel();
        Object[] col_comments={"ID","Yorum","Puan"};
        mdl_comments.setColumnIdentifiers(col_comments);
        row_comments=new Object[col_comments.length];
        loadCommentModel();
        tbl_commnets.setModel(mdl_comments);


        btn_exit.addActionListener(e -> {
            dispose();
            StudentGUI studentGUI=new StudentGUI(student);
        });

        btn_save.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_comment)){
                Helper.showMsg("fill");
            }else{
                Comment.add(fld_comment.getText(),student.getId(),topics.getId(),cmb_star.getSelectedItem().toString());
                Helper.showMsg("success");
                loadCommentModel();
                fld_comment.setText(null);
            }
        });
    }

    private void loadCommentModel(){
        DefaultTableModel clearModel= (DefaultTableModel) tbl_commnets.getModel();
        clearModel.setRowCount(0);
        for(Comment comment: Comment.getList(topics.getId())){
                row_comments[0]=comment.getId();
                row_comments[1]=comment.getComment();
                row_comments[2]=comment.getPoint();
                mdl_comments.addRow(row_comments);
        }
    }





}
