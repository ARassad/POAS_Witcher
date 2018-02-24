package prin366_2018.client;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResponderFragment extends Fragment {

    private String respond, icon;

    public ResponderFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ResponderFragment(String respond, String icon) {
        this.respond = respond;
        this.icon = icon;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_responder, container, false);
        ((TextView)view.findViewById(R.id.text_responder)).setText(respond);
        ((TextView)view.findViewById(R.id.text_status_responder)).setText(icon);
        return view;
    }

}
