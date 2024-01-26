package com.learnjava.service;


import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;

public class CheckoutService {

    private PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkout(Cart cart) {

        startTimer();
        List<CartItem> priceValidationList = cart.getCartItemList()
//                .stream()
                .parallelStream()
                .map(cartItem -> {
                    boolean isPriceValid = priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceValid);
                    return cartItem;
                })
                .filter(CartItem::isExpired)
                .collect(toList());
        timeTaken();
        stopWatchReset();

        if (priceValidationList.size() > 0) {
            log("Checkout Error");
            return new CheckoutResponse(CheckoutStatus.FAILURE, priceValidationList);
        }

        double finalRate = calculateFinalPriceWithReduce(cart);

        return new CheckoutResponse(CheckoutStatus.SUCCESS, finalRate);
    }

    private double calculateFinalPriceWithCollect(Cart cart) {
        return cart.getCartItemList()
//                .stream()
                .parallelStream()
                .map(item -> item.getQuantity() * item.getRate())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private double calculateFinalPriceWithReduce(Cart cart) {
        return cart.getCartItemList()
//                .stream()
                .parallelStream()
                .map(item -> item.getQuantity() * item.getRate())
                .reduce(0.0, Double::sum);
    }
}
