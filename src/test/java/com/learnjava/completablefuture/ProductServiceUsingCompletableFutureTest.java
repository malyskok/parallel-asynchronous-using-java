package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    private ProductInfoService productInfoService = new ProductInfoService();
    private ReviewService reviewService = new ReviewService();
    private InventoryService inventoryService = new InventoryService();

    ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(
            productInfoService, reviewService, inventoryService);

    @BeforeEach
    void beforeEach() {
        stopWatchReset();
        startTimer();
    }
    @AfterEach
    void afterEach(){
        timeTaken();
        stopWatchReset();
    }

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
        CompletableFuture<Product> actual = productService.retrieveProductDetailsAsServer("abc");

        assertNotNull(actual);
        actual.thenAccept((result) -> {
                    assertNotNull(result);
                    assertEquals("abc", result.getProductId());
                    assertNotNull(result.getProductInfo());
                    assertNotNull(result.getReview());
                })
                .join();
    }

    @Test
    void retrieveProductDetailsWithInventory() {

        //given
        String productId = "ABC123";

        //when
        Product product = productService.retrieveProductDetailsAsClientWithInventory(productId);
        System.out.println("product:  " + product);

        //then
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
        });

        assertNotNull(product.getReview());
    }
}