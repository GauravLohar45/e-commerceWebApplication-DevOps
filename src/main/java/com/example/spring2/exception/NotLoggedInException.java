package com.example.spring2.exception;


import lombok.Getter;

@Getter
public class NotLoggedInException extends RuntimeException {
	String message = "Invalid Session, Login Again";
}