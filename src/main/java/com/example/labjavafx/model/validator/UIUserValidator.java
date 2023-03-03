package com.example.labjavafx.model.validator;

public class UIUserValidator implements Validator<Long> {
    public void validate(Long id) throws ValidationException {
        String errMsg = "";
        if (!Long.toString(id).matches("[0-9]+")) {
            errMsg += "ID must be a number!\n";
        }

        if (!errMsg.isEmpty()) {
            throw new ValidationException(errMsg);
        }
    }
}
