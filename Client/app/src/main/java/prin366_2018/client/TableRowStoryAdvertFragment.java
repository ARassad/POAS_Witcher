package prin366_2018.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 *
 *
 *
 */
public class TableRowStoryAdvertFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String date;
    private String title;
    private String status;

    public TableRowStoryAdvertFragment() {
        date = "01.01.1000";
        title = "Без названия";
        status = "Завершен";
    }

    @SuppressLint("ValidFragment")
    public TableRowStoryAdvertFragment(String _date, String _title, String _status) {
        date = _date;
        title = _title;
        status = _status;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_row_story_advert, container, false);
        setTableRow(view, date, title, status);
        return view;
    }

    private void setTableRow(View view, String date, String title, String status) {
        ((TextView)view.findViewById(R.id.tablerow_date)).setText(date);
        ((TextView)view.findViewById(R.id.tablerow_title)).setText(title);
        ((TextView)view.findViewById(R.id.tablerow_status)).setText(status);
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
