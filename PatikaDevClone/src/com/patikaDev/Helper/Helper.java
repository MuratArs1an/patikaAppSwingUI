package com.patikaDev.Helper;

import javax.swing.*;
import java.awt.*;

public class Helper {
    public static int screenCenter(String point, Dimension size){
        int screenPoint=0;
        switch (point){
            case "x":
                screenPoint=(Toolkit.getDefaultToolkit().getScreenSize().width-size.width)/2;
                break;
            case "y":
                screenPoint=(Toolkit.getDefaultToolkit().getScreenSize().height-size.height)/2;
                break;
            default:
                screenPoint=0;

        }
        return screenPoint;
    }

    public static void setLayout(){
        for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
            if("Metal".equals(info.getName())){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                         InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //Kullanıcı ekleme fieldlarının bos olup olmadıgı kontrol eden fonksiyon
    // bossa false doner
    public static boolean isFieldEmpty(JTextField textField){
        return textField.getText().trim().isEmpty();
    }

    public static void showMsg(String str){
        String msj;
        String err;
        switch (str){
            case "fill":
                msj="Lütfen Bütün Alanları Doldurunuz";
                err="ERROR";
                break;
            case "success":
                msj="İşlem Başarılı";
                err="SUCCESS";
                break;
            case "error":
                msj="Bir Hata Olustu";
                err="ERROR";
                break;
            case "repeat":
                msj="Bu derse zaten kayıtlısınız";
                err="UYARI";
                break;
            default:
                msj=str;
                err="HATA";
        }
        JOptionPane.showMessageDialog(null,msj,err,JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str){
        String msg;
        switch (str){
            case "sure":
                msg="Emin misiniz?";
                break;
            default:
                msg=str;
        }
        return JOptionPane.showConfirmDialog(null,msg,"Are You Sure",JOptionPane.YES_NO_OPTION)==0;
    }

}
