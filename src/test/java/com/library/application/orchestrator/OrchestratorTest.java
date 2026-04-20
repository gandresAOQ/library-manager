package com.library.application.orchestrator;

import com.library.application.services.BookingService;
import com.library.model.errors.LibraryException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class OrchestratorTest {

    @Inject
    Orchestrator orchestrator;

    @InjectMock
    BookingService bookingService;

    private static final String UUID = "test-uuid";
    private static final String PATH = "bookings";
    private static final String PATH_WITH_ID = "bookings/book-1";
    private static final String ID = "book-1";

    private final JsonObject mockResult = new JsonObject().put("status", 200).put("payload", "ok");

    @BeforeEach
    void setUp() {
        Mockito.reset(bookingService);
    }

    @Test
    void execute_GET_withoutId_shouldCallServiceGet() {
        Mockito.when(bookingService.get(UUID)).thenReturn(Uni.createFrom().item(mockResult));

        JsonObject result = orchestrator.execute(UUID, null, PATH, "GET").await().indefinitely();

        assertEquals(mockResult, result);
        Mockito.verify(bookingService).get(UUID);
    }

    @Test
    void execute_GET_withId_shouldCallServiceGetById() {
        Mockito.when(bookingService.get(UUID, ID)).thenReturn(Uni.createFrom().item(mockResult));

        JsonObject result = orchestrator.execute(UUID, null, PATH_WITH_ID, "GET").await().indefinitely();

        assertEquals(mockResult, result);
        Mockito.verify(bookingService).get(UUID, ID);
    }

    @Test
    void execute_POST_shouldCallServicePost() {
        JsonObject body = new JsonObject().put("name", "Clean Code");
        Mockito.when(bookingService.post(UUID, body)).thenReturn(Uni.createFrom().item(mockResult));

        JsonObject result = orchestrator.execute(UUID, body, PATH, "POST").await().indefinitely();

        assertEquals(mockResult, result);
        Mockito.verify(bookingService).post(UUID, body);
    }

    @Test
    void execute_PUT_withId_shouldCallServicePut() {
        JsonObject body = new JsonObject().put("name", "Refactoring");
        Mockito.when(bookingService.put(UUID, ID, body)).thenReturn(Uni.createFrom().item(mockResult));

        JsonObject result = orchestrator.execute(UUID, body, PATH_WITH_ID, "PUT").await().indefinitely();

        assertEquals(mockResult, result);
        Mockito.verify(bookingService).put(UUID, ID, body);
    }

    @Test
    void execute_PUT_withoutId_shouldThrowLibraryException() {
        LibraryException ex = assertThrows(LibraryException.class,
                () -> orchestrator.execute(UUID, new JsonObject(), PATH, "PUT"));

        assertEquals("PUT resource must have ID", ex.getMessage());
        assertEquals(400, ex.getHttpStatus());
        assertEquals("Bad Request", ex.getHttpStatusMessage());
    }

    @Test
    void execute_DELETE_withId_shouldCallServiceDelete() {
        Mockito.when(bookingService.delete(UUID, ID)).thenReturn(Uni.createFrom().item(mockResult));

        JsonObject result = orchestrator.execute(UUID, null, PATH_WITH_ID, "DELETE").await().indefinitely();

        assertEquals(mockResult, result);
        Mockito.verify(bookingService).delete(UUID, ID);
    }

    @Test
    void execute_DELETE_withoutId_shouldThrowLibraryException() {
        LibraryException ex = assertThrows(LibraryException.class,
                () -> orchestrator.execute(UUID, null, PATH, "DELETE"));

        assertEquals("DELETE resource must have ID", ex.getMessage());
        assertEquals(400, ex.getHttpStatus());
        assertEquals("Bad Request", ex.getHttpStatusMessage());
    }

    @Test
    void execute_unknownMethod_shouldReturnNull() {
        Uni<JsonObject> result = orchestrator.execute(UUID, null, PATH_WITH_ID, "PATCH");

        assertNull(result);
    }
}