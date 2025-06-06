package ru.shift.data;

import lombok.Getter;

@Getter
public class Resource {
    private final int id;
    private static int nextId = 0;

    public Resource() {
        synchronized (Resource.class) {
            this.id = nextId++;
        }
    }
}
