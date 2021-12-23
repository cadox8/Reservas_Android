package es.ivan.reservas.activities.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.ivan.reservas.R;

public class InstallationsAdapter extends ArrayAdapter<String> {

    private final Activity context;


    private final String[] title;
    private final String[] subtitle;
    private final int[] imgid;

    public InstallationsAdapter(Activity context, String[] title, String[] subtitle, int[] imgid) {
        super(context, R.layout.installations_list, title);


        this.context = context;
        this.title = title;
        this.subtitle = subtitle;
        this.imgid = imgid;

    }

    public View getView(int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.installations_list, null, true);

        final TextView titleText = (TextView) rowView.findViewById(R.id.title);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        final TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(title[position]);
        imageView.setImageResource(imgid[position]);
        subtitleText.setText(subtitle[position]);

        return rowView;
    }
}
