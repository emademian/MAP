package com.example.labjavafx.observer;
import com.example.labjavafx.events.Event;

public interface Observable<E extends Event>{
    void addObserver(Observer<E> e);

    void removeObserver(Observer<E> e);

    void notifyObservers(E t);
}
