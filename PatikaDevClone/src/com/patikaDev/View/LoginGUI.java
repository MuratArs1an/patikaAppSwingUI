package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Model.Educator;
import com.patikaDev.Model.Operator;
import com.patikaDev.Model.Student;
import com.patikaDev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wTop;
    private JPanel wBottom;
    private JTextField fld_userName;
    private JPasswordField fld_userPass;
    private JButton btn_login;
    private JButton btn_signIn;

    public LoginGUI() {
        add(wrapper);
        setSize(400,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(Helper.screenCenter("x",getSize()), Helper.screenCenter("y",getSize()));
        setTitle(Config.PROJECT_TİTLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_userName) || Helper.isFieldEmpty(fld_userPass)){
                Helper.showMsg("fill");
            }else{
                User u=User.getFetch(fld_userName.getText(),fld_userPass.getText());
                if(u==null){
                    Helper.showMsg("Kullanıcı Bulunamadı ! ");
                }else{
                    switch (u.getUserType()){
                        case "operator":
                            OperatorGUI opGUI=new OperatorGUI((Operator) u);
                            break;
                        case "student":
                            StudentGUI stuGUI=new StudentGUI((Student) u);
                            break;
                        case "educator":
                            EducatorGUI eduGUI=new EducatorGUI((Educator) u);
                            break;
                    }
                    dispose();
                }
            }
        });
        btn_signIn.addActionListener(e -> {
            SignInGUI signIn =new SignInGUI();
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login =new LoginGUI();
    }

}
