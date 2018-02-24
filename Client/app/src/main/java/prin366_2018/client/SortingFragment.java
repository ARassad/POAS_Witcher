package prin366_2018.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sorting, container, false);

        String[] sort1 = { "Награде", "Названию", "Локации", "Дате" };
        String[] sort2 = { "Возрастанию", "Убыванию" };
        String[] kingdoms = { "Не указано" };
        String[] cities = { "Не указано" };

        setButton((Button)view.findViewById(R.id.button_sort), view.findViewById(R.id.form_sort));

        setSpinner((Spinner)view.findViewById(R.id.spinner_sort_main), sort1, "Сортировать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_sort_other), sort2, "Сортировать по");
        setSpinner((Spinner)view.findViewById(R.id.spinner_kingdom), kingdoms, "Королевства");
        setSpinner((Spinner)view.findViewById(R.id.spinner_city), cities, "Города");

        ((Spinner)view.findViewById(R.id.spinner_sort_main)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                switch(i) {
                    case 2:
                        view.findViewById(R.id.locations).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.rewards).setVisibility(View.GONE);
                        break;
                    case 0:
                        view.findViewById(R.id.locations).setVisibility(View.GONE);
                        view.findViewById(R.id.rewards).setVisibility(View.VISIBLE);
                        break;
                    default:
                        view.findViewById(R.id.locations).setVisibility(View.GONE);
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

        void onFragmentInteraction(Uri uri);
    }
}
