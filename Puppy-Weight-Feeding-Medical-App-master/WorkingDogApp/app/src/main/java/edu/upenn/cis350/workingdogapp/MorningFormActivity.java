package edu.upenn.cis350.workingdogapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MorningFormActivity extends MainActivity {

//The dog that is being referenced (see FosterActivity.java) and the XML entities on the page.
    Database.DogEntry dog;
    private EditText nameField;
    private EditText emailField;
    private EditText feedbackField;
    private Spinner feedbackSpinner;

    /** Called when the activity is first created. */

    /*Arranges the XML entities (form.xml) so that they can be references once
     text input has occurred from the client.
     Additionally their fonts are changed programmatically from
    fontSize in SharedPreferences. See SettingsActivity.java for details.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        dog = (Database.DogEntry) getIntent().getExtras().getSerializable("dog");
        final TextView titleView = (TextView) findViewById(R.id.TextViewTitle);
        nameField = (EditText) findViewById(R.id.EditTextName);
        emailField = (EditText) findViewById(R.id.EditTextEmail);
        feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
        feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        ArrayAdapter<CharSequence> feedbackTypeListAdapter = ArrayAdapter.createFromResource(
                this, R.array.feedbacktypelist, R.layout.spinner_layout);
        final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);


        ArrayAdapter<CharSequence> feedbackTypeList = new ArrayAdapter<CharSequence>(this, R.layout.spinner_layout){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                // do whatever you want with this text view
                textView.setTextSize(50);
                return view;
            }
        };

        feedbackTypeListAdapter.setDropDownViewResource(R.layout.spinner_layout);
        feedbackSpinner.setAdapter(feedbackTypeListAdapter);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        int fontSize = settings.getInt("fontSize", 18);
        titleView.setTextSize(fontSize);
        nameField.setTextSize(fontSize);
        emailField.setTextSize(fontSize);
        feedbackField.setTextSize(fontSize);
        responseCheckbox.setTextSize(fontSize);


    }

    /*gets the user input on the form. Calls sendFeedbackMessage() once finished to see if
      the input is satisfactory.
     */
    public void sendFeedback(View button) {
        String name = nameField.getText().toString();

        String email = emailField.getText().toString();

        String feedback = feedbackField.getText().toString();

        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        //BREAKFAST STUFF GOES HERE
        final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
        boolean bRequiresResponse = responseCheckbox.isChecked();


        sendFeedbackMessage(name, email, feedback, feedbackType, bRequiresResponse);
    }

    /*helper function for sendFeedback(). Checks if all fields have been filled out. Otherwise, show
    a Toast message.
     */
    public void sendFeedbackMessage(String parent, String weight, String comment, String metric, Boolean tf) {
        if (parent.equals("") || weight.equals("") || metric.equals("")) {
            LayoutInflater inflater = getLayoutInflater();
            Toast.makeText(getApplicationContext(), "You cannot leave any field blank!", Toast.LENGTH_LONG).show();
        } else {
            dog.fosterParent = parent;

            if (metric.equals("Kilograms")) {
                dog.weightLb = Double.parseDouble(weight) * 0.453592;
            } else {
                dog.weightLb = Double.parseDouble(weight);
            }

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            //sets the timestamp and comment for a NON-EMPTY comment
            //and concats to the already existing comments
            if (!comment.equals("") || comment != null) {
                dog.comments = dog.comments.concat(((year) + ":" + (month + 1) + ":" +
                        (day) + ":" + hour + ":" + minute + ":" + second +
                        "*** " + comment + "\n"));
            }

            //update the dog's information. See Database.java for details
            Database.getInstance().updateDogData(dog);
            Intent fosterActivity = new Intent(getApplicationContext(), FosterActivity.class);

            /*go straight back to the foster activity once finished. The current form screen is not
            saved in the activity history stack
            */
            fosterActivity.putExtra("dog", dog);
            startActivity(fosterActivity);
            finish();
        }
    }

    //allows for the popup menu to be seen on the screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popupmenu, menu);
        return true;
    }

}
