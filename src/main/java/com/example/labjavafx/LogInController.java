package com.example.labjavafx;

import com.example.labjavafx.model.User;
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
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LogInController {
    UserValidator userValidator = new UserValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();
    UserRepositoryDB repo1 = new UserRepositoryDB(userValidator);
    FriendshipRepositoryDB repo2 = new FriendshipRepositoryDB(friendshipValidator);
    RequestRepositoryDB repo3 = new RequestRepositoryDB();
    MessageValidator msgValidator=new MessageValidator();
    MessageRepositoryDB repo4=new MessageRepositoryDB(msgValidator);
    Service service = Service.getInstance(repo1, repo2, repo3,repo4);
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private CheckBox showPassCheck;

    @FXML
    private TextField passwordShow;

    @FXML
    private Label warningLabel;

    public void labelDesign(String msg) {
        Color color = Color.web("FF0000", 0.18);
        BackgroundFill backgroundFill = new BackgroundFill(color, new CornerRadii(5), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        warningLabel.setBackground(background);
        warningLabel.setText(msg);
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setWrapText(true);
        warningLabel.setVisible(true);
    }

    @FXML
    public void onLogInButtonClick(ActionEvent event) throws IOException {
        String username1 = username.getText();
        String password1 = password.getText();
        String password2 = passwordShow.getText();
        if (username1.isEmpty() || password1.isEmpty()) {
            labelDesign("Please fill all fields!");
        } else {
            User user1 = null;
            for (User user : service.getAllUsers()) {
                if (Objects.equals(user.getUsername(), username1) && (Objects.equals(user.getPassword(), password1) || Objects.equals(user.getPassword(), password2))) {
                    user1 = user;
                }
            }
            if (user1 != null) {
                service.setUser(user1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                stage.setTitle("Hello, " + user1.getFirstName() + " " + user1.getLastName() + "!");
                stage.setScene(scene);
                stage.show();
            } else {
                labelDesign("Incorrect username or password!");
            }
        }
    }

    @FXML
    public void onSignInButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sign_up.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        stage.setTitle("SOCIAL NETWORK SIGN UP");
        stage.setScene(scene);
        stage.show();
    }

    public void onShowPasswordChecked() {
        if (showPassCheck.isSelected()) {
            passwordShow.setText(password.getText());
            passwordShow.setVisible(true);
            password.setVisible(false);
        } else {
            password.setText(passwordShow.getText());
            password.setVisible(true);
            passwordShow.setVisible(false);
        }

    }

    public void onForgotPassButtonClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forgot_pass.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 400);
        stage.setTitle("CHANGE PASSWORD");
        stage.setScene(scene);
        stage.show();
    }
}
