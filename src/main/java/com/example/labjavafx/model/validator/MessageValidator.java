package com.example.labjavafx.model.validator;

import com.example.labjavafx.model.Message;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        String errMsg = "";
        if (entity.getText().length() > 150) {
            errMsg += "The message is too long!";
        }
        if (errMsg.length() > 0) {
            throw new ValidationException(errMsg);
        }
    }
}
