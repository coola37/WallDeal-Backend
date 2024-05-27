package com.zerone.walldeal.api.exceptions;

public class CoupleRequestNotFoundException extends RuntimeException{
    public CoupleRequestNotFoundException(String id) {
        super("Couple Request with ID " + id + " not found");
    }
}
