package com.example.labjavafx.model.validator;


import com.example.labjavafx.model.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User user) throws ValidationException {
        String errMsg = "";
        if (user.getLastName() == null) {
            errMsg += "Last name cannot be empty!\n";
        }
        if (user.getFirstName() == null) {
            errMsg += "Fist name cannot be empty!\n";
        }
        if (user.getId() == null || user.getId() < 0) {
            errMsg += "Id cannot be empty or negative!\n";
        }
        if (!user.getFirstName().matches("[a-zA-Z]+")) {
            errMsg += "First name must only contain letters!\n";
        }
        if (!user.getLastName().matches("[a-zA-Z]+")) {
            errMsg += "Last name must only conatin letters!\n";
        }
        if (!Integer.toString(user.getAge()).matches("[0-9]+")) {
            errMsg += "Age must be a number!\n";
        }
        if (user.getAge() < 0) {
            errMsg += "Age cannot be negative!\n";
        }
        if (user.getEmail() == null) {
            errMsg += "Email cannot be null!\n";
        }
        if (!user.getEmail().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            errMsg += "Invalid email!\n";
        }
        if(!user.getUsername().matches("[a-z0-9]+")){
            errMsg+="Username cannot contain capital letters!\n";
        }
        if(!user.getPassword().matches("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,}$")){
            errMsg+="Password must contain at least 8 characters including 1 capital letter and 1 number!\n";
        }
        if (errMsg.length() > 0) {
            throw new ValidationException(errMsg);
        }
    }
}
