package es.ivan.espinardo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class User extends AbstractAPI {

    private final int id;

    private final String token;

    private final String username;
    private final String email;
}
