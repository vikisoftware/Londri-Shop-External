package com.vikisoft.londrishops.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;

import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class AppIndexingUpdateService extends JobIntentService {

    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;

    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // TODO Insert your Indexable objects — for example, the recipe notes look as follows:

        ArrayList<Indexable> indexableNotes = new ArrayList<>();

        /*for (Recipe recipe : getAllRecipes()) {
            ContactsContract.CommonDataKinds.Note note = recipe.getNote();
            if (note != null) {
                Indexable noteToIndex = Indexables.noteDigitalDocumentBuilder()
                        .setName(recipe.getTitle() + " Note")
                        .setText(note.getText())
                        .setUrl(recipe.getNoteUrl())
                        .build();

                indexableNotes.add(noteToIndex);
            }
        }*/

        if (indexableNotes.size() > 0) {
            Indexable[] notesArr = new Indexable[indexableNotes.size()];
            notesArr = indexableNotes.toArray(notesArr);

            // batch insert indexable notes into index
            FirebaseAppIndex.getInstance().update(notesArr);
        }
    }

    // ...
}