package es.ivan.espinardo.activities.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.ivan.espinardo.R;
import es.ivan.espinardo.api.bookings.Booking;
import es.ivan.espinardo.api.bookings.Bookings;
import es.ivan.espinardo.api.installations.Installation;

public class BookingsAdapter extends ArrayAdapter<Booking> {

    private final Activity context;

    private final Booking[] bookings;

    public BookingsAdapter(Activity context, Booking[] bookings) {
        super(context, R.layout.installations_list, bookings);

        this.context = context;
        this.bookings = bookings;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.installations_list, null, true);

        final TextView titleText = rowView.findViewById(R.id.title);
        final ImageView imageView = rowView.findViewById(R.id.icon);
        final TextView subtitleText = rowView.findViewById(R.id.subtitle);

        titleText.setText(bookings[position].getInstallation().getName());

        // Fetch image
        final int resourceImg = this.context.getResources().getIdentifier(bookings[position].getInstallation().getThumbnail(), "drawable", "es.ivan.espinardo");
        imageView.setImageDrawable(this.context.getDrawable(resourceImg));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookings[position].getDate());
        //calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(bookings[position].getTimes().split(":")[0]));

        subtitleText.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(calendar.getTime()));

        return rowView;
    }
}
