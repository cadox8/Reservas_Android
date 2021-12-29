package es.ivan.espinardo.providers;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import es.ivan.espinardo.api.payment.PaymentIntent;

public class PaymentProvider extends AbstractProvider {

    public PaymentIntent fetchPaymentIntent(double price, int hours) {
        final HashMap<String, String> body = new HashMap<>();
        body.put("price", String.valueOf(price));
        body.put("hours", String.valueOf(hours));

        PaymentIntent paymentIntent;
        try {
            paymentIntent = this.pool.submit(() -> this.post(PaymentIntent.class, "checkout/intent", body)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            paymentIntent = null;
        }
        return paymentIntent;
    }
}
