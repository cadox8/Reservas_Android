package es.ivan.espinardo.activities.helpers;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.zxing.WriterException;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.main.BookingsActivity;
import es.ivan.espinardo.api.bookings.Booking;
import es.ivan.espinardo.providers.BookingProvider;

public class BookingActivity extends AppCompatActivity {

    private static Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_booking);

        ((TextView)this.findViewById(R.id.booking_name)).setText(booking.getInstallation().getName());
        ((TextView)this.findViewById(R.id.booking_date)).setText(new SimpleDateFormat("dd-MM-yyy").format(booking.getDate()) + ' ' + booking.getTimes());

        // --- QR ---

        final Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        final Point point = new Point();
        display.getSize(point);

        final int width = point.x;
        final int height = point.y;

        final QRGEncoder qr = new QRGEncoder(booking.getToken(), null, QRGContents.Type.TEXT, Math.min(width, height) * 3 / 4);
        try {
            ((ImageView) this.findViewById(R.id.booking_qr)).setImageBitmap(qr.encodeAsBitmap());
        } catch (WriterException e) {
            e.printStackTrace();
            DynamicToast.makeError(this, "No se ha podido generar el QR", Toast.LENGTH_SHORT).show();
        }
        // --- ---

        // --- Cancel booking ---
        this.findViewById(R.id.button_cancel_booking).setOnClickListener(view -> {
            new BookingProvider().removeBooking(booking.getToken());
            this.startActivity(new Intent(this, BookingsActivity.class));
            this.finish();
        });
        // --- ----

        // Chip moment!
        final Chip chip = this.findViewById(R.id.booking_tag);
        chip.setText(booking.getInstallation().getInstallationType().capitalize());
        chip.setTextColor(Color.BLACK);
        chip.setChipIconTint(ColorStateList.valueOf(Color.BLACK));

        switch (booking.getInstallation().getInstallationType()) {
            case VARIADO:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#673ab7")));
                chip.setChipIcon(this.getDrawable(R.drawable.i_general));
                break;
            case FUTBOL:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.GREEN));
                chip.setChipIcon(this.getDrawable(R.drawable.i_football));
                break;
            case BALONCESTO:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.YELLOW));
                chip.setChipIcon(this.getDrawable(R.drawable.i_basket));
                break;
            case TENIS:
            case PADEL:
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.CYAN));
                chip.setChipIcon(this.getDrawable(R.drawable.i_tennis));
                break;
        }
    }

    public static BookingActivity from(Booking booking) {
        BookingActivity.booking = booking;
        return new BookingActivity();
    }
}
