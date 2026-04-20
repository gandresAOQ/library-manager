package com.library.infrastructure.contracts;

import com.booking.grpc.Booking;
import com.library.model.booking.BookModel;
import io.smallrye.mutiny.Uni;

public interface IBookingRepository {

    Uni<Booking.BookingResponse> get(String uuid);
    Uni<Booking.BookingResponse> get(String uuid, String id);
    Uni<Booking.BookingResponse> post(String uuid, BookModel body);
    Uni<Booking.BookingResponse> put(String uuid, String id, BookModel body);
    Uni<Booking.BookingResponse> delete(String uuid, String id);

}
