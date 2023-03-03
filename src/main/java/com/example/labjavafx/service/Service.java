package com.example.labjavafx.service;


import com.example.labjavafx.events.ChangeEventType;
import com.example.labjavafx.events.FriendshipEntityChangeEvent;
import com.example.labjavafx.events.UserEntityChangeEvent;
import com.example.labjavafx.model.Friendship;
import com.example.labjavafx.model.Message;
import com.example.labjavafx.model.Request;
import com.example.labjavafx.model.User;
import com.example.labjavafx.repository.FriendshipRepositoryDB;
import com.example.labjavafx.repository.MessageRepositoryDB;
import com.example.labjavafx.repository.RequestRepositoryDB;
import com.example.labjavafx.repository.UserRepositoryDB;
import com.example.labjavafx.observer.Observable;
import com.example.labjavafx.observer.Observer;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Service implements Observable<FriendshipEntityChangeEvent> {
    private final List<Observer<FriendshipEntityChangeEvent>> observers = new ArrayList<>();

    private static Service service = null;
    private final UserRepositoryDB repoUser;
    private final FriendshipRepositoryDB repoFriendship;
    private final RequestRepositoryDB repoRequest;
    private final MessageRepositoryDB repoMessage;

    private User user = null;
    private User userConv = null;

    private Service(UserRepositoryDB repoUser, FriendshipRepositoryDB repoFriendship, RequestRepositoryDB repoRequest, MessageRepositoryDB repoMessage) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
        this.repoRequest = repoRequest;
        this.repoMessage = repoMessage;
    }

    public synchronized static Service getInstance(UserRepositoryDB repoUser, FriendshipRepositoryDB repoFriendship, RequestRepositoryDB repoRequest, MessageRepositoryDB repoMessage) {
        if (service == null)
            service = new Service(repoUser, repoFriendship, repoRequest, repoMessage);
        return service;
    }

    public void addUser(String firstName, String lastName, int age, String email, String username, String password) {
        Long id = getNewIdUser();
        User user = new User(firstName, lastName, age, email, username, password, id);
        for (User user1 : repoUser.findAll()) {
            if (user1.getUsername().equals(username)) {
                throw new ServiceException("This username is already used!");
            }
            if (user1.getEmail().equals(email)) {
                throw new ServiceException("This email is already used!");
            }
        }
        this.repoUser.save(user);
    }

    public void deleteUser(Long id) {
        if (id == null) {
            throw new ServiceException("ID cannot be null!");
        }
        User user = repoUser.findOne(id);
        if (user == null) {
            throw new ServiceException("User does not exist!");
        }
        for (Friendship friendship : repoFriendship.findAll()) {
            if (friendship.getIdUser1().equals(id) || friendship.getIdUser2().equals(id))
                repoFriendship.delete(friendship.getId());
        }
        this.repoUser.delete(id);
    }

    public void updateUser(User user) {
        repoUser.update(user);
    }


    public void addFriendship(Long id1, Long id2) {
        Long id_fr = getNewIdFriendship();
        if (id_fr == null) {
            throw new ServiceException("ID cannot be null!");
        }
        if (findOneUser(id1) == null || findOneUser(id2) == null) {
            throw new ServiceException("Users not in the list!");
        }
        for (Friendship fr : repoFriendship.findAll()) {
            if ((Objects.equals(fr.getIdUser1(), id1) && Objects.equals(fr.getIdUser2(), id2)) || (Objects.equals(fr.getIdUser1(), id2) && Objects.equals(fr.getIdUser2(), id1))) {
                throw new ServiceException("You are already friends!");
            }
        }
        User user1 = findOneUser(id1);
        User user2 = findOneUser(id2);
        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
        Friendship friendship = new Friendship(id_fr, id1, id2, LocalDateTime.now());
        repoFriendship.save(friendship);
        notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.ADD,findOneFriendship(id_fr)) );
    }

    public void deleteFriendship(Long id_fr, Long id1, Long id2) {
        if (id_fr == null) {
            throw new ServiceException("ID cannot be null");
        }
        if (findOneUser(id1) == null || findOneUser(id2) == null) {
            throw new ServiceException("Users not in the list");
        }
        if (findOneFriendship(id_fr) == null) {
            throw new ServiceException("Friendship does not exist");
        }
        User user1 = findOneUser(id1);
        User user2 = findOneUser(id2);
        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);
        repoFriendship.delete(id_fr);
    }

    public void addRequest(String username1, String username2) {
        Long id_request = getNewIdRequest();
        String status = "pending..";
        Request request = new Request(id_request, username1, username2, LocalDateTime.now(), status);
        for (Request request1 : repoRequest.findAll()) {
            if (request1.getUsername1().equals(username1) && request1.getUsername2().equals(username2)) {
                throw new ServiceException("Request already sent!");
            }
            if (request1.getUsername1().equals(username2) && request1.getUsername2().equals(username1)) {
                throw new ServiceException("Request already received!");
            }
        }
        repoRequest.save(request);
    }

    public void deleteRequest(Long id_request) {
        if (id_request == null) {
            throw new ServiceException("ID cannot be null!");
        }
        if (findOneRequest(id_request) == null) {
            throw new ServiceException("Request does not exist!");
        }
        repoRequest.delete(id_request);
    }

    public void updateRequest(Request request) {
        repoRequest.update(request);
    }

    public void addMessage(Long userFrom, Long userTo, String text) {
        Long idMsg = getNewIdMessage();
        String status = "delivered";
        if (idMsg == null) {
            throw new ServiceException("ID cannot be null!");
        }
        if (findOneUser(userFrom) == null || findOneUser(userTo) == null) {
            throw new ServiceException("Users not in the list!");
        }
        Message message = new Message(idMsg, userFrom, userTo, text, LocalDateTime.now(), status);
        repoMessage.save(message);
    }

    public void deleteMessage(Long idMsg) {
        if (idMsg == null) {
            throw new ServiceException("ID cannot be null!");
        }
        if (findOneRequest(idMsg) == null) {
            throw new ServiceException("Message does not exist!");
        }
        repoMessage.delete(idMsg);
    }

    public void updateMessage(Message message) {
        repoMessage.update(message);
    }

    public Iterable<User> getAllUsers() {
        return repoUser.findAll();
    }

    public Iterable<Friendship> getAllFriendships() {
        return repoFriendship.findAll();
    }

    public Iterable<Request> getAllRequests() {
        return repoRequest.findAll();
    }

    public Iterable<Message> getAllMessages() {
        return repoMessage.findAll();
    }

    public User findOneUser(Long id) {
        return repoUser.findOne(id);
    }

    public Friendship findOneFriendship(Long id) {
        return repoFriendship.findOne(id);
    }

    public Request findOneRequest(Long id) {
        return repoRequest.findOne(id);
    }

    public Message findOneMessage(Long id) {
        return repoMessage.findOne(id);
    }

    public Long getNewIdUser() {
        List<Long> list = new ArrayList<>();
        Long id;
        for (User u : repoUser.findAll())
            list.add(u.getId());
        Collections.sort(list);
        id = list.get(list.size() - 1);
        return id + 1;
    }

    public Long getNewIdFriendship() {
        List<Long> list = new ArrayList<>();
        Long id;
        for (Friendship u : repoFriendship.findAll())
            list.add(u.getId());
        Collections.sort(list);
        id = list.get(list.size() - 1);
        return id + 1;
    }

    public Long getNewIdRequest() {
        List<Long> list = new ArrayList<>();
        Long id;
        for (Request u : repoRequest.findAll())
            list.add(u.getId());
        Collections.sort(list);
        id = list.get(list.size() - 1);
        return id + 1;
    }

    public Long getNewIdMessage() {
        List<Long> list = new ArrayList<>();
        Long id;
        for (Message u : repoMessage.findAll())
            list.add(u.getId());
        Collections.sort(list);
        id = list.get(list.size() - 1);
        return id + 1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUserConv() {
        return userConv;
    }

    public void setUserConv(User userConv) {
        this.userConv = userConv;
    }

    @Override
    public void addObserver(Observer<FriendshipEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipEntityChangeEvent> e) {
        observers.remove(e);
    }
    @Override
    public void notifyObservers(FriendshipEntityChangeEvent t) {
        observers.stream().forEach(x -> x.update(t));
    }
}
