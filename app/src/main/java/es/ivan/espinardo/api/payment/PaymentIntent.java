package es.ivan.espinardo.api.payment;

import es.ivan.espinardo.api.AbstractAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class PaymentIntent extends AbstractAPI {

    private final String clientSecret;
}
