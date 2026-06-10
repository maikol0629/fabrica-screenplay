package com.fabrica.screenplay.tasks;

import com.fabrica.screenplay.interactions.DoGet;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class FindAllServicios implements Task {

    private final String endpoint;

    public FindAllServicios(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(DoGet.resource(endpoint));
    }

    public static FindAllServicios from(String endpoint) {
        return instrumented(FindAllServicios.class, endpoint);
    }
}
