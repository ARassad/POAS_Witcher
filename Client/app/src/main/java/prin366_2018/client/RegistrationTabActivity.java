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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import ServerExchange.LocationsList;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.AuthorizationRequest;
import ServerExchange.ServerRequests.RegistrationRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;

public class RegistrationTabActivity extends AppCompatActivity {

    static final int validLengthLogin = 5;
    static final int validLengthPassword = 5;

    private AutoCompleteTextView mLoginView;
    private AutoCompleteTextView mPasswordView;
    private AutoCompleteTextView mPasswordRepitView;
    private RadioButton mRadioButtWitcher;
    private RadioButton mRadioButtClient;
    private View mProgressView;
    private View mLoginFormView;

    static final private int SAVE_DATA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_tab);

        mLoginView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (AutoCompleteTextView) findViewById(R.id.password);
        mPasswordRepitView = (AutoCompleteTextView) findViewById(R.id.password_repeat);
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

    private boolean isLoginValid(String login){
        boolean isOk = true;
        if (login.length() < validLengthLogin)
            isOk = false;
        return isOk;
    }

    private boolean isPasswordValid(String password){
        boolean isOk = true;
        if (password.length() < validLengthPassword)
            isOk = false;
        return isOk;
    }

    private boolean registration(){
        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);
        mPasswordRepitView.setError(null);
        mRadioButtWitcher.setError(null);
        mRadioButtClient.setError(null);

        final String login = mLoginView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String passwordRepit = mPasswordRepitView.getText().toString();
        boolean isWitcher = mRadioButtWitcher.isChecked();
        boolean isClient = mRadioButtClient.isChecked();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (login.isEmpty()) {
            mLoginView.setError("Поле \"Логин\" необходимо для заполнения");
            focusView = mLoginView;
            cancel = true;
        }
        else if (!isLoginValid(login)) {
            mLoginView.setError("Некорректный логин");
            focusView = mLoginView;
            cancel = true;
        }
        if (password.isEmpty()){
            mPasswordView.setError(" Поле \"Пароль\" необходимо для заполнения");
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if (!isPasswordValid(password)) {
            mPasswordView.setError("Некорректный пароль");
            focusView = mPasswordView;
            cancel = true;
        }
        if ( !password.equals(passwordRepit) ){
            mPasswordRepitView.setError("Пароли не совпадают");
            focusView = mPasswordRepitView;
            cancel = true;
        }
        if ( isWitcher == isClient ) {
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
            //showProgress(true);

            Profile.ProfileType typeClient = isWitcher ? Profile.ProfileType.WITCHER :
                    Profile.ProfileType.CUSTOMER;

            //LocationsList.refillFromServer();

            new RegistrationRequest().registration(login, password, typeClient, new DefaultServerAnswerHandler<Boolean>(RegistrationTabActivity.this) {
                @Override
                public void handle(Boolean answ) {
                    if (answ!= null && answ == true){
                        FirebaseAuth.getInstance().signOut();
                        showProgress(true);
                        Intent intent = new Intent(RegistrationTabActivity.this, LoginActivity.class);


                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Подчищаем окна с регистрацией, чтоб к ним нельзя было вернуться через кнопку "назад"

                        intent.putExtra("email", login);
                        intent.putExtra("password", password);


                        startActivityForResult(intent, SAVE_DATA );
                    }
                    else{
                        View focusView = null;
                        mLoginView.setError("Логин занят, придумайте другой");
                        focusView = mLoginView;
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
