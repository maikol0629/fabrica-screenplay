Feature: Gestion de Usuarios
  Como administrador del sistema
  Quiero gestionar las personas registradas
  Para poder administrar los usuarios de la barberia

  Background:
    Given que el administrador inicia sesion con sus credenciales de prueba
    And que el administrador quiere gestionar las personas

  @CRUD_Usuario
  Scenario: Listar todas las personas
    When consulta la lista de todas las personas
    Then deberia obtener un codigo de respuesta 200
    And la respuesta debe contener un listado de personas

  @CRUD_Usuario
  Scenario: Consultar persona por documento existente
    When consulta la persona con documento "123456789"
    Then deberia obtener un codigo de respuesta 200
    And los datos de la persona coinciden con los del administrador

  @CRUD_Usuario
  Scenario: Consultar persona por email existente
    When consulta la persona con email "admin@barberia.com"
    Then deberia obtener un codigo de respuesta 200

  @CRUD_Usuario
  Scenario: Listar barberos registrados
    When consulta la lista de barberos
    Then deberia obtener un codigo de respuesta 200
    And la respuesta debe contener un listado de personas

  @CRUD_Usuario
  Scenario: Listar clientes bloqueados
    When consulta la lista de clientes bloqueados
    Then deberia obtener un codigo de respuesta 200
    And la respuesta debe contener un listado de personas

  @CRUD_Usuario
  Scenario: Registrar una nueva persona
    When registra una nueva persona con datos unicos
    Then deberia obtener un codigo de respuesta 201
    And la persona fue registrada con los datos enviados

  @CRUD_Usuario
  Scenario: Cambiar rol de una persona
    Given que existe una persona registrada
    When cambia el rol de la persona guardada a barbero
    Then deberia obtener un codigo de respuesta 200
    And la persona ahora tiene rol de barbero

  @CRUD_Usuario
  Scenario: Bloquear un cliente
    Given que existe una persona registrada
    When bloquea la persona guardada
    Then deberia obtener un codigo de respuesta 200
    And la persona esta bloqueada

  @CRUD_Usuario
  Scenario: Desbloquear un cliente
    Given que existe una persona registrada
    And que la persona esta bloqueada
    When desbloquea la persona guardada
    Then deberia obtener un codigo de respuesta 200
    And la persona no esta bloqueada

  @Negativo
  Scenario: Consultar persona por documento inexistente
    When consulta la persona con documento "DOCNOEXISTE"
    Then deberia obtener un codigo de respuesta 404

  @Negativo
  Scenario: Consultar persona por email inexistente
    When consulta la persona con email "noexiste@test.com"
    Then deberia obtener un codigo de respuesta 404

  @Negativo
  Scenario: Registrar persona con documento repetido
    Given que existe una persona registrada
    When registra una persona con el mismo documento de la persona guardada
    Then deberia obtener un codigo de respuesta 400

  @Contrato
  Scenario: Validar estructura de respuesta de persona
    When consulta la persona con documento "123456789"
    Then la respuesta de persona debe contener las propiedades requeridas
