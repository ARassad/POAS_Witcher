package prin366_2018.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Dryush on 18.02.2018.
 * Edit by Alexander on 18.02.2018
 */

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final private int RESULT_CANCEL = 0;
    static final private int RESULT_OK = 1;
    static final private int SAVE_DATA = 2;

    TextView name;
    TextView aboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.text_name);
        aboutMe = (TextView) findViewById(R.id.text_about);


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
        setButton((Button)findViewById(R.id.button_advert_story), (TableLayout)findViewById(R.id.table_advert_story));

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        Button buttonEdit = (Button)findViewById(R.id.button_edit);
        buttonEdit.setTypeface(typeface);
        buttonEdit.setText("\uf044");

        Button buttonSendComment = (Button)findViewById(R.id.imagebutton_send_comment);
        buttonSendComment.setTypeface(typeface);
        buttonSendComment.setText("\uf1d8");



        buttonEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("aboutMe", aboutMe.getText().toString());
                startActivityForResult(intent, SAVE_DATA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVE_DATA) {
            if (resultCode == RESULT_OK) {
                TextView name = (TextView) findViewById(R.id.text_name);
                name.setText(data.getStringExtra("name"));
                //Сохранить информацию о имени в БД

                TextView aboutMe = (TextView) findViewById(R.id.text_about);
                aboutMe.setText(data.getStringExtra("aboutMe"));
                //Сохранить информацию "о себе" в БД
            }
        }
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
}
