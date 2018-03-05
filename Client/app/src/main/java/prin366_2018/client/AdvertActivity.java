package prin366_2018.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import ServerExchange.Advert;
import ServerExchange.Comment;
import ServerExchange.ImageConvert;
import ServerExchange.Location;
import ServerExchange.Profile;
import ServerExchange.ProfilePart;
import ServerExchange.ServerRequests.AddCommentContractRequest;
import ServerExchange.ServerRequests.AddWitcherInContractRequest;
import ServerExchange.ServerRequests.AnswerWitcherInContractRequest;
import ServerExchange.ServerRequests.ExitProfileRequest;
import ServerExchange.ServerRequests.GetAdvertRequest;
import ServerExchange.ServerRequests.GetCommentsRequest;
import ServerExchange.ServerRequests.GetWitcherDesiredContractRequest;
import ServerExchange.ServerRequests.LoginRequest;
import ServerExchange.ServerRequests.RefuseContractRequest;
import ServerExchange.ServerRequests.RemoveAdvertRequest;
import ServerExchange.ServerRequests.SelectExecutorRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;
import ServerExchange.ServerRequests.SetAdvertCompleteRequest;

public class AdvertActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , ResponderFragment.OnResponderClick
        {


    static final private int SAVE_DATA = 2;

    private GetAdvertRequest getAdvertRequest = new GetAdvertRequest();


    @Override
    public void onSetExecutor(final long witcher_id) {
        AlertDialog.Builder dlgb = new AlertDialog.Builder(AdvertActivity.this);
        dlgb.setTitle("Выбор Ведьмака исполнителем");
        dlgb.setCancelable(true);

        if (advert.getExecutorId() == null) {
            dlgb.setMessage("Вы уверены,что хотите назначить этого Ведьмака?");

            dlgb.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new SelectExecutorRequest().SelectWitcherInContract(witcher_id, advertId, new DefaultServerAnswerHandler<Boolean>(AdvertActivity.this) {
                        @Override
                        public void handle(Boolean answ) {
                            advert.setExecutorId(witcher_id);
                        }
                    });
                }
            });
            dlgb.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Игнорчииик)))
                }
            });
            dlgb.create().show();
        } else {
            dlgb.setMessage("На текущий момент исполнителем назначен другой Ведьмак");
            dlgb.create().show();
        }
    }

    @Override
    public void onGetDesiredWitcherProfile(long witcher_id) {
        Intent intent = new Intent(AdvertActivity.this, ProfileActivity.class);
        intent.putExtra("profileId", witcher_id);
        startActivity(intent);
    }


    private class onBooleanAnswer extends DefaultServerAnswerHandler<Boolean> {
        public onBooleanAnswer(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {

        }
    }


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

                if (answ.getExecutorId() == LoginRequest.getLoggedUserId()){
                    if (answ.getStatus() == Advert.AdvertStatus.ASSIGNED_WITCHER){
                        buttonAcceptAnsw.setVisibility(View.VISIBLE);
                        buttonDiscardAnsw.setVisibility(View.VISIBLE);
                    }
                    else if (answ.getStatus()== Advert.AdvertStatus.IN_PROCESS){
                        if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER) {
                            buttonComplete.setVisibility(View.VISIBLE);
                        }
                        buttonDiscard.setVisibility(View.VISIBLE);
                        if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER)
                            buttonDiscard.setText("Отказаться от дальнейшего выполнения заказа");
                        else
                            buttonDiscard.setText("Отказаться от услуг ведьмака");
                    }
                }

                if ( answ.getAuthorId() == LoginRequest.getLoggedUserId()){
                    if ( answ.getStatus() == Advert.AdvertStatus.IN_PROCESS || answ.getStatus() == Advert.AdvertStatus.ASSIGNED_WITCHER) {
                        buttonDiscard.setVisibility(View.VISIBLE);
                    }
                }
            }
            else {
                executorNameView.setText("не выбран");

                if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER) {
                    buttonRespond.setVisibility(View.VISIBLE);
                }
            }
            if (answ.getAuthorId() == LoginRequest.getLoggedUserId() ||
                    LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER){

                if (answ.getAuthorId() == LoginRequest.getLoggedUserId()) {
                    ifCreatedByLoggedUser();
                }

                signedSpinner.show();
                getDesiredRequest.getDesired(answ.getId(), new onGetDesiredList(AdvertActivity.this));
            }

            Bitmap authPhoto = answ.getAuthorPhoto();
            if (authPhoto != null){
                try {
                    //int btnHeight = btnProfileImage.getHeight();
                    //int btnWidth = btnProfileImage.getWidth();
                    //authPhoto = Bitmap.createScaledBitmap(authPhoto, btnWidth, btnHeight, false);
                }catch (Exception e){
                    int stopForDebug = 2;
                }
                btnProfileImage.setImageBitmap(authPhoto);
            }
            infoSpinner.disable();
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
            commentsSpinner.disable();
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

            if (LoginRequest.getLoggedUserType() == Profile.ProfileType.CUSTOMER){

                for (ProfilePart profile : answ){
                    setNewResponder(profile.getName(), profile.getId(), advert.getStatus());
                }
            }

            if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER){
                if (advert.getExecutorId() != LoginRequest.getLoggedUserId()) {

                    for (ProfilePart profile : answ) {
                        if (LoginRequest.getLoggedUserId() == profile.getId()) {
                            buttonRespond.setVisibility(View.VISIBLE);
                            buttonRespond.setEnabled(false);
                            buttonRespond.setText("Вы откликнулись");
                            break;
                        }
                    }
                }
            }
            signedSpinner.disable();

        }
    }

    //TODO: upend
    private SelectExecutorRequest selectExecutorRequest = new SelectExecutorRequest();

    private AddWitcherInContractRequest addWitcherInContractRequest = new AddWitcherInContractRequest();
    private class onAddWitcherInContractAnswer extends DefaultServerAnswerHandler<Boolean>{
        public onAddWitcherInContractAnswer(Context context) {super(context);}

        @Override
        public void handle (Boolean answ){

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
    private Button buttonRespond, buttonResponders, buttonAcceptAnsw, buttonDiscardAnsw, buttonDiscard, buttonComplete;
    private ImageButton btnProfileImage;
    private ImageView[] photos = new ImageView[10];

    private void connectViews(){
        headerView          = findViewById(R.id.text_title_advert);
        descriptionView     = findViewById(R.id.text_description);
        authorNameView      = findViewById(R.id.text_name_client);
        rewardView          = findViewById(R.id.text_cost_advert);
        locationView        = findViewById(R.id.text_kingdom_city_advert);
        executorNameView    = findViewById(R.id.text_executor);
        newCommentView      = findViewById(R.id.input_comment);

        buttonAcceptAnsw    = findViewById(R.id.button_accept_answ);
        buttonDiscardAnsw   = findViewById(R.id.button_discard_answ);
        buttonDiscard       = findViewById(R.id.button_discard);
        buttonComplete      = findViewById(R.id.button_set_completed);

        buttonRespond       = findViewById(R.id.button_respond);
        btnProfileImage = findViewById(R.id.btn_profile_image);

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
    }

    private void setOnClickButtons(){
        buttonAcceptAnsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AnswerWitcherInContractRequest().acceptToAdvert(advertId, new onBooleanAnswer( AdvertActivity.this));
                buttonAcceptAnsw.setEnabled(false);
                buttonAcceptAnsw.setText("Вы приняли заказ");
                buttonDiscardAnsw.setVisibility(View.GONE);
            }
        });

        buttonDiscardAnsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AnswerWitcherInContractRequest().discardAdvert(advertId, new onBooleanAnswer( AdvertActivity.this));
                buttonDiscardAnsw.setEnabled(false);
                buttonDiscardAnsw.setText("Вы отказались");
                buttonAcceptAnsw.setVisibility(View.GONE);
            }
        });

        buttonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RefuseContractRequest().RefuseContract(advertId, new onBooleanAnswer( AdvertActivity.this));
                buttonDiscard.setEnabled(false);
                if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER)
                    buttonDiscard.setText("Вы отказались от дальнейшего выполнения");
                else
                    buttonDiscard.setText("Вы отказались от услуг ведьмака");
                buttonComplete.setVisibility(View.GONE);
            }
        });

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SetAdvertCompleteRequest().setComplete(advertId, new onBooleanAnswer( AdvertActivity.this));
                buttonComplete.setEnabled(false);
                buttonComplete.setText("Вы сообщили о выполнении заказа");
                buttonDiscard.setVisibility(View.GONE);
            }
        });

        buttonRespond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWitcherInContractRequest.addWitcherInContract(advertId, new onAddWitcherInContractAnswer(AdvertActivity.this));
                buttonRespond.setEnabled(false);
                buttonRespond.setText("Вы откликнулись");
            }
        });
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

                byte[][] bytes = new byte[photos.length][];
                for (int i = 0; i < photos.length; ++i) {
                    Bitmap bmp = ((BitmapDrawable)photos[i].getDrawable()).getBitmap();
                    int size = bmp.getRowBytes() * bmp.getHeight();
                    ByteBuffer buf = ByteBuffer.allocate(size);
                    bmp.copyPixelsToBuffer(buf);
                    try {
                        buf.get(bytes[i], 0, size);
                    } catch (BufferUnderflowException e) {
                        // always happens
                    }
                }
                for (int i = 0; i < bytes.length; i++){
                    intent.putExtra("photo"+String.valueOf(i), bytes[i]);
                }
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
    private ProgressSpinner commentsSpinner, signedSpinner, infoSpinner;
    private void spinnersInit(){
        View commentsView = findViewById(R.id.comments_list);
        ProgressBar commentsProgressBar = findViewById(R.id.comments_progress);
        commentsSpinner = new ProgressSpinner(commentsView, commentsProgressBar);

        View signedListView = findViewById(R.id.list_responders);
        ProgressBar signedProgressBar = findViewById(R.id.responders_progress);
        signedSpinner = new ProgressSpinner(signedListView, signedProgressBar);

        View fullInfoView = findViewById(R.id.advert_info_form);
        ProgressBar infoProgressBar = findViewById(R.id.advert_info_progress);
        infoSpinner = new ProgressSpinner(fullInfoView, infoProgressBar);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

        connectViews();
        spinnersInit();
        setOnClickButtons();

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
        setButton((Button)findViewById(R.id.button_responders), findViewById((R.id.list_responders)));


        btnProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(AdvertActivity.this, ProfileActivity.class);
                toProfile.putExtra("profileId", advert.getAuthorId());
                startActivity(toProfile);
            }
        });

        typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        if (LoginRequest.getLoggedUserType()== Profile.ProfileType.WITCHER) {

        }

        buttonResponders = (Button)findViewById(R.id.button_responders);
        if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER){
            buttonResponders.setVisibility(View.GONE);
        }
        if (LoginRequest.getLoggedUserType() == Profile.ProfileType.CUSTOMER){
            buttonRespond.setVisibility(View.GONE);
        }


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
            infoSpinner.show();
            getAdvertRequest.getAdvert(advertId, new onGetAdvert(AdvertActivity.this));
            commentsSpinner.show();
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

                String[] bms = intent.getStringArrayExtra("bitmaps");
                if (bms != null) {
                    for (int i = 0; i < bms.length; ++i)
                        photos[i].setImageBitmap(ImageConvert.fromBase64Str(bms[i]));
                }
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

    private void setNewResponder(String witcher, long id, Advert.AdvertStatus status) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ResponderFragment respond = new ResponderFragment(witcher, id, status);
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
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(intent);
        }
        else if (id == R.id.nav_profile) {
            Intent intent = new Intent(AdvertActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        else if (id == R.id.nav_exit) {
            // TODO: Добавить пере Firebase запрос к серверу на выход из профиля
            // TODO: ExitProfile: token
            new ExitProfileRequest().exit(new DefaultServerAnswerHandler<Boolean>(AdvertActivity.this) {
                @Override
                public void handle(Boolean answ) {
                }
            });
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdvertActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
