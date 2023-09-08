package ru.practicum.explorewithme.exceptions;

public class UnsupportedBookingStatusFilterException extends RuntimeException {
    public UnsupportedBookingStatusFilterException(String message) {
        super(message);
    }
}
