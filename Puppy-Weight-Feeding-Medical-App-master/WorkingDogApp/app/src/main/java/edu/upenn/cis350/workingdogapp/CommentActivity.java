package edu.upenn.cis350.workingdogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.io.Serializable;
import java.util.Calendar;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * COMMENT ACTIVITY WHEN CLICKING THE BUTTON "ADD COMMENT"
 * ON A DOG'S PAGE IN FOSTERACTIVITY.JAVA
 */

public class CommentActivity extends MainActivity {
    EditText text;
    Database.DogEntry dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        text = (EditText) findViewById(R.id.editText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        dog = (Database.DogEntry) getIntent().getExtras().getSerializable("dog");
    }

    //WHEN HITTING THE SUBMIT BUTTON
    public void submit(View view) {
        text = (EditText) findViewById(R.id.editText);
        if (text.getText().toString().equals("") ||
                text.getText().toString().equals(" ") ||
                text == null) {
            LayoutInflater inflater = getLayoutInflater();
            Toast.makeText(getApplicationContext(), "You have entered an empty comment",
                    Toast.LENGTH_LONG).show();
        }
        else {
            //starts creating the timestamp for the comment
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

                //the comment list on a dog's page is by creating a concatenation of several comments
                String comment = ("\t\t" + (year) + "/" + (month + 1) + "/" + (day) + " - " +
                        hour + ":" + minute + ":" + second +
                        ":\n\t\t\t\t" + text.getText().toString() + "\n\n");

            //if the comment is not null or empty, and tests if the existent comment is not null either
            if (!comment.equals("") || comment != null) {
                if (dog.comments != null) {
                    dog.comments = dog.comments.concat(comment);
                }
                else {
                    dog.comments = comment;
                }
            }
            Intent intent = new Intent(getApplicationContext(), FosterActivity.class);
            Database.getInstance().updateDogData(dog);
            intent.putExtra("dog", (Serializable) dog);
            startActivity(intent);
        }
    }

    //creates the popup menu. See PopupMenu.java for more information.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        RelativeLayout activity_menu;
        getMenuInflater().inflate(R.menu.popupmenu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra("dog", (Serializable) dog);
            startActivity(refresh);
            this.finish();
        }
    }
}
