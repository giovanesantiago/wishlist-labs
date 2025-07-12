package com.labs.wishlistservice.domain;

import dtos.WishlistDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter @NoArgsConstructor
@Document(collection = "wishlist")
public class Wishlist {
    @Id
    private String id;
    @Indexed
    private String customerId;
    private String productId;
    private String tagsCategory;

    public Wishlist(String customerId, String productId, String tagsCategory) {
        this.customerId = customerId;
        this.productId = productId;
        this.tagsCategory = tagsCategory;
        this.id = generateCompositeId(customerId, productId);
    }

    public Wishlist(WishlistDTO wishlist) {
        this.customerId = wishlist.customerId();
        this.productId = wishlist.productId();
        this.tagsCategory = wishlist.tagsCategory();
        this.id = generateCompositeId(customerId, productId);
    }

    public static String generateCompositeId(String customerId, String productId) {
        return "customer#" + customerId + "-product#" + productId;
    }
}

