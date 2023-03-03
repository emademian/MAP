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
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Objects;

public class ForgotPassController {
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
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label warningLabel;

    public void labelDesign(String msg, String color) {
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

    public void onNewPassButtonClick() {
        String username1 = username.getText();
        String password1 = password.getText();
        String confirmPassword1 = confirmPassword.getText();
        if (username1.isEmpty() || password1.isEmpty() || confirmPassword1.isEmpty()) {
            labelDesign("Please fill all fields!", "FF0000");
        } else {
            User user = null;
            for (User user1 : service.getAllUsers()) {
                if (user1.getUsername().equals(username1)) {
                    user = user1;
                }
            }
            if (user == null) {
                labelDesign("Incorrect username!", "FF0000");
            } else try {
                if (!Objects.equals(password1, confirmPassword1)) {
                    labelDesign("Passwords do not match!", "FF0000");
                } else {
                    user.setPassword(password1);
                    service.updateUser(user);
                    labelDesign("Password successfully changed!", "4CBB17");
                }
            } catch (Exception e) {
                labelDesign(e.getMessage(), "FF0000");
            }
        }
    }
}
