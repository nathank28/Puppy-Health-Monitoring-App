package edu.upenn.cis350.workingdogapp;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class LoginManager {

    private static LoginManager instance;
    private Database databaseInstance;
    private Set<LoginObserver> observers;

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    private String loggedInUserName;

    //retrieves the app data upon startup. Called from MainActivity -> LoginManager.getInstance()
    private LoginManager(Context context){
        //loggedIn = false;
        databaseInstance = Database.getInstance();
        observers = new HashSet<>();
    }

//called from MainActivity upon app startup and further instances of viewing MainActivity
    public static LoginManager getInstance(Context context){
        if(instance == null){
            instance = new LoginManager(context);

        }
        return instance;
    }

    //called from MainActivity
    public void addObserver(LoginObserver observer){
        observers.add(observer);
    }

    //called from hamburger menu (in MainActivity so far)
    public void logout(){
        // loggedIn = false;
        loggedInUserName = null;
        for(LoginObserver observer: observers){
            observer.loginNotify(loggedInUserName);
        }
    }

    //called from hamburger menu (in MainActivity so far)
    private void login(String userName){
        //  loggedIn = true;
        loggedInUserName = userName;
        for(LoginObserver observer: observers){
            observer.loginNotify(loggedInUserName);
        }
    }


    //code for changing password. called from hamburger menu (in MainActivity so far)
    public void showPasswordChangeDialog(Context context) {
        final Dialog changePasswordDialog = new Dialog(context);
        changePasswordDialog.setContentView(R.layout.change_password_window);
        Button changePassword = (Button) changePasswordDialog.findViewById(R.id.changePasswordButton);
        Button btnCancel = (Button) changePasswordDialog.findViewById(R.id.btnCancel);
        final EditText currentPasswordField =
                (EditText) changePasswordDialog.findViewById(R.id.EditEnterCurrentPassword);
        final EditText newPasswordField =
                (EditText) changePasswordDialog.findViewById(R.id.EditEnterNewPassword);
        final EditText confirmNewPasswordField =
                (EditText) changePasswordDialog.findViewById(R.id.EditConfirmNewPassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPass = currentPasswordField.getText().toString();
                String newPass = newPasswordField.getText().toString();
                String confirmNewPass = confirmNewPasswordField.getText().toString();
                if (currentPass.equals("") ||
                        newPass.equals("") ||
                        confirmNewPass.equals("")) {
                    Toast.makeText(changePasswordDialog.getContext(), "You cannot leave any " +
                            "field blank!", Toast.LENGTH_LONG).show();
                }
                else if (!currentPass.equals(databaseInstance.getPassword(loggedInUserName))) {
                    Toast.makeText(changePasswordDialog.getContext(), "The value you entered for " +
                            "current password is incorrect.", Toast.LENGTH_LONG).show();
                }
                else if(!newPass.equals(confirmNewPass.toString())) {
                    Toast.makeText(changePasswordDialog.getContext(),
                            "New password and password reentry do not match.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    databaseInstance.changePassword(loggedInUserName, newPass);
                    changePasswordDialog.dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog.dismiss();
            }
        });

        changePasswordDialog.show();

    }

    //code for logging in. called from hamburger menu (in MainActivity so far)
    public void showLoginDialog(Context context){
        final Dialog loginDialog = new Dialog(context);
        loginDialog.setContentView(R.layout.login_window);

        Button btnLogin = (Button) loginDialog.findViewById(R.id.btnLogin);
        Button btnCancel = (Button) loginDialog.findViewById(R.id.btnCancel);
        final EditText txtPassword = (EditText)loginDialog.findViewById(R.id.txtPassword);
        final EditText txtUsername = (EditText)loginDialog.findViewById(R.id.txtUsername);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("login", txtUsername.getText().toString());
                Log.d("login", txtPassword.getText().toString());
                Log.d("login", databaseInstance.getPassword(txtUsername.getText().toString()));

                if (txtPassword.getText().toString().equals
                        (databaseInstance.getPassword(txtUsername.getText().toString()))){
                    login(txtUsername.getText().toString());
                    loginDialog.dismiss();
                } else {
                    Toast.makeText(loginDialog.getContext(),
                            "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });

        loginDialog.show();
    }


    public interface LoginObserver{
         void loginNotify(String loggedInUserName);
    }
}
