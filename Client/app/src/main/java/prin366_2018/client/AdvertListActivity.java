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

import java.util.LinkedList;

import ServerExchange.Advert;
import ServerExchange.Profile;
import ServerExchange.ServerRequests.GetAdvertsRequest;
import ServerExchange.ServerRequests.LoginRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;

public class AdvertListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SortingFragment.OnFragmentInteractionListener {

    public enum GroupAdvert {
        ALL_ADVERT,//все объявления
        WITCHER_NOT_CHOSEN, //ведьмак не выбран
        WITCHER_CHOSEN, //ведьмак выбран
        DURING, //Исполняются
        SUBSRIBED, //Подписаны
        EXECUTED //Исполнены
    };
    private static final int NEW_ADVERT = 2222;

    GetAdvertsRequest getAdvertsRequest = new GetAdvertsRequest();

    class onGetAverts extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAverts(Context context) {
            super(context);
        }

        private GroupAdvert group = GroupAdvert.ALL_ADVERT;
        public onGetAverts(Context cont, GroupAdvert group){
            super(cont);
            this.group = group;
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert(this.group, advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()), advert.getId());
            }
        }
    }

    void updateList(GroupAdvert gr){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        ((TextView)findViewById(R.id.window_title)).setText("Объявл..");
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

        //advertListSetting(false, false);

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


        getAdvertsRequest.getFreeSortedBy(GetAdvertsRequest.SortType.BY_ALPHABET, new onGetAverts(AdvertListActivity.this));
    }

    private void setNewAdvert(GroupAdvert id, String title, String description, String kingdom, String city, String cost, long advertId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        AdvertFragment newRow = new AdvertFragment(title, description, kingdom, city, cost, advertId);
        int idxml = R.id.all_advert;
        switch (id) {
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
    public void onFragmentInteraction(Uri uri) {
        String stop = "debug";
    }
}
