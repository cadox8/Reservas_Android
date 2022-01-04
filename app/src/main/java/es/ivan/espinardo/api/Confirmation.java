package es.ivan.espinardo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Confirmation extends AbstractAPI {

    private final String code;
}
