package com.fabrica.screenplay.tasks;

import com.fabrica.screenplay.interactions.DoGet;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class FindServicioById implements Task {

    private final String endpoint;

    public FindServicioById(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(DoGet.resource(endpoint));
    }

    public static FindServicioById withId(String endpoint) {
        return instrumented(FindServicioById.class, endpoint);
    }
}
