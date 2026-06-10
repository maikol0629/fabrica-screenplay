package com.fabrica.screenplay.tasks;

import com.fabrica.screenplay.interactions.DoDelete;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteServicio implements Task {

    private final String endpoint;

    public DeleteServicio(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(DoDelete.from(endpoint));
    }

    public static DeleteServicio withId(String endpoint) {
        return instrumented(DeleteServicio.class, endpoint);
    }
}
