package com.bank.observer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventRegistry {
    private final ConcurrentHashMap<EventType, CopyOnWriteArrayList<Observer<TransactionEvent>>> listeners = new ConcurrentHashMap<>();

    public void subscribe(EventType type, Observer<TransactionEvent> listener) {
        listeners.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void unsubscribe(EventType type, Observer<TransactionEvent> listener) {
        CopyOnWriteArrayList<Observer<TransactionEvent>> list = listeners.get(type);
        if (list != null) {
            list.remove(listener);
        }
    }

    public void notifyListeners(TransactionEvent event) {
        List<Observer<TransactionEvent>> list = listeners.get(event.type());
        if (list != null) {
            for (Observer<TransactionEvent> listener : list) {
                listener.onEvent(event);
            }
        }
    }
}