package com.labs.wishlistservice.exceptions;

public class WishlistLimitExceededException extends RuntimeException {
    public WishlistLimitExceededException(String message) {
        super(message);
    }
}
