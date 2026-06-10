package com.fabrica.screenplay.stepdefinitions;

import com.fabrica.screenplay.interactions.DoGet;
import com.fabrica.screenplay.interactions.DoPost;
import com.fabrica.screenplay.interactions.DoPut;
import com.fabrica.screenplay.questions.LastResponseStatusCode;
import com.fabrica.screenplay.questions.ServerResponse;
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

public class UsuarioStepDefinition {

    private Actor user;

    @Before(order = 1)
    public void config() {
        user = OnStage.theActorCalled("admin");
    }

    @Given("que el administrador quiere gestionar las personas")
    public void administradorQuiereGestionarPersonas() {
        user.remember("endpoint", "/api/v1/personas");
    }

    @Given("que existe una persona registrada")
    public void existePersonaRegistrada() {
        long ts = System.currentTimeMillis();
        String doc = "DOC" + ts;
        String email = "test_" + ts + "@test.com";
        String body = "{\"numeroDocumento\":\"" + doc + "\",\"numeroCelular\":\"3001112222\","
            + "\"email\":\"" + email + "\",\"nombrePersona\":\"Persona Test\","
            + "\"contraseña\":\"password123\",\"confirmarContraseña\":\"password123\"}";
        user.attemptsTo(DoPost.to("/api/v1/personas/registro", body));
        user.should(seeThat(LastResponseStatusCode.is(), equalTo(201)));
        user.remember("personaDoc", doc);
        user.remember("personaEmail", email);
    }

    @Given("que la persona esta bloqueada")
    public void personaBloqueada() {
        String doc = user.recall("personaDoc");
        user.attemptsTo(DoPut.to("/api/v1/personas/" + doc + "/bloquear", ""));
        user.should(seeThat(LastResponseStatusCode.is(), equalTo(200)));
    }

    @When("consulta la lista de todas las personas")
    public void consultaListaPersonas() {
        user.attemptsTo(DoGet.resource("/api/v1/personas"));
    }

    @When("consulta la persona con documento {string}")
    public void consultaPersonaPorDocumento(String documento) {
        user.attemptsTo(DoGet.resource("/api/v1/personas/documento/" + documento));
    }

    @When("consulta la persona con email {string}")
    public void consultaPersonaPorEmail(String email) {
        user.attemptsTo(DoGet.resource("/api/v1/personas/email/" + email));
    }

    @When("consulta la lista de barberos")
    public void consultaListaBarberos() {
        user.attemptsTo(DoGet.resource("/api/v1/personas/barberos"));
    }

    @When("consulta la lista de clientes bloqueados")
    public void consultaListaBloqueados() {
        user.attemptsTo(DoGet.resource("/api/v1/personas/bloqueados"));
    }

    @When("registra una nueva persona con datos unicos")
    public void registraPersonaUnica() {
        long ts = System.currentTimeMillis();
        String doc = "DOC" + ts;
        String email = "nuevo_" + ts + "@test.com";
        user.remember("registroDoc", doc);
        user.remember("registroEmail", email);
        String body = "{\"numeroDocumento\":\"" + doc + "\",\"numeroCelular\":\"3003334444\","
            + "\"email\":\"" + email + "\",\"nombrePersona\":\"Nueva Persona\","
            + "\"contraseña\":\"password123\",\"confirmarContraseña\":\"password123\"}";
        user.attemptsTo(DoPost.to("/api/v1/personas/registro", body));
    }

    @When("cambia el rol de la persona guardada a barbero")
    public void cambiaRolPersonaGuardadaABarbero() {
        String doc = user.recall("personaDoc");
        user.attemptsTo(DoPut.to("/api/v1/personas/" + doc + "/rol?nuevoRol=2", ""));
    }

    @When("bloquea la persona guardada")
    public void bloqueaPersonaGuardada() {
        String doc = user.recall("personaDoc");
        user.attemptsTo(DoPut.to("/api/v1/personas/" + doc + "/bloquear", ""));
    }

    @When("desbloquea la persona guardada")
    public void desbloqueaPersonaGuardada() {
        String doc = user.recall("personaDoc");
        user.attemptsTo(DoPut.to("/api/v1/personas/" + doc + "/desbloquear", ""));
    }

