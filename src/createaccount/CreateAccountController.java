package createaccount;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import login.Banking;


/**
 *
 * @author ravi
 */
public class CreateAccountController implements Initializable {
    private FileChooser filechooser=new FileChooser();
    private File file;
    private FileInputStream fis;
    @FXML
    private TextField name;
    @FXML
    private TextField idcardno;
    @FXML
    private TextField mobileno;
    @FXML
    private TextField City;
    @FXML
    private TextField Address;
    @FXML
    private Text AccountNo;
    @FXML
    private TextField Balance;
    @FXML
    private PasswordField PIN;
    @FXML
    private TextField Ans;
    @FXML
    private ComboBox<String> Gender;
    @FXML
    private ComboBox<String> Martial;
    @FXML
    private ComboBox<String> Religion;
    @FXML
    private ComboBox<String> Type;
    @FXML
    private ComboBox<String> Security;
    @FXML
    private DatePicker dob;
    @FXML
    private ImageView Photo;

    ObservableList<String> list=FXCollections.observableArrayList("MALE","FEMALE","OTHER","PREFER NOT TO SAY");
    ObservableList<String> list1=FXCollections.observableArrayList("Single","Married");
    ObservableList<String> list2=FXCollections.observableArrayList("Hindu","Islam","Christian","PREFER NOT TO SAY");
    ObservableList<String> list3=FXCollections.observableArrayList("Saving","Current");
    ObservableList<String> list4=FXCollections.observableArrayList("What is your pet name?","Where is your hometown?","Who is your favourite Writer?");


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
    public void createNewAccount(MouseEvent event)
    {
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
            String sql="INSERT INTO userdata (Name,IDC,mobile,gender,martial,religion,DOB,city,address,accountno,balance,accounttype,pin,security,answer,photo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps=conn.prepareStatement(sql);

            Random rand=new Random();
            int acc=rand.nextInt();
            AccountNo.setText(String.valueOf(acc));
            ps.setString(1,name.getText());
            ps.setString(2,idcardno.getText());
            ps.setString(3,mobileno.getText());
            ps.setString(4,Gender.getValue());
            ps.setString(5,Martial.getValue());
            ps.setString(6,Religion.getValue());
            ps.setString(7,((TextField)dob.getEditor()).getText());
            ps.setString(8,City.getText());
            ps.setString(9,Address.getText());
            ps.setString(10,String.valueOf(acc));
            ps.setString(11,Balance.getText());
            ps.setString(12,Type.getValue());
            ps.setString(13,PIN.getText());
            ps.setString(14,Security.getValue());
            ps.setString(15,Ans.getText());
            fis=new FileInputStream(file);
            ps.setBinaryStream(16,(InputStream)fis,(int)file.length());

            int i=ps.executeUpdate();
            if(i>0)
            {
                Alert a=new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Account Created");
                a.setHeaderText("Account Created Successfully");
                a.setContentText("Your account is  created successfully.You can now LOGIN using Account No and PIN.");
                a.showAndWait();
            }
            else
            {
                Alert a=new Alert(Alert.AlertType.ERROR);
                a.setTitle("ERROR");
                a.setHeaderText("Error in creating account");
                a.setContentText("Your account is not created due to some error.!!! TRY AGAIN !!!");
                a.showAndWait();
            }

        }
        catch (Exception e)
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Error in creating account");
            a.setContentText("Your account is not created due to some Technical error");
            a.showAndWait();
        }
    }

    @FXML
    public void setProfilePhoto(MouseEvent event)
    {
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.jpg","*.png"));
        file=filechooser.showOpenDialog(null);
        if(file!=null)
        {
            Image img=new Image(file.toURI().toString(),150,150,true,true);
            Photo.setImage(img);
            Photo.setPreserveRatio(true);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Gender.setItems(list);
        Martial.setItems(list1);
        Religion.setItems(list2);
        Type.setItems(list3);
        Security.setItems(list4);

    }
}
