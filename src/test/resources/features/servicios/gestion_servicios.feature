Feature: Gestion de Servicios
  Como administrador del sistema de barberia
  Quiero gestionar los servicios ofrecidos
  Para poder mantener actualizado el catalogo de servicios

  Background:
    Given que el administrador inicia sesion con sus credenciales de prueba
    And que el administrador quiere gestionar los servicios

  @CRUD_Servicio
  Scenario: Crear un servicio exitosamente
    When crea un nuevo servicio con nombre "Corte de Cabello" descripcion "Corte clasico para caballero" precio 25000.0 duracion 30 minutos
    Then deberia obtener un codigo de respuesta 201
    And los datos del servicio creado coinciden con los enviados
    And el servicio fue registrado con un ID valido

  @CRUD_Servicio
  Scenario: Consultar listado de servicios
    When consulta la lista completa de servicios
    Then deberia obtener un codigo de respuesta 200
    And la respuesta debe contener un listado de servicios

  @CRUD_Servicio
  Scenario: Consultar servicio por ID
    Given que existe un servicio creado previamente
    When consulta el servicio guardado por su ID
    Then deberia obtener un codigo de respuesta 200
    And los datos del servicio coinciden con los esperados

  @CRUD_Servicio
  Scenario: Actualizar un servicio existente
    Given que existe un servicio creado previamente
    When actualiza el servicio guardado con nuevos datos
    Then deberia obtener un codigo de respuesta 200
    And los cambios persisten al consultar el servicio

  @CRUD_Servicio
  Scenario: Eliminar un servicio existente
    Given que existe un servicio creado previamente
    When elimina el servicio guardado
    Then deberia obtener un codigo de respuesta 200
    And al consultar el servicio eliminado se obtiene 404

  @Contrato
  Scenario: Validar estructura de un servicio
    Given que existe un servicio creado previamente
    When consulta el servicio guardado por su ID
    Then la respuesta debe contener las propiedades requeridas
    And "nombreServicio" no debe estar vacio
    And "costo" debe ser un numero positivo
    And "duracion" debe ser un entero positivo

  @Negativo
  Scenario: Crear servicio con body vacio
    When envia una solicitud POST a "/api/v1/servicios/crear" con body "{}"
    Then deberia obtener un codigo de respuesta 400

  @Negativo
  Scenario: Consultar servicio inexistente
    When consulta el servicio con id 999999
    Then deberia obtener un codigo de respuesta 404

  @Negativo
  Scenario: Actualizar servicio inexistente
    When actualiza el servicio con id 999999 con nombre "N/A" descripcion "N/A" costo 0.0 duracion 0 minutos
    Then deberia obtener un codigo de respuesta 400

  @Negativo
  Scenario: Eliminar servicio inexistente
    When elimina el servicio con id 999999
    Then deberia obtener un codigo de respuesta 400
