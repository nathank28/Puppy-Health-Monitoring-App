package edu.upenn.cis350.workingdogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class is the Archive's analog to FosterActivity.java. Please see FosterActivity.java for
 * more information
 */


public class DogInfoFragment extends Fragment implements View.OnClickListener{
    Database.DogEntry dog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DogInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dog = (Database.DogEntry) getArguments().getSerializable("dog");
        setHasOptionsMenu(true);
    }


    /* See SettingsActivity.java for more information abuot SharedPreferences. So far it is only
    used to programatically change font sizes of XML text entities.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_dog_info, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
        int fontSize = settings.getInt("fontSize", 18);

        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText("Name:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.name);
        name.setTextSize(fontSize);

        TextView parent = (TextView)view.findViewById(R.id.parent);
        parent.setText("Parent(s):\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.fosterParent);
        parent.setTextSize(fontSize);

        TextView breed = (TextView)view.findViewById(R.id.breed);
        breed.setText("Breed:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.breed);
        breed.setTextSize(fontSize);

        TextView age = (TextView)view.findViewById(R.id.age);
        age.setText("Age:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.age);
        age.setTextSize(fontSize);

        TextView weight = (TextView)view.findViewById(R.id.weight);
        weight.setText("Weight:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.weightLb + " lbs");
        weight.setTextSize(fontSize);

        TextView bodyScore = (TextView)view.findViewById(R.id.bodyScore);
        bodyScore.setText("Body Score:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.bodyScore);
        bodyScore.setTextSize(fontSize);

        double num = dog.bodyScore;
        if(num < 4.0 || num > 5.0){
            bodyScore.setTextColor(Color.RED);
        }

        TextView foodSize = (TextView)view.findViewById(R.id.foodSize);
        foodSize.setText("Amount of food to feed:\t\t\t\t\t\t" + dog.feederType);
        foodSize.setTextSize(fontSize);

        TextView feederType = (TextView)view.findViewById(R.id.feederType);
        feederType.setText("Feeder Type:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.feederType);
        feederType.setTextSize(fontSize);

        TextView foodType = (TextView)view.findViewById(R.id.foodType);
        foodType.setText("Food Type:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.foodType);
        foodType.setTextSize(fontSize);

        TextView treatRestrictions = (TextView)view.findViewById(R.id.treatRestrictions);
        treatRestrictions.setText("Treat Restrictions:\t\t\t\t\t\t\t\t\t\t\t" + dog.treatRestrictions);
        treatRestrictions.setTextSize(fontSize);

        TextView numMeals = (TextView)view.findViewById(R.id.meals);
        if (numMeals != null) {
            numMeals.setText("Number of Meals:\t\t\t\t\t\t\t\t\t\t\t" + dog.treatRestrictions);
            numMeals.setTextSize(fontSize);
        }

        TextView allergies = (TextView)view.findViewById(R.id.allergies);
        allergies.setText("Allergies:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.allergies);
        allergies.setTextSize(fontSize);

        TextView fishOil = (TextView)view.findViewById(R.id.fishOil);
        fishOil.setText("Fish Oil:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.fishOil);
        fishOil.setTextSize(fontSize);

        TextView comments = (TextView)view.findViewById(R.id.comments);
        comments.setText("\nCOMMENTS:\n\n" + dog.comments);

        comments.setTextSize(fontSize);

        Button restoreDogButton = (Button) view.findViewById(R.id.button_restore_dog);
        restoreDogButton.setOnClickListener(this);

        return view;
    }

    //puts the dog back into the active dog's list
    private void restoreDog(){
        Database.getInstance().restoreDogEntry(dog);
        getActivity().getSupportFragmentManager().popBackStack();
    }


    //calls restoreDog()
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_restore_dog:
                restoreDog();
        }
    }
}

