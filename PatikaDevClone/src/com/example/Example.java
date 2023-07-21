package com.example;

import javax.swing.*;
import java.awt.*;

public class Example extends JFrame {

    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;

    public Example(){
        setContentPane(wrapper);
        setSize(400,400);
        setTitle("PatikaDev");
        setVisible(true);
        int x=(Toolkit.getDefaultToolkit().getScreenSize().width-getSize().width)/2;
        int y=(Toolkit.getDefaultToolkit().getScreenSize().height-getSize().height)/2;
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
