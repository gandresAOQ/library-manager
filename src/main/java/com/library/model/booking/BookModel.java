package com.library.model.booking;

import com.booking.grpc.Booking;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookModel {

    private String id;
    private String name;
    private String author;
    private Double price;
    private Booking.BookLanguage language;
    private int pages;
    private Booking.BookFormat format;

    public static BookModel toBookModel(JsonObject body) {
        System.out.println(body.toString());
        return BookModel
                .builder()
                .id(body.getString("id"))
                .name(body.getString("name"))
                .author(body.getString("author"))
                .price(body.getDouble("price"))
                .language(Booking.BookLanguage.valueOf(body.getString("language")))
                .pages(body.getInteger("pages"))
                .format(Booking.BookFormat.valueOf(body.getString("format")))
                .build();
    }

}
