package com.example.labjavafx.ui;


import com.example.labjavafx.model.Friendship;
import com.example.labjavafx.model.validator.UIUserValidator;
import com.example.labjavafx.model.validator.Validator;
import com.example.labjavafx.service.Service;

import java.util.Scanner;

public class Console {
    private static Service service;
    private final Validator<Long> validator = new UIUserValidator();

    public Console(Service serviceUser) {
        service = serviceUser;
    }

    public static void menu() {
        System.out.println("1.Add user");
        System.out.println("2.Delete user");
        System.out.println("3.Show users");
        System.out.println("4.Add friendship");
        System.out.println("5.Delete friendship");
        System.out.println("6.Show friendships");
        System.out.println("0.Exit");
    }

    public void uiAddUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("First name: ");
        String firstName = sc.next();
        System.out.print("Last name: ");
        String lastName = sc.next();
        System.out.print("Age: ");
        int age = sc.nextInt();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();

        try {
            service.addUser(firstName, lastName, age, email,username,password);
            System.out.println("User added successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void uiDeleteUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Id: ");
        Long id = sc.nextLong();
        try {
            validator.validate(id);
            service.deleteUser(id);
            System.out.println("User deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void uiAddFriendship() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Id1: ");
        Long id1 = sc.nextLong();
        System.out.print("Id2: ");
        Long id2 = sc.nextLong();
        try {
            validator.validate(id1);
            validator.validate(id2);
            service.addFriendship(id1, id2);
            System.out.println("Friendship added successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void uiDeleteFriendship() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Id1: ");
        Long id1 = sc.nextLong();
        System.out.print("Id2: ");
        Long id2 = sc.nextLong();
        System.out.print("Id friendship: ");
        Long id_fr = sc.nextLong();
        try {
            validator.validate(id1);
            validator.validate(id2);
            validator.validate(id_fr);
            service.deleteFriendship(id_fr, id1, id2);
            System.out.println("Friendship deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showUsers() {
        System.out.println("Users list: ");
        for (Object user : service.getAllUsers()) {
            System.out.println(user.toString());
        }
        System.out.println();
    }

    public static void showFriendships() {
        System.out.println("Friendships list: ");
        for (Friendship friendship : service.getAllFriendships()) {
            System.out.println(friendship.toString());
        }
        System.out.println();
    }

    public void run() {
        while (true) {
            menu();
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter your option: ");
            int opt = sc.nextInt();
            if (opt == 1) {
                uiAddUser();
            } else if (opt == 2) {
                uiDeleteUser();
            } else if (opt == 3) {
                showUsers();
            } else if (opt == 4) {
                uiAddFriendship();
            } else if (opt == 5) {
                uiDeleteFriendship();
            } else if (opt == 6) {
                showFriendships();
            } else if (opt == 0) {
                break;
            } else {
                System.out.println("This option is not in the menu");
            }
        }
    }
}


