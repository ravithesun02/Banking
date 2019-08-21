package dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import login.LoginscreenController;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class MainscreenController implements Initializable {

    public static String ac= LoginscreenController.acc;

    @FXML
    private Label name;
    @FXML
    private Label body;

    public void setMainInfo()
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
              name.setText(rs.getString("Name"));

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
        body.setText("Türkiye Cumhuriyeti Ziraat Bankası, commonly known as Ziraat Bank, \nis a state-owned bank in Turkey founded in 1863. Ziraat Bank is the \n second biggest Turkish bank after Garanti Bankası since 2012\n according to the Bankscope database measured by total assets in USD.");
        setMainInfo();
    }
}
