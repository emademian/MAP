package com.example.labjavafx.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;

    private int age;

    private String email;

    private String username;

    private String password;

    private final List<User> friends;

    public User(String firstName, String lastName, int age, String email,String username,String password, Long id) {
        super.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.username=username;
        this.password=password;
        this.friends = new LinkedList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFriends() {
        return friends;
    }

//    public void addFriend(User user) {
//        friends.add(user);
//    }
//
//    public void removeFriend(User user) {
//        friends.remove(user);
//    }

    @Override
    public String toString() {
        StringBuilder friend_list = new StringBuilder();
        for (User each : friends) {
            friend_list.append(each.getId());
            friend_list.append(", ");
        }
        return "ID: " + super.getId() +
                " || Name: " + this.firstName + " " + this.lastName +
                " || Age: " + this.age +
                " || Email: " + this.email +
                " || Friends: " + friend_list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(super.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, super.getId());
    }
}
