package es.ivan.espinardo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class Installations extends AbstractAPI {

    public Installation[] installations;
}
