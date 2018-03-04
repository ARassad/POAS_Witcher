package prin366_2018.client;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.internal.bind.MapTypeAdapterFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import ServerExchange.LocationsList;
import ServerExchange.ServerRequests.CreateAdvertRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;
import ServerExchange.ServerRequests.UpdateAdvertRequest;
import ServerExchange.ServerRequests.UpdateProfileRequest;

public class EditAdvertActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 2;
    Boolean isPhotoChanged = false;

    private TextView title, description, cost;
    private Spinner kingdom, city;

    private int selectedKingdomIndex;
    private int selectedCityIndex;
    private long[] locsIds;
    private long locId;

    ImageView[] photos = new ImageView[10];
    UpdateAdvertRequest updateRequest = new UpdateAdvertRequest();
    CreateAdvertRequest createRequest = new CreateAdvertRequest();

    LinkedList<Bitmap> bitmaps;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        //ImageButton imageView = (ImageButton) findViewById(R.id.image);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    isPhotoChanged = true;
                    ClipData selectedImage = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        selectedImage = imageReturnedIntent.getClipData();
                    }
                    try {
                        for (int i = 0; i < selectedImage.getItemCount(); ++i) {
                            bitmaps.add(MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage.getItemAt(i).getUri()));
                            Bitmap bm = Bitmap.createScaledBitmap(bitmaps.getLast(), 100, 100, false);
                            photos[i].setImageBitmap(bm);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private class onCreateAdvert extends DefaultServerAnswerHandler<Boolean>{

        public onCreateAdvert(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {
            if (answ != null && answ == true){
                goBack();
            }
        }
    }

    private class onUpdateAdvert extends DefaultServerAnswerHandler<Boolean>{

        public onUpdateAdvert(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {
            if (answ != null && answ == true){
                goBack();
            }
        }
    }


    private String kingdomVal = "";
    private String cityVal = "";
    private int bountyVal = 1;
    private String headerVal = "";
    private String descriptionVal = "";
    private long advertId = -1;
    private void getFromBeforeActivity(){
        Intent intent = getIntent();
        kingdomVal = intent.getStringExtra("kingdom");
        cityVal = intent.getStringExtra("city");
        bountyVal = intent.getIntExtra("bounty", 1);
        headerVal = intent.getStringExtra("header");
        descriptionVal = intent.getStringExtra("description");
        advertId = intent.getLongExtra("advertId", -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advert);

        final LocationsList locsList =LocationsList.getInstance();
        //{"Не указано"};

        getFromBeforeActivity();

                photos[0] = (ImageView)findViewById(R.id.image1);
                photos[1] = (ImageView)findViewById(R.id.image2);
                photos[2] = (ImageView)findViewById(R.id.image3);
                photos[3] = (ImageView)findViewById(R.id.image4);
                photos[4] = (ImageView)findViewById(R.id.image5);
                photos[5] = (ImageView)findViewById(R.id.image6);
                photos[6] = (ImageView)findViewById(R.id.image7);
                photos[7] = (ImageView)findViewById(R.id.image8);
                photos[8] = (ImageView)findViewById(R.id.image9);
                photos[9] = (ImageView)findViewById(R.id.image10);

        kingdom = ((Spinner)findViewById(R.id.spinner_edit_kingdom));
        String[] kingdoms = new String[0];
        kingdoms = locsList.getKingdoms().toArray(kingdoms);
        setSpinner(kingdom, kingdoms, "Королевство");
        String advertKingdom = kingdomVal;
        selectedKingdomIndex = 0;
        if (advertKingdom != null && !"".equals(advertKingdom)){
            int kingdomsSize = kingdoms.length;
            for (int i = 0; i < kingdomsSize; i++){
                if (kingdoms[i].equals(advertKingdom)){
                    selectedKingdomIndex = i; break;
                }
            }
        }
        kingdom.setSelection(selectedKingdomIndex);
        kingdom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedKingdomIndex = position;
                Pair<long[], String[]> idsAndNamesC = locsList.getCitiesIdsAndNames((String) kingdom.getSelectedItem());

                locsIds = idsAndNamesC.first;
                String cities[] = idsAndNamesC.second;

                setSpinner(city, cities, city.getPrompt().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Pair<long[], String[]> idsAndNamesC = locsList.getCitiesIdsAndNames((String) kingdom.getSelectedItem());

        locsIds = idsAndNamesC.first;
        String cities[] = idsAndNamesC.second;

        selectedCityIndex = 0;
        String advertCity = cityVal;
        if (advertCity != null && !"".equals(advertCity)){
            int citiesSize = cities.length;
            for (int i = 0; i < citiesSize; i++){
                if (cities[i].equals(advertCity)){
                    selectedCityIndex = i; break;
                }
            }
        }
        locId = locsIds[selectedCityIndex];
        city = ((Spinner)findViewById(R.id.spinner_edit_city));
        setSpinner(city, cities, "Город");
        city.setSelection(selectedCityIndex);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locId = locsIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        title = (TextView)findViewById(R.id.text_title_advert);
        if (headerVal != null) {
            title.setText(headerVal);
        }

        description = (TextView)findViewById(R.id.text_description);
        if (description != null) {
            description.setText(descriptionVal);
        }

        cost = (TextView)findViewById(R.id.edit_advert_cost);
        cost.setText(String.valueOf(bountyVal));


        final String oldTitleVal = title.getText().toString();
        final String oldDescriptionValue = description.getText().toString();
        final int oldCostVal = bountyVal;
        final long oldLockId = locId;

        setImage(R.id.get_photos);

        Button buttonPublic = (Button)findViewById(R.id.button_public);
        buttonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Публикация объявления
                String newTitleVal = title.getText().toString();
                String newDescriptionVal = description.getText().toString();
                int newCostVal = Integer.parseInt(cost.getText().toString());
                long newLocId = locId;

                if (getIntent().getBooleanExtra("isCreate", false)){
                      createRequest.createAdvert(newTitleVal, newDescriptionVal, newCostVal, locId, null, new onCreateAdvert(EditAdvertActivity.this));
                } else {
                    //TODO: add and delete photos
                    updateRequest.updateAdvert( advertId,
                                ! newTitleVal.equals(oldTitleVal) ? newTitleVal : null,
                                ! newDescriptionVal.equals(oldDescriptionValue) ? newDescriptionVal : null,
                                oldLockId != newLocId ? newLocId : null,
                                oldCostVal != newCostVal ? newCostVal : null,
                                new LinkedList<Bitmap>(),
                                new LinkedList<Bitmap>(),
                                new onUpdateAdvert(EditAdvertActivity.this)
                    );
                }


            }
        });
    }

    private void setImage(final int imageId) {
        ((Button)findViewById(imageId)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ШОТО ПРОИСХОДИТ ПРИ НАЖАТИИ НА КНОПКУ ФОТОЧКИ

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });
    }

    private void goBack(){
        Intent intent = new Intent();
        intent.putExtra("header",       title.getText().toString());
        intent.putExtra("description",  description.getText().toString());
        intent.putExtra("bounty",       cost.getText().toString());
        intent.putExtra("kingdom",      (String)kingdom.getSelectedItem());
        intent.putExtra("city",         (String) city.getSelectedItem());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setSpinner(Spinner spinner, String[] data, String title) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt(title);
    }
}
