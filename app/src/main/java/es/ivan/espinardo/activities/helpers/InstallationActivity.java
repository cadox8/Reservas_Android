package es.ivan.espinardo.activities.helpers;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.error.ErrorActivity;
import es.ivan.espinardo.api.AbstractAPI;
import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.api.payment.PaymentIntent;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.notification.NotificationPublisher;
import es.ivan.espinardo.notification.NotificationUtils;
import es.ivan.espinardo.providers.BookingProvider;
import es.ivan.espinardo.providers.PaymentProvider;
import es.ivan.espinardo.utils.DataUtils;
import es.ivan.espinardo.utils.Utils;

public class InstallationActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private static Installation installation;

    private PaymentSheet paymentSheet;

    private CharSequence[] availableTimes;
    private String time = null;

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, 0); // smooth transitions
        this.setContentView(R.layout.activity_installation);

        final SessionManager sessionManager = new SessionManager(this);
        final BookingProvider bookingProvider = new BookingProvider();

        if (!sessionManager.canAccess()) {
            this.finish();
            return;
        }

        final SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // --- Data ---
        this.availableTimes = bookingProvider.getAvailableTimes(installation.getId(), new Date());
        // --- ---

        // --- Payment ---
        final MaterialButton button = this.findViewById(R.id.button_booking);
        this.updateTextButton(button);

        // Card: 4000 0072 4000 0007

        button.setOnClickListener((view) -> {
            if (this.time == null || this.time.trim().isEmpty()) {
                DynamicToast.makeError(this, "Selecciona las horas para la reserva", Toast.LENGTH_SHORT).show();
                return;
            }

            final PaymentProvider paymentProvider = new PaymentProvider();
            final PaymentIntent paymentIntent = paymentProvider.fetchPaymentIntent(installation.getPrice());

            if (paymentIntent == null) {
                this.startActivity(new Intent(this, ErrorActivity.with("Server error").getClass()));
                this.finish();
                return;
            }

            if (paymentIntent.getError() != null && !paymentIntent.getError().trim().isEmpty()) {
                DynamicToast.makeError(this, paymentIntent.getError(), Toast.LENGTH_SHORT).show();
                button.setEnabled(false);
                return;
            }

            final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("Espinardo", null,
                    new PaymentSheet.GooglePayConfiguration(PaymentSheet.GooglePayConfiguration.Environment.Test, "ES"),
                    ColorStateList.valueOf(Color.parseColor("#673ab7")), new PaymentSheet.BillingDetails());

            this.paymentSheet.presentWithPaymentIntent(paymentIntent.getClientSecret(), configuration);
        });

        // --- Populate page ---
        ((TextView) this.findViewById(R.id.installation_name)).setText(installation.getName());

        final MaterialButton times = this.findViewById(R.id.button_time);
        times.setOnClickListener(view -> {
            if (this.availableTimes.length == 0) {
                DynamicToast.makeError(this, "No hay horas disponibles para el día seleccionado", Toast.LENGTH_SHORT).show();
                return;
            }

            final MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this).setTitle(this.getResources().getString(R.string.select_time));
            dialogBuilder.setNeutralButton("Cancelar", (dialog, witch) -> {

            }).setPositiveButton("Aceptar", (dialog, witch) -> {

            }).setSingleChoiceItems(this.availableTimes, -1, (dialog, which) -> {
                this.time = this.availableTimes[which].toString();
                this.updateTextButton(button);
            });
            dialogBuilder.show();
        });

        final Calendar calendar = Calendar.getInstance();
        final MaterialButton date = this.findViewById(R.id.button_date);
        date.setText(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
        date.setOnClickListener((view) -> {
            final CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build();
            final MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder).setTitleText("Seleccione la fecha")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
            picker.show(this.getSupportFragmentManager(), "date_espinardo");
            picker.addOnPositiveButtonClickListener(selection -> {
                calendar.setTimeInMillis(selection);
                this.availableTimes = bookingProvider.getAvailableTimes(installation.getId(), calendar.getTime());
                date.setText(new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
            });
        });

        // --- Payment listener ---
        this.paymentSheet = new PaymentSheet(this, (paymentSheetResult) -> {
            if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
                DynamicToast.makeSuccess(this, "Pago aceptado!", Toast.LENGTH_SHORT).show();
                button.setText("Reservado");

                //
                final AbstractAPI error = bookingProvider.booking(DataUtils.getUser().getId(), installation.getId(), calendar.getTimeInMillis(), this.time);

                if (error.getError() != null && !error.getError().trim().isEmpty()) {
                    DynamicToast.makeError(this, "No se ha podido completar la reserva!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // --- Schedule Notification ---
/*                final Intent notificationIntent = new Intent(this, NotificationPublisher.class);
                notificationIntent.putExtra("espinardo_info", 1);
                notificationIntent.putExtra("expinardo_book", new NotificationUtils().createNotification(this, "Reserva " + installation.getName()));
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                final AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                assert alarmManager != null;
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                calendar.add(Calendar.MINUTE, 30);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/

                DynamicToast.makeSuccess(this, "Reserva completada", Toast.LENGTH_SHORT).show();
                // Disable inputs
                button.setEnabled(false);
                date.setEnabled(false);
                times.setEnabled(false);
            } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
                DynamicToast.makeWarning(this, "Pago cancelado", Toast.LENGTH_SHORT).show();
            } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
                final Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
                new Utils(this).showAlert("Pago fallido", error.getLocalizedMessage());
            }
        });
        // --- ---

        // Chip moment!
        final Chip chip = this.findViewById(R.id.installation_tag);
        chip.setText(installation.getInstallationType().capitalize());
        chip.setTextColor(Color.BLACK);
        chip.setChipIconTint(ColorStateList.valueOf(Color.BLACK));

        switch (installation.getInstallationType()) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        this.gMap.setMyLocationEnabled(true);

        final Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.WALKING).withListener(this)
                .waypoints(this.gMap.getCameraPosition().target, installation.getLocation())
                .key("AIzaSyDu4WmedJpgV45HscxoGFALkhMaFgrIOmo")
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Log.e("check", e.getMessage());
    }

    @Override
    public void onRoutingStart() {
        Log.e("check", "onRoutingStart");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        Log.e("check", "onRoutingSuccess");
        CameraUpdate center = CameraUpdateFactory.newLatLng(this.gMap.getCameraPosition().target);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        List<Polyline> polylines = new ArrayList<>();

        this.gMap.moveCamera(center);


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(Color.RED);
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = this.gMap.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.e("check", "onRoutingCancelled");
    }

    public static InstallationActivity from(Installation installation) {
        InstallationActivity.installation = installation;
        return new InstallationActivity();
    }

    private void updateTextButton(MaterialButton button) {
        final String text = this.getResources().getString(R.string.reservar);
        button.setText(text.replaceAll("%s", installation.getPrice() + ""));
    }

    private boolean anyMatch(boolean[] array, boolean match) {
        for (boolean b : array) if (b == match) return true;
        return false;
    }
}
