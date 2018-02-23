package prin366_2018.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import ServerExchange.AdvertCard;
import ServerExchange.Comment;
import ServerExchange.ImageConvert;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.AddCommentProfileRequest;
import ServerExchange.ServerRequests.GetCommentsRequest;
import ServerExchange.ServerRequests.GetProfileRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;
import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Dryush on 18.02.2018.
 * Edit by Alexander on 18.02.2018
 */

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TableRowStoryAdvertFragment.OnFragmentInteractionListener {


    TextView name;
    TextView aboutMe;
    TextView role;
    ImageView image;

    static final private int RESULT_CANCEL = 0;
    static final private int RESULT_OK = 1;
    static final private int SAVE_DATA = 2;

    class onGetProfile extends DefaultServerAnswerHandler<Profile> {

        public onGetProfile(Context context) {
            super(context);
        }

        @Override
        public void handle(Profile answ) {

            name.setText(answ.getName() != null ? answ.getName() : "NoName");

            aboutMe.setText( answ.getInfo() != null ? answ.getInfo() : "Nobody");
            role.setText( answ.getType() == Profile.ProfileType.WITCHER ? "Ведьмак" : "Наниматель");
            if (answ.getImage() != null) {
                image.setImageBitmap(answ.getImage());
            }

            ArrayList<AdvertCard> history = answ.getHistory();
            for (AdvertCard historyElem : history){
                setTableRow(historyElem.getLastStatusUpdate().toString(), historyElem.getAdvertHeader(), historyElem.getStatus().toRuString());
            }
        }
    }


    GetProfileRequest profileRequest = new GetProfileRequest();


   class onAddComment extends DefaultServerAnswerHandler<Boolean>{

       public onAddComment(Context context) {
           super(context);
       }

       @Override
       public void handle(Boolean answ) {


       }
   }

    AddCommentProfileRequest addComment = new AddCommentProfileRequest();

   class onGetComments extends DefaultServerAnswerHandler<LinkedList<Comment>>{

       public onGetComments(Context context) {
           super(context);
       }

       @Override
       public void handle(LinkedList<Comment> answ) {

           for (Comment comment : answ){
               SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy '-' hh:mm");
               setNewComment(comment.getAuthorAvatar(),comment.getText(), formatForDateNow.format(comment.getDateOfCreate()));
           }
       }
   }

   GetCommentsRequest getCommentsRequest = new GetCommentsRequest();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.text_name);
        aboutMe = (TextView) findViewById(R.id.text_about);
        role = (TextView) findViewById(R.id.text_role);
        image = (ImageView) findViewById(R.id.image);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ProfileActivity.this);

        setButton((Button)findViewById(R.id.button_about), (TextView)findViewById(R.id.text_about));
        setButton((Button)findViewById(R.id.button_advert_story), (TableLayout)findViewById(R.id.table_advert_story));
        setButton((Button)findViewById(R.id.button_comments), findViewById(R.id.comments_list));

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        final Button buttonEdit = (Button)findViewById(R.id.button_edit);
        buttonEdit.setTypeface(typeface);
        buttonEdit.setText("\uf044");
        buttonEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);


                intent.putExtra("name", name.getText().toString());
                intent.putExtra("aboutMe", aboutMe.getText().toString());
                startActivityForResult(intent, SAVE_DATA);
            }
        });

        //Вот здесь я пишу код (Андрей)
        //profileRequest.getProfile(9, new onGetProfile(ProfileActivity.this));
        profileRequest.getLoggedProfile(new onGetProfile(ProfileActivity.this));
        getCommentsRequest.getLoggedProfile(new onGetComments(ProfileActivity.this));

        Button buttonSendComment = (Button)findViewById(R.id.imagebutton_send_comment);
        buttonSendComment.setTypeface(typeface);
        buttonSendComment.setText("\uf1d8");
        buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((TextView)findViewById(R.id.input_comment)).getText().toString();

                if (text.isEmpty() == false ){
                    boolean stringContainsSymbol = false;
                    for (int i=0; i<text.length(); i++){
                        if (text.charAt(i) != ' ' && text.charAt(i) !='\n'){
                            stringContainsSymbol = true;
                            break;
                        }
                    }

                    if (stringContainsSymbol == true){
                        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy '-' hh:mm");
                        setNewComment(Bitmap.createBitmap(120, 160, Bitmap.Config.ARGB_8888),
                                text,
                                formatForDateNow.format(new Date()));

                        addComment.getLoggedProfile(text, new onAddComment(ProfileActivity.this));
                    }

                }

                ((TextView)findViewById(R.id.input_comment)).setText("");

            }
        });


        //Метод установки новой строки в таблицу
        //setTableRow("18.03.2018", "Название", "Завершено");
        //Метод установки нового комментария
        setNewComment(Bitmap.createBitmap(120, 160, Bitmap.Config.ARGB_8888), "Комментарий", "01.01.2001 - 19:00");



    }

    private void setNewComment(Bitmap photo, String text, String datetime) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        CommentFragment comment = new CommentFragment(photo, text, datetime);
        ft.add(R.id.comments_list, comment);
        ft.commit();
    }

    private void setTableRow(String date, String title, String status) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        TableRowStoryAdvertFragment newRow = new TableRowStoryAdvertFragment(date, title, status);
        ft.add(R.id.table_advert_story, newRow);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVE_DATA) {
            if (resultCode == RESULT_OK) {

                name.setText(data.getStringExtra("name"));
                //Сохранить информацию о имени в БД
                aboutMe.setText(data.getStringExtra("aboutMe"));
                //Сохранить информацию "о себе" в БД
                Bitmap bitmap = ImageConvert.fromBase64Str(data.getStringExtra("photo"));
                if (bitmap != null)
                    image.setImageBitmap(bitmap);
                //Сохранить фоточку в БД
            }
        }
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_advert) {
            Intent intent = new Intent(ProfileActivity.this, AdvertListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
