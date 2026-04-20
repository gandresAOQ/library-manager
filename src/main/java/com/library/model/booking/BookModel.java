package com.library.model.booking;

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
    private String price;
    private String language;
    private String pages;
    private String format;

    public static BookModel toBookModel(JsonObject body) {
        return BookModel
                .builder()
                .id(body.getString("id"))
                .name(body.getString("name"))
                .name(body.getString("author"))
                .name(body.getString("price"))
                .name(body.getString("language"))
                .name(body.getString("pages"))
                .name(body.getString("format"))
                .build();
    }

}
