package com.bank.observer;

public interface Observer<T> {
    void onEvent(T event);
}