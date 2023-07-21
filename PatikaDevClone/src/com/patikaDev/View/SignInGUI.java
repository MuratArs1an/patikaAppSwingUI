package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Model.User;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_name;
    private JTextField fld_username;
    private JTextField fld_password;
    private JButton btn_signIn;
    public SignInGUI() {

        add(wrapper);
        setSize(400,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(Helper.screenCenter("x",getSize()), Helper.screenCenter("y",getSize()));
        setTitle(Config.PROJECT_TİTLE);
        setResizable(false);
        setVisible(true);



        btn_signIn.addActionListener(e -> {
            String name=fld_name.getText();
            String username=fld_username.getText();
            String password=fld_password.getText();
            User.add(name,username,password,"student");
            LoginGUI login = new LoginGUI();
        });
}
}
