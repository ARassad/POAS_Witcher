package prin366_2018.client;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import ServerExchange.ServerRequests.CreateAdvertRequest;
import ServerExchange.ServerRequests.GetAdvertRequest;
import ServerExchange.ServerRequests.GetAdvertsRequest;
import ServerExchange.ServerRequests.IServerAnswerHandler;
import ServerExchange.ServerRequests.UpdateProfileRequest;

/**
 * Created by Dryush on 18.02.2018.
 * Edit by Alexander on 18.02.2018
 */

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        setButton((Button)findViewById(R.id.button_about), (TextView)findViewById(R.id.text_about));
        setButton((Button)findViewById(R.id.button_advert_story), (TableLayout)findViewById(R.id.table_advert_story));
        setButton((Button)findViewById(R.id.button_advert_story), (TableLayout)findViewById(R.id.table_advert_story));

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-solid-900.ttf");

        Button buttonMenu = (Button)findViewById(R.id.button_menu);
        buttonMenu.setTypeface(typeface);
        buttonMenu.setText("\uf0c9");

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
                startActivity(intent);
            }
        });


        new UpdateProfileRequest().updateProfile("Test Name", "Test Text", null,
                new Debaug());
        //new CreateAdvertRequest().createAdvert("Test Advert Name", "Test Text");
        new GetAdvertsRequest().getSortedBy(GetAdvertsRequest.SortType.BY_ALPHABET, new Debaug());

    }

    class Debaug implements IServerAnswerHandler{

        @Override
        public void handle(Object answ) {
            Object letslookresult = answ;
        }
    };
    @Override
    protected void onPostResume() {
        super.onPostResume();
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
