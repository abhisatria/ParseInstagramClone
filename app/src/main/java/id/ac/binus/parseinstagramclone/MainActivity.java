package id.ac.binus.parseinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener{
    EditText etUsername,etPassword;
    Button btnSignUp;
    TextView tvLogin,tvDesc;
    Boolean signUpModeActive = true;
    ImageView logoImage;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btSignUp);
        tvDesc = findViewById(R.id.tvAlready);
        tvLogin = findViewById(R.id.tvLogin);
        logoImage = findViewById(R.id.logoIg);
        relativeLayout = findViewById(R.id.relativelayout);
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(logoImage.getWindowToken(),0);
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(relativeLayout.getWindowToken(),0);
            }
        });

        etPassword.setOnKeyListener(this);
        if(ParseUser.getCurrentUser() !=null)
        {
            Intent intent = new Intent(MainActivity.this,UserListActivity.class);
            startActivity(intent);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpClicked();
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signUpModeActive)
                {
                    signUpModeActive = false;
                    btnSignUp.setText("Login");
                    tvDesc.setText("Don't have an account?");
                    tvLogin.setText("Sign up here");
                }
                else{
                    signUpModeActive = true;
                    btnSignUp.setText("Sign Up");
                    tvDesc.setText("Already have an account?");
                    tvLogin.setText("Login here");
                }

            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        }

    public void signUpClicked() {
        if(signUpModeActive)
        {
            if(etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Username and Password are required!",Toast.LENGTH_SHORT).show();
            }
            else{
                String username,password;
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            Toast.makeText(getApplicationContext(),"Sign up Successfull",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
        else{
            //login code
            ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user!=null)
                    {
                        Log.i("Login Success","OKEE!");
                        Intent intent = new Intent(MainActivity.this,UserListActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            signUpClicked();
        }
        return false;
    }
}