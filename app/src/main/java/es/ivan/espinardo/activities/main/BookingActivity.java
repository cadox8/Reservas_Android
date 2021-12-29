package es.ivan.espinardo.activities.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import java.util.Arrays;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.InstallationActivity;
import es.ivan.espinardo.activities.adapter.BookingsAdapter;
import es.ivan.espinardo.activities.adapter.InstallationsAdapter;
import es.ivan.espinardo.activities.error.ErrorActivity;
import es.ivan.espinardo.api.bookings.Booking;
import es.ivan.espinardo.api.bookings.Bookings;
import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.providers.BookingProvider;
import es.ivan.espinardo.providers.InstallationsProvider;
import es.ivan.espinardo.utils.DataUtils;
import es.ivan.espinardo.utils.Navigation;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_booking);

        final Navigation navigation = new Navigation(this);
        navigation.changePage(R.id.reservas);

        final SessionManager sessionManager = new SessionManager(this);

        if (!sessionManager.canAccess()) {
            this.finish();
            return;
        }

        final BookingProvider bookingProvider = new BookingProvider();
        final Bookings bookings = bookingProvider.getAllBookings(DataUtils.getUser().getToken());

        if (bookings == null) {
            this.startActivity(new Intent(this, ErrorActivity.with("Server error").getClass()));
            this.finish();
            return;
        }

        final Chip chip = this.findViewById(R.id.booking_tag);

        if (bookings.getError() != null || !bookings.getError().trim().isEmpty() || bookings.getBookings().length == 0) {
            chip.setTextColor(Color.BLACK);
            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.RED));
            chip.setText("No hay reservas");
            chip.setChipIcon(this.getDrawable(R.drawable.ic_error));
            chip.setChipIconTint(ColorStateList.valueOf(Color.BLACK));
            chip.setVisibility(View.VISIBLE);
            return;
        }

        chip.setVisibility(View.GONE);

        final BookingsAdapter adapter = new BookingsAdapter(this, bookings.getBookings());
        final ListView listView = this.findViewById(R.id.list_installations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
/*            this.startActivity(new Intent(this, InstallationActivity.from(bookings[position]).getClass()));
            this.finish();*/
        });
    }
}
