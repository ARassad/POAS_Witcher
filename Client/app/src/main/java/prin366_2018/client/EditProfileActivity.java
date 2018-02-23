package prin366_2018.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        final AutoCompleteTextView name = (AutoCompleteTextView)findViewById(R.id.text_name);
        name.setText(getIntent().getStringExtra("name"));
        final AutoCompleteTextView aboutMe = (AutoCompleteTextView)findViewById(R.id.text_about);
        aboutMe.setText(getIntent().getStringExtra("aboutMe"));
        final ImageButton photo = (ImageButton)findViewById(R.id.image);


        Button buttonSave = (Button)findViewById(R.id.button_save_edit);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("aboutMe", aboutMe.getText().toString());
                intent.putExtra("photo", encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ImageButton image = (ImageButton)findViewById(R.id.image);
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

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageButton imageView = (ImageButton) findViewById(R.id.image);

        switch(requestCode) {
            case GALLERY_REQUEST:
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 120, 160, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);

        }
    }
}