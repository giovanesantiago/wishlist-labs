package com.labs.wishlistservice.service;


import com.labs.wishlistservice.domain.Wishlist;
import com.labs.wishlistservice.exceptions.WishlistLimitExceededException;
import com.labs.wishlistservice.exceptions.WishlistNotFoundException;
import com.labs.wishlistservice.repositories.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    private WishlistRepository wishlistRepository;
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        wishlistRepository = mock(WishlistRepository.class);
        wishlistService = new WishlistService(wishlistRepository);
    }

    @Test
    void testSaveWishlist_Success() {
        Wishlist wishlist = new Wishlist("customer1", "product1", "tech");

        when(wishlistRepository.countByCustomerId("customer1")).thenReturn(0L);
        when(wishlistRepository.insert(wishlist)).thenReturn(wishlist);

        Wishlist saved = wishlistService.save(wishlist);

        assertNotNull(saved);
        verify(wishlistRepository).insert(wishlist);
    }

    @Test
    void testSaveWishlist_ThrowsException_WhenLimitExceeded() {
        Wishlist wishlist = new Wishlist("customer1", "product1", "tech");

        when(wishlistRepository.countByCustomerId("customer1")).thenReturn(20L);

        assertThrows(WishlistLimitExceededException.class, () -> wishlistService.save(wishlist));
        verify(wishlistRepository, never()).insert((Wishlist) any());
    }

    @Test
    void testDeleteWishlist_Success() {
        String id = "wishlist123";
        when(wishlistRepository.existsById(id)).thenReturn(true);

        wishlistService.delete(id);

        verify(wishlistRepository).deleteById(id);
    }

    @Test
    void testDeleteWishlist_ThrowsException_WhenNotExists() {
        String id = "wishlist123";
        when(wishlistRepository.existsById(id)).thenReturn(false);

        assertThrows(WishlistNotFoundException.class, () -> wishlistService.delete(id));
        verify(wishlistRepository, never()).deleteById(id);
    }

    @Test
    void testFindAllCustomer_Success() {
        String customerId = "customer1";
        List<Wishlist> mockList = List.of(new Wishlist(customerId, "product1", "tech"));

        when(wishlistRepository.existsByCustomerId(customerId)).thenReturn(true);
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(mockList);

        List<Wishlist> result = wishlistService.findAllCustomer(customerId);

        assertEquals(1, result.size());
    }

    @Test
    void testFindAllCustomer_ThrowsException_WhenNotFound() {
        String customerId = "customer1";

        when(wishlistRepository.existsByCustomerId(customerId)).thenReturn(false);

        assertThrows(WishlistNotFoundException.class, () -> wishlistService.findAllCustomer(customerId));
    }

    @Test
    void testExistsByCustomerIdAndProductId() {
        String customerId = "customer1";
        String productId = "product1";

        when(wishlistRepository.existsByCustomerIdAndProductId(customerId, productId)).thenReturn(true);

        assertTrue(wishlistService.existsByCustomerIdAndProductId(customerId, productId));
    }

    @Test
    void testFindByTagsCategory() {
        String tag = "tech";
        List<Wishlist> mockList = List.of(new Wishlist("customer1", "product1", tag));

        when(wishlistRepository.findByTagsCategory(tag)).thenReturn(mockList);
        when(wishlistRepository.existsByTagsCategory(tag)).thenReturn(true);

        List<Wishlist> result = wishlistService.findByTagsCategory(tag);


        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testFindByTagsCategory_ThrowsException_WhenNotFound() {
        String tag = "tech";

        when(wishlistRepository.existsByTagsCategory(tag)).thenReturn(false);

        assertThrows(WishlistNotFoundException.class, () -> wishlistService.findByTagsCategory(tag));
    }
}
