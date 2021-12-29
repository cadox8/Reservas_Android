package es.ivan.espinardo.providers;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import es.ivan.espinardo.api.AbstractAPI;
import es.ivan.espinardo.api.bookings.AvailableTimes;
import es.ivan.espinardo.api.bookings.Booking;
import es.ivan.espinardo.api.bookings.Bookings;
import es.ivan.espinardo.api.payment.PaymentIntent;

public class BookingProvider extends AbstractProvider {

    public AbstractAPI booking(int user_id, int installation_id, long date, String times) {
        final HashMap<String, String> body = new HashMap<>();
        body.put("user_id", String.valueOf(user_id));
        body.put("installation_id", String.valueOf(installation_id));
        body.put("date", String.valueOf(date));
        body.put("times", times);

        AbstractAPI status;
        try {
            status = this.pool.submit(() -> this.post(AbstractAPI.class, "booking/book", body)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            status = null;
        }
        return status;
    }

    public CharSequence[] getAvailableTimes(int installationId, Date date) {
        AvailableTimes booking;
        try {
            booking = this.pool.submit(() -> this.get(AvailableTimes.class, "booking/ava/" + installationId + "/" + date.getTime())).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            booking = null;
        }

        final CharSequence[] availableTimes = booking != null ? booking.getAvailableTimes() : new CharSequence[0];

        if (availableTimes.length == 0) return availableTimes;
        for (int i = 0; i < availableTimes.length; i++) {
            final StringBuilder sb = new StringBuilder();
            if (availableTimes[i].length() == 1) {
                sb.append(0).append(availableTimes[i]);
            } else {
                sb.append(availableTimes[i]);
            }
            sb.append(":").append("00");
            availableTimes[i] = sb.toString();
        }

        return availableTimes;
    }

    public Booking getBooking(String bookingToken) {
        Booking booking;
        try {
            booking = this.pool.submit(() -> this.get(Booking.class, "booking/" + bookingToken)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            booking = null;
        }
        return booking;
    }

    public Bookings getAllBookings(String userToken) {
        Bookings booking;
        try {
            booking = this.pool.submit(() -> this.get(Bookings.class, "booking/user/" + userToken)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            booking = null;
        }
        return booking;
    }
}
