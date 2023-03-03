package com.example.labjavafx.events;

import com.example.labjavafx.model.Friendship;

public class FriendshipEntityChangeEvent implements Event{
    private final ChangeEventType type;
    private final Friendship data;
    private Friendship oldData;

    public FriendshipEntityChangeEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
