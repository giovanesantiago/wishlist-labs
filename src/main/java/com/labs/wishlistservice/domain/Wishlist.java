package com.labs.wishlistservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "wishlist")
public class Wishlist {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String productId;
    private String tagsCategory;

    public Wishlist(String userId, String productId, String tagsCategory) {
        this.userId = userId;
        this.productId = productId;
        this.tagsCategory = tagsCategory;
        this.id = generateCompositeId(userId, productId);
    }

    public static String generateCompositeId(String userId, String productId) {
        return "user#" + userId + "-product#" + productId;
    }
}

