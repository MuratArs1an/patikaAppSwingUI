package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Model.Patika;
import com.patikaDev.Model.Topics;

import javax.swing.*;

public class UpdateTopicsGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_topicUpdate;
    private JButton btn_topicUpdate;
    private Topics topic;

    public UpdateTopicsGUI(Topics topic){
        this.topic=topic;
        add(wrapper);
        setSize(300,150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setTitle(Config.PROJECT_TİTLE);
        setVisible(true);
        //update fieldımızın içine patikanın adını attık
        fld_topicUpdate.setText(topic.getTitle());

        //guncelle butonunun listenerı
        btn_topicUpdate.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_topicUpdate)){
                Helper.showMsg("fill");
            }else{
                if(Topics.update(topic.getId(),fld_topicUpdate.getText())){
                    Helper.showMsg("Success");
                }
                dispose();
            }
        });
    }
}
