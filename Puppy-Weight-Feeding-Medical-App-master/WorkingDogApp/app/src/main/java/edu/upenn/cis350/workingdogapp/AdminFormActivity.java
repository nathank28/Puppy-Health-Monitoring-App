package edu.upenn.cis350.workingdogapp;

import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.data;

/**
 * USED FOR ADMIN EDITING A DOG (this is NOT the form for creating a new dog)
 * SIMILAR, ALMOST IDENTICAL, SETUP FOR MorningFormActivity
 * PLEASE SEE MorningFormActivity.java for more information
 *
 * MUST BE AN ADMIN (THE ONLY ACCOUNT TYPE MADE THUS FAR) TO ACCESS FUNCTIONALITY WITHIN THIS
 * CLASS. ACCESSED THROUGH "admin editing" ON A DOG'S PAGE ON FosterActivity.java
 */

public class AdminFormActivity extends MainActivity {

    Database.DogEntry dog;
    SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminform);
        dog = (Database.DogEntry) getIntent().getExtras().getSerializable("dog");
        settings = getSharedPreferences("settings", 0);
        int fontSize = settings.getInt("fontSize", 18);

        final TextView textViewTitle = (TextView) findViewById(R.id.TextViewTitle);
        textViewTitle.setTextSize(fontSize);

        final TextView parentNameTitle = (TextView) findViewById(R.id.parentNameTitle);
        parentNameTitle.setTextSize(fontSize);
        final EditText parentNameField = (EditText) findViewById(R.id.EditParentName);
        parentNameField.setTextSize(fontSize);
        parentNameField.setText(dog.fosterParent);

        //Dog Breed
        final TextView breedFieldTitle = (TextView) findViewById(R.id.breedNameTitle);
        breedFieldTitle.setTextSize(fontSize);
        final EditText breedField = (EditText) findViewById(R.id.EditBreedName);
        breedField.setTextSize(fontSize);
        breedField.setText(dog.breed);

        //Age
        final TextView ageFieldTitle = (TextView) findViewById(R.id.ageTitle);
        ageFieldTitle.setTextSize(fontSize);
        final EditText ageField = (EditText) findViewById(R.id.EditAge);
        ageField.setTextSize(fontSize);
        ageField.setText("" + dog.age);

        //Weight
        final TextView weightTitle = (TextView) findViewById(R.id.weightTitle);
        weightTitle.setTextSize(fontSize);
        final EditText weightField = (EditText) findViewById(R.id.EditTextWeight);
        weightField.setTextSize(fontSize);
        weightField.setText("" + dog.weightLb);

        //Body Score
        final TextView bsFieldTitle = (TextView) findViewById(R.id.bodyScoreTitle);
        bsFieldTitle.setTextSize(fontSize);
        final EditText bsField = (EditText) findViewById(R.id.EditBodyScore);
        bsField.setTextSize(fontSize);
        bsField.setText("" + dog.bodyScore);

        //Amount Feed
        final TextView amountFieldTitle = (TextView) findViewById(R.id.amountFeedTitle);
        amountFieldTitle.setTextSize(fontSize);
        final EditText amountField = (EditText) findViewById(R.id.EditAmountFeed);
        amountField.setTextSize(fontSize);
        amountField.setText("" + dog.feedingAmount);

        //FeederType
        final TextView feederFieldTitle = (TextView) findViewById(R.id.feedTypeTitle);
        feederFieldTitle.setTextSize(fontSize);
        final EditText feederField = (EditText) findViewById(R.id.EditFeedType);
        feederField.setTextSize(fontSize);
        feederField.setText(dog.feederType);

        //Food Type
        final TextView foodFieldTitle = (TextView) findViewById(R.id.foodTitle);
        foodFieldTitle.setTextSize(fontSize);
        final EditText foodField = (EditText) findViewById(R.id.EditFood);
        foodField.setTextSize(fontSize);
        foodField.setText(dog.foodType);

        //Treat restrictions
        final TextView treatsFieldTitle = (TextView) findViewById(R.id.treatsTitle);
        treatsFieldTitle.setTextSize(fontSize);
        final EditText treatsField = (EditText) findViewById(R.id.EditTreats);
        treatsField.setTextSize(fontSize);
        treatsField.setText(dog.treatRestrictions);

        //Meals
        final TextView mealsFieldTitle = (TextView) findViewById(R.id.mealsTitle);
        mealsFieldTitle.setTextSize(fontSize);
        final EditText mealsField = (EditText) findViewById(R.id.Editmeals);
        mealsField.setTextSize(fontSize);
        mealsField.setText("" + dog.meals);

        //Allergies
        final TextView allergiesFieldTitle = (TextView) findViewById(R.id.allergiesTitle);
        allergiesFieldTitle.setTextSize(fontSize);
        final EditText allergiesField = (EditText) findViewById(R.id.EditAllergies);
        allergiesField.setTextSize(fontSize);
        allergiesField.setText(dog.allergies);

        //Fish oil?
        final TextView fishOilFieldTitle = (TextView) findViewById(R.id.fishOilTitle);
        fishOilFieldTitle.setTextSize(fontSize);
        final RadioGroup fishOilGroup = (RadioGroup) findViewById(R.id.FishOilGroup);
        if (dog.fishOil != null) {
            if (dog.fishOil.equals("Yes")) {
                fishOilGroup.check(R.id.FishOilYes);
            }
        } else {
            fishOilGroup.check(R.id.FishOilNo);
        }
        final RadioButton fishOilYes = (RadioButton) findViewById(R.id.FishOilYes);
        fishOilYes.setTextSize(fontSize);
        final RadioButton fishOilNo = (RadioButton) findViewById(R.id.FishOilNo);
        fishOilNo.setTextSize(fontSize);

        //Comments
        final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
        feedbackField.setTextSize(fontSize);
    }

    public void sendFeedback(View button) {
        String error = "";
        settings = getSharedPreferences("settings", 0);
        int fontSize = settings.getInt("fontSize", 18);
        final EditText parentNameField = (EditText) findViewById(R.id.EditParentName);
        parentNameField.setTextSize(fontSize);
        String parentName = parentNameField.getText().toString();
        if (parentName.equals("")) {
            error.concat("Parent name field cannot be empty! ");
        }

        //Dog Breed
        final EditText breedField = (EditText) findViewById(R.id.EditBreedName);
        breedField.setTextSize(fontSize);
        String breed = breedField.getText().toString();
        if (breed.equals("")) {
            error.concat("Breed field cannot be empty! ");
        }

        //Age
        final EditText ageField = (EditText) findViewById(R.id.EditAge);
        ageField.setTextSize(fontSize);
        String age = ageField.getText().toString().toString();
        if (age.equals("")) {
            error.concat("Age field cannot be empty! ");
        }

        //Weight
        final EditText weightField = (EditText) findViewById(R.id.EditTextWeight);
        weightField.setTextSize(fontSize);
        String weight = weightField.getText().toString();
        if (weight.equals("")) {
            error.concat("Email field cannot be empty! ");
        }

        //Body Score
        final EditText bsField = (EditText) findViewById(R.id.EditBodyScore);
        bsField.setTextSize(fontSize);
        String bodyScore = bsField.getText().toString();
        if (bodyScore.equals("")) {
            error.concat("Body Score field cannot be empty!");
        } else {
            double num = Double.parseDouble(bodyScore);

            if (num < 4.0 || num > 5.0) {
                bsField.setTextColor(255);
            }
        }

        //Amount Feed
        final EditText amountField = (EditText) findViewById(R.id.EditAmountFeed);
        amountField.setTextSize(fontSize);
        String amount = amountField.getText().toString();
        if (amountField == null) {
            error.concat("Food amount field cannot be empty!");
        }

        //FeederType
        final EditText feederField = (EditText) findViewById(R.id.EditFeedType);
        feederField.setTextSize(fontSize);
        String feed = feederField.getText().toString();

        //Food Type
        final EditText foodField = (EditText) findViewById(R.id.EditFood);
        foodField.setTextSize(fontSize);
        String foodtype = foodField.getText().toString();

        //Treat restrictions
        final EditText treatsField = (EditText) findViewById(R.id.EditTreats);
        treatsField.setTextSize(fontSize);
        String treats = treatsField.getText().toString();

        //Meals
        final EditText mealsField = (EditText) findViewById(R.id.Editmeals);
        mealsField.setTextSize(fontSize);
        String meals = mealsField.getText().toString();

        //Allergies
        final EditText allergiesField = (EditText) findViewById(R.id.EditAllergies);
        allergiesField.setTextSize(fontSize);
        String allergies = allergiesField.getText().toString();

        final RadioGroup fishOilGroup = (RadioGroup) findViewById(R.id.FishOilGroup);
        boolean fishOil;
        Button checkedFishOilButton = (Button) findViewById(fishOilGroup.getCheckedRadioButtonId());
        if (checkedFishOilButton.getId() == R.id.FishOilYes) {
            fishOil = true;
        } else {
            fishOil = false;
        }

        //Comments
        final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
        feedbackField.setTextSize(fontSize);
        String feedback = feedbackField.getText().toString();

        //spinner for pounds/kg
        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();

        sendFeedbackMessage(parentName, breed, age, weight, bodyScore, amount, feed, foodtype,
                treats, meals, allergies, fishOil, feedback, feedbackType);
    }

    public void sendFeedbackMessage(String parentName, String breed, String age, String weight,
                                    String bodyScore, String amount, String feed,
                                    String foodtype, String treats, String meals, String allergies,
                                    boolean fishOil, String comment, String metric) {

        dog.fosterParent = parentName;
        dog.breed = breed;
        dog.feederType = feed;
        dog.foodType = foodtype;
        dog.treatRestrictions = treats;
        dog.meals = Double.parseDouble(meals);
        dog.allergies = allergies;
        if (fishOil) {
            dog.fishOil = "Yes";
        } else {
            dog.fishOil = "No";
        }
        dog.age = Integer.parseInt(age);
        double num = Double.parseDouble(bodyScore);

        dog.bodyScore = num;
        dog.feedingAmount = Double.parseDouble(amount);

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
        if (!comment.equals("") || comment != null) {
            dog.comments = dog.comments.concat(("\t\t" + (year) + "/" + (month + 1) + "/" +
                    (day) + " - " + hour + ":" + minute + ":" + second +
                    ":\n\t\t\t\t" + comment + "\n"));
        }

        Database.getInstance().updateDogData(dog);
        Intent fosterActivity = new Intent(getApplicationContext(), FosterActivity.class);
        fosterActivity.putExtra("dog", dog);
        startActivity(fosterActivity);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popupmenu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent refresh = new Intent(this, FosterActivity.class);
        refresh.putExtra("dog", Database.getInstance().getDogEntryByName(dog.name));
        startActivity(refresh);
        this.finish();
    }
}
