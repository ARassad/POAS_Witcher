package prin366_2018.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.system.Os;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import ServerExchange.ImageConvert;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;
import ServerExchange.ServerRequests.UpdateProfileRequest;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Dryush on 18.02.2018.
 */


public class EditProfileActivity extends AppCompatActivity {

    static final private int RESULT_CANCEL = 0;
    static final private int RESULT_OK = 1;
    static final int GALLERY_REQUEST = 2;

    AutoCompleteTextView name;
    AutoCompleteTextView aboutMe;
    ImageButton image;

    String oldName;
    String oldAboutMe;
    Boolean isPhotoChanged = false;

    AlertDialog pleaseWaitMessage;


    //TODO: Протестить
    private class onImageEdited extends DefaultServerAnswerHandler<Boolean> {

        boolean isEnded = false;
        boolean isOk = true;

        public onImageEdited(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {

            Intent intent = new Intent();
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("aboutMe", aboutMe.getText().toString());
            if ( bitmap != null)
                intent.putExtra("photo", ImageConvert.toBase64Str(bitmap));
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void errorHandle(String errorMessage) {

            super.errorHandle(errorMessage);
        }

        @Override
        public void exceptionHandle(Exception excp) {

            super.exceptionHandle(excp);
        }
    }
    UpdateProfileRequest saveRequest = new UpdateProfileRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        pleaseWaitMessage = new AlertDialog.Builder(EditProfileActivity.this).create();
        pleaseWaitMessage.setTitle("Сохраняем");
        pleaseWaitMessage.setCancelable(false);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        name = (AutoCompleteTextView)findViewById(R.id.text_name);
        oldName = getIntent().getStringExtra("name");
        name.setText(oldName);


        aboutMe = (AutoCompleteTextView)findViewById(R.id.text_about);
        oldAboutMe = getIntent().getStringExtra("aboutMe");
        aboutMe.setText(oldAboutMe);


        image = (ImageButton) findViewById(R.id.image);

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
                                            isPhotoChanged  && bitmap != null ? bitmap : null, //TODO: Сделать получение картинки с виджета, а то криво
                                            oie);
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ШОТО ПРОИСХОДИТ ПРИ НАЖАТИИ НА КНОПКУ ФОТОЧКИ
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
    }
/*
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
*/
    Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        //ImageButton imageView = (ImageButton) findViewById(R.id.image);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    isPhotoChanged = true;
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 120, 160, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //imageView.setImageBitmap(bitmap);
                    image.setImageBitmap(bitmap);

            }
        }
    }
}