package prin366_2018.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import ServerExchange.LocationsList;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.AuthorizationRequest;
import ServerExchange.ServerRequests.RegistrationRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;

public class RegistrationTabActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordRepitView;
    private RadioButton mRadioButtWitcher;
    private RadioButton mRadioButtClient;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_tab);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordRepitView = (EditText) findViewById(R.id.password_repeat);
        mRadioButtWitcher = (RadioButton) findViewById(R.id.radButWitcher);
        mRadioButtClient = (RadioButton) findViewById(R.id.radButClient);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        mPasswordRepitView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        Button buttReg = (Button) findViewById(R.id.email_registration_button);
        buttReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.email_login_form);
    }

    private boolean registration(){
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordRepitView.setError(null);
        mRadioButtWitcher.setError(null);
        mRadioButtClient.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRepit = mPasswordRepitView.getText().toString();
        boolean isWitcher = mRadioButtWitcher.isChecked();
        boolean isClient = mRadioButtClient.isChecked();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !LoginActivity.isPasswordValid(password)) {
            mPasswordView.setError("Некорректный пароль");
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!TextUtils.isEmpty(password) && !password.equals(passwordRepit) ){
            mPasswordRepitView.setError("Пароли не совпадают");
            focusView = mPasswordRepitView;
            cancel = true;
        }
        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Это поле необходимо для заполнения");
            focusView = mEmailView;
            cancel = true;
        } else if (!LoginActivity.isEmailValid(email)) {
            mEmailView.setError("Некорректный логин");
            focusView = mEmailView;
            cancel = true;
        } else if ( isWitcher == isClient ) {
            mRadioButtWitcher.setError("Некорректный тип пользователя");
            focusView = mRadioButtWitcher;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Profile.ProfileType typeClient = Profile.ProfileType.WITCHER;
            if ( isClient )
                typeClient = Profile.ProfileType.CUSTOMER;

            LocationsList.refillFromServer();
            new RegistrationRequest().registration(email, password, typeClient, new DefaultServerAnswerHandler<Boolean>(RegistrationTabActivity.this) {
                @Override
                public void handle(Boolean answ) {
                    if (answ!= null && answ == true){
                        startActivity( new Intent(RegistrationTabActivity.this, LoginActivity.class));
                    }
                }
            });
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }



        return true;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
