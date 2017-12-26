package  edu.upenn.cis350.workingdogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class FosterActivity extends MainActivity {
    Database.DogEntry dog;


    /* mainly sets up XML entities (activity_foster) and uses SharedPreferences for font settings
    (see SettingsActivity.java).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LoginManager loginManager = LoginManager.getInstance(this);
        setContentView(R.layout.activity_foster);
        dog = (Database.DogEntry) getIntent().getExtras().getSerializable("dog");
        SharedPreferences settings = getSharedPreferences("settings", 0);
        int fontSize = settings.getInt("fontSize", 18);


        TextView name = (TextView)findViewById(R.id.name);
        name.setText("Name:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.name);
        name.setTextSize(fontSize);

        TextView parent = (TextView)findViewById(R.id.parent);
        parent.setText("Parent(s):\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.fosterParent);
        parent.setTextSize(fontSize);

        TextView breed = (TextView)findViewById(R.id.breed);
        breed.setText("Breed:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.breed);
        breed.setTextSize(fontSize);

        TextView age = (TextView)findViewById(R.id.age);
        age.setText("Age:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.age);
        age.setTextSize(fontSize);

        TextView weight = (TextView)findViewById(R.id.weight);
        weight.setText("Weight:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.weightLb + " lbs");
        weight.setTextSize(fontSize);

        TextView bodyScore = (TextView)findViewById(R.id.bodyScore);
        bodyScore.setText("Body Score:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.bodyScore);
        bodyScore.setTextSize(fontSize);

        double num = dog.bodyScore;
        if(num < 4.0 || num > 5.0){
            bodyScore.setTextColor(Color.RED);
        }

        TextView foodSize = (TextView)findViewById(R.id.foodSize);
        foodSize.setText("Amount of food to feed:\t\t\t\t\t\t" + dog.feederType);
        foodSize.setTextSize(fontSize);

        TextView feederType = (TextView)findViewById(R.id.feederType);
        feederType.setText("Feeder Type:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.feederType);
        feederType.setTextSize(fontSize);

        TextView foodType = (TextView)findViewById(R.id.foodType);
        foodType.setText("Food Type:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.foodType);
        foodType.setTextSize(fontSize);

        TextView treatRestrictions = (TextView)findViewById(R.id.treatRestrictions);
        treatRestrictions.setText("Treat Restrictions:\t\t\t\t\t\t\t\t\t\t\t" + dog.treatRestrictions);
        treatRestrictions.setTextSize(fontSize);

        TextView numMeals = (TextView)findViewById(R.id.meals);
        if (numMeals != null) {
            numMeals.setText("Number of Meals:\t\t\t\t\t\t\t\t\t\t\t" + dog.meals);
            numMeals.setTextSize(fontSize);
        }

        TextView allergies = (TextView)findViewById(R.id.allergies);
        allergies.setText("Allergies:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.allergies);
        allergies.setTextSize(fontSize);

        TextView fishOil = (TextView)findViewById(R.id.fishOil);
        fishOil.setText("Fish Oil:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + dog.fishOil);
        fishOil.setTextSize(fontSize);

        TextView comments = (TextView)findViewById(R.id.comments);
        comments.setText("\nCOMMENTS:\n\n" + dog.comments);

        comments.setTextSize(fontSize);
    }

    //for the upper menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean loggedin = LoginManager.getInstance(this).getLoggedInUserName() != null;
        menu.findItem(R.id.add_dog).setVisible(loggedin);
        menu.findItem(R.id.edit_dog).setVisible(loggedin);
        menu.findItem(R.id.delete_dog).setVisible(loggedin);
        return super.onPrepareOptionsMenu(menu);
    }

    //button Morning Form
    public void launchForm(View button) {
        Intent morningForm = new Intent(this, MorningFormActivity.class);
        morningForm.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        morningForm.putExtra("dog", (Serializable) dog);
        startActivity(morningForm);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popupmenu, menu);
        getMenuInflater().inflate(R.menu.dog_menu, menu);
        return true;
    }

    //LAUNCH MORNING WEIGH IN FORM...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent refresh = new Intent(this, FosterActivity.class);
        List<Database.DogEntry> dogs = Database.getInstance().getDogEntryList();
        Database.DogEntry doge = null;
        for (Database.DogEntry d : dogs) {
            if (d.name.equals(dog.name)) {
                doge = d;
            }
        }
        refresh.putExtra("dog", doge);
        refresh.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(refresh);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.edit_dog:
                launchAdminForm();
                return super.onOptionsItemSelected(item);
            case R.id.delete_dog:
                removeDog();
                return super.onOptionsItemSelected(item);
            default:
                intent = PopupMenu.Select(item.getItemId(), this);
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivityForResult(intent, 1);
                }
                return super.onOptionsItemSelected(item);
        }
    }

    //overrides pressing the back button for activity history stack purposes
    @Override
    public void onBackPressed() {
        Intent backToMain = new Intent(getApplicationContext(), MainActivity.class);
        backToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backToMain);
        finish();
    }

    //create Dog ("+" button)
    public void launchAdminForm() {
        if (LoginManager.getInstance(this).getLoggedInUserName() != null) {
            Intent adminForm = new Intent(this, AdminFormActivity.class);
            adminForm.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            adminForm.putExtra("dog", (Serializable) dog);
            startActivityForResult(adminForm, 0);
        } else {
            Toast.makeText(this, "You are not logged in!", Toast.LENGTH_LONG).show();
        }

    }

    //button Add Comment
    public void commentsSection (View button) {
        Intent adminForm = new Intent(this, CommentActivity.class);
        adminForm.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        adminForm.putExtra("dog", (Serializable) dog);
        startActivityForResult(adminForm, 0);
    }

    //button Remove Dog
    public void removeDog () {
        if (LoginManager.getInstance(this).getLoggedInUserName() != null) {
            Database.getInstance().archiveDogEntry(dog);
            Intent backToMain = new Intent(getApplicationContext(), MainActivity.class);
            backToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backToMain);
            finish();
        } else {
            Toast.makeText(this, "You are not logged in!", Toast.LENGTH_LONG).show();
        }
    }

    //button Weight History
    public void weightHistory (View Button ){
        Intent commentsForm = new Intent(this, DogHistoryActivity.class);
        commentsForm.putExtra("dog", (Serializable) dog);
        startActivityForResult(commentsForm, 0);
    }
}
