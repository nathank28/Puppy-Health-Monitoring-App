package edu.upenn.cis350.workingdogapp;

import android.app.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class SettingsActivity extends MainActivity implements AdapterView.OnItemSelectedListener {
    Spinner fontSpinner;
    int fontSelection;
    SharedPreferences settings;
    String[] fontOptions = {"Small", "Medium", "Large"};


    /*
    SharedPreferences are client-specific preferences stored on the device. In this case, we store
    the fontSelection into the SharedPreferences called "settings.
    "
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        RadioGroup fontButtons = (RadioGroup) findViewById(R.id.fontButtons);

        settings = getSharedPreferences("settings", 0);

        fontSelection = 12;

        Button fontButton1 = (RadioButton) findViewById(R.id.radioButton1);
        fontButton1.setTextSize(settings.getInt("fontSize", 18));

        Button fontButton2 = (RadioButton) findViewById(R.id.radioButton2);
        fontButton2.setTextSize(settings.getInt("fontSize", 18));

        Button fontButton3 = (RadioButton) findViewById(R.id.radioButton3);
        fontButton3.setTextSize(settings.getInt("fontSize", 18));

        TextView settingsTitle = (TextView) findViewById(R.id.settingsTitle);
        settingsTitle.setTextSize(settings.getInt("fontSize", 18));

        TextView fontSizeTitle = (TextView) findViewById(R.id.fontSizeTitle);
        fontSizeTitle
                .setTextSize(settings.getInt("fontSize", 18));

        if (settings.getInt("fontSize", 0) == 12){
            fontButtons.check(R.id.radioButton1);

        }
        else if (settings.getInt("fontSize", 0) == 18){
            fontButtons.check(R.id.radioButton2);

        }
        else {
            fontButtons.check(R.id.radioButton3);

        }
    }

    //ignore
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == fontSpinner) {
            Toast.makeText(getApplicationContext(), fontOptions[position], Toast.LENGTH_SHORT).show();
            if (fontOptions[position].equals("Small")) {
                fontSelection = 12;

            }
            else if (fontOptions[position].equals("Medium")) {
                fontSelection = 18;
            }
            else {
                fontSelection = 24;
            }
        }

    }

//Changes the fontSelection field depending on what font option you picked.
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                if (checked)
                    fontSelection = 12;
                    break;
            case R.id.radioButton2:
                if (checked)
                    fontSelection = 18;
                    break;
            case R.id.radioButton3:
                if (checked)
                    fontSelection = 24;
                    break;
        }
    }

    //Just ignore this. Should go back to whatever text size you had it at.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Function used when hitting the save settings button.
    public void saveSettings(View view) {
        setResult(RESULT_OK, null);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("fontSize", fontSelection);
        editor.commit();
        FileOutputStream settingsStream;
        byte[] bytes = ("FontSize:" + fontSelection).getBytes();
        try {
            settingsStream = openFileOutput("settingsText", Context.MODE_PRIVATE);
            settingsStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }
}
