package com.example.notesapp;

import com.example.notesapp.model.Note;

import org.junit.Test;

import static org.junit.Assert.*;
public class ExampleUnitTest {

    @Test
    public void note_constructorStoresFields() {
        String name = "Shopping list";
        String content = "Milk, bread, eggs";

        Note note = new Note(name, content);

        assertEquals("Name should be stored correctly", name, note.getName());
        assertEquals("Content should be stored correctly", content, note.getContent());
    }

    @Test
    public void note_toStringReturnsName() {
        Note note = new Note("My title", "Some content");
        assertEquals("My title", note.toString());
    }

    @Test
    public void note_allowsDifferentNotes() {
        Note n1 = new Note("Title 1", "Content 1");
        Note n2 = new Note("Title 2", "Content 2");

        assertNotEquals("Different notes should have different names", n1.getName(), n2.getName());
        assertNotEquals("Different notes should have different contents", n1.getContent(), n2.getContent());
    }
}
