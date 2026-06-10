package com.fabrica.screenplay.tasks;

import com.fabrica.screenplay.interactions.DoPut;
import com.fabrica.screenplay.models.ModelCreateServicio;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Actor;
import com.google.gson.Gson;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class UpdateServicio implements Task {

    private final ModelCreateServicio servicioData;
    private final String endpoint;

    public UpdateServicio(String endpoint, ModelCreateServicio servicioData) {
        this.endpoint = endpoint;
        this.servicioData = servicioData;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String body = new Gson().toJson(servicioData);
        actor.attemptsTo(DoPut.to(endpoint, body));
    }

    public static UpdateServicio withData(String endpoint, ModelCreateServicio servicioData) {
        return instrumented(UpdateServicio.class, endpoint, servicioData);
    }
}
