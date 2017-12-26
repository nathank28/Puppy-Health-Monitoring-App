package edu.upenn.cis350.workingdogapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


public class PopupMenu {

    //called whenever we select an item from the top bar menu
    public static Intent Select(int itemId, Context context) {
        switch (itemId) {
            //case for adding a dog
            case R.id.add_dog:
                if (LoginManager.getInstance(context).getLoggedInUserName() != null) {
                    Intent newdog = new Intent(context, NewDogFormActivity.class);

                    //We create a "new dog" before going into NewDogFormActivity
                    newdog.putExtra("dog", new Database.DogEntry());
                    return newdog;
                }
                else {
                    Toast.makeText(context, "You are not logged in!", Toast.LENGTH_LONG).show();
                    return null;
                }

                //case for the font settings (so far)
            case R.id.settingsOption:
                Intent intent = new Intent(context, SettingsActivity.class);
                return intent;

            //case for the exporting option
            case R.id.export:
                Export a = new Export();
                a.export(Database.getInstance().getDogEntryList(),
                        Database.getInstance().getArchivedDogEntryList());
                Toast.makeText(context, "Dogs.txt will be saved in your Download folder", Toast.LENGTH_LONG).show();
                return null;
            case R.id.email:
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setType("text/plain");
                email.putExtra(Intent.EXTRA_SUBJECT, "");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setData(Uri.parse("mailto:"));
                email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Export exp = new Export();

                exp.export(Database.getInstance().getDogEntryList(),
                        Database.getInstance().getArchivedDogEntryList());

                File root = new File(Environment.getExternalStorageDirectory(), "Download");
                File file = new File(root, "dogs.txt");
                if (!file.exists() || !file.canRead()) {
                    return email;
                }
                Uri uri = Uri.fromFile(file);
                email.putExtra(Intent.EXTRA_STREAM, uri);
                return email;
            default:
                return null;
        }
    }
}
