package forgotpass;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import login.Banking;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ForgotpasswordController implements Initializable {

    @FXML
    private TextField AccountNo;
    @FXML
    private TextField Ans;
    @FXML
    private ComboBox<String> Security;

    ObservableList<String> questions = FXCollections.observableArrayList("What is your pet name?","Where is your hometown?","Who is your favourite Writer?");

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
   @FXML
    public void backToLogin(MouseEvent event) throws IOException {
        Banking.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/login/Loginscreen.fxml")));
    }

    @FXML
    public void passwordRecovery(MouseEvent event)
    {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
            String sql="SELECT * FROM userdata WHERE accountno=? and security=? and answer=?";
            ps=conn.prepareStatement(sql);
            ps.setString(1,AccountNo.getText());
            ps.setString(2,Security.getValue());
            ps.setString(3,Ans.getText());

            rs=ps.executeQuery();

            if(rs.next())
            {
                Alert a=new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Password Recovery");
                a.setHeaderText("Credentials Correct");
                a.setContentText("Your Acoount No:  "+rs.getString("accountno")+"\n PIN :- "+rs.getString("pin"));
                a.showAndWait();
            }
            else
            {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setTitle("ERROR");
                a.setHeaderText("Wrong Details");
                a.setContentText("!!! TRY AGAIN !!!");
                a.showAndWait();
            }

        }
        catch (Exception e)
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Error in Password Recovery");
            a.setContentText("Password is not recovered due to some Technical error");
            a.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Security.setItems(questions);
    }
}
