package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Model.Patika;
import com.patikaDev.Model.Topics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePatikaGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_updatePatika;
    private JButton btn_updatePatika;
    private Patika patika;
    private Topics topic;

    public UpdatePatikaGUI(Patika patika){
        this.patika=patika;
        add(wrapper);
        setSize(300,150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setTitle(Config.PROJECT_TİTLE);
        setVisible(true);
        //update fieldımızın içine patikanın adını attık
        fld_updatePatika.setText(patika.getName());

        //guncelle butonunun listenerı
        btn_updatePatika.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_updatePatika)){
                Helper.showMsg("fill");
            }else{
                if(Patika.update(patika.getId(),fld_updatePatika.getText())){
                    Helper.showMsg("Success");
                }
                dispose();
            }
        });
    }
}
