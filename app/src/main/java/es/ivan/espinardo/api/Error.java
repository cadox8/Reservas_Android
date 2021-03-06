package es.ivan.espinardo.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Error extends AbstractAPI {

    @Getter private final String errorMessage;
}
