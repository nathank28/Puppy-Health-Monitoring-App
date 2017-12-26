package edu.upenn.cis350.workingdogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * USED FOR DISPLAYING A LIST OF ARCHIVED DOGS, WORKS SIMILARLY TO MAINACTIVITY
 * USES FRAGMENTS INSTEAD OF ACTIVITITES WHEN SWITCHING SCREENS (DESIGN CHANGE NOT YET PROPGATED
 * TO REST OF APP)
 */

public class ArchiveActivity extends MainActivity implements ArchiveDogSelectFragment.InteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        if(findViewById(R.id.fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }

            ArchiveDogSelectFragment archiveDogSelectFragment = new ArchiveDogSelectFragment();
            archiveDogSelectFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, archiveDogSelectFragment)
                    .commit();
        }
    }

    /**
     * Callback used by DogSelectFragment when Dog is clicked
     * @param item Database.DogEntry
     */
    @Override
    public void onDogSelectListInteraction(Database.DogEntry item) {
        Bundle args = new Bundle();
        args.putSerializable("dog", item);
        DogInfoFragment fragment = new DogInfoFragment();
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, ArchiveActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }
}
