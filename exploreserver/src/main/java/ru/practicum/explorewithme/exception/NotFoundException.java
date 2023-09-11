package ru.practicum.explorewithme.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(long id, Class<?> cl) {
        super("Id: " + id + " | Name: " + cl.getName());
    }

    public NotFoundException(String message) {
        super(message);
    }
}
