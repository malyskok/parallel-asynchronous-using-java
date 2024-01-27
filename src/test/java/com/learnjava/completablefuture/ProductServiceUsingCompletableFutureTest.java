package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    private ProductInfoService productInfoService = new ProductInfoService();
    private ReviewService reviewService = new ReviewService();

    ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);

    @Test
    void retrieveProductDetails() {
        Product actual = productService.retrieveProductDetailsAsClient("abc");

        assertNotNull(actual);
        assertEquals("abc", actual.getProductId());
        assertNotNull(actual.getProductInfo());
        assertNotNull(actual.getReview());
    }

    @Test
    void retrieveProductDetailsAsServer() {
        startTimer();
        CompletableFuture<Product> actual = productService.retrieveProductDetailsAsServer("abc");

        assertNotNull(actual);
        actual.thenAccept((result) -> {
                    assertNotNull(result);
                    assertEquals("abc", result.getProductId());
                    assertNotNull(result.getProductInfo());
                    assertNotNull(result.getReview());
                })
                .join();
        timeTaken();
    }
}