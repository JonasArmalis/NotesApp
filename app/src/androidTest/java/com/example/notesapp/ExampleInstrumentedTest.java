package com.example.notesapp;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.action.ViewActions.replaceText;

import com.example.notesapp.model.Note;
import com.example.notesapp.storage.NoteRepository;
import com.example.notesapp.storage.RepositoryFactory;
import com.example.notesapp.storage.StorageMode;
import com.example.notesapp.storage.StoragePrefs;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        Context appContext = ApplicationProvider.getApplicationContext();
        assertEquals("com.example.notesapp", appContext.getPackageName());
    }

    /**
     * Senarijus:
     * - Išvalo užrašus
     * - Paleidžia AddNoteActivity
     * - Įveda pavadinimą ir turinį, paspaudžia "Save"
     * - Patikrina, ar naujas užrašas atsirado NoteRepository.
     */
    @Test
    public void addNote_savesToRepository() throws InterruptedException {
        Context appContext = ApplicationProvider.getApplicationContext();

        StoragePrefs.setMode(appContext, StorageMode.SHARED_PREFS);
        NoteRepository repo = RepositoryFactory.get(appContext);

        repo.saveAll(appContext, new ArrayList<>());

        String testName = "Test note";
        String testContent = "This is test content";

        try (ActivityScenario<AddNoteActivity> scenario =
                     ActivityScenario.launch(AddNoteActivity.class)) {

            onView(withId(R.id.txtName))
                    .perform(replaceText(testName), closeSoftKeyboard());

            onView(withId(R.id.txtContent))
                    .perform(replaceText(testContent), closeSoftKeyboard());

            onView(withId(R.id.btnSave)).perform(click());
        }

        Thread.sleep(1000);

        List<Note> allNotes = repo.loadAll(appContext);
        boolean found = false;
        for (Note n : allNotes) {
            if (testName.equals(n.getName()) &&
                    testContent.equals(n.getContent())) {
                found = true;
                break;
            }
        }

        assertTrue("Newly added note should be saved in repository", found);
    }


    /**
     * Senarijus:
     * - Paruošia vieną užrašą saugykloje
     * - Paleidžia DeleteNoteActivity
     * - Paspaudžia "Delete" mygtuką
     * - Patikrina, kad užrašas ištrintas iš NoteRepository.
     */
    @Test
    public void deleteNote_removesFromRepository() {
        Context appContext = ApplicationProvider.getApplicationContext();

        StoragePrefs.setMode(appContext, StorageMode.SHARED_PREFS);
        NoteRepository repo = RepositoryFactory.get(appContext);

        String nameToDelete = "Note to delete";
        String contentToDelete = "Temporary note";

        ArrayList<Note> seed = new ArrayList<>();
        seed.add(new Note(nameToDelete, contentToDelete));
        repo.saveAll(appContext, seed);

        try (ActivityScenario<DeleteNoteActivity> scenario =
                     ActivityScenario.launch(DeleteNoteActivity.class)) {

            onView(withId(R.id.btnDelete)).perform(click());
        }

        List<Note> remaining = repo.loadAll(appContext);
        boolean stillExists = false;
        for (Note n : remaining) {
            if (nameToDelete.equals(n.getName())) {
                stillExists = true;
                break;
            }
        }

        assertFalse("Note should be removed from repository after delete", stillExists);
    }
}
