package accountinfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import login.LoginscreenController;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AccountinformationController implements Initializable {
    public static String ac= LoginscreenController.acc;
    @FXML
    private Text account;
    @FXML
    private Text gen;
    @FXML
    private Text type;
    @FXML
    private Label BAL;
    @FXML
    private Text Mobile;
    @FXML
    private Pane account_information;

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
                account.setText(rs.getString("accountno"));
                gen.setText(rs.getString("gender"));
                type.setText(rs.getString("accounttype"));
                BAL.setText(rs.getString("balance"));
                Mobile.setText(rs.getString("mobile"));

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

    @FXML
    public void withdrawEvent(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/withdraw/Withdraw.fxml"));
        account_information.getChildren().removeAll();
        account_information.getChildren().addAll(fxml);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfo();
    }
}
