package prin366_2018.client;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertFragment extends Fragment {

    private String title, description, kingdom, city, cost;

    public AdvertFragment() {}

    @SuppressLint("ValidFragment")
    public AdvertFragment(String title, String description, String kingdom, String city, String cost) {
        this.title = title;
        this.description = description;
        this.kingdom = kingdom;
        this.city = city;
        this.cost = cost;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advert, container, false);

        view.findViewById(R.id.fr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdvertActivity.class);
                startActivity(intent);
            }
        });
        ((TextView)view.findViewById(R.id.text_advert_title)).setText(title);
        ((TextView)view.findViewById(R.id.text_advert_info)).setText(description);
        String c = cost + " оренов";
        ((TextView)view.findViewById(R.id.text_cost)).setText(c);
        String k_c = kingdom + ", " + city;
        ((TextView)view.findViewById(R.id.text_kingdom)).setText(k_c);

        return view;
    }
}
