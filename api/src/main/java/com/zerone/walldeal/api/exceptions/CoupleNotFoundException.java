package com.zerone.walldeal.api.exceptions;

public class CoupleNotFoundException extends RuntimeException{
    public CoupleNotFoundException() {
        super("Couple not found");
    }
}
