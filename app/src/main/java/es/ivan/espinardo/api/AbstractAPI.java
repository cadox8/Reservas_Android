package es.ivan.espinardo.api;

import java.io.Serializable;

import lombok.Getter;

public abstract class AbstractAPI implements Serializable {

    private static final long serialVersionUID = 2733656478674716638L;

    @Getter private String error;
}
