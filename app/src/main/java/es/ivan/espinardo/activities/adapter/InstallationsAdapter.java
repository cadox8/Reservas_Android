package es.ivan.espinardo.activities.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.ivan.espinardo.R;
import es.ivan.espinardo.api.Installation;
import es.ivan.espinardo.api.Installations;

public class InstallationsAdapter extends ArrayAdapter<Installation> {

    private final Activity context;

    private final Installation[] installations;

    public InstallationsAdapter(Activity context, Installation[] installations) {
        super(context, R.layout.installations_list, installations);

        this.context = context;
        this.installations = installations;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.installations_list, null, true);

        final TextView titleText = (TextView) rowView.findViewById(R.id.title);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        final TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(installations[position].getName());
        imageView.setImageResource(R.drawable.no_image_background);
        //imageView.setImageResource(this.context.g);
        subtitleText.setText(installations[position].getInstallationType().capitalize() + " - " + installations[position].getPrice() + "€");

        return rowView;
    }
}
