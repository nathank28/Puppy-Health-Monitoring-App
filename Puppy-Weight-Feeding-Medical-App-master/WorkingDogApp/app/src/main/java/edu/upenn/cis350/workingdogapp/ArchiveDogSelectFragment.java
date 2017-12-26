package edu.upenn.cis350.workingdogapp;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * DISPLAYS LIST OF DOGS FOR ARCHIVEACTIVITY, EXTENDS DOGSELECTFRAGMENT AND OVERRIDES THE
 * DATA READ FROM THE DATABASE
 */

public class ArchiveDogSelectFragment extends DogSelectFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArchiveDogSelectFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDogsList = Database.getInstance().getArchivedDogEntryList();
    }

    @Override
    public void onDataBaseChanged() {
        mDogsList = Database.getInstance().getArchivedDogEntryList();
        updateDogsListView();
    }
}
