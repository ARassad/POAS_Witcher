package prin366_2018.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import ServerExchange.Advert;
import ServerExchange.Comment;
import ServerExchange.Location;
import ServerExchange.Profile;
import ServerExchange.ProfilePart;
import ServerExchange.ServerRequests.AddCommentContractRequest;
import ServerExchange.ServerRequests.GetAdvertRequest;
import ServerExchange.ServerRequests.GetAdvertsRequest;
import ServerExchange.ServerRequests.GetCommentsRequest;
import ServerExchange.ServerRequests.GetWitcherDesiredContractRequest;
import ServerExchange.ServerRequests.LoginRequest;
import ServerExchange.ServerRequests.RefuseContractRequest;
import ServerExchange.ServerRequests.RemoveAdvertRequest;
import ServerExchange.ServerRequests.SelectExecutorRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;


import static prin366_2018.client.ProfileActivity.decodeBase64;

public class AdvertActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    static final private int SAVE_DATA = 2;

    private GetAdvertRequest getAdvertRequest = new GetAdvertRequest();
    private class onGetAdvert extends DefaultServerAnswerHandler<Advert>{

        public onGetAdvert(Context context) {
            super(context);
        }

        @Override
        public void handle(Advert answ) {
            advert = answ;

            if (answ.getAuthorName() != null) {
                authorNameView.setText(answ.getAuthorName());
            }
            if (answ.getInfo()!= null){
                descriptionView.setText(answ.getInfo());
            }
            if (answ.getName() != null){
                headerView.setText(answ.getName());
            }
            if (answ.getReward() != null) {
                rewardView.setText(String.valueOf(answ.getReward()));
            }

            if (answ.getLocation() != null) {
                locationView.setText(answ.getLocation().toString());
            }
            if (answ.getExecutorId() != null) {
                executorNameView.setText(answ.getExecutorName());
            }
            if (answ.getAuthorId() == LoginRequest.getLoggedUserId()){
                getDesiredRequest.getDesired(answ.getId(), new onGetDesiredList(AdvertActivity.this));
                ifCreatedByLoggedUser();
            }
        }
    }

    private GetCommentsRequest getCommentsRequest = new GetCommentsRequest();
    private class onGetComments extends DefaultServerAnswerHandler<LinkedList<Comment>>{

        public onGetComments(Context context) {
            super(context);
        }

        @Override
        public void handle(LinkedList<Comment> answ) {

            Collections.sort(answ, Comment.DateComparator);
            for (Comment comment : answ){
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy '-' HH:mm");
                setNewComment(comment.getAuthorAvatar(),comment.getText(), formatForDateNow.format(comment.getDateOfCreate()));
            }
        }
    }

    private AddCommentContractRequest addCommentsRequest = new AddCommentContractRequest();
    private class onAddComment extends DefaultServerAnswerHandler<Boolean>{

        public onAddComment(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {

        }
    }

    private GetWitcherDesiredContractRequest getDesiredRequest = new GetWitcherDesiredContractRequest();
    private class onGetDesiredList extends DefaultServerAnswerHandler<ArrayList<ProfilePart>> {

        public onGetDesiredList(Context context) {
            super(context);
        }

        @Override
        public void handle(ArrayList<ProfilePart> answ) {
            for (ProfilePart profile : answ){
                //TODO: Заполнять список
            }
        }
    }

    //TODO: upend
    private SelectExecutorRequest selectExecutorRequest = new SelectExecutorRequest();
    private class onSelectExecutorAnswer extends DefaultServerAnswerHandler<Boolean>{

        public onSelectExecutorAnswer(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {

        }
    }

    private RemoveAdvertRequest removeAdvertRequest = new RemoveAdvertRequest();
    private class onRemoveAdvertAnswer extends DefaultServerAnswerHandler<Boolean>{

        public onRemoveAdvertAnswer(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {
            finish();
        }
    }

    //TODO: upend
    private RefuseContractRequest refuseContractRequest = new RefuseContractRequest();
    private class onRefuseAnswer extends DefaultServerAnswerHandler<Boolean>{

        public onRefuseAnswer(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {

        }
    }


    private TextView headerView, descriptionView, authorNameView, rewardView, locationView, executorNameView;
    private TextView newCommentView;
    private void connectViews(){
        headerView          = findViewById(R.id.text_title_advert);
        descriptionView     = findViewById(R.id.text_description);
        authorNameView      = findViewById(R.id.text_name_client);
        rewardView          = findViewById(R.id.text_cost_advert);
        locationView        = findViewById(R.id.text_kingdom_city_advert);
        executorNameView    = findViewById(R.id.text_executor);
        newCommentView      = findViewById(R.id.input_comment);
    }

    private Typeface typeface;

    private void ifCreatedByLoggedUser(){
        Button buttonEdit = (Button)findViewById(R.id.button_edit);
        buttonEdit.setTypeface(typeface);
        buttonEdit.setText("\uf044");
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvertActivity.this, EditAdvertActivity.class);
                intent.putExtra("isCreate", false);
                intent.putExtra("kingdom", advert.getLocation().getKingdom());
                intent.putExtra("city", advert.getLocation().getCity());
                intent.putExtra("bounty", advert.getReward());
                intent.putExtra("header", advert.getName());
                intent.putExtra("description", advert.getInfo());
                intent.putExtra("advertId", advert.getId());
                startActivityForResult(intent, SAVE_DATA);
            }
        });

        Button buttonDelete = (Button)findViewById(R.id.button_delete);
        buttonDelete.setTypeface(typeface);
        buttonDelete.setText("\uf2ed");
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: show message box
                removeAdvertRequest.removeAdvert(advertId, new onRemoveAdvertAnswer(AdvertActivity.this));
            }
        });
    }


    private long advertId = -1;
    private Advert advert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

        connectViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AdvertActivity.this);

        setButton((Button)findViewById(R.id.button_description), (TextView)findViewById(R.id.text_description));
        setButton((Button)findViewById(R.id.button_images), findViewById(R.id.images_advert));
        setButton((Button)findViewById(R.id.button_comments), findViewById(R.id.comments_list));

        typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        Button buttonSendComment = (Button)findViewById(R.id.imagebutton_send_comment);
        buttonSendComment.setTypeface(typeface);
        buttonSendComment.setText("\uf1d8");

        buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = ((TextView)findViewById(R.id.input_comment)).getText().toString();

                if (text.isEmpty() == false ) {
                    boolean stringContainsSymbol = false;
                    for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) != ' ' && text.charAt(i) != '\n') {
                            stringContainsSymbol = true;
                            break;
                        }
                    }

                    if (stringContainsSymbol == true){

                        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy '-' HH:mm");
                        setNewComment(Bitmap.createBitmap(120, 160, Bitmap.Config.ARGB_8888),
                                text,
                                formatForDateNow.format(new Date()));

                        addCommentsRequest.addCommentContract(text, advertId, new onAddComment(AdvertActivity.this));

                    }

                }

                ((TextView)findViewById(R.id.input_comment)).setText("");


            }
        });


        advertId = getIntent().getLongExtra("advertId",-1);
        if (advertId >= 0){
            getAdvertRequest.getAdvert(advertId, new onGetAdvert(AdvertActivity.this));
            getCommentsRequest.getAdvertComments(advertId, new onGetComments(AdvertActivity.this));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVE_DATA) {
            if (resultCode == RESULT_OK) {
                //TODO
                Intent intent = getIntent();
                String h = intent.getStringExtra("header");
                if (h != null) headerView.setText(h);
                String d = intent.getStringExtra("description");
                if (d!=null) descriptionView.setText(d);
                Integer b = intent.getIntExtra("bounty", 1);
                if (b!= null) rewardView.setText(String.valueOf(b));
                String k = intent.getStringExtra("kingdom");
                String c = intent.getStringExtra("city");
                if (k!= null && c!= null)
                locationView.setText( new Location(k,c).toString());
            }
        }
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

    private void setNewResponder(String witcher, String icon) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ResponderFragment respond = new ResponderFragment(witcher, icon);
        ft.add(R.id.list_responders, respond);
        ft.commit();
    }

    private void setNewComment(Bitmap photo, String text, String datetime) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        CommentFragment comment = new CommentFragment(photo, text, datetime);
        ft.add(R.id.comments_list, comment);
        ft.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_advert) {
            Intent intent = new Intent(AdvertActivity.this, AdvertListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            Intent intent = new Intent(AdvertActivity.this, ProfileActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
