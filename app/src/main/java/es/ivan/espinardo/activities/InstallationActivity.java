package es.ivan.espinardo.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.Date;

import es.ivan.espinardo.R;
import es.ivan.espinardo.activities.error.ErrorActivity;
import es.ivan.espinardo.api.AbstractAPI;
import es.ivan.espinardo.api.installations.Installation;
import es.ivan.espinardo.api.payment.PaymentIntent;
import es.ivan.espinardo.managers.SessionManager;
import es.ivan.espinardo.providers.BookingProvider;
import es.ivan.espinardo.providers.PaymentProvider;
import es.ivan.espinardo.utils.DataUtils;
import es.ivan.espinardo.utils.Utils;

public class InstallationActivity extends AppCompatActivity {

    private static Installation installation;

    private PaymentSheet paymentSheet;

    private int counter = 0;

    private CharSequence[] availableTimes;
    private boolean[] selectedTimes;

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

        // --- Data ---
        this.availableTimes = bookingProvider.getAvailableTimes(installation.getId(), new Date());
        this.selectedTimes = new boolean[availableTimes.length];
        // --- ---

        // --- Payment ---
        final MaterialButton button = this.findViewById(R.id.button_booking);
        this.updateTextButton(button);

        // Card: 4000 0072 4000 0007

        button.setOnClickListener((view) -> {
            if (!this.anyMatch(this.selectedTimes, true)) {
                DynamicToast.makeError(this, "Selecciona las horas para la reserva", Toast.LENGTH_SHORT).show();
                return;
            }

            final PaymentProvider paymentProvider = new PaymentProvider();
            final PaymentIntent paymentIntent = paymentProvider.fetchPaymentIntent(installation.getPrice(), 1);

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
        ((TextView)this.findViewById(R.id.installation_name)).setText(installation.getName());

        final MaterialButton times = this.findViewById(R.id.button_time);
        times.setOnClickListener(view -> {
            if (this.availableTimes.length == 0) {
                DynamicToast.makeError(this, "No hay horas disponibles para el día seleccionado", Toast.LENGTH_SHORT).show();
                return;
            }

            final MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this).setTitle(this.getResources().getString(R.string.select_time));
            dialogBuilder.setNeutralButton("Cancelar", (dialog, witch) -> {

            }).setPositiveButton("Aceptar", (dialog, witch) -> {

            }).setMultiChoiceItems(this.availableTimes, this.selectedTimes, (dialog, which, isChecked) -> {
                this.selectedTimes[which] = isChecked;
                if (isChecked) {
                    this.counter++;
                } else {
                    this.counter--;
                }
                this.updateTextButton(button);
            });
            dialogBuilder.show();
        });

        final Calendar calendar = Calendar.getInstance();
        final MaterialButton date = this.findViewById(R.id.button_date);
        date.setText(new SimpleDateFormat("dd-mm-yyyy").format(Calendar.getInstance().getTime()));
        date.setOnClickListener((view) -> {
            final CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build();
            final MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder).setTitleText("Seleccione la fecha")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
            picker.show(this.getSupportFragmentManager(), "date_espinardo");
            picker.addOnPositiveButtonClickListener(selection -> {
                calendar.setTimeInMillis(selection);
                this.availableTimes = bookingProvider.getAvailableTimes(installation.getId(), calendar.getTime());
                this.selectedTimes = new boolean[availableTimes.length];
                date.setText(new SimpleDateFormat("dd-mm-yyyy").format(calendar.getTime()));
            });
        });

        // --- Payment listener ---
        this.paymentSheet = new PaymentSheet(this, (paymentSheetResult) -> {
            if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
                DynamicToast.makeSuccess(this, "Pago aceptado!", Toast.LENGTH_SHORT).show();
                button.setText("Reservado");

                //
                final StringBuilder sb = new StringBuilder();

                for (int i = 0; i < this.availableTimes.length; i++) {
                    if (this.selectedTimes[i]) {
                        String ti = this.availableTimes[i].toString().split(":")[0];
                        if (ti.startsWith("0")) ti = ti.substring(1, 1);
                        sb.append(ti).append("_");
                    }
                }

                if (sb.charAt(sb.length() - 1) == '_') sb.deleteCharAt(sb.length() - 1);

                final AbstractAPI error = bookingProvider.booking(DataUtils.getUser().getId(), installation.getId(), calendar.getTime().getTime(), sb.toString());

                if (error.getError() != null || !error.getError().trim().isEmpty()) {
                    DynamicToast.makeError(this, "No se ha podido completar la reserva!", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    public static InstallationActivity from(Installation installation) {
        InstallationActivity.installation = installation;
        return new InstallationActivity();
    }

    private void updateTextButton(MaterialButton button) {
        final String text = this.getResources().getString(R.string.reservar);
        button.setText(text.replaceAll("%s", (this.counter == 0 ? installation.getPrice() : installation.getPrice() * this.counter) + ""));
    }

    private boolean anyMatch(boolean[] array, boolean match) {
        for (boolean b : array) if (b == match) return true;
        return false;
    }
}
