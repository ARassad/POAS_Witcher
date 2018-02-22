package prin366_2018.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class EditAdvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advert);

        String[] kingdoms = {"Не указано"};
        String[] cities = {"Не указано"};
        setSpinner((Spinner)findViewById(R.id.spinner_edit_kingdom), kingdoms, "Королевство");
        setSpinner((Spinner)findViewById(R.id.spinner_edit_city), kingdoms, "Город");

        Button buttonPublic = (Button)findViewById(R.id.button_public);
        buttonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Публикация объявления
            }
        });

    }

    private void setSpinner(Spinner spinner, String[] data, String title) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt(title);
    }
}
