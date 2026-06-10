package com.fabrica.screenplay.tasks;

import com.fabrica.screenplay.interactions.DoPost;
import com.fabrica.screenplay.models.ModelCreateServicio;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Actor;
import com.google.gson.Gson;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateNewServicio implements Task {

    private final ModelCreateServicio servicioData;
    private final String endpoint;

    public CreateNewServicio(String endpoint, ModelCreateServicio servicioData) {
        this.endpoint = endpoint;
        this.servicioData = servicioData;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String body = new Gson().toJson(servicioData);
        actor.attemptsTo(DoPost.to(endpoint, body));
    }

    public static CreateNewServicio withData(String endpoint, ModelCreateServicio servicioData) {
        return instrumented(CreateNewServicio.class, endpoint, servicioData);
    }
}
