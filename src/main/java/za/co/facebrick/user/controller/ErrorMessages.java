package za.co.facebrick.user.controller;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    USER_DOES_NOT_EXIST("Provided user does not exist"),

    USER_IS_INVALID("Provided user is invalid"),

    USER_ALREADY_EXISTS("Provided user already exists");

    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
