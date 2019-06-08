package com.example.ScienceStationProject.util.exception;

public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException(){
        super("Email already in use!");
    }
}
