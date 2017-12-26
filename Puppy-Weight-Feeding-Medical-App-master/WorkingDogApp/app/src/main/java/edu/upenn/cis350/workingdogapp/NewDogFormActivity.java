package edu.upenn.cis350.workingdogapp;

import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class NewDogFormActivity extends MainActivity {

    Database.DogEntry dog;
    SharedPreferences settings;


    /*Creates the form and uses the SharedPreferences font settings to update the text size for each
    XML entity. See createdog.xml for a view of all of the EditText's.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createdog);

        //This dog should be a new Dog. See PopupMenu.java for details
        dog = (Database.DogEntry) getIntent().getExtras().getSerializable("dog");

        /*fetches the font settings. See SettingsActivity.java and SharedPreferences android docs
        for details. The font is changed programatically.
        */
        settings = getSharedPreferences("settings", 0);
        int fontSize = settings.getInt("fontSize", 18);

        //Dog's name
        final EditText dogNameField = (EditText) findViewById(R.id.EditDogName);
        dogNameField.setTextSize(fontSize);
        String dogName = dogNameField.getText().toString();

        //Parent's name
        final EditText parentNameField = (EditText) findViewById(R.id.EditTextName);
        parentNameField.setTextSize(fontSize);
        String parentName = parentNameField.getText().toString();

        //Dog Breed
        final EditText breedField = (EditText) findViewById(R.id.EditBreedName);
        breedField.setTextSize(fontSize);
        String breed = breedField.getText().toString();

        //Age
        final EditText ageField = (EditText) findViewById(R.id.EditAge);
        ageField.setTextSize(fontSize);
        String age = ageField.getText().toString().toString();

        //Weight
        final EditText weightField = (EditText) findViewById(R.id.EditTextWeight);
        weightField.setTextSize(fontSize);
        String weight = weightField.getText().toString();

        //Body Score
        final EditText bsField = (EditText) findViewById(R.id.EditBodyScore);
        bsField.setTextSize(fontSize);
        String bodyScore = bsField.getText().toString();


        //Amount Feed
        final EditText amountField = (EditText) findViewById(R.id.EditAmountFeed);
        amountField.setTextSize(fontSize);
        String amount = amountField.getText().toString();

        //Number of Meals
        final EditText numMeals = (EditText) findViewById(R.id.EditMeals);
        numMeals.setTextSize(fontSize);

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

        //Treat restrictions
        final EditText allergiesField = (EditText) findViewById(R.id.EditAllergies);
        allergiesField.setTextSize(fontSize);
        String allergies = allergiesField.getText().toString();


        //Comments
        final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
        feedbackField.setTextSize(fontSize);
        String comment = feedbackField.getText().toString();

        //spinner for pounds/kg
        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();


        //Fish Oil title
        TextView fishOilView = (TextView) findViewById(R.id.createFishOilTextView);
        fishOilView.setTextSize(fontSize);

        //buttons for Fish Oil
        final RadioGroup fishOilGroup = (RadioGroup) findViewById(R.id.createFishOilGroup);
        fishOilGroup.check(R.id.createFishOilYes);
        final RadioButton fishOilYes = (RadioButton) findViewById(R.id.createFishOilYes);
        final  RadioButton fishOilNo = (RadioButton) findViewById(R.id.createFishOilNo);
        fishOilYes.setTextSize(fontSize);
        fishOilNo.setTextSize(fontSize);

        //breakfast checkbox
        final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
        responseCheckbox.setTextSize(fontSize);

    }

    //Gets all the information that the client entered into the form
    public void sendFeedback(View button) {
        ArrayList<String> attributeList = new ArrayList<String>();
        settings = getSharedPreferences("settings", 0);
        int fontSize = settings.getInt("fontSize", 18);

        //Dog's name
        final EditText dogNameField = (EditText) findViewById(R.id.EditDogName);
        dogNameField.setTextSize(fontSize);
        String dogName = dogNameField.getText().toString();
        attributeList.add(dogName);

        //Parent's name
        final EditText parentNameField = (EditText) findViewById(R.id.EditTextName);
        parentNameField.setTextSize(fontSize);
        String parentName = parentNameField.getText().toString();
        attributeList.add(parentName);

        //Dog Breed
        final EditText breedField = (EditText) findViewById(R.id.EditBreedName);
        breedField.setTextSize(fontSize);
        String breed = breedField.getText().toString();
        attributeList.add(breed);

        //Age
        final EditText ageField = (EditText) findViewById(R.id.EditAge);
        ageField.setTextSize(fontSize);
        String age = ageField.getText().toString().toString();
        attributeList.add(age);

        //Weight
        final EditText weightField = (EditText) findViewById(R.id.EditTextWeight);
        weightField.setTextSize(fontSize);
        String weight = weightField.getText().toString();
        attributeList.add(weight);

        //Body Score
        final EditText bsField = (EditText) findViewById(R.id.EditBodyScore);
        bsField.setTextSize(fontSize);
        String bodyScore = bsField.getText().toString();
        attributeList.add(bodyScore);

        //Amount Feed
        final EditText amountField = (EditText) findViewById(R.id.EditAmountFeed);
        amountField.setTextSize(fontSize);
        String amount = amountField.getText().toString();
        attributeList.add(amount);

        //FeederType
        final EditText feederField = (EditText) findViewById(R.id.EditFeedType);
        feederField.setTextSize(fontSize);
        String feed = feederField.getText().toString();
        attributeList.add(feed);

        //Food Type
        final EditText foodField = (EditText) findViewById(R.id.EditFood);
        foodField.setTextSize(fontSize);
        String foodtype = foodField.getText().toString();
        attributeList.add(foodtype);

        //Treat restrictions
        final EditText treatsField = (EditText) findViewById(R.id.EditTreats);
        treatsField.setTextSize(fontSize);
        String treats = treatsField.getText().toString();
        attributeList.add(treats);

        //Treat restrictions
        final EditText allergiesField = (EditText) findViewById(R.id.EditAllergies);
        allergiesField.setTextSize(fontSize);
        String allergies = allergiesField.getText().toString();
        attributeList.add(allergies);

        //Comments
        final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
        feedbackField.setTextSize(fontSize);
        String comment = feedbackField.getText().toString();

        //spinner for pounds/kg
        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();


        //Fish Oil title
        final TextView fishOilView = (TextView) findViewById(R.id.createFishOilTextView);
        fishOilView.setTextSize(fontSize);

        //buttons for Fish Oil
        final RadioGroup fishOilGroup = (RadioGroup) findViewById(R.id.createFishOilGroup);
        fishOilGroup.check(R.id.createFishOilYes);
        final RadioButton fishOilYes = (RadioButton) findViewById(R.id.createFishOilYes);
        final  RadioButton fishOilNo = (RadioButton) findViewById(R.id.createFishOilNo);
        fishOilYes.setTextSize(fontSize);
        fishOilNo.setTextSize(fontSize);

        //breakfast checkbox
        final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
        responseCheckbox.setTextSize(fontSize);
        boolean bRequiresResponse = responseCheckbox.isChecked();

        boolean formFilledOut;
        Iterator<String> attributeIter = attributeList.iterator();

        //IF NOT ALL FIELDS HAVE BEEN FILLED OUT, SHOW AN ERROR MESSAGE
        while (attributeIter.hasNext()) {
            String attribute = attributeIter.next();
            if (attribute.equals("")) {
             Toast t = Toast.makeText(this,
                     "You cannot leave any fields empty! Please go over the form again.",
                     Toast.LENGTH_LONG);
                t.show();
                break;
            }
            else if (!attributeIter.hasNext()) {
                sendFeedbackMessage(dogName, parentName, breed, age, weight, bodyScore, amount, feed, foodtype,
                        treats, allergies, comment, feedbackType, bRequiresResponse, fishOilGroup);
            }

        }

    }

    //Helper function for when we know every field has been filled out.
    //It is called in sendFeedback()
    public void sendFeedbackMessage(String dogName, String parentName, String breed, String age, String weight, String bodyScore, String amount, String feed,
                                    String foodType, String treats, String allergies, String comment, String metric, Boolean tf, RadioGroup fishOilGroup) {
        dog.name = dogName;
        dog.fosterParent = parentName;
        dog.breed = breed;
        dog.feederType = feed;
        dog.foodType = foodType;
        dog.treatRestrictions = treats;
        dog.allergies = allergies;
        dog.age = Integer.parseInt(age);
        dog.bodyScore = Double.parseDouble(bodyScore);
        dog.feedingAmount = Double.parseDouble(amount);

        if (fishOilGroup.getCheckedRadioButtonId() == R.id.createFishOilYes) {
            dog.fishOil = "Yes";
        }
        else {
            dog.fishOil = "No";
        }

        if (metric.equals("Kilograms")) {
            dog.weightLb = Double.parseDouble(weight) * 0.453592;
        }
        else {
            dog.weightLb = Double.parseDouble(weight);
        }

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        System.out.println("COMMENT " + comment);
        if (!comment.equals("")) {
            dog.comments = "" + ("\t\t" + (year) + ":" + (month + 1) + ":" + (day) + ":" + hour + ":" + minute + ":" + second +
                    "\n\t\t\t\t" + comment + "\n");
        }
        else {
            dog.comments = "";
        }

        //Updates the dog's data!
        Database.getInstance().updateDogData(dog);
        finish();
    }
}
