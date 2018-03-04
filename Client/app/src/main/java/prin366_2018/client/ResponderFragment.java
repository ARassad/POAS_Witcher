package prin366_2018.client;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ServerExchange.Advert;
import ServerExchange.ServerRequests.SelectExecutorRequest;

import static android.graphics.Typeface.createFromAsset;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResponderFragment extends Fragment {

    private String respond, icon;
    private long id_witcher;
    private Advert.AdvertStatus advertStatus;

    public ResponderFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ResponderFragment(String respond, long id_witcher, Advert.AdvertStatus advertStatus){
        this.id_witcher = id_witcher;
        this.respond = respond;
        this.advertStatus = advertStatus;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_responder, container, false);
        ((TextView)view.findViewById(R.id.button_get_responder)).setText(respond);
        ((TextView)view.findViewById(R.id.id_respond)).setText(String.valueOf(id_witcher));

        Button setExecutorButton = view.findViewById(R.id.button_set_executor);
        if (Advert.AdvertStatus.FREE != this.advertStatus){
            setExecutorButton.setEnabled(false);
        }
        setExecutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSetExecutor(id_witcher);
            }
        });


        ;
        Typeface tf = createFromAsset(getActivity().getAssets(), "fonts/fa-solid-900.ttf");
        setExecutorButton.setTypeface(tf);
        setExecutorButton.setText("\uf234");

        Button getProfile = view.findViewById(R.id.button_get_responder);
        getProfile.setText(respond);

        getProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onGetDesiredWitcherProfile(id_witcher);
            }
        });



        return view;
    }




    public interface OnResponderClick{
        public void onSetExecutor(long witcher_id);
        public void onGetDesiredWitcherProfile(long witcher_id);
    }

    private OnResponderClick mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResponderClick) {
            mListener = (OnResponderClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
