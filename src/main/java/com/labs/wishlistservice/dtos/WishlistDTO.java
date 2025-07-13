package com.labs.wishlistservice.dtos;

import com.labs.wishlistservice.domain.Wishlist;
import jakarta.validation.constraints.NotBlank;

public record WishlistDTO(@NotBlank(message = "Customer ID is required") String customerId,
                          @NotBlank(message = "Product ID is required") String productId,
                          String tagsCategory) {

    public static WishlistDTO from(Wishlist wishlist) {
        return new WishlistDTO(wishlist.getCustomerId(), wishlist.getProductId(), wishlist.getTagsCategory());
    }
}
