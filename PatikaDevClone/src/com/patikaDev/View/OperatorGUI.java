package com.patikaDev.View;

import com.patikaDev.Helper.Config;
import com.patikaDev.Helper.DbConnector;
import com.patikaDev.Helper.Helper;
import com.patikaDev.Helper.Item;
import com.patikaDev.Model.Course;
import com.patikaDev.Model.Operator;
import com.patikaDev.Model.Patika;
import com.patikaDev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_userList;
    private JScrollPane scrl_userList;
    private JTable tbl_userList;
    private JTextField fld_name;
    private JTextField fld_username;
    private JTextField fld_password;
    private JComboBox cmb_type;
    private JButton btn_addUser;
    private JTextField fld_userDelete;
    private JButton btn_userDelete;
    private JTextField fld_search_name;
    private JTextField fld_search_username;
    private JComboBox cmb_search_type;
    private JButton btn_search;
    private JPanel pnl_patikaList;
    private JTable tbl_patikaList;
    private JTextField fld_PatikaAdd;
    private JButton btn_patikaAdd;
    private JPanel pnl_coursesList;
    private JTable tbl_courseList;
    private JTextField fld_courseName;
    private JTextField fld_courseLang;
    private JComboBox cmb_coursePat;
    private JComboBox cmb_courseUser;
    private JButton btn_courseAdd;
    private final Operator operator;
    private DefaultTableModel mdl_userList;
    private Object[] row_userList;
    private DefaultTableModel mdl_patikaList;
    private Object[] row_patikaList;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_courseList;
    private Object[] row_courseList;
    public OperatorGUI(Operator operator){
        this.operator = operator;
        add(wrapper);
        setSize(1000,500);
        int x= Helper.screenCenter("x",getSize());
        int y=Helper.screenCenter("y",getSize());
        setLocation(x,y);
        // x bastıgımızda javanın arkada calısmasını iptal etme
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TİTLE);
        setVisible(true);
        lbl_welcome.setText(operator.getName());
        DbConnector.getInstance();

        //                                  KULLANICI LİSTESİ
        // Table Modelini olusturma
        // Tabloda elle id nin degistirilmesini önledik
        mdl_userList=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };
        // colon isimlerini bir array içerisinde tuttuk
        Object[] col_userList={"İD","Kullanıcı Adı","Ad-Soyad","Şifre","Kullanıcı Tipi"};
        //colon isimlerini bunları modelimize ekledik
        mdl_userList.setColumnIdentifiers(col_userList);
        // modelimizide tablomuzun modeli olarak belirledik
        tbl_userList.setModel(mdl_userList);
        // tablo baslıklarının yerinin değiştirilmesini iptal etme
        tbl_userList.getTableHeader().setReorderingAllowed(false);
        tbl_userList.getColumnModel().getColumn(0).setMaxWidth(50);

        //Tabloyu dinliyoruz ve secilen rowun id sini alıypruz
        tbl_userList.getSelectionModel().addListSelectionListener(e -> {
            try{
                String selected_userID=tbl_userList.getValueAt(tbl_userList.getSelectedRow(),0).toString();
                fld_userDelete.setText(selected_userID);
            }catch (Exception ex){

            }
        });

        //Tabloyu dinleyerek yapılan update işlemini veri tabanında guncelliyoruz
        tbl_userList.getModel().addTableModelListener(e -> {
            if(e.getType()== TableModelEvent.UPDATE){
                int userID=Integer.parseInt(tbl_userList.getValueAt(tbl_userList.getSelectedRow(),0).toString());
                String name=tbl_userList.getValueAt(tbl_userList.getSelectedRow(),2).toString();
                String userName=tbl_userList.getValueAt(tbl_userList.getSelectedRow(),1).toString();
                String password=tbl_userList.getValueAt(tbl_userList.getSelectedRow(),3).toString();
                String type=tbl_userList.getValueAt(tbl_userList.getSelectedRow(),4).toString();
                if (User.update(userID,name,userName,password,type)){
                    Helper.showMsg("success");

                }
                loadUserModel();
                loadEducatorCombo();
                loadCouserModel();
            }
        });

        row_userList=new Object[col_userList.length];
        //Listeyi yuklemek için fonksiyonumuzu cagırıyoruz
        loadUserModel();



        //                                          PATİKALAR
        //popup menu olusturma (sag tık )
        patikaMenu=new JPopupMenu();
        // sag tıkta guncelle ve sil komutları acılacak
        JMenuItem update=new JMenuItem("Güncelle");
        JMenuItem delete=new JMenuItem("Sil");
        patikaMenu.add(update);
        patikaMenu.add(delete);

        //update Menusunu listener ekşeyerek hangi row üzerinden işlem yapılacagını diğer menuye gecişi yapacagız
        update.addActionListener(e -> {
            //seçilen satır ve 0 ıncı kolun bize secilen patikanın idsini verecek
            int selectedId=Integer.parseInt(tbl_patikaList.getValueAt(tbl_patikaList.getSelectedRow(),0).toString());
            //UpdateGUI mızı cagırıyoruz
            UpdatePatikaGUI updateGUI=new UpdatePatikaGUI(Patika.getFetch(selectedId));
            //guncelle penceresi kapandıgı zaman listemizi yenilemek için pencerenin kapanma olayına listener ekliyoruz
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCouserModel();
                }
            });
        });

        //Delete işlemi için listener ekliyoruz
        delete.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int selectedId=Integer.parseInt(tbl_patikaList.getValueAt(tbl_patikaList.getSelectedRow(),0).toString());
                Patika.delete(selectedId);
                Helper.showMsg("success");
                loadPatikaModel();
                loadPatikaCombo();
                loadCouserModel();
            }
        });

        //patikalarımızın table modelini olusturduk
        mdl_patikaList=new DefaultTableModel();
        // colon isimlerini ver colonları olusturduk
        Object[] col_patikaList={"ID","Patika Adı"};
        // colonlarımızı modelımıze yukledık
        mdl_patikaList.setColumnIdentifiers(col_patikaList);
        row_patikaList=new Object[col_patikaList.length];
        loadPatikaModel();
        loadPatikaCombo();
        tbl_patikaList.setModel(mdl_patikaList);
        //popup menuyu modelimize ekledik
        tbl_patikaList.setComponentPopupMenu(patikaMenu);
        //colonlarının yerinin degistirilmesini engelledik
        tbl_patikaList.getTableHeader().setReorderingAllowed(false);
        //id colonun genişliğini azalttık
        tbl_patikaList.getColumnModel().getColumn(0).setMaxWidth(50);

        // mouse nın sag tıkladıgı yerı bulup bu row uzerınde guncelleme ve sılme ıslemı yapacagız
        // bunun ıcın mouse lıstener kullanıyoruz
        tbl_patikaList.addMouseListener(new MouseAdapter() {
            @Override
            //mouse tıklandıgında basıldıgında ne olacak
            public void mousePressed(MouseEvent e) {
                // point sınıfı bizim için x ve y yani kordinat tutan bir sınıf
                //getPoint ile tıklanınlan yerin kordinatlarını alıyoruz
                Point point=e.getPoint();
                // rowAtpoint ile kordinatlardaki rowu buluyoruz
                int selected_row=tbl_patikaList.rowAtPoint(point);
                //sag tıklandıgında da rowun secılmesı ıcın
                tbl_patikaList.setRowSelectionInterval(selected_row,selected_row);
            }
        });




        //                                          BUTONLAR
        //Ekle butonuna basıldıgında ne olacagını belırledıgımız lıstenır
        btn_addUser.addActionListener(e -> {
            //once butun fieldların dolu olup olmadıgını kontrol ediyoruz
            if(Helper.isFieldEmpty(fld_name)|| Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_password)){
                // Hata mesajını hazırladık
                Helper.showMsg("fill");
            }else{
                String userType=cmb_type.getSelectedItem().toString();
                if(User.add(fld_name.getText(),fld_username.getText(),fld_password.getText(),userType)){
                    Helper.showMsg("success");
                    loadUserModel();
                    loadEducatorCombo();
                    // işlem başarılı ise fieldlarmızdaki yazıları temizliyoruz
                    fld_name.setText(null);
                    fld_username.setText(null);
                    fld_password.setText(null);
                }

            }
        });


        //delete btn listener
        btn_userDelete.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_userDelete)){
                Helper.showMsg("fill");
            }else {
                //field daki değeri integer e cevirdik
                if(Helper.confirm("sure")){
                    int id=Integer.parseInt(fld_userDelete.getText());
                    if(User.delete(id)){
                        Helper.showMsg("success");
                        loadUserModel();
                        loadEducatorCombo();
                        loadCouserModel();
                        fld_userDelete.setText(null) ;
                    }else{
                        Helper.showMsg("error");
                    }
                }
            }
        });

        //ARAMA BUTONU LISTENERI
        btn_search.addActionListener(e -> {
            String name=fld_search_name.getText();
            String userName=fld_search_username.getText();
            String type=cmb_search_type.getSelectedItem().toString();
            String query= User.searchQuery(name,userName,type);
            ArrayList<User> searchingUser=User.searchUserlist(query);
            //Tablomuzu yenilemek için load fonkiyonuna overloading yaptık ve
            // artık bizim search yaptıgımız listeyi sıralayacak
            loadUserModel(searchingUser);
        });

        //cıkıs butonu listener
        btn_logout.addActionListener(e -> {
            //dispose cıkıs yapmamızı saglıyor
            dispose();
            //yeniden login ekranına donus yapıyoruz
            LoginGUI login=new LoginGUI();
        });

        //patika add btn listener
        btn_patikaAdd.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_PatikaAdd)){
                Helper.showMsg("fill");
            }else{
                if(Patika.add(fld_PatikaAdd.getText())){
                    Helper.showMsg("success");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_PatikaAdd.setText(null);
                }else{
                    Helper.showMsg("error");
                }
            }
        });

        //CourseList
        //Table Modelimizi oluşturduk
        mdl_courseList=new DefaultTableModel();
        //Colon isimlerini verdik
        Object[] col_courseList={"ID","Ders Adı", "Programlama Dili","Patika","Eğitmen"};
        //colun isimlerimizi tabloya verdik
        mdl_courseList.setColumnIdentifiers(col_courseList);
        row_courseList=new Object[col_courseList.length];
        loadCouserModel();
        tbl_courseList.setModel(mdl_courseList);
        tbl_courseList.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_courseList.getTableHeader().setReorderingAllowed(false);

        //Combobox içerisini doldurmak için bir fonksiyon yazıyoruz
        loadPatikaCombo();
        loadEducatorCombo();


        //Course Ekleme Butonu

        btn_courseAdd.addActionListener(e -> {
            Item patikaCombo=(Item) cmb_coursePat.getSelectedItem();
            Item userCombo=(Item) cmb_courseUser.getSelectedItem();
            if(Helper.isFieldEmpty(fld_courseName) || Helper.isFieldEmpty(fld_courseLang)){
                Helper.showMsg("fill");
            }else{
                Course.add(userCombo.getKey(), patikaCombo.getKey(), fld_courseName.getText(),fld_courseLang.getText());
                Helper.showMsg("success");
                loadCouserModel();
                fld_courseName.setText(null);
                fld_courseLang.setText(null);
            }
        });
    }

    private void loadPatikaCombo(){
        //combo boxın ıcerısını bosaltıyoruz
        cmb_coursePat.removeAllItems();
        for(Patika obj: Patika.getList()){
            cmb_coursePat.addItem(new Item(obj.getId(),obj.getName()));
        }
    }

    //comboBox içerisine eğitmenleri doldurma için
    private void loadEducatorCombo(){
        cmb_courseUser.removeAllItems();
        for(User obj:User.getList()){
            if(obj.getUserType().equals("educator")){
                cmb_courseUser.addItem(new Item(obj.getId(),obj.getName()));
            }
        }

    }

    private void loadCouserModel() {
        DefaultTableModel clearModel= (DefaultTableModel) tbl_courseList.getModel();
        clearModel.setRowCount(0);
        for(Course obj: Course.getList()){
            row_courseList[0]=obj.getId();
            row_courseList[1]=obj.getName();
            row_courseList[2]=obj.getLang();
            row_courseList[3]=obj.getPatika().getName();
            row_courseList[4]=obj.getEducator().getName();
            mdl_courseList.addRow(row_courseList);


        }
    }
    private void loadPatikaModel() {
        DefaultTableModel clearTable= (DefaultTableModel) tbl_patikaList.getModel();
        clearTable.setRowCount(0);

        for(Patika obj: Patika.getList()){
            row_patikaList[0]=obj.getId();
            row_patikaList[1]=obj.getName();
            mdl_patikaList.addRow(row_patikaList);
        }
    }

    public void loadUserModel(){
        // kullanıcı listemizi sıfırlıyoruz
        // Boylece tablomuzu refresh etmis olıuyoruz
        DefaultTableModel clearModel= (DefaultTableModel) tbl_userList.getModel();
        clearModel.setRowCount(0);

        // user getList ile veri tabanımızda butun userları gezdik
        // bunları row isminde obj list timize attık
        // row obj mizide addrow ile modelimize ekledik
        for(User obj: User.getList()){
            row_userList[0]=obj.getId();
            row_userList[1]=obj.getUserName();
            row_userList[2]=obj.getName();
            row_userList[3]=obj.getPassword();
            row_userList[4]=obj.getUserType();
            mdl_userList.addRow(row_userList);
        }

    }
    //OVERLOADING YAPIYORUZ
    public void loadUserModel(ArrayList<User> list){
        // kullanıcı listemizi sıfırlıyoruz
        // Boylece tablomuzu refresh etmis olıuyoruz
        DefaultTableModel clearModel= (DefaultTableModel) tbl_userList.getModel();
        clearModel.setRowCount(0);

        // user getList ile veri tabanımızda butun userları gezdik
        // bunları row isminde obj list timize attık
        // row obj mizide addrow ile modelimize ekledik
        for(User obj: list){
            row_userList[0]=obj.getId();
            row_userList[1]=obj.getUserName();
            row_userList[2]=obj.getName();
            row_userList[3]=obj.getPassword();
            row_userList[4]=obj.getUserType();
            mdl_userList.addRow(row_userList);
        }

    }

    public static void main(String[] args) {
        Operator operator=new Operator();
        operator.setName("Murat ARSLAN");
        operator.setId(1);
        operator.setPassword("12345");
        operator.setUserName("Murat");
        operator.setUserType("operator");

        OperatorGUI opGUI=new OperatorGUI(operator);
        Helper.setLayout();
    }

}
