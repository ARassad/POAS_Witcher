package prin366_2018.client;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentContainer;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ServerExchange.Advert;
import ServerExchange.LocationsList;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.ExitProfileRequest;
import ServerExchange.ServerRequests.GetAdvertsRequest;
import ServerExchange.ServerRequests.LoginRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;

/*

    ОСТОРОЖНО!
    ПРОСМОТР ДАННОГО КОДА ЗАПРЕЩЁН ДЛЯ ЛИЦ НЕ ДОСТИГШИХ 18 ЛЕТ И БЕРЕМЕННЫМ ЖЕНЩИНАМ!
    ПРОДОЛЖАЯ ПРОСМОТР ДАННОГО КОДА ВЫ ПОДТВЕРЖДАЕТЕ СВОЮ ОСВЕДОМЛЁННОСТЬ О НАЛИЧИИ ШОК-КОНТЕНТА
    И ОТКАЗЫВАЕТЕСЬ ОТ ПРАВА ПОДАТЬ В СУД НА АВТОРА ЗА ПОЛУЧЕННЫЙ МОРАЛЬНЫЙ И ФИЗИЧЕСКИЙ ВРЕД
    ОТ ПОПЫТОК ДЕБАГА И РЕФАКТОРИНГА ДАННОГО КОДА

    данное соглашение также распространяется  на класс SortingFragment

 */

public class AdvertListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SortingFragment.OnFragmentInteractionListener {

    private int freeAdvertsSortingId = -1, witcherChosenSotringId=-1, witcherNotChosenSotringId=-1, completedSotringId=-1, subscribedSotringId=-1;

