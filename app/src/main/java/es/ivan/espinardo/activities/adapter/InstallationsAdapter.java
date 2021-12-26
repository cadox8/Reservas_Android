package es.ivan.espinardo.activities.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.ivan.espinardo.R;
import es.ivan.espinardo.api.installations.Installation;

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

        final TextView titleText = rowView.findViewById(R.id.title);
        final ImageView imageView = rowView.findViewById(R.id.icon);
        final TextView subtitleText = rowView.findViewById(R.id.subtitle);

        titleText.setText(installations[position].getName());
        //imageView.setImageResource(this.context.g);

        // Fetch image
        final int resourceImg = this.context.getResources().getIdentifier(installations[position].getThumbnail(), "drawable", "es.ivan.espinardo");
        imageView.setImageDrawable(this.context.getDrawable(resourceImg));

        subtitleText.setText(installations[position].getInstallationType().capitalize() + " - " + installations[position].getPrice() + "€");

        return rowView;
    }
}
