package prin366_2018.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class EditAdvertActivity extends AppCompatActivity {

    private TextView title, description, cost;
    private String kingdom, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advert);

        String[] kingdoms = {"Не указано"};
        String[] cities = {"Не указано"};
        setSpinner((Spinner)findViewById(R.id.spinner_edit_kingdom), kingdoms, "Королевство");
        setSpinner((Spinner)findViewById(R.id.spinner_edit_city), cities, "Город");

        title = (TextView)findViewById(R.id.text_title_advert);
        description = (TextView)findViewById(R.id.text_description);
        cost = (TextView)findViewById(R.id.edit_advert_cost);
        kingdom = ((Spinner)findViewById(R.id.spinner_edit_kingdom)).getSelectedItem().toString();
        city = ((Spinner)findViewById(R.id.spinner_edit_city)).getSelectedItem().toString();

        Button buttonPublic = (Button)findViewById(R.id.button_public);
        buttonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Публикация объявления
                Intent intent = new Intent();
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("cost", cost.getText().toString());
                intent.putExtra("kingdom", kingdom);
                intent.putExtra("city", city);
                setResult(RESULT_OK, intent);
                finish();
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