    @Override
    public void onFragmentInteraction(int fragmentId, GetAdvertsRequest.SortType sortType, GetAdvertsRequest.OrderType orderType, GetAdvertsRequest.FilterType filterType, String kingdomFilter, String cityFilter, Integer minReward, Integer maxReward) {

        GetAdvertsRequest getAdvertsRequest = new GetAdvertsRequest();

        if (fragmentId == freeAdvertsSortingId){
            GroupAdvert groupAdvert = GroupAdvert.ALL_ADVERT;
            spinners.get(GroupAdvert.ALL_ADVERT).show();
            Advert.AdvertStatus advertStatus = Advert.AdvertStatus.FREE;

            OnGetAdverts onGetAdverts = new OnGetAdverts(AdvertListActivity.this, groupAdvert, fragmentId);
            if (filterType == GetAdvertsRequest.FilterType.BY_LOCATE) {
                getAdvertsRequest.getFreeFilteredByLocation( filterType, sortType, orderType, kingdomFilter, cityFilter, onGetAdverts);
            } else if (filterType == GetAdvertsRequest.FilterType.BY_REWARD) {
                minReward = minReward == null ? 0 : minReward;
                maxReward = maxReward == null ? Integer.MAX_VALUE : maxReward;

                getAdvertsRequest.getFreeFilteredByReward( filterType, minReward, maxReward, sortType, orderType, onGetAdverts);
            } else {
                getAdvertsRequest.getFreeSortedBy( sortType, orderType, onGetAdverts);
            }
        }
        else if (LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER){
            GroupAdvert groupAdvert = GroupAdvert.ALL_ADVERT;
            Advert.AdvertStatus advertStatus = Advert.AdvertStatus.FREE;

            if (fragmentId==completedSotringId)        { groupAdvert = GroupAdvert.EXECUTED;   advertStatus = Advert.AdvertStatus.COMPLETED; }
            else if (fragmentId==subscribedSotringId)       { groupAdvert = GroupAdvert.SUBSRIBED;  advertStatus = Advert.AdvertStatus.FREE; }

            spinners.get(groupAdvert).show();

            OnGetAdverts onGetAdverts = new OnGetAdverts(AdvertListActivity.this, groupAdvert, fragmentId);
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

            if (fragmentId == witcherChosenSotringId)       { groupAdvert = GroupAdvert.WITCHER_CHOSEN;      advertStatus = Advert.AdvertStatus.ASSIGNED_WITCHER;}
            else if (fragmentId==witcherNotChosenSotringId) { groupAdvert = GroupAdvert.WITCHER_NOT_CHOSEN;  advertStatus = Advert.AdvertStatus.FREE;}
            else if (fragmentId==completedSotringId)        { groupAdvert = GroupAdvert.EXECUTED;            advertStatus = Advert.AdvertStatus.COMPLETED; }

            spinners.get(groupAdvert).show();
            OnGetAdverts onGetAdverts = new OnGetAdverts(AdvertListActivity.this, groupAdvert, fragmentId);

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
    };
    private static final int NEW_ADVERT = 2222;


    class OnGetAdverts extends DefaultServerAnswerHandler <ArrayList<Advert>> {

        public OnGetAdverts(Activity context) {
            super(context);
        }

        private GroupAdvert group;
        private Integer fragmentId = null;

        public OnGetAdverts(Activity context, GroupAdvert group) {
            super(context);
            this.group = group;
        }

        public OnGetAdverts(Activity context, GroupAdvert group, int fragmentId){
            this(context, group);
            this.fragmentId = fragmentId;
        }



        @Override
        public void handle( ArrayList<Advert> answ) {
            refillAdvertsList(group, answ);
            spinners.get(group).disable();
            if ( fragmentId != null) {
                ((SortingFragment) getSupportFragmentManager().findFragmentById(fragmentId)).enable();
            }
            //((SortingFragment)(Fragment)findViewById(this.fragmentId)).enable();
            /*
            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert(group, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
            }
            */
        }
    }


    Map<GroupAdvert, ProgressSpinner> spinners;
    void initSpinners(){
        int progressBarIds[]= {R.id.all_adverts_progress, R.id.witcher_not_chosen_progress, R.id.witcher_chosen_progress, R.id.during_advert_progress, R.id.witcher_subsribed_progress, R.id.executed_progress};
        int viewsIds[] =      {R.id.adlist_all_adverts,   R.id.adlist_witcher_not_chosen,   R.id.adlist_witcher_chosen,   R.id.adlist_during,          R.id.adlist_subsribed,           R.id.adlist_executed };
        GroupAdvert[] groups ={GroupAdvert.ALL_ADVERT,    GroupAdvert.WITCHER_NOT_CHOSEN,   GroupAdvert.WITCHER_CHOSEN,   GroupAdvert.DURING,          GroupAdvert.SUBSRIBED,           GroupAdvert.EXECUTED};
        spinners = new HashMap<>();
        for (int i = 0; i < groups.length; i++){
            spinners.put(groups[i], new ProgressSpinner(findViewById(viewsIds[i]), (ProgressBar) findViewById(progressBarIds[i])));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationsList.refillFromServer();
        setContentView(R.layout.activity_advert_list);

        initSpinners();

        ((TextView)findViewById(R.id.window_title)).setText("Объявления");
        ((TextView)findViewById(R.id.window_title)).setTextSize(14);


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



        setButton((Button)findViewById(R.id.button_witcher_not_chosen), findViewById(R.id.form_witcher_not_chosen));
        setButton((Button)findViewById(R.id.button_witcher_chosen), findViewById(R.id.form_witcher_chosen));
        setButton((Button)findViewById(R.id.button_during), findViewById(R.id.adlist_during));
        setButton((Button)findViewById(R.id.button_executed), findViewById(R.id.form_executed));
        setButton((Button)findViewById(R.id.button_subsribed), findViewById(R.id.form_subscribed));
        setButton((Button)findViewById(R.id.button_all_adverts), findViewById(R.id.form_all_advert));

        advertListSetting(LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER, false);

        ((Switch)findViewById(R.id.switch_advert)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужные группы объявлений
                advertListSetting(LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER, isChecked);
            }
        });

        advertListSetting(LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER, true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        freeAdvertsSortingId = R.id.free_adverts_sort;
        witcherChosenSotringId = R.id.witcher_chosen_sort;
        witcherNotChosenSotringId = R.id.witcher_not_chosen_adverts_sort;
        subscribedSotringId = R.id.witcher_subsribed_sort;
        completedSotringId = R.id.executed_sort;

        spinners.get(GroupAdvert.ALL_ADVERT).show();
        new GetAdvertsRequest().getFreeSortedBy(GetAdvertsRequest.SortType.BY_ALPHABET, new OnGetAdverts(AdvertListActivity.this, GroupAdvert.ALL_ADVERT, freeAdvertsSortingId));

        //GroupAdvert.init();

        if ( LoginRequest.getLoggedUserType() == Profile.ProfileType.WITCHER){
            OnGetAdverts onGetAdvertsFillDuring = new OnGetAdverts(AdvertListActivity.this, GroupAdvert.DURING);
            spinners.get(GroupAdvert.DURING).show();
            new GetAdvertsRequest().getWitcherSortedBy(Advert.AdvertStatus.IN_PROCESS, GetAdvertsRequest.SortType.BY_ALPHABET, onGetAdvertsFillDuring);
        }
    }

    private void refillAdvertsList(GroupAdvert groupAdvert,  List<Advert> adverts ){

        int idxml = R.id.adlist_all_adverts;
        switch (groupAdvert) {
            case WITCHER_NOT_CHOSEN:
                idxml = R.id.adlist_witcher_not_chosen;
                break;
            case WITCHER_CHOSEN:
                idxml = R.id.adlist_witcher_chosen;
                break;
            case DURING:
                idxml = R.id.adlist_during;
                break;
            case SUBSRIBED:
                idxml = R.id.adlist_subsribed;
                break;
            case EXECUTED:
                idxml = R.id.adlist_executed;
                break;
        }

        deleteAdverts(idxml);
        for( Advert advert: adverts){
            setNewAdvert(idxml, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
        }
    }

    private void setNewAdvert(int idXml, String title, String description, String kingdom, String city, String cost, long advertId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        AdvertFragment newRow = new AdvertFragment(title, description, kingdom, city, cost, advertId);


        ft.add(idXml, newRow);
        ft.commit();

    }

    private void deleteAdverts(int idXml) {
        LinearLayout ll = (LinearLayout)findViewById(idXml);
        int size = ll.getChildCount();
        View sort = ll.getChildAt(0);
        ((LinearLayout)ll).removeAllViews();

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
            findViewById(R.id.full_form_all_adverts).setVisibility(View.VISIBLE);
            findViewById(R.id.full_form_my_adverts).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.full_form_all_adverts).setVisibility(View.GONE);
            findViewById(R.id.full_form_my_adverts).setVisibility(View.VISIBLE);
            if (isWitcher) {
                findViewById(R.id.full_form_witcher_not_chosen).setVisibility(View.GONE);
                findViewById(R.id.full_form_witcher_chosen).setVisibility(View.GONE);
                findViewById(R.id.form_during).setVisibility(View.VISIBLE);
                findViewById(R.id.full_form_subsribed).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.full_form_witcher_not_chosen).setVisibility(View.VISIBLE);
                findViewById(R.id.full_form_witcher_chosen).setVisibility(View.VISIBLE);
                findViewById(R.id.form_during).setVisibility(View.GONE);
                findViewById(R.id.full_form_subsribed).setVisibility(View.GONE);
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(AdvertListActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        else if (id == R.id.nav_exit) {
            new ExitProfileRequest().exit(new DefaultServerAnswerHandler<Boolean>(AdvertListActivity.this) {
                @Override
                public void handle(Boolean answ) {
                }
            });

            FirebaseAuth.getInstance().signOut();
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
