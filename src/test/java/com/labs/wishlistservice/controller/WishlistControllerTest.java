package com.labs.wishlistservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labs.wishlistservice.domain.Wishlist;
import com.labs.wishlistservice.exceptions.WishlistLimitExceededException;
import com.labs.wishlistservice.exceptions.WishlistNotFoundException;
import com.labs.wishlistservice.infra.GlobalExceptionHandler;
import com.labs.wishlistservice.service.WishlistService;
import com.labs.wishlistservice.dtos.WishlistDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishlistController.class)
@Import(GlobalExceptionHandler.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishlistService wishlistService;

    ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class TestConfig {
        @Bean
        public WishlistService wishlistService() {
            return Mockito.mock(WishlistService.class);
        }
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(wishlistService);
    }

    @Test
    void testSaveWishlist_Success() throws Exception {
        WishlistDTO dto = new WishlistDTO("cust1", "prod1", "");

        mockMvc.perform(post("/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Wishlist save sucess"));
    }

    @Test
    void testSaveWishlist_LimitExceeded() throws Exception {
        WishlistDTO dto = new WishlistDTO("cust-limit", "prod1", "");

        doThrow(new WishlistLimitExceededException("Limit exceeded"))
                .when(wishlistService).save(any(Wishlist.class));

        mockMvc.perform(post("/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Limit exceeded"));
    }

    @Test
    void testSaveWishlist_DuplicateKey() throws Exception {
        WishlistDTO dto = new WishlistDTO("cust1", "prod-duplicate", "");

        doThrow(new DuplicateKeyException("duplicate"))
                .when(wishlistService).save(any(Wishlist.class));

        mockMvc.perform(post("/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("A record with the same key already exists."));
    }

    @Test
    void testSaveWishlist_ValidationFailed() throws Exception {
        WishlistDTO invalidDto = new WishlistDTO("", "", "");

        mockMvc.perform(post("/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.customerId").value("Customer ID is required"))
                .andExpect(jsonPath("$.errors.productId").value("Product ID is required"));
    }

    @Test
    void testDelete_Success() throws Exception {
        WishlistDTO dto = new WishlistDTO("cust1", "prod1", "");

        mockMvc.perform(delete("/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Wishlist deleted successfully"));

        verify(wishlistService).delete(anyString());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        WishlistDTO dto = new WishlistDTO("cust1", "prod-notfound", "");

        doThrow(new WishlistNotFoundException("Not found"))
                .when(wishlistService).delete(anyString());

        mockMvc.perform(delete("/wishlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not found"));
    }

    @Test
    void testFindAllCustomer_Success() throws Exception {
        when(wishlistService.findAllCustomer("cust1"))
                .thenReturn(List.of(new Wishlist("cust1", "prod1", "cat")));

        mockMvc.perform(get("/wishlist/customer/cust1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wishlists").isArray());
    }

    @Test
    void testFindAllCustomer_NotFound() throws Exception {
        when(wishlistService.findAllCustomer("cust1"))
                .thenThrow(new WishlistNotFoundException("No wishlists found"));

        mockMvc.perform(get("/wishlist/customer/cust1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No wishlists found"));
    }

    @Test
    void testExistsByCustomerIdAndProductId() throws Exception {
        when(wishlistService.existsByCustomerIdAndProductId("cust1", "prod1")).thenReturn(true);

        mockMvc.perform(get("/wishlist/exists")
                        .param("customerId", "cust1")
                        .param("productId", "prod1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    @Test
    void testFindByTagsCategory() throws Exception {
        when(wishlistService.findByTagsCategory("tech"))
                .thenReturn(List.of(new Wishlist("cust1", "prod1", "tech")));

        mockMvc.perform(get("/wishlist/tags/tech"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wishlists").isArray());
    }
}
