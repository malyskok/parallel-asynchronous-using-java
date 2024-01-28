package com.learnjava.completablefuture;

import com.learnjava.domain.*;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;
    private final InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService,
                                                InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetailsAsClient(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture = CompletableFuture.supplyAsync(() ->
                productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() ->
                reviewService.retrieveReviews(productId));

        Product product = productInfoCompletableFuture.thenCombine(reviewCompletableFuture, (pInfo, review) ->
                        new Product(productId, pInfo, review))
                .join();

        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
        return product;
    }

    public Product retrieveProductDetailsAsClientWithInventory(String productId) {

        CompletableFuture<ProductInfo> productInfoCompletableFuture = CompletableFuture.supplyAsync(() ->
                        productInfoService.retrieveProductInfo(productId))
                .thenApply((productInfo -> {
                    productInfo.setProductOptions(
                            updateInventoryToProductOptionAsync(productInfo));
                    return productInfo;
                }));
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() ->
                reviewService.retrieveReviews(productId));

        Product product = productInfoCompletableFuture.thenCombine(reviewCompletableFuture, (pInfo, review) ->
                        new Product(productId, pInfo, review))
                .join();
        return product;
    }

    private List<ProductOption> updateInventoryToProductOption(ProductInfo productInfo) {
        List<ProductOption> productOptionList = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    Inventory inventory = inventoryService.retrieveInventory(productOption);
                    productOption.setInventory(inventory);
                    return productOption;
                })
                .collect(Collectors.toList());

        return productOptionList;
    }

    private List<ProductOption> updateInventoryToProductOptionAsync(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> productOptionList = productInfo.getProductOptions()
                .stream()
                .map(   productOption ->
                    CompletableFuture.supplyAsync(() ->
                            inventoryService.retrieveInventory(productOption)
                    ).thenApply((inventory -> {
                        productOption.setInventory(inventory);
                        return productOption;
                    }))).collect(Collectors.toList());

        return productOptionList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public CompletableFuture<Product> retrieveProductDetailsAsServer(String productId) {

        CompletableFuture<ProductInfo> productInfoCompletableFuture = CompletableFuture.supplyAsync(() ->
                productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() ->
                reviewService.retrieveReviews(productId));

        return productInfoCompletableFuture.thenCombine(reviewCompletableFuture, (pInfo, review) ->
                new Product(productId, pInfo, review));
    }

    public static void main(String[] args) {

//        ProductInfoService productInfoService = new ProductInfoService();
//        ReviewService reviewService = new ReviewService();
//        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, null);
//        String productId = "ABC123";
//        Product product = productService.retrieveProductDetailsAsClient(productId);
//        log("Product is " + product);

    }
}
