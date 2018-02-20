package prin366_2018.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ServerExchange.Location;
import ServerExchange.LocationsList;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.AuthorizationRequest;
import ServerExchange.ServerRequests.IServerAnswerHandler;
import ServerExchange.ServerRequests.LoginRequest;
import ServerExchange.ServerRequests.RegistrationRequest;
import ServerExchange.ServerRequests.ServerRequest;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;



    class onLoginHandler implements IServerAnswerHandler<Boolean>{

        @Override
        public void handle(Boolean answ) {
            if (answ){
                //LocationsList.refillFromServer();
                Intent intent= new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(intent);
            }

        }
    }
    /**
     * В этот метод пихать всё, что должно произойти при загрузке приложения
     * Проследите, что была защита от затирания т.к. этот метод будет заупскаться каждый раз,
     * Когда будет открываться экран Входа в приложуху
     */
    boolean isFirstLoad = true;
    protected void onAppStart(){
        if (isFirstLoad) {
            SharedPreferences params = getSharedPreferences("settings", MODE_PRIVATE);
            //Персональная настройка Андрея
            //ServerRequest.setDefaultAddress( params.getString("server_address", "localhost"));
            //ServerRequest.setDefaultAddress(params.getString("server_address", "192.168.0.4"));

            //ip Миши
             ServerRequest.setDefaultAddress( params.getString("server_address", "212.237.54.117"));

            if (!params.contains("server_address")) {
                params.edit().putString("server_address", "localhost");
            }
            isFirstLoad = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onAppStart();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new AuthorizationRequest().login(mEmailView.getText().toString(), mPasswordView.getText().toString(), new onLoginHandler());
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }





}

