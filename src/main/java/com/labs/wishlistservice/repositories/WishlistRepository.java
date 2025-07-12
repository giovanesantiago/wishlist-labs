package com.labs.wishlistservice.repositories;

import com.labs.wishlistservice.domain.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    Long countByCustomerId(String customerId);

    List<Wishlist> findByCustomerId(String customerId);

    Boolean existsByCustomerId(String customerId);

    Boolean existsByCustomerIdAndProductId(String customerId, String productId);

    @Query(value = "{ 'tagsCategory': { $regex: ?0, $options: 'i' } }", exists = true)
    Boolean existsByTagsCategory(String tag);

    @Query("{ 'tagsCategory': { $regex: ?0, $options: 'i' } }")
    List<Wishlist> findByTagsCategory(String texto);
}
