package com.example.myinsta;

import android.content.Intent;
import android.hardware.input.InputManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    Button button;
    TextView loginorsignup;
    EditText username,password;

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }

    public void signUp(View view){
        if(username.getText().toString().matches("")||password.getText().toString().matches("")){
            Toast.makeText(this, "Username & Password required", Toast.LENGTH_SHORT).show();
        }
        else{
            ParseUser parseUser = new ParseUser();
            parseUser.setUsername(username.getText().toString());
            parseUser.setPassword(password.getText().toString());
            Log.i("doing this",button.getText().toString());
            if(button.getText().toString().matches("LOGIN")){
//                Log.i("Logging in","Logging In");
                parseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user == null){
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.i("Login Successfull","Login Successfull");
                            Toast.makeText(MainActivity.this,"Login Successfull", Toast.LENGTH_SHORT).show();
                            showUserList();
                        }
                    }
                });
            }else if (button.getText().toString().matches("SIGN UP")){
//                Log.i("Signing up","Signing up");
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.i("SignUp Successfull","SignUp Successfull");
                            Toast.makeText(MainActivity.this,"SignUp Successfull", Toast.LENGTH_SHORT).show();
                            showUserList();
                        }
                        else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        loginorsignup = (TextView)findViewById(R.id.loginOrSignup);
        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        password.setOnKeyListener(this);

        loginorsignup.setOnClickListener(this);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(this);
        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginOrSignup){
            if(button.getText().toString() == "LOGIN"){
                Log.i("sdfsd",button.getText().toString());
                username.setText("");
                password.setText("");
                button.setText("SIGN UP");
                loginorsignup.setText("or Login?");
            }else{
                username.setText("");
                password.setText("");
                button.setText("LOGIN");
                loginorsignup.setText("or SignUp?");
            }
        }
        if(v.getId() == R.id.constraintLayout){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()== KeyEvent.ACTION_DOWN){
            signUp(v);
        }
        return false;
    }
}
