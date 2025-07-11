package com.labs.wishlistservice.repositories;

import com.labs.wishlistservice.domain.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    @Query("{ 'tagsCategory': { $regex: ?0, $options: 'i' } }")
    List<Wishlist> findByTagsCategory(String texto);

}
