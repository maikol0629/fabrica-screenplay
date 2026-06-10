package com.fabrica.screenplay.stepdefinitions;

import com.fabrica.screenplay.interactions.DoPost;
import com.fabrica.screenplay.models.ModelCreateServicio;
import com.fabrica.screenplay.questions.LastResponseStatusCode;
import com.fabrica.screenplay.questions.ServerResponse;
import com.fabrica.screenplay.tasks.*;
import com.fabrica.screenplay.utils.Utils;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import io.restassured.response.Response;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;

public class ServicioStepDefinition {

    private Actor user;

    @Before(order = 1)
    public void config() {
        user = OnStage.theActorCalled("admin");
    }

    @Given("que el administrador quiere gestionar los servicios")
    public void administradorQuiereGestionarServicios() {
        user.remember("endpoint", "/api/v1/servicios");
    }

    @Given("que existe un servicio creado previamente")
    public void existeUnServicioCreadoPreviamente() {
        ModelCreateServicio servicio = Utils.createUniqueServicio();
        user.remember("servicioData", servicio);
        user.attemptsTo(CreateNewServicio.withData("/api/v1/servicios/crear", servicio));
        user.should(seeThat(LastResponseStatusCode.is(), equalTo(201)));
        Response response = CallAnApi.as(user).getLastResponse();
        int id = response.jsonPath().getInt("data.idServicio");
        user.remember("servicioId", id);
    }

    @When("consulta la lista completa de servicios")
    public void consultaListaServicios() {
        user.attemptsTo(FindAllServicios.from("/api/v1/servicios"));
    }

    @When("crea un nuevo servicio con nombre {string} descripcion {string} precio {double} duracion {int} minutos")
    public void creaNuevoServicio(String nombre, String descripcion, double precio, int duracion) {
        ModelCreateServicio servicio = new ModelCreateServicio(nombre, descripcion, precio, duracion);
        user.remember("servicioData", servicio);
        user.attemptsTo(CreateNewServicio.withData("/api/v1/servicios/crear", servicio));
    }

    @When("consulta el servicio con id {int}")
    public void consultaServicioPorId(int id) {
        user.attemptsTo(FindServicioById.withId("/api/v1/servicios/buscar/" + id));
    }

    @When("consulta el servicio guardado por su ID")
    public void consultaServicioGuardadoPorId() {
        int id = user.recall("servicioId");
        user.attemptsTo(FindServicioById.withId("/api/v1/servicios/buscar/" + id));
    }

    @When("actualiza el servicio con id {int} con nombre {string} descripcion {string} costo {double} duracion {int} minutos")
    public void actualizaServicio(int id, String nombre, String descripcion, double costo, int duracion) {
        ModelCreateServicio servicio = new ModelCreateServicio(nombre, descripcion, costo, duracion);
        user.attemptsTo(UpdateServicio.withData("/api/v1/servicios/actualizar/" + id, servicio));
    }

    @When("actualiza el servicio guardado con nuevos datos")
    public void actualizaServicioGuardado() {
        int id = user.recall("servicioId");
        long ts = System.currentTimeMillis();
        ModelCreateServicio updatedData = new ModelCreateServicio(
            "Corte Moderno " + ts,
            "Descripcion actualizada " + ts,
            30000.0,
            40
        );
        user.remember("updatedServicioData", updatedData);
        user.attemptsTo(UpdateServicio.withData("/api/v1/servicios/actualizar/" + id, updatedData));
    }

    @When("elimina el servicio con id {int}")
    public void eliminaServicio(int id) {
        user.attemptsTo(DeleteServicio.withId("/api/v1/servicios/eliminar/" + id));
    }

    @When("elimina el servicio guardado")
    public void eliminaServicioGuardado() {
        int id = user.recall("servicioId");
        user.attemptsTo(DeleteServicio.withId("/api/v1/servicios/eliminar/" + id));
    }

    @When("envia una solicitud POST a {string} con body {string}")
    public void enviaPOSTConBody(String endpoint, String body) {
        user.attemptsTo(DoPost.to(endpoint, body));
    }

    @Then("deberia obtener un codigo de respuesta {int}")
    public void deberiaObtenerCodigoRespuesta(int statusCode) {
        user.should(seeThat(LastResponseStatusCode.is(), equalTo(statusCode)));
    }

