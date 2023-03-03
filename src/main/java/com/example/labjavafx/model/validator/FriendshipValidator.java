package com.example.labjavafx.model.validator;

import com.example.labjavafx.model.Friendship;

import java.util.Objects;


public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errMsg = "";
        if(Objects.equals(entity.getIdUser1(), entity.getIdUser2()))
            errMsg+="Ids must be different";
        if (errMsg.length() > 0) {
            throw new ValidationException(errMsg);
        }
    }
}
