package prin366_2018.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import ServerExchange.Location;
import ServerExchange.LocationsList;
import ServerExchange.ServerRequests.GetAdvertsRequest;


/**
 *
 */
public class SortingFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private void setSpinner(Spinner spinner, String[] data, String title) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt(title);
    }

    private GetAdvertsRequest.SortType sortType;
    private GetAdvertsRequest.OrderType orderType;
    private GetAdvertsRequest.FilterType filterType;
    private String cityFilter;
    private String kingdomFilter;
    private Integer minBountyFilter;
    private Integer maxBountyFilter;

    String[] kingdoms = new String[0];
    String[] cities = new String[0];


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

    private View view;

    private Button buttonSearch;
    private Spinner spinnerSort, spinnerOrder, spinnerFilter, spinnerKingdom, spinnerCity;
    private EditText minBountyEdit, maxBountyEdit;

    void initElements(View view){

        buttonSearch        = view.findViewById(R.id.button_find);

        spinnerSort         = view.findViewById(R.id.spinner_sort_main);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortType = GetAdvertsRequest.SortType.fromInt( position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sortType = null;
            }
        });

        spinnerOrder        = view.findViewById(R.id.spinner_sort_other);
        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderType = GetAdvertsRequest.OrderType.fromInt(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                orderType = null;
            }
        });

        spinnerFilter       = view.findViewById(R.id.spinner_filter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( position <= 1) {
                    filterType = GetAdvertsRequest.FilterType.fromInt(position);
                }
                filterType = null;
                kingdomFilter = null;
                cityFilter = null;
                minBountyFilter = null;
                maxBountyFilter = null;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterType = null;
            }
        });

        spinnerKingdom      = view.findViewById(R.id.spinner_kingdom);
        spinnerKingdom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kingdomFilter = kingdoms[position];

                cities =   locationsList.getCitiesStr(kingdoms[0]).toArray(cities);
                ArrayList<String> notChosenVal = new ArrayList<String>(1);
                notChosenVal.add("Любой");
                Collections.addAll(notChosenVal, cities);
                cities = notChosenVal.toArray(cities);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kingdomFilter = null;
            }
        });

        spinnerCity         = view.findViewById(R.id.spinner_city);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cityFilter = cities[position-1];
                }
                else{
                    cityFilter = null;
                }
            };

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cityFilter = null;
            }
        });

        minBountyEdit       = view.findViewById(R.id.edit_startcost);
        minBountyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                minBountyFilter = s != null && !"".equals(s.toString()) ? Integer.parseInt( s.toString()) : null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        maxBountyEdit       = view.findViewById(R.id.edit_endcost);
        maxBountyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                maxBountyFilter = s != null && !"".equals(s.toString()) ? Integer.parseInt( s.toString()) : null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void getDataFromViews(){

    }

    LocationsList locationsList = LocationsList.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sorting, container, false);

        initElements(view);
        //String[] sort1 = { "Награде", "Названию", "Локации", "Дате" };
        String[] sort1 = { "Названию", "Локации", "Дате" };

        String[] sort2 = { "Возрастанию", "Убыванию" };
        final String[] filter = { "Не задано", "Награде", "Локации" };

        kingdoms = locationsList.getKingdoms().toArray(kingdoms);


        cities =   locationsList.getCitiesStr(kingdoms[0]).toArray(cities);
        ArrayList<String> notChosenVal = new ArrayList<>();
        notChosenVal.add("Любой");
        Collections.addAll(notChosenVal, cities);
        cities = notChosenVal.toArray(cities);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(v.getId(),sortType, orderType, filterType, kingdomFilter, cityFilter, minBountyFilter, maxBountyFilter);
            }
        });

        setButton((Button)view.findViewById(R.id.button_sort), view.findViewById(R.id.form_sort));

        setSpinner((Spinner)view.findViewById(R.id.spinner_sort_main), sort1, "Сортировать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_sort_other), sort2, "Сортировать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_filter), filter, "Фильтровать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_kingdom), kingdoms, "Королевства");
        setSpinner((Spinner)view.findViewById(R.id.spinner_city), cities, "Города");



        ((Spinner)view.findViewById(R.id.spinner_filter)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                switch(i) {
                    case 0:
                        view.findViewById(R.id.locations).setVisibility(View.GONE);
                        view.findViewById(R.id.rewards).setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        view.findViewById(R.id.locations).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.rewards).setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(int fragmentId,
                                   GetAdvertsRequest.SortType sortType,
                                   GetAdvertsRequest.OrderType orderType,
                                   GetAdvertsRequest.FilterType filterType,
                                   String kingdomFilter, String cityFilter,
                                   Integer minReward, Integer maxReward);
    }

}
