package com.labs.wishlistservice.controller;

import com.labs.wishlistservice.domain.Wishlist;
import com.labs.wishlistservice.service.WishlistService;
import dtos.WishlistDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody WishlistDTO wishlist) {
        wishlistService.save(new Wishlist(wishlist));
        return ResponseEntity.status(201).body(Map.of("message", "Wishlist save sucess"));
    }

    @DeleteMapping()
    protected  ResponseEntity<Map<String, Object>> delete(@Valid @RequestBody WishlistDTO wishlist) {
        wishlistService.delete(Wishlist.generateCompositeId(wishlist.customerId(), wishlist.productId()));
        return ResponseEntity.ok(Map.of("message", "Wishlist deleted successfully"));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Map<String, Object>> findAllCustomer(@PathVariable String customerId) {
        List<Wishlist> wishlists = wishlistService.findAllCustomer(customerId);
        return ResponseEntity.ok(
                Map.of("wishlists", wishlists.stream().map(WishlistDTO::from).toList())
        );
    }

    @GetMapping("/tags/{tag}")
    public ResponseEntity<Map<String, Object>> findByTagsCategory(@PathVariable String tag) {
        return ResponseEntity.ok(Map.of("wishlists", wishlistService.findByTagsCategory(tag)));
    }

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> existsByCustomerIdAndProductId(@RequestParam String customerId, @RequestParam String productId) {
        boolean exists = wishlistService.existsByCustomerIdAndProductId(customerId, productId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

}
