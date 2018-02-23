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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;

import ServerExchange.Advert;
import ServerExchange.ServerRequests.GetAdvertsRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;

public class AdvertListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SortingFragment.OnFragmentInteractionListener {

    private static final int NEW_ADVERT = 2222;

    GetAdvertsRequest getAdvertsRequest = new GetAdvertsRequest();

    class onGetAverts extends DefaultServerAnswerHandler<LinkedList<Advert>> {

        public onGetAverts(Context context) {
            super(context);
        }

        @Override
        public void handle( LinkedList<Advert> answ) {

            for (Advert advert : answ){
                //TODO: Возможны баги с id long  в int
                setNewAdvert((int)advert.getId(), advert.getName(), advert.getInfo(), advert.getKingdom(), advert.getCity(), String.valueOf(advert.getReward()));
            }
        }
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

        setButton((Button)findViewById(R.id.button_witcher_not_chosen), findViewById(R.id.form_witcher_not_chosen));
        setButton((Button)findViewById(R.id.button_witcher_chosen), findViewById(R.id.form_witcher_chosen));
        setButton((Button)findViewById(R.id.button_during), findViewById(R.id.adlist_during));
        setButton((Button)findViewById(R.id.button_executed), findViewById(R.id.adlist_executed));


        getAdvertsRequest.getSortedBy(GetAdvertsRequest.SortType.BY_ALPHABET, new onGetAverts(AdvertListActivity.this));
    }

    private void setNewAdvert(int id, String title, String description, String kingdom, String city, String cost) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        AdvertFragment newRow = new AdvertFragment(title, description, kingdom, city, cost);
        ft.add(id, newRow);
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
                data.getStringExtra("title");
                setNewAdvert(R.id.adlist_witcher_not_chosen,
                        data.getStringExtra("title"),
                        data.getStringExtra("description"),
                        data.getStringExtra("kingdom"),
                        data.getStringExtra("city"),
                        data.getStringExtra("cost"));

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

    }
}
