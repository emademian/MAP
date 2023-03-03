package com.example.labjavafx;

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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestsController {
    UserValidator userValidator = new UserValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();
    UserRepositoryDB repo1 = new UserRepositoryDB(userValidator);
    FriendshipRepositoryDB repo2 = new FriendshipRepositoryDB(friendshipValidator);
    RequestRepositoryDB repo3 = new RequestRepositoryDB();
    MessageValidator msgValidator=new MessageValidator();
    MessageRepositoryDB repo4=new MessageRepositoryDB(msgValidator);
    Service service = Service.getInstance(repo1, repo2, repo3,repo4);
    private final ObservableList<Request> requestModel = FXCollections.observableArrayList();
    private final ObservableList<Request> sentRequestModel = FXCollections.observableArrayList();

    private void initModel() {
        String username = service.getUser().getUsername();
        List<Request> requests = new ArrayList<>();
        List<Request> requests1 = new ArrayList<>();
        List<Request> sentRequests = new ArrayList<>();
        for (Request request : service.getAllRequests()) {
            if (request.getUsername2().equals(username)) {
                requests.add(request);
            }
            if (request.getUsername2().equals(username) && request.getStatus().equals("pending..")) {
                requests1.add(request);
            }
            if (request.getUsername1().equals(username) && request.getStatus().equals("pending..")) {
                sentRequests.add(request);
            }
        }
        requestModel.setAll(requests);
        requestsNumber.setText("(" + requests1.size() + ")");
        sentRequestModel.setAll(sentRequests);
    }

    @FXML
    private TableView<Request> requestsTableView;

    @FXML
    private TableColumn<Request, String> usernameColumn;

    @FXML
    private TableColumn<Request, LocalDateTime> timeColumn;

    @FXML
    private TableColumn<Request, String> statusColumn;

    @FXML
    private Label requestsNumber;
    @FXML
    private Label requestsLbl;

    @FXML
    private TableView<Request> sentRequestsTableView;

    @FXML
    private TableColumn<Request, String> usernameColumn1;

    @FXML
    private TableColumn<Request, LocalDateTime> timeColumn1;

    @FXML
    private TableColumn<Request, String> statusColumn1;

    @FXML
    private Button sentBtn;
    @FXML
    private Button closeBtn;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button declineBtn;
    @FXML
    private Button deleteBtn;


    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username1"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        requestsTableView.setItems(requestModel);
        usernameColumn1.setCellValueFactory(new PropertyValueFactory<>("username2"));
        timeColumn1.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
        statusColumn1.setCellValueFactory(new PropertyValueFactory<>("status"));
        sentRequestsTableView.setItems(sentRequestModel);
        initModel();
    }

    public void onConfirmButtonClick() throws IOException {
        Request requestConfirm = requestsTableView.getSelectionModel().getSelectedItem();
        switch (requestConfirm.getStatus()) {
            case "pending.." -> {
                Long id1 = service.getUser().getId();
                String username = requestConfirm.getUsername1();
                Long id2 = null;
                for (User user : service.getAllUsers()) {
                    if (user.getUsername().equals(username)) {
                        id2 = user.getId();
                    }
                }
                service.addFriendship(id1, id2);
                requestConfirm.setStatus("confirmed!");
                service.updateRequest(requestConfirm);
                initModel();
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
//                Parent root = fxmlLoader.load();
//                UserController userController = fxmlLoader.getController();
//                userController.initModel();
            }
            case "confirmed!" -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Invalid data!");
                alert.setContentText("Request already confirmed!");
                alert.show();
            }
        }
    }

    public void onDeclineButtonClick() {
        Request requestDeclined = requestsTableView.getSelectionModel().getSelectedItem();
        switch (requestDeclined.getStatus()) {
            case "pending.." -> {
                service.deleteRequest(requestDeclined.getId());
                initModel();
            }
            case "confirmed!" -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Invalid data!");
                alert.setContentText("Request already confirmed!");
                alert.show();
            }
        }
    }

    public void onSentButtonClick() {
        sentRequestsTableView.setVisible(true);
        closeBtn.setVisible(true);
        requestsTableView.setVisible(false);
        confirmBtn.setVisible(false);
        declineBtn.setVisible(false);
        requestsNumber.setVisible(false);
        requestsLbl.setVisible(false);
        sentBtn.setVisible(false);
        deleteBtn.setVisible(true);
    }

    public void onCloseButtonClick() {
        sentRequestsTableView.setVisible(false);
        closeBtn.setVisible(false);
        requestsTableView.setVisible(true);
        confirmBtn.setVisible(true);
        declineBtn.setVisible(true);
        requestsNumber.setVisible(true);
        requestsLbl.setVisible(true);
        sentBtn.setVisible(true);
        deleteBtn.setVisible(false);
    }

    public void onDeleteButtonClick() {
        Request requestDelete = sentRequestsTableView.getSelectionModel().getSelectedItem();
        service.deleteRequest(requestDelete.getId());
        initModel();
    }
}
