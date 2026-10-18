package org.example.dto;

public class UserEvent {

    private String operation;
    private String email;

    public UserEvent() {
    }

    public UserEvent(String operation, String email) {
        this.operation = operation;
        this.email = email;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}