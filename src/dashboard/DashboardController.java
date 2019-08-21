package dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import login.Banking;
import login.LoginscreenController;
public class DashboardController implements Initializable {

    public static String ac=LoginscreenController.acc;

    private double xoffset=0;
    private double yoffset=0;

    @FXML
    private Pane dashboard_main;
    @FXML
    private Text name;
    @FXML
    private FontAwesomeIcon ico;
    @FXML
    private Circle profilepic;

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    private void minimizeApp(MouseEvent event)
    {
        Stage stage=(Stage)ico.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setData()
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
                InputStream is=rs.getBinaryStream("photo");
                OutputStream os=new FileOutputStream(new File("pic.jpg"));
                byte[] content=new byte[1024];
                int size=0;
                while((size=is.read(content))!=-1)
                {
                    os.write(content,0,size);
                }
                os.close();
                is.close();
                Image img=new Image("file:pic.jpg",false);
                profilepic.setFill(new ImagePattern(img));

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
    public void click(MouseEvent event)
    {
        xoffset=event.getSceneX();
        yoffset=event.getSceneY();
    }

    @FXML
    public void drag(MouseEvent event)
    {
        LoginscreenController.stage.setX(event.getScreenX()-xoffset);
        LoginscreenController.stage.setY(event.getScreenY()-yoffset);
    }

    @FXML
    public void accountInformation(MouseEvent event) throws IOException {
        Parent fxml=FXMLLoader.load(getClass().getResource("/accountinfo/Accountinformation.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);

    }

    @FXML
    public void withdrawEvent(MouseEvent event) throws IOException {
        Parent fxml=FXMLLoader.load(getClass().getResource("/withdraw/Withdraw.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);

    }

    public void mainScreen() throws IOException {
        Parent fxml=FXMLLoader.load(getClass().getResource("Mainscreen.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        try {
            mainScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Banking.stage.close();

    }
}
