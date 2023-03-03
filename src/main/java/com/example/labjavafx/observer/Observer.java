package com.example.labjavafx.observer;

import com.example.labjavafx.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
