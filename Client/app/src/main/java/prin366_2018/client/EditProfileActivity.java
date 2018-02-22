package prin366_2018.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;
import ServerExchange.ServerRequests.UpdateProfileRequest;

/**
 * Created by Dryush on 18.02.2018.
 */


public class EditProfileActivity extends AppCompatActivity {

    static final private int RESULT_CANCEL = 0;
    static final private int RESULT_OK = 1;

    String oldName;
    String oldAboutMe;
    Boolean isPhotoChanged = false;

    //TODO: Протестить
    private class onImageEdited extends DefaultServerAnswerHandler<Boolean> {

        boolean isEnded = false;
        boolean isOk = true;

        public onImageEdited(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {
            isEnded = true;
            isOk = answ;
        }

        @Override
        public void errorHandle(String errorMessage) {
            isEnded = true;
            isOk = false;
            super.errorHandle(errorMessage);
        }

        @Override
        public void exceptionHandle(Exception excp) {
            isEnded = true;
            isOk = false;
            super.exceptionHandle(excp);
        }
    }
    UpdateProfileRequest saveRequest = new UpdateProfileRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        final AutoCompleteTextView name = (AutoCompleteTextView)findViewById(R.id.text_name);
        name.setText(getIntent().getStringExtra("name"));
        oldName = name.getText().toString();
        final AutoCompleteTextView aboutMe = (AutoCompleteTextView)findViewById(R.id.text_about);
        aboutMe.setText(getIntent().getStringExtra("aboutMe"));
        oldAboutMe = aboutMe.getText().toString();

        final ImageView photo = (ImageView) findViewById(R.id.image);

        Button buttonSave = (Button)findViewById(R.id.button_save_edit);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ШОТО ПРОИСХОДИТ ПРИ НАЖАТИИ НА КНОПКУ "СОХРАНИТЬ"
                onImageEdited oie = new onImageEdited(EditProfileActivity.this);
                String newName = name.getText().toString();
                String newAboutMe = aboutMe.getText().toString();

                saveRequest.updateProfile( newName.equals(oldName) ? null : newName,
                                            newAboutMe.equals(oldAboutMe) ? null : newAboutMe,
                                            isPhotoChanged ? ((BitmapDrawable)photo.getDrawable()).getBitmap() : null,
                                            oie);
                while (!oie.isEnded){
                    //Ждём пока не закончится поток
                }
                if (!oie.isOk){
                    //TODO: Как-то обработать ошибку
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("aboutMe", aboutMe.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        Button image = (Button)findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ШОТО ПРОИСХОДИТ ПРИ НАЖАТИИ НА КНОПКУ ФОТОЧКИ
                isPhotoChanged = true;
            }
        });
    }

    private void setButton(Button button, final View v) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v.getVisibility() == View.VISIBLE)
                    v.setVisibility(View.GONE);
                else
                    v.setVisibility(View.VISIBLE);
            }
        });
    }
}