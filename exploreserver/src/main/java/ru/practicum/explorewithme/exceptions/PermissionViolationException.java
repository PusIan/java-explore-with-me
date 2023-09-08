package ru.practicum.explorewithme.exceptions;

public class PermissionViolationException extends RuntimeException {
    public PermissionViolationException(String message) {
        super(message);
    }
}
