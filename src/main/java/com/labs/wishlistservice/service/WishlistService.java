package com.labs.wishlistservice.service;

import com.labs.wishlistservice.domain.Wishlist;
import com.labs.wishlistservice.exceptions.WishlistLimitExceededException;
import com.labs.wishlistservice.exceptions.WishlistNotFoundException;
import com.labs.wishlistservice.repositories.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class WishlistService {

    private WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    private static final int WISHLIST_LIMIT_CUSTUMER = 20;

    private Boolean wishlistLimitExceeded(String customerId) {
        return wishlistRepository.countByCustomerId(customerId) >= WISHLIST_LIMIT_CUSTUMER;
    }
    private String wrapRegex(String value) {
        return ".*" + Pattern.quote(value)  + ".*";
    }


    public Wishlist save(Wishlist wishlist) {
        if (wishlistLimitExceeded(wishlist.getCustomerId()))
            throw new WishlistLimitExceededException(String.format("Customer has reached the maximum number of wishlists (%s).", WISHLIST_LIMIT_CUSTUMER));

        return wishlistRepository.insert(wishlist);
    }

    public void delete(String id) {
        if(wishlistRepository.existsById(id)) {
            wishlistRepository.deleteById(id);
        } else {
            throw new WishlistNotFoundException("Wishlist with id " + id + " does not exist.");
        }
    }

    public List<Wishlist> findAllCustomer(String customerId) {
        if(wishlistRepository.existsByCustomerId(customerId)){
            return wishlistRepository.findByCustomerId(customerId);
        } else {
            throw new WishlistNotFoundException("No wishlists found for customer with id " + customerId);
        }
    }

    public Boolean existsByCustomerIdAndProductId(String customerId, String productId) {
        return wishlistRepository.existsByCustomerIdAndProductId(customerId, productId);
    }

    public List<Wishlist> findByTagsCategory(String tag) {
        if (wishlistRepository.existsByTagsCategory(wrapRegex(tag))){
            return wishlistRepository.findByTagsCategory(wrapRegex(tag));
        } else {
            throw new WishlistNotFoundException("No wishlists found with tag category: " + tag);
        }
    }
}
