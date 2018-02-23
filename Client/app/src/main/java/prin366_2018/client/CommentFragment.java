package prin366_2018.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 *
 */
public class CommentFragment extends Fragment {

    private Bitmap bitmap;
    private String text;
    private String dateTime;

    public CommentFragment() {

    }

    @SuppressLint("ValidFragment")
    public CommentFragment(Bitmap b, String t, String dt) {
        bitmap = b;
        text = t;
        dateTime = dt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ((ImageView)view.findViewById(R.id.image_comment)).setImageBitmap(bitmap);
        ((TextView)view.findViewById(R.id.text_comment)).setText(text);
        ((TextView)view.findViewById(R.id.text_date_time)).setText(dateTime);
        return view;
    }
}
