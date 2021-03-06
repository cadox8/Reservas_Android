package es.ivan.espinardo.api.installations;

import es.ivan.espinardo.api.AbstractAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class Installations extends AbstractAPI {

    private Installation[] installations;
}
