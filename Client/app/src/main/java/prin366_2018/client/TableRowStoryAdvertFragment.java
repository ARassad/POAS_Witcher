package prin366_2018.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private long advert_id;

    public TableRowStoryAdvertFragment() {
        date = "01.01.1000";
        title = "Без названия";
        status = "Завершен";
    }

    @SuppressLint("ValidFragment")
    public TableRowStoryAdvertFragment(String _date, String _title, String _status, long advert_id) {
        date = _date;
        title = _title;
        status = _status;
        this.advert_id = advert_id;
    }

    Button buttonHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_row_story_advert, container, false);
        ((TextView)view.findViewById(R.id.tablerow_date)).setText(date);
        buttonHeader = view.findViewById(R.id.btn_title);
        buttonHeader.setText(title);
        buttonHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onHeaderClick(advert_id);
            }
        });
        ((TextView)view.findViewById(R.id.tablerow_status)).setText(status);
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
        void onHeaderClick(long advert_id);
    }
}
