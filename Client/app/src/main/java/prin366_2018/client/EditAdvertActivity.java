package prin366_2018.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeSet;

import ServerExchange.ImageConvert;
import ServerExchange.LocationsList;
import ServerExchange.ServerRequests.CreateAdvertRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;
import ServerExchange.ServerRequests.UpdateAdvertRequest;
import ServerExchange.ServerRequests.UpdateProfileRequest;

import static ServerExchange.ImageConvert.toBase64Str;

public class EditAdvertActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 2;
    private Boolean isPhotoChanged = false;

    private TextView title, description, cost;
    private Spinner kingdom, city;

    private int selectedKingdomIndex;
    private int selectedCityIndex;
    private long[] locsIds;
    private long locId;

    private ImageButton[] photos = new ImageButton[10];
    private UpdateAdvertRequest updateRequest = new UpdateAdvertRequest();
    private CreateAdvertRequest createRequest = new CreateAdvertRequest();

    private LinkedList<Bitmap> bitmaps = new LinkedList<Bitmap>();

    private LinkedList<Bitmap> bitmapsToDel = new LinkedList<>();

    private final int maxPhotosCount = 10;
    private int[] idPhotos = new int[maxPhotosCount];
    private boolean isThisPhotoChanged[] = new boolean[maxPhotosCount];

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        //ImageButton imageView = (ImageButton) findViewById(R.id.image);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST: {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(EditAdvertActivity.this);
                    isPhotoChanged = true;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ClipData selectedImage = imageReturnedIntent.getClipData();

                        try {
                            int maxPossible =  maxPhotosCount - bitmaps.size();
                            if (selectedImage.getItemCount() > maxPossible){
                                dlg.setTitle("Многовато будет").setMessage("К сожалению, можно выбрать только до 10 изображений").create().show();
                            }
                            for (int i = 0; i < selectedImage.getItemCount() && i < maxPossible; ++i) {
                                Uri uri = selectedImage.getItemAt(i).getUri();
                                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                int maxSize = Math.max( bm.getWidth(), bm.getHeight());
                                double mashtab = ((double)maxSize) / 500.0;
                                if (mashtab > 1){
                                    bm = Bitmap.createScaledBitmap(bm, (int)(bm.getWidth()/mashtab), (int)(bm.getHeight()/mashtab),false);
                                }
                                bitmaps.addLast(bm);
                                isThisPhotoChanged[bitmaps.size() - 1] = true;
                                //Bitmap bm = Bitmap.createScaledBitmap(bitmaps.getLast(), 100, 100, false);
                                photos[bitmaps.size()-1 ].setImageBitmap(bitmaps.getLast());
                                photos[bitmaps.size()-1 ].setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {

                            dlg.setTitle("Упс! У вас ошибочка!");
                            dlg.setMessage(e.getMessage());
                            dlg.create().show();
                        }
                    }
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
        for (int i = 0; i < maxPhotosCount; i++) {
            byte[] bs = intent.getByteArrayExtra("photo"+String.valueOf(i));
            if (bs != null) {
                bitmaps.add(BitmapFactory.decodeByteArray(bs, 0, bs.length));
                isThisPhotoChanged[i] = false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advert);

        final LocationsList locsList = LocationsList.getInstance();
        //{"Не указано"};

        getFromBeforeActivity();

        for (int i = 0; i < isThisPhotoChanged.length; i++){
            isThisPhotoChanged[i] = false;
        }
        idPhotos[0] = (R.id.image1);
        idPhotos[1] = (R.id.image2);
        idPhotos[2] = (R.id.image3);
        idPhotos[3] = (R.id.image4);
        idPhotos[4] = (R.id.image5);
        idPhotos[5] = (R.id.image6);
        idPhotos[6] = (R.id.image7);
        idPhotos[7] = (R.id.image8);
        idPhotos[8] = (R.id.image9);
        idPhotos[9] = (R.id.image10);

        for (int i = 0; i < idPhotos.length; i++){
            photos[i] = findViewById(idPhotos[i]);
            photos[i].setVisibility(View.GONE);
        }

        for (int i = 0; i < bitmaps.size(); i++){
            photos[i].setImageBitmap(bitmaps.get(i));
            photos[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < photos.length; i++){

            photos[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                final View view_ = view;
                AlertDialog.Builder dlg = new AlertDialog.Builder(EditAdvertActivity.this);
                dlg.setTitle("Подтверждение");
                dlg.setMessage("Удалить?");
                dlg.setCancelable(true);
                dlg.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int iDontKnowWhatIsIt) {
                        int order = 0;
                        int myId = view_.getId();
                        for ( order = 0; order < idPhotos.length; order++) {
                            if (myId == idPhotos[order]){
                                break;
                            }
                        }

                        if (!isThisPhotoChanged[order]) {
                            bitmapsToDel.addLast(bitmaps.get(order));
                        }
                        bitmaps.remove(order);
                        //bitmaps.addLast();
                        for ( int i = order; i < bitmaps.size(); i++){
                            photos[i].setImageBitmap(bitmaps.get(i));
                        }
                        isThisPhotoChanged[bitmaps.size()] = false;
                        photos[bitmaps.size()].setVisibility(View.GONE);
                        photos[bitmaps.size()].setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));


                    }
                });
                dlg.create().show();
                return true;
                }
            });
        }

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

        ((Button)findViewById(R.id.get_photos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ШОТО ПРОИСХОДИТ ПРИ НАЖАТИИ НА КНОПКУ ФОТОЧКИ

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_PICK);

                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });


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
                      createRequest.createAdvert(newTitleVal, newDescriptionVal, newCostVal, locId, bitmaps, new onCreateAdvert(EditAdvertActivity.this));
                } else {
                    //TODO: add and delete photos
                    LinkedList<Bitmap> addBitmaps = new LinkedList<>();
                    for (int i = 0; i < bitmaps.size(); i++){
                        if (isThisPhotoChanged[i]) {
                            addBitmaps.addLast(bitmaps.get(i));
                        }
                    }
                    updateRequest.updateAdvert( advertId,
                                ! newTitleVal.equals(oldTitleVal) ? newTitleVal : null,
                                ! newDescriptionVal.equals(oldDescriptionValue) ? newDescriptionVal : null,
                                oldLockId != newLocId ? newLocId : null,
                                oldCostVal != newCostVal ? newCostVal : null,
                                addBitmaps,
                                bitmapsToDel,
                                new onUpdateAdvert(EditAdvertActivity.this)
                    );
                }

            }
        });
    }



    private String[] BitmapsToBundle(LinkedList<Bitmap> b) {
        String[] stringBmps = new String[10];
        for (int i = 0; i < b.size(); ++i) {
            stringBmps[i] = ImageConvert.toBase64Str(b.get(i));
        }
        return stringBmps;
    }

    private void goBack(){
        Intent intent = new Intent();
        intent.putExtra("header",       title.getText().toString());
        intent.putExtra("description",  description.getText().toString());
        intent.putExtra("bounty",       cost.getText().toString());
        intent.putExtra("kingdom",      (String)kingdom.getSelectedItem());
        intent.putExtra("city",         (String) city.getSelectedItem());

        if (bitmaps != null) {
            intent.putExtra("bitmaps", BitmapsToBundle(bitmaps));
        }
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
