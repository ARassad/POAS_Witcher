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
import android.widget.LinearLayout;
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

    private String[] kingdoms;
    private String[] cities;


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
    private LinearLayout rewardSettingsGroup, locationSettingsGroup;

    void setListeners(){
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



        spinnerKingdom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    //Видишь костыли? Я тоже, исправлятьна свой страх и риск 5-часовой отладки

                    //Выбранное королевство
                    kingdomFilter = kingdoms[position];
                    //"Массив" с записью "Любой"
                    ArrayList<String> notChosenVal = new ArrayList<String>(0);
                    notChosenVal.add("Любой");

                    //Получаем города, соответсвующие королевству из списка
                    String cities_[] = new String[0];
                    cities_ =   locationsList.getCitiesStr(kingdomFilter).toArray(cities_);
                    //Добавляем "Любой" в массив городов
                    Collections.addAll(notChosenVal, cities_);
                    cities_ = notChosenVal.toArray(cities_);
                    //Изменяем венешний список, чтоб потом определить, какой именно город выбрали
                    cities = cities_;
                    //обновляем спинер городов
                    setSpinner(spinnerCity, cities, "Города");
                /*

                cities = notChosenVal.toArray(cities);
                //setSpinner((Spinner)view.findViewById(R.id.spinner_city), cities, "Города");

                */
                } catch(Exception e){
                    String stopme;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kingdomFilter = null;
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    cityFilter = cities[position];
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


        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override


            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( position >= 1 && position<=2) {
                    filterType = GetAdvertsRequest.FilterType.fromInt(position-1);
                }

                switch(position) {
                    case 0:
                        locationSettingsGroup.setVisibility(View.GONE);
                        rewardSettingsGroup.setVisibility(View.GONE);
                        filterType = null;
                        kingdomFilter = null;
                        cityFilter = null;
                        minBountyFilter = null;
                        maxBountyFilter = null;
                        break;
                    case 1:
                        locationSettingsGroup.setVisibility(View.GONE);
                        rewardSettingsGroup.setVisibility(View.VISIBLE);
                        kingdomFilter = null;
                        cityFilter = null;
                        //setSpinner((Spinner)view.findViewById(R.id.spinner_kingdom), kingdoms, "Королевства");
                        break;
                    case 2:
                        locationSettingsGroup.setVisibility(View.VISIBLE);
                        rewardSettingsGroup.setVisibility(View.GONE);
                        minBountyFilter = null;
                        maxBountyFilter = null;



                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterType = null;
            }
        });
    }

    void initElements(View view){

        rewardSettingsGroup = view.findViewById(R.id.rewards);
        spinnerSort         = view.findViewById(R.id.spinner_sort_main);
        spinnerOrder        = view.findViewById(R.id.spinner_sort_other);
        spinnerFilter       = view.findViewById(R.id.spinner_filter);
        spinnerKingdom      = view.findViewById(R.id.spinner_kingdom);
        spinnerCity         = view.findViewById(R.id.spinner_city);
        minBountyEdit       = view.findViewById(R.id.edit_startcost);
        maxBountyEdit       = view.findViewById(R.id.edit_endcost);


        locationSettingsGroup = view.findViewById(R.id.locations);



    }

    void getDataFromViews(){

    }

    LocationsList locationsList;

    public void enable(){
        buttonSearch.setEnabled(true);
    }

    public void disable(){
        buttonSearch.setEnabled(false);
    }

    int fragmentId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        locationsList = LocationsList.getInstance();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sorting, container, false);

        fragmentId = this.getId();

        buttonSearch        = view.findViewById(R.id.button_find);
        //String[] sort1 = { "Награде", "Названию", "Локации", "Дате" };
        String[] sort1 = { "Названию", "Локации", "Дате" };

        String[] sort2 = { "Возрастанию", "Убыванию" };
        final String[] filter = { "Не задано", "Награде", "Локации" };

        kingdoms = new String[0];
        kingdoms = locationsList.getKingdoms().toArray(kingdoms);

        String cities[] = new String[0];
        cities =   locationsList.getCitiesStr(kingdoms[0]).toArray(cities);
        ArrayList<String> notChosenVal = new ArrayList<>();
        notChosenVal.add("Любой");
        Collections.addAll(notChosenVal, cities);
        cities = notChosenVal.toArray(cities);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disable();
                mListener.onFragmentInteraction(fragmentId,sortType, orderType, filterType, kingdomFilter, cityFilter, minBountyFilter, maxBountyFilter);
            }
        });

        initElements(view);


        setButton((Button)view.findViewById(R.id.button_sort), view.findViewById(R.id.form_sort));

        setSpinner((Spinner)view.findViewById(R.id.spinner_sort_main), sort1, "Сортировать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_sort_other), sort2, "Сортировать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_filter), filter, "Фильтровать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_kingdom), kingdoms, "Королевства");
        setSpinner((Spinner)view.findViewById(R.id.spinner_city), cities, "Города");

        setListeners();

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
