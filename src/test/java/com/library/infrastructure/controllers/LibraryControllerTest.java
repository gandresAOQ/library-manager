package com.library.infrastructure.controllers;

import com.library.application.ports.IOrchestrator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.library.model.Constants.UUID_HEADER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class LibraryControllerTest {

    @InjectMock
    IOrchestrator orchestrator;

    private static final String UUID = "test-uuid-123";
    private static final String BASE_PATH = "/v1/library/bookings";

    private final JsonObject mockResponse = new JsonObject()
            .put("id", "1")
            .put("title", "Clean Code");

    @BeforeEach
    void setUp() {
        Mockito.reset(orchestrator);
    }

    // GET
    @Test
    void get_shouldReturn200_whenOrchestratorSucceeds() {
        Mockito.when(orchestrator.execute(UUID, null, "bookings", "GET"))
                .thenReturn(Uni.createFrom().item(mockResponse));

        given()
                .header(UUID_HEADER, UUID)
        .when()
                .get(BASE_PATH)
        .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void get_shouldReturn200_withNestedPath() {
        Mockito.when(orchestrator.execute(UUID, null, "bookings/123", "GET"))
                .thenReturn(Uni.createFrom().item(mockResponse));

        given()
                .header(UUID_HEADER, UUID)
        .when()
                .get("/v1/library/bookings/123")
        .then()
                .statusCode(200)
                .log()
                .all()
                .body("id", is("1"))
                .body("title", is("Clean Code"));
    }

    @Test
    void get_shouldReturn500_whenOrchestratorFails() {
        Mockito.when(orchestrator.execute(UUID, null, "bookings", "GET"))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Service unavailable")));

        given()
                .header(UUID_HEADER, UUID)
        .when()
                .get(BASE_PATH)
        .then()
                .statusCode(500)
                .body(containsString("Service unavailable"));
    }

    // POST
    @Test
    void post_shouldReturn200_whenOrchestratorSucceeds() {
        Mockito.when(orchestrator.execute(Mockito.eq(UUID), Mockito.any(), Mockito.eq("bookings"), Mockito.eq("POST")))
                .thenReturn(Uni.createFrom().item(mockResponse));

        given()
                .header(UUID_HEADER, UUID)
                .contentType("application/json")
                .body("{\n" +
                        "    \"id\": \"1\",\n" +
                        "    \"name\": \"Test book\",\n" +
                        "    \"author\": \"Test author\",\n" +
                        "    \"price\": 123.0,\n" +
                        "    \"language\": \"SPANISH\",\n" +
                        "    \"pages\": 100,\n" +
                        "    \"format\": \"EBOOK\"\n" +
                        "}")
        .when()
                .post(BASE_PATH)
        .then()
                .body("id", is("1"));
    }

    @Test
    void post_shouldReturn500_whenOrchestratorFails() {
        Mockito.when(orchestrator.execute(Mockito.eq(UUID), Mockito.any(), Mockito.eq("bookings"), Mockito.eq("POST")))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Service unavailable")));

        given()
                .header(UUID_HEADER, UUID)
                .contentType("application/json")
                .body("{\n" +
                        "    \"id\": \"1\",\n" +
                        "    \"name\": \"Test book\",\n" +
                        "    \"author\": \"Test author\",\n" +
                        "    \"price\": 123.0,\n" +
                        "    \"language\": \"SPANISH\",\n" +
                        "    \"pages\": 100,\n" +
                        "    \"format\": \"EBOOK\"\n" +
                        "}")
        .when()
                .post(BASE_PATH)
        .then()
                .statusCode(500)
                .body(containsString("Service unavailable"));
    }

    // PUT
    @Test
    void put_shouldReturn200_whenOrchestratorSucceeds() {
        Mockito.when(orchestrator.execute(Mockito.eq(UUID), Mockito.any(), Mockito.eq("bookings"), Mockito.eq("PUT")))
                .thenReturn(Uni.createFrom().item(mockResponse));

        given()
                .header(UUID_HEADER, UUID)
                .contentType("application/json")
                .body("{\n" +
                        "    \"id\": \"1\",\n" +
                        "    \"name\": \"Test book\",\n" +
                        "    \"author\": \"Test author\",\n" +
                        "    \"price\": 123.0,\n" +
                        "    \"language\": \"SPANISH\",\n" +
                        "    \"pages\": 100,\n" +
                        "    \"format\": \"EBOOK\"\n" +
                        "}")
        .when()
                .put(BASE_PATH)
        .then()
                .statusCode(200)
                .body("title", is("Clean Code"));
    }

    @Test
    void put_shouldReturn500_whenOrchestratorFails() {
        Mockito.when(orchestrator.execute(Mockito.eq(UUID), Mockito.any(), Mockito.eq("bookings"), Mockito.eq("PUT")))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Service unavailable")));

        given()
                .header(UUID_HEADER, UUID)
                .contentType("application/json")
                .body("{\n" +
                        "    \"id\": \"1\",\n" +
                        "    \"name\": \"Test book\",\n" +
                        "    \"author\": \"Test author\",\n" +
                        "    \"price\": 123.0,\n" +
                        "    \"language\": \"SPANISH\",\n" +
                        "    \"pages\": 100,\n" +
                        "    \"format\": \"EBOOK\"\n" +
                        "}")
        .when()
                .put(BASE_PATH)
        .then()
                .statusCode(500)
                .body(containsString("Service unavailable"));
    }

    // DELETE
    @Test
    void delete_shouldReturn200_whenOrchestratorSucceeds() {
        Mockito.when(orchestrator.execute(UUID, null, "bookings", "DELETE"))
                .thenReturn(Uni.createFrom().item(mockResponse));

        given()
                .header(UUID_HEADER, UUID)
        .when()
                .delete(BASE_PATH)
        .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void delete_shouldReturn500_whenOrchestratorFails() {
        Mockito.when(orchestrator.execute(UUID, null, "bookings", "DELETE"))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Service unavailable")));

        given()
                .header(UUID_HEADER, UUID)
        .when()
                .delete(BASE_PATH)
        .then()
                .statusCode(500)
                .body(containsString("Service unavailable"));
    }
}