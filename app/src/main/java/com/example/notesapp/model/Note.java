// Note.java
package com.example.notesapp.model;

public class Note {
    private final String name;
    private final String content;

    public Note(String name, String content) {
        this.name = name;
        this.content = content;
    }
    public String getName() { return name; }
    public String getContent() { return content; }

    @Override
    public String toString() {
        return name;
    }
}