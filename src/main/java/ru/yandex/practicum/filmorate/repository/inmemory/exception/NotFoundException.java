package ru.yandex.practicum.filmorate.repository.inmemory.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String mess) {
        super(mess);
    }
}
