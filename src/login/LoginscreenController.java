package login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ravi
 */
public class LoginscreenController implements Initializable {

    public static Stage stage=null;
    public static String acc;

    @FXML
    private TextField accountno;
    @FXML
    private PasswordField pin;
    
    @FXML
    private Pane main_area;
    
    @FXML
    private void closeApp(MouseEvent event) {
      Platform.exit();
      System.exit(0);
    }
    @FXML
    public void createAccount(MouseEvent event) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/createaccount/CreateAccount.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }
    @FXML
    public void forgotPassword(MouseEvent event) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/forgotpass/Forgotpassword.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }

    @FXML
    public void loginAccount(MouseEvent event)
    {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
           String sql="SELECT * FROM userdata WHERE accountno=? and PIN=?";
            ps=conn.prepareStatement(sql);
            ps.setString(1,accountno.getText());
            ps.setString(2,pin.getText());
            acc=accountno.getText();
            rs=ps.executeQuery();

            if(rs.next())
            {
                Parent root=FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
                Scene scene=new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/design/design.css").toExternalForm());
                Stage stage=new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();
                this.stage=stage;

            }
            else
            {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setTitle("ERROR");
                a.setHeaderText("Error in Login");
                a.setContentText("Unable to Login Account no or PIN is wrong!!! TRY AGAIN !!!");
                a.showAndWait();
            }

        }
        catch (Exception e)
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Error in Login account");
            a.setContentText("Your account is not logged in due to some Technical error");
            a.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
