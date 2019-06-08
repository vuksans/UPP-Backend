package com.example.ScienceStationProject.util.exception;

public class PasswordNotMatchingException extends Exception {
    public PasswordNotMatchingException(){
        super("Old passwords don't match!");
    }
}
