package edu.upenn.cis350.workingdogapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DogSelectFragment.InteractionListener, LoginManager.LoginObserver {
    protected String[] mPlanetTitles;
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected CharSequence mDrawerTitle;
    protected CharSequence mTitle;
    protected ActionBarDrawerToggle mDrawerToggle;

    /* calls LoginManager which will set up the entire dog list upon app startup
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoginManager loginManager = LoginManager.getInstance(MainActivity.this);
        loginManager.addObserver(this);
        onCreateDrawer();
    }

    protected void onCreateDrawer() {
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        if (mDrawerList != null) {
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, mPlanetTitles));
            // Set the list's click listener
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                //R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        if (mDrawerLayout != null) {
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

    /* once a dog is selected from the list, we access that dog from Database.java and use its data
    in FosterActivity.class which just shows the dog's data plus
    some other buttons (see FosterActivity.java). Make sure to finish MainActivity from the history
    stack.
     */
    @Override
    public void onDogSelectListInteraction(Database.DogEntry item) {
        Intent intent = new Intent(getApplicationContext(), FosterActivity.class);
        intent.putExtra("dog", item);
        startActivity(intent);
        finish();
    }


    // creates call to onPrepareOptionsMenu()
    @Override
    public void loginNotify(String loggedInUserName) {
        invalidateOptionsMenu();
    }

    //inflates the popup menu upon creating this activity. See PopupMenu.java for details
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popupmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* if logged in (we only have admin accounts so far), show the "add dog" button and allow
    access.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean loggedin = LoginManager.getInstance(this).getLoggedInUserName() != null;
        menu.findItem(R.id.add_dog).setVisible(loggedin);
        return super.onPrepareOptionsMenu(menu);
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
            default:
                intent = PopupMenu.Select(item.getItemId(), getApplicationContext());
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivityForResult(intent, 1);

                }
                return super.onOptionsItemSelected(item);
        }
    }

    //helper function for handling the dog list's makeup
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /* helper function related to pressing buttons in
    the hamburger menu, each button has functionality dpeending if client is logged in or not
     */
    private void selectItem(int position) {
        switch (position) {
            case 0:
                if (LoginManager.getInstance(this).getLoggedInUserName() == null) {
                    LoginManager.getInstance(this).showLoginDialog(this);
                } else {
                    Toast.makeText(this, "You are already logged in!", Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                if (LoginManager.getInstance(this).getLoggedInUserName() != null) {
                    LoginManager.getInstance(this).logout();
                    Toast.makeText(this, "You have logged out!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "You are not logged in!", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (LoginManager.getInstance(this).getLoggedInUserName() != null) {
                    LoginManager.getInstance(this).showPasswordChangeDialog(this);
                } else {
                    Toast.makeText(this, "You are not logged in!", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                if (LoginManager.getInstance(this).getLoggedInUserName() != null) {
                    Intent intent = new Intent(MainActivity.this, ArchiveActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "You are not logged in!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    //update the list if anything changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //ignore
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }
}