    @Then("los datos del servicio creado coinciden con los enviados")
    public void datosServicioCreadoCoinciden() {
        ModelCreateServicio expected = user.recall("servicioData");
        user.should(
            seeThat("nombreServicio", ServerResponse.jsonPathString("data.nombreServicio"),
                equalTo(expected.getNombreServicio())),
            seeThat("descripcion", ServerResponse.jsonPathString("data.descripcion"),
                equalTo(expected.getDescripcion())),
            seeThat("costo", ServerResponse.jsonPathDouble("data.costo"),
                equalTo(expected.getCosto())),
            seeThat("duracion", ServerResponse.jsonPathInt("data.duracion"),
                equalTo(expected.getDuracion()))
        );
    }

    @Then("el servicio fue registrado con un ID valido")
    public void servicioRegistradoConIdValido() {
        user.should(seeThat(ServerResponse.jsonPathInt("data.idServicio"), greaterThan(0)));
        int id = CallAnApi.as(user).getLastResponse().jsonPath().getInt("data.idServicio");
        user.remember("servicioId", id);
    }

    @Then("la respuesta debe contener un listado de servicios")
    public void respuestaContieneListado() {
        user.should(seeThat(actor -> {
            Response r = CallAnApi.as(actor).getLastResponse();
            return r.jsonPath().getList("data") != null;
        }, equalTo(true)));
    }

    @Then("los datos del servicio coinciden con los esperados")
    public void datosCoincidenConEsperados() {
        ModelCreateServicio expected = user.recall("servicioData");
        user.should(
            seeThat("nombreServicio", ServerResponse.jsonPathString("data.nombreServicio"),
                equalTo(expected.getNombreServicio())),
            seeThat("descripcion", ServerResponse.jsonPathString("data.descripcion"),
                equalTo(expected.getDescripcion())),
            seeThat("costo", ServerResponse.jsonPathDouble("data.costo"),
                equalTo(expected.getCosto())),
            seeThat("duracion", ServerResponse.jsonPathInt("data.duracion"),
                equalTo(expected.getDuracion()))
        );
    }

    @Then("los cambios persisten al consultar el servicio")
    public void cambiosPersistenAlConsultar() {
        int id = user.recall("servicioId");
        ModelCreateServicio updatedData = user.recall("updatedServicioData");
        user.attemptsTo(FindServicioById.withId("/api/v1/servicios/buscar/" + id));
        user.should(
            seeThat(LastResponseStatusCode.is(), equalTo(200)),
            seeThat("nombreServicio", ServerResponse.jsonPathString("data.nombreServicio"),
                equalTo(updatedData.getNombreServicio())),
            seeThat("descripcion", ServerResponse.jsonPathString("data.descripcion"),
                equalTo(updatedData.getDescripcion())),
            seeThat("costo", ServerResponse.jsonPathDouble("data.costo"),
                equalTo(updatedData.getCosto())),
            seeThat("duracion", ServerResponse.jsonPathInt("data.duracion"),
                equalTo(updatedData.getDuracion()))
        );
    }

    @Then("al consultar el servicio eliminado se obtiene {int}")
    public void alConsultarServicioEliminado(int statusCode) {
        int id = user.recall("servicioId");
        user.attemptsTo(FindServicioById.withId("/api/v1/servicios/buscar/" + id));
        user.should(seeThat(LastResponseStatusCode.is(), equalTo(statusCode)));
    }

    @Then("la respuesta debe contener las propiedades requeridas")
    public void respuestaContienePropiedadesRequeridas() {
        user.should(
            seeThat("id", ServerResponse.jsonPath("data.idServicio"), notNullValue()),
            seeThat("nombreServicio", ServerResponse.jsonPath("data.nombreServicio"), notNullValue()),
            seeThat("descripcion", ServerResponse.jsonPath("data.descripcion"), notNullValue()),
            seeThat("costo", ServerResponse.jsonPath("data.costo"), notNullValue()),
            seeThat("duracion", ServerResponse.jsonPath("data.duracion"), notNullValue())
        );
    }

    @Then("{string} no debe estar vacio")
    public void campoNoDebeEstarVacio(String campo) {
        user.should(seeThat(ServerResponse.jsonPathString("data." + campo), not(isEmptyOrNullString())));
    }

    @Then("{string} debe ser un numero positivo")
    public void campoDebeSerNumeroPositivo(String campo) {
        user.should(seeThat(ServerResponse.jsonPathDouble("data." + campo), greaterThan(0.0)));
    }

    @Then("{string} debe ser un entero positivo")
    public void campoDebeSerEnteroPositivo(String campo) {
        user.should(seeThat(ServerResponse.jsonPathInt("data." + campo), greaterThan(0)));
    }
}
