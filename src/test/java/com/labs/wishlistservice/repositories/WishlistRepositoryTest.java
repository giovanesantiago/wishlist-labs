package com.labs.wishlistservice.repositories;

import com.labs.wishlistservice.domain.Wishlist;
import org.springframework.dao.DuplicateKeyException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    private Wishlist wishlist;

    @BeforeEach
    void setUp() {
        String userId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        wishlist = new Wishlist(userId, productId, "eletronicos cozinha");
        wishlist = wishlistRepository.insert(wishlist);
        Assertions.assertThrows(DuplicateKeyException.class, () -> wishlistRepository.insert(wishlist));
    }

    @AfterEach
    void tearDown() {
        wishlistRepository.deleteById(wishlist.getId());
    }

    @Test
    @Order(1)
    void testSaveAndFindById() {
        Optional<Wishlist> found = wishlistRepository.findById(wishlist.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).contains(wishlist.getCustomerId());
        assertThat(found.get().getId()).contains(wishlist.getProductId());
    }

    @Test
    @Order(3)
    void testDelete() {
        wishlistRepository.deleteById(wishlist.getId());
        Optional<Wishlist> found = wishlistRepository.findById(wishlist.getId());
        assertThat(found).isNotPresent();
    }

    @Test
    @Order(2)
    void findByTagsCategory() {
        List<Wishlist> found = wishlistRepository.findByTagsCategory("eletronicos");
        assertFalse(found.isEmpty());
        found = wishlistRepository.findByTagsCategory("Cozinha");
        assertFalse(found.isEmpty());
    }
}
