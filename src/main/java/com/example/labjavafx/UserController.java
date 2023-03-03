package com.example.labjavafx;


import com.example.labjavafx.model.Friendship;
import com.example.labjavafx.model.Message;
import com.example.labjavafx.model.Request;
import com.example.labjavafx.model.User;
import com.example.labjavafx.model.validator.FriendshipValidator;
import com.example.labjavafx.model.validator.MessageValidator;
import com.example.labjavafx.model.validator.UserValidator;
import com.example.labjavafx.repository.FriendshipRepositoryDB;
import com.example.labjavafx.repository.MessageRepositoryDB;
import com.example.labjavafx.repository.RequestRepositoryDB;
import com.example.labjavafx.repository.UserRepositoryDB;
import com.example.labjavafx.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserController {
    UserValidator userValidator = new UserValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();
    UserRepositoryDB repo1 = new UserRepositoryDB(userValidator);
    FriendshipRepositoryDB repo2 = new FriendshipRepositoryDB(friendshipValidator);
    RequestRepositoryDB repo3 = new RequestRepositoryDB();
    MessageValidator msgValidator = new MessageValidator();
    MessageRepositoryDB repo4 = new MessageRepositoryDB(msgValidator);
    Service service = Service.getInstance(repo1, repo2, repo3, repo4);
    private final ObservableList<String> userModel = FXCollections.observableArrayList();

    private final ObservableList<String> usernameModel = FXCollections.observableArrayList();

    public void initModel() {
        Long id = service.getUser().getId();
        List<String> users = new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        for (Friendship friendship : service.getAllFriendships()) {
            if (friendship.getIdUser1().equals(id)) {
                users.add(service.findOneUser(friendship.getIdUser2()).getUsername());
            } else if (friendship.getIdUser2().equals(id)) {
                users.add(service.findOneUser(friendship.getIdUser1()).getUsername());
            }
        }
        for (User user : service.getAllUsers()) {
            if (!Objects.equals(user, service.getUser()) && !users.contains(user.getUsername())) {
                usernames.add(user.getUsername());
            }
        }
        usernameModel.setAll(usernames);
        userModel.setAll(users);
        styleLabel(notifLbl);
    }

    @FXML
    private TextField username;

    @FXML
    private ListView<String> usernamesListView;

    @FXML
    private ScrollPane usernamesPane;

    @FXML
    private Label notifLbl;

    @FXML
    private ListView<String> friendsListView;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField textField;

    @FXML
    private Line line;

    @FXML
    private TitledPane chatPane;

    @FXML
    public void initialize() {
        usernamesListView.setItems(usernameModel);
        friendsListView.setItems(userModel);
        initModel();
    }

    public void onLogOutButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log_in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("SOCIAL NETWORK");
        stage.setScene(scene);
        stage.show();
    }

    public void onRemoveFriendButtonClick() {
        chatPane.setVisible(false);
        line.setVisible(false);
        User user1 = service.getUser();
        Long id1 = user1.getId();
        Long id2 = null;
        String userRemove = friendsListView.getSelectionModel().getSelectedItem();
        for (User user : service.getAllUsers()) {
            if (user.getUsername().equals(userRemove)) {
                id2 = user.getId();
            }
        }
        for (Friendship friendship : service.getAllFriendships()) {
            if ((id1.equals(friendship.getIdUser1()) && Objects.equals(id2, friendship.getIdUser2())) || (Objects.equals(id2, friendship.getIdUser1()) && id1.equals(friendship.getIdUser2()))) {
                service.deleteFriendship(friendship.getId(), id1, id2);
                for (Request request : service.getAllRequests()) {
                    if ((user1.getUsername().equals(request.getUsername1()) && userRemove.equals(request.getUsername2())) || (user1.getUsername().equals(request.getUsername2()) && userRemove.equals(request.getUsername1()))) {
                        service.deleteRequest(request.getId());
                    }
                }
            }
        }
        initModel();
    }

    public void onAddFriendButtonClick() {
        User user1 = service.getUser();
        String username1 = user1.getUsername();
        String username2 = username.getText();
        if (username2.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText("Username cannot be empty!");
            alert.show();
        }
        User user2 = null;
        for (User user : service.getAllUsers()) {
            if (Objects.equals(user.getUsername(), username2)) {
                user2 = user;
            }
        }
        if (user2 == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText("Username does not exist!");
            alert.show();
        } else try {
            service.addRequest(username1, username2);
            username.setText("");
            usernamesPane.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Request sent!");
            alert.setContentText("");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void onFriendRequestsButtonClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend_requests.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 475, 393);
        stage.setTitle("FRIEND REQUESTS");
        stage.setScene(scene);
        stage.show();
    }

    public void onSearchType() {
        usernamesPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        usernamesPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        usernamesPane.setHmax(0);
        usernamesPane.setHvalue(0);
        usernamesPane.setContent(usernamesListView);
        usernamesPane.setVisible(!username.getText().isEmpty());
    }

    public void onUsernameClick() {
        String usernameSelected = usernamesListView.getSelectionModel().getSelectedItem();
        username.setText(usernameSelected);
    }

    public void styleLabel(Label label) {
        String username = service.getUser().getUsername();
        List<Request> requests1 = new ArrayList<>();
        for (Request request : service.getAllRequests()) {
            if (request.getUsername2().equals(username) && request.getStatus().equals("pending..")) {
                requests1.add(request);
            }
        }
        label.setText(String.valueOf(requests1.size()));
    }

    public void onFriendClick() {
        User user1 = service.getUser();
        User userConv = null;
        String user2 = friendsListView.getSelectionModel().getSelectedItem();
        for (User user : service.getAllUsers()) {
            if (user.getUsername().equals(user2)) {
                userConv = user;
            }
        }
        if (userConv != null) {
            service.setUserConv(userConv);
            textArea.clear();
            for (Message message : service.getAllMessages()) {
                if ((message.getUserFrom().equals(user1.getId()) && message.getUserTo().equals(userConv.getId())) || (message.getUserTo().equals(user1.getId())) && message.getUserFrom().equals(userConv.getId())) {
                    textArea.appendText(service.findOneUser(message.getUserFrom()).getUsername() + ": " + message.getText() + "\n");
                }
                if (message.getUserFrom().equals(userConv.getId()) && message.getUserTo().equals(user1.getId())) {
                    message.setMsgStatus("read");
                    service.updateMessage(message);
                }
            }
            chatPane.setVisible(true);
            chatPane.setText(userConv.getUsername());
            line.setVisible(true);
        }
    }

    public void onSendMsgButtonClick() {
        User user1 = service.getUser();
        Long id1 = user1.getId();
        User user2 = service.getUserConv();
        Long id2 = user2.getId();
        String text = textField.getText();
        textField.clear();
        service.addMessage(id1, id2, text);
        textArea.appendText(user1.getUsername() + ": " + text + "\n");
    }

    public int nrMsg(Long id1, Long id2) {
        List<Message> nrMsg = new ArrayList<>();
        for (Message message : service.getAllMessages()) {
            if (message.getUserFrom().equals(id2) && message.getUserTo().equals(id1) && message.getMsgStatus().equals("delivered")) {
                nrMsg.add(message);
            }
        }
        return nrMsg.size();
    }

    public void onEnterReleased(KeyEvent keyEvent) {
        if(keyEvent.getCode()== KeyCode.ENTER){
            onSendMsgButtonClick();
        }
    }
}