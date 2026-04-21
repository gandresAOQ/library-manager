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
        System.out.println(body.toString());
        return BookModel
                .builder()
                .id(body.getString("id"))
                .name(body.getString("name"))
                .author(body.getString("author"))
                .price(body.getString("price"))
                .language(body.getString("language"))
                .pages(body.getString("pages"))
                .format(body.getString("format"))
                .build();
    }

}