    @When("registra una persona con el mismo documento de la persona guardada")
    public void registraPersonaConMismoDocumento() {
        String doc = user.recall("personaDoc");
        String body = "{\"numeroDocumento\":\"" + doc + "\",\"numeroCelular\":\"3005556666\","
            + "\"email\":\"otro@test.com\",\"nombrePersona\":\"Otra Persona\","
            + "\"contraseña\":\"password123\",\"confirmarContraseña\":\"password123\"}";
        user.attemptsTo(DoPost.to("/api/v1/personas/registro", body));
    }

    @Then("la respuesta debe contener un listado de personas")
    public void respuestaContieneListadoPersonas() {
        user.should(seeThat(actor -> {
            Response r = CallAnApi.as(actor).getLastResponse();
            return r.jsonPath().getList("data") != null;
        }, equalTo(true)));
    }

    @Then("los datos de la persona coinciden con los del administrador")
    public void datosPersonaCoincidenConAdmin() {
        user.should(
            seeThat("numeroDocumento", ServerResponse.jsonPathString("data.numeroDocumento"),
                equalTo("123456789")),
            seeThat("email", ServerResponse.jsonPathString("data.email"),
                equalTo("admin@barberia.com")),
            seeThat("nombrePersona", ServerResponse.jsonPathString("data.nombrePersona"),
                not(isEmptyOrNullString())),
            seeThat("idRol", ServerResponse.jsonPathInt("data.idRol"), equalTo(1)),
            seeThat("idEstado", ServerResponse.jsonPathInt("data.idEstado"), notNullValue())
        );
    }

    @Then("la persona fue registrada con los datos enviados")
    public void personaRegistradaConDatosEnviados() {
        String doc = user.recall("registroDoc");
        String email = user.recall("registroEmail");
        user.should(
            seeThat("numeroDocumento", ServerResponse.jsonPathString("data.numeroDocumento"),
                equalTo(doc)),
            seeThat("email", ServerResponse.jsonPathString("data.email"),
                equalTo(email)),
            seeThat("nombrePersona", ServerResponse.jsonPathString("data.nombrePersona"),
                equalTo("Nueva Persona")),
            seeThat("numeroCelular", ServerResponse.jsonPathString("data.numeroCelular"),
                equalTo("3003334444")),
            seeThat("idRol", ServerResponse.jsonPathInt("data.idRol"), equalTo(3)),
            seeThat("idEstado", ServerResponse.jsonPathInt("data.idEstado"), equalTo(1))
        );
    }

    @Then("la persona ahora tiene rol de barbero")
    public void personaAhoraTieneRolBarbero() {
        user.should(
            seeThat("idRol", ServerResponse.jsonPathInt("data.idRol"), equalTo(2))
        );
    }

    @Then("la persona esta bloqueada")
    public void personaEstaBloqueada() {
        String doc = user.recall("personaDoc");
        user.attemptsTo(DoGet.resource("/api/v1/personas/documento/" + doc));
        user.should(
            seeThat(LastResponseStatusCode.is(), equalTo(200)),
            seeThat("idEstado", ServerResponse.jsonPathInt("data.idEstado"), equalTo(4))
        );
    }

    @Then("la persona no esta bloqueada")
    public void personaNoEstaBloqueada() {
        String doc = user.recall("personaDoc");
        user.attemptsTo(DoGet.resource("/api/v1/personas/documento/" + doc));
        user.should(
            seeThat(LastResponseStatusCode.is(), equalTo(200)),
            seeThat("idEstado", ServerResponse.jsonPathInt("data.idEstado"), equalTo(1))
        );
    }

    @Then("la respuesta de persona debe contener las propiedades requeridas")
    public void respuestaPersonaContienePropiedadesRequeridas() {
        user.should(
            seeThat("numeroDocumento", ServerResponse.jsonPath("data.numeroDocumento"), notNullValue()),
            seeThat("numeroCelular", ServerResponse.jsonPath("data.numeroCelular"), notNullValue()),
            seeThat("email", ServerResponse.jsonPath("data.email"), notNullValue()),
            seeThat("nombrePersona", ServerResponse.jsonPath("data.nombrePersona"), notNullValue()),
            seeThat("idEstado", ServerResponse.jsonPath("data.idEstado"), notNullValue()),
            seeThat("idRol", ServerResponse.jsonPath("data.idRol"), notNullValue()),
            seeThat("fechaRegistro", ServerResponse.jsonPath("data.fechaRegistro"), notNullValue())
        );
    }
}
