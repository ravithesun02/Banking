package withdraw;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import login.LoginscreenController;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class WithdrawController implements Initializable {
    public static String ac= LoginscreenController.acc;
    @FXML
    private Label account_field;
    @FXML
    private Label Amt;
    @FXML
    private TextField amount_field;
    @FXML
    private TextField pin_field;

    Calendar cal=Calendar.getInstance();
    int year=cal.get(Calendar.YEAR);
    int month=cal.get(Calendar.MONTH);
    int day=cal.get(Calendar.DAY_OF_MONTH);
    int hour=cal.get(Calendar.HOUR);
    int min=cal.get(Calendar.MINUTE);
    int sec=cal.get(Calendar.SECOND);
    int daynight=cal.get(Calendar.AM_PM);

    DateFormat dateformate=new SimpleDateFormat("yyyy/MM/dd");
    Date d=new Date();
    String date=dateformate.format(d);

    LocalTime localTime=LocalTime.now();
    DateTimeFormatter dt=DateTimeFormatter.ofPattern("hh:mm:ss a");
    String time=localTime.format(dt);

    public void setInfo()
    {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
            String sql="SELECT * FROM userdata WHERE accountno=?";
            ps=conn.prepareStatement(sql);
            ps.setString(1,ac);

            rs=ps.executeQuery();

            if(rs.next())
            {
                account_field.setText(rs.getString("accountno"));
                Amt.setText(rs.getString("balance"));

            }
            else
            {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setTitle("ERROR");
                a.setHeaderText("Error in Login");
                a.setContentText("Unable to Login!!! TRY AGAIN !!!");
                a.showAndWait();
            }

        }
        catch (Exception e)
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Error in Login account");
            a.setContentText("There is some Technical error .");
            a.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfo();

    }

    public void withdrawButton(javafx.scene.input.MouseEvent event) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
            String sql="SELECT * FROM userdata WHERE accountno=? and pin=?";
            ps=conn.prepareStatement(sql);
            ps.setString(1,ac);
            ps.setString(2,pin_field.getText());

            rs=ps.executeQuery();

            if(rs.next())
            {
                int wda=Integer.parseInt(amount_field.getText());
                int ta=Integer.parseInt(Amt.getText());
                if(wda>ta)
                {
                    Alert a=new Alert(Alert.AlertType.ERROR);
                    a.setTitle("ERROR");
                    a.setHeaderText("Low Balance");
                    a.setContentText("Could not process withdrawl");
                    a.showAndWait();
                }
                else
                {
                    int total=ta-wda;
                    String sql1="UPDATE userdata SET balance='"+total+"' WHERE accountno='"+ac+"'";
                    ps=conn.prepareStatement(sql1);
                    ps.execute();
                    String sql2="INSERT INTO withdraw(accountno,withdraw,remaining,date,time) VALUES(?,?,?,?,?)";
                    ps=conn.prepareStatement(sql2);
                    ps.setString(1,ac);
                    ps.setString(2,amount_field.getText());
                    ps.setString(3,Integer.toString(total));
                    ps.setString(4,date);
                    ps.setString(5,time);
                    int i=ps.executeUpdate();

                    if(i>0)
                    {
                        Alert a=new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Amount Withdrawn");
                        a.setHeaderText("Amount Withdrawn successfully");
                        a.setContentText("Amount withdrawn "+wda+"\n Current Balance "+total);
                        a.showAndWait();

                        amount_field.clear();
                        pin_field.clear();
                        Amt.setText(String.valueOf(total));
                    }
                }
            }
            else
            {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setTitle("ERROR");
                a.setHeaderText("Error in Login");
                a.setContentText("Unable to Login!!! TRY AGAIN !!!");
                a.showAndWait();
            }

        }
        catch (Exception e)
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Error in Login account");
            a.setContentText("There is some Technical error .");
            a.showAndWait();
        }
    }
}
