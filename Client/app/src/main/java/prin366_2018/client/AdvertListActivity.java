package prin366_2018.client;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ServerExchange.Advert;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.GetAdvertsRequest;
import ServerExchange.ServerRequests.LoginRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;


public class AdvertListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SortingFragment.OnFragmentInteractionListener {

    private int freeAdvertsSortingId = -1, witcherChosenSotringId=-1, witcherNotChosenSotringId=-1, completedSotringId=-1, subscribedSotringId=-1;

    @Override
    public void onFragmentInteraction(int fragmentId, GetAdvertsRequest.SortType sortType, GetAdvertsRequest.OrderType orderType, GetAdvertsRequest.FilterType filterType, String kingdomFilter, String cityFilter, Integer minReward, Integer maxReward) {
        if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER){
            GroupAdvert groupAdvert = GroupAdvert.ALL_ADVERT;
            Advert.AdvertStatus advertStatus = Advert.AdvertStatus.FREE;
            if (fragmentId == freeAdvertsSortingId)         { groupAdvert = GroupAdvert.ALL_ADVERT; advertStatus = Advert.AdvertStatus.FREE;}
            else if (fragmentId==completedSotringId)        { groupAdvert = GroupAdvert.EXECUTED;   advertStatus = Advert.AdvertStatus.COMPLETED; }
            else if (fragmentId==subscribedSotringId)       { groupAdvert = GroupAdvert.SUBSRIBED;  advertStatus = Advert.AdvertStatus.FREE; }


            OnGetAdverts onGetAdverts = new OnGetAdverts(AdvertListActivity.this, groupAdvert);
            if (filterType == GetAdvertsRequest.FilterType.BY_LOCATE) {
                getAdvertsRequest.getWitcherFilteredByLocation(advertStatus, filterType, sortType, orderType, kingdomFilter, cityFilter, onGetAdverts);
            } else if (filterType == GetAdvertsRequest.FilterType.BY_REWARD) {
                minReward = minReward == null ? 0 : minReward;
                maxReward = maxReward == null ? Integer.MAX_VALUE : maxReward;

                getAdvertsRequest.getWitcherFilteredByReward(advertStatus, filterType, minReward, maxReward, sortType, orderType, onGetAdverts);
            } else {
                getAdvertsRequest.getWitcherSortedBy(advertStatus, sortType, orderType, onGetAdverts);
            }
        }
        else {
            GroupAdvert groupAdvert = GroupAdvert.ALL_ADVERT;
            Advert.AdvertStatus advertStatus = Advert.AdvertStatus.FREE;
            if (fragmentId == freeAdvertsSortingId)            { groupAdvert = GroupAdvert.ALL_ADVERT;          advertStatus = Advert.AdvertStatus.FREE;}
            else if (fragmentId == witcherChosenSotringId)     { groupAdvert = GroupAdvert.WITCHER_CHOSEN;      advertStatus = Advert.AdvertStatus.ASSIGNED_WITCHER;}
            else if (fragmentId==witcherNotChosenSotringId)    { groupAdvert = GroupAdvert.WITCHER_NOT_CHOSEN;  advertStatus = Advert.AdvertStatus.FREE;}


            OnGetAdverts onGetAdverts = new OnGetAdverts(AdvertListActivity.this, groupAdvert);

            if (filterType == GetAdvertsRequest.FilterType.BY_LOCATE) {
                getAdvertsRequest.getClientFilteredByLocation(advertStatus, filterType, sortType, orderType, kingdomFilter, cityFilter, onGetAdverts);
            } else if (filterType == GetAdvertsRequest.FilterType.BY_REWARD) {
                minReward = minReward == null ? 0 : minReward;
                maxReward = maxReward == null ? Integer.MAX_VALUE : maxReward;

                getAdvertsRequest.getClientFilteredByReward(advertStatus, filterType, minReward, maxReward, sortType, orderType, onGetAdverts);
            }
            else{
                getAdvertsRequest.getClientSortedBy(advertStatus, sortType, orderType, onGetAdverts);
            }

        }
    }


    public enum GroupAdvert {
        ALL_ADVERT,//все объявления
        WITCHER_NOT_CHOSEN, //ведьмак не выбран
        WITCHER_CHOSEN, //ведьмак выбран
        DURING, //Исполняются
        SUBSRIBED, //Подписаны
        EXECUTED; //Исполнены

        private static Integer all, chosen, notchosen, dur, subsr, execut;
        public static void init(){
            all = R.id.all_advert;
            chosen = R.id.adlist_witcher_chosen;
            notchosen = R.id.adlist_witcher_not_chosen;
            dur = R.id.adlist_during;
            subsr = R.id.adlist_subsribed;
            execut = R.id.adlist_executed;
        }
        public int toIdXml(){
            switch (this) {
                case ALL_ADVERT:
                    return all;
                case WITCHER_NOT_CHOSEN:
                    return notchosen;
                case WITCHER_CHOSEN:
                    return chosen;
                case DURING:
                    return  dur;
                case SUBSRIBED:
                    return subsr;
                case EXECUTED:
                    return execut;
                default: throw new RuntimeException("Добавил в GroupAdvert новое значение? Добавь и в метод toIdXml");
            }
        }


    };
    private static final int NEW_ADVERT = 2222;

    GetAdvertsRequest getAdvertsRequest = new GetAdvertsRequest();

    class OnGetAdverts extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public OnGetAdverts(Context context) {
            super(context);
        }

        private GroupAdvert group;
        public OnGetAdverts(Context context, GroupAdvert group){
            super(context);
            this.group = group;
        }

        @Override
        public void handle( LinkedList<Advert> answ) {
            refillAdvertsList(group, answ);
            /*
            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert(group, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
            }
            */
        }
    }


    /*
    class onGetAdvertsFillFree extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAdvertsFillFree(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert(GroupAdvert.ALL_ADVERT, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
            }
        }
    }

    class onGetAdvertsFillWitcherChosen extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAdvertsFillWitcherChosen(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert(GroupAdvert.WITCHER_CHOSEN, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
            }
        }
    }

    class onGetAdvertsFillWitcherNotChosen extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAdvertsFillWitcherNotChosen(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert(GroupAdvert.WITCHER_NOT_CHOSEN, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
            }
        }
    }

    class onGetAdvertsFillDuring extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAdvertsFillDuring(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert(GroupAdvert.DURING, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
            }
        }
    }

    class onGetAdvertsFillInDuringForWitcher extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAdvertsFillInDuringForWitcher(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                if (advert.getStatus() == Advert.AdvertStatus.IN_PROCESS) {
                    setNewAdvert(GroupAdvert.DURING, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
                }
            }
        }
    }

    class onGetAdvertsFillingCompleted extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAdvertsFillingCompleted(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                if (advert.getStatus() == Advert.AdvertStatus.IN_PROCESS) {
                    setNewAdvert(GroupAdvert.EXECUTED, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
                }
            }
        }
    }

    class onGetAdvertsFillingDesired extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAdvertsFillingDesired(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                if (advert.getStatus() == Advert.AdvertStatus.IN_PROCESS) {
                    setNewAdvert(GroupAdvert.SUBSRIBED, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
                }
            }
        }
    }
*/
    void updateList(GroupAdvert gr){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        ((TextView)findViewById(R.id.window_title)).setText("Объявл..");
        ((TextView)findViewById(R.id.window_title)).setTextSize(14);

        GroupAdvert.init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AdvertListActivity.this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        //Кнопка добавить объявление
        Button buttonAddAdvert = (Button)findViewById(R.id.button_add_advert);
        buttonAddAdvert.setTypeface(typeface);
        buttonAddAdvert.setText("\uf055");
        buttonAddAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdvertListActivity.this, EditAdvertActivity.class);
                intent.putExtra("isCreate", true);
                startActivityForResult(intent, NEW_ADVERT);
            }
        });
        if(LoginRequest.getLoggedUserType()== Profile.ProfileType.WITCHER)
        {
            buttonAddAdvert.setVisibility(View.GONE);
        }

        advertListSetting(LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER, true);

        setButton((Button)findViewById(R.id.button_witcher_not_chosen), findViewById(R.id.form_witcher_not_chosen));
        setButton((Button)findViewById(R.id.button_witcher_chosen), findViewById(R.id.form_witcher_chosen));
        setButton((Button)findViewById(R.id.button_during), findViewById(R.id.adlist_during));
        setButton((Button)findViewById(R.id.button_executed), findViewById(R.id.adlist_executed));


        ((Switch)findViewById(R.id.switch_advert)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужные группы объявлений
                advertListSetting(LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER, !isChecked);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        freeAdvertsSortingId = R.id.free_adverts_sort;
        witcherChosenSotringId = R.id.witcher_chosen_sort;
        witcherNotChosenSotringId = R.id.witcher_not_chosen_adverts_sort;
        subscribedSotringId = witcherNotChosenSotringId;
        completedSotringId = witcherChosenSotringId;
        SortingFragment freeAdvertsSorting  = (SortingFragment) fragmentManager.findFragmentById(R.id.free_adverts_sort);
        SortingFragment inProcessAdvertsSorting = (SortingFragment) fragmentManager.findFragmentById(R.id.witcher_not_chosen_adverts_sort);
        SortingFragment completedAdvertsSorting = (SortingFragment) fragmentManager.findFragmentById(R.id.witcher_chosen_sort);
        SortingFragment witcherNotChoosen = (SortingFragment) fragmentManager.findFragmentById(R.id.witcher_not_chosen_adverts_sort);
        SortingFragment witcherChoosen = (SortingFragment) fragmentManager.findFragmentById(R.id.witcher_chosen_sort);

        getAdvertsRequest.getFreeSortedBy(GetAdvertsRequest.SortType.BY_ALPHABET, new OnGetAdverts(AdvertListActivity.this, GroupAdvert.ALL_ADVERT));

        if ( LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER){
            OnGetAdverts onGetAdvertsFillDuring = new OnGetAdverts(AdvertListActivity.this, GroupAdvert.DURING);
            getAdvertsRequest.getWitcherSortedBy(Advert.AdvertStatus.IN_PROCESS, GetAdvertsRequest.SortType.BY_ALPHABET, onGetAdvertsFillDuring);
        }
    }

    private void refillAdvertsList(GroupAdvert groupAdvert,  List<Advert> adverts ){

        ((LinearLayout)findViewById(groupAdvert.toIdXml())).removeAllViews();
        for( Advert advert: adverts){
            setNewAdvert(groupAdvert, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
        }
    }
    private void setNewAdvert(GroupAdvert id, String title, String description, String kingdom, String city, String cost, long advertId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        AdvertFragment newRow = new AdvertFragment(title, description, kingdom, city, cost, advertId);
        int idxml = id.toIdXml();

        ft.add(idxml, newRow);
        ft.commit();

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((TextView)findViewById(R.id.window_title)).setText("Объявления");
            ((TextView)findViewById(R.id.window_title)).setTextSize(24);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ((TextView)findViewById(R.id.window_title)).setText("Объявл..");
            ((TextView)findViewById(R.id.window_title)).setTextSize(14);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ADVERT) {
            if (resultCode == RESULT_OK) {
                //Лучше обновим с сервера, а то не понятно, в какое место пихать
                /*
                setNewAdvert(GroupAdvert.WITCHER_NOT_CHOSEN,
                        data.getStringExtra("title"),
                        data.getStringExtra("description"),
                        data.getStringExtra("kingdom"),
                        data.getStringExtra("city"),
                        data.getStringExtra("cost"),
                        data.getLongExtra("advertId", -1)
                );
                */
            }
        }
    }



    /**
     * Отображение определенных групп объявлений в зависимости от типа пользователя и списка объявлений
     * isWitcher - true: ведьмак, false: заказчик
     * isAllAdvert - true: все объявления, false: свои
     */
    private void advertListSetting(boolean isWitcher, boolean isAllAdvert) {
        if (isAllAdvert) {
            findViewById(R.id.all_advert).setVisibility(View.VISIBLE);
            findViewById(R.id.full_form).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.all_advert).setVisibility(View.GONE);
            findViewById(R.id.full_form).setVisibility(View.VISIBLE);
            if (isWitcher) {
                findViewById(R.id.full_form_witcher_not_chosen).setVisibility(View.GONE);
                findViewById(R.id.full_form_witcher_chosen).setVisibility(View.GONE);
                findViewById(R.id.form_during).setVisibility(View.VISIBLE);
                findViewById(R.id.form_subscribed).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.full_form_witcher_not_chosen).setVisibility(View.VISIBLE);
                findViewById(R.id.full_form_witcher_chosen).setVisibility(View.VISIBLE);
                findViewById(R.id.form_during).setVisibility(View.GONE);
                findViewById(R.id.form_subscribed).setVisibility(View.GONE);
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(AdvertListActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_exit) {
            Intent intent = new Intent(AdvertListActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

}
