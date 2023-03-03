package com.example.labjavafx;

import com.example.labjavafx.model.validator.FriendshipValidator;
import com.example.labjavafx.model.validator.MessageValidator;
import com.example.labjavafx.model.validator.UserValidator;
import com.example.labjavafx.repository.FriendshipRepositoryDB;
import com.example.labjavafx.repository.MessageRepositoryDB;
import com.example.labjavafx.repository.RequestRepositoryDB;
import com.example.labjavafx.repository.UserRepositoryDB;
import com.example.labjavafx.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignUpController {
    UserValidator userValidator = new UserValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();
    UserRepositoryDB repo1 = new UserRepositoryDB(userValidator);
    FriendshipRepositoryDB repo2 = new FriendshipRepositoryDB(friendshipValidator);
    RequestRepositoryDB repo3 = new RequestRepositoryDB();
    MessageValidator msgValidator=new MessageValidator();
    MessageRepositoryDB repo4=new MessageRepositoryDB(msgValidator);
    Service service = Service.getInstance(repo1, repo2, repo3,repo4);
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField age;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label warningLabel;

    public void labelDesign(String msg,String color) {
        Color colorBackground = Color.web(color, 0.18);
        BackgroundFill backgroundFill = new BackgroundFill(colorBackground, new CornerRadii(5), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        warningLabel.setBackground(background);
        warningLabel.setText(msg);
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setTextFill(Paint.valueOf(color));
        warningLabel.setWrapText(true);
        warningLabel.setVisible(true);
    }
    public void onCreateUserButtonClick() throws IOException {
        String firstName1 = firstName.getText();
        String lastName1 = lastName.getText();
        int age1 = Integer.parseInt(age.getText());
        String email1 = email.getText();
        String username1 = username.getText();
        String password1 = password.getText();
        String confirmPassword1 = confirmPassword.getText();
        if (firstName1.isEmpty() || lastName1.isEmpty() || email1.isEmpty() || age1 == 0 || username1.isEmpty() || password1.isEmpty()) {
            labelDesign("Please fill all fields!","FF0000");
        } else {
            try {
                if (!Objects.equals(password1, confirmPassword1)) {
                    labelDesign("Passwords do not match!","FF0000");
                } else {
                    service.addUser(firstName1, lastName1, age1, email1, username1, password1);
                    labelDesign("Account successfully created!","4CBB17");
                    firstName.setText("");
                    lastName.setText("");
                    age.setText("");
                    email.setText("");
                    username.setText("");
                    password.setText("");
                    confirmPassword.setText("");
                }
            } catch (Exception e) {
                labelDesign(e.getMessage(),"FF0000");
            }
        }
    }

    public void onLogInButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log_in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("SOCIAL NETWORK");
        stage.setScene(scene);
        stage.show();
    }
}
