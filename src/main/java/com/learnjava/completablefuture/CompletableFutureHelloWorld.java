/*
 * Copyright (c) Flooid Limited 2024. All rights reserved.
 * This source code is confidential to and the copyright of Flooid Limited ("Flooid"), and must not be
 * (i) copied, shared, reproduced in whole or in part; or
 * (ii) used for any purpose other than the purpose for which it has expressly been provided by Flooid under the terms of a license agreement; or
 * (iii) given or communicated to any third party without the prior written consent of Flooid.
 * Flooid at all times reserves the right to modify the delivery and capabilities of its products and/or services.
 * "Flooid", "FlooidCore", "FlooidCommerce", "Flooid Hub", "PCMS", "Vision", "VISION Commerce Suite", "VISION OnDemand", "VISION eCommerce",
 * "VISION Engaged", "DATAFIT", "PCMS DATAFIT" and "BeanStore" are registered trademarks of Flooid.
 * All other brands and logos (that are not registered and/or unregistered trademarks of Flooid) are registered and/or
 * unregistered trademarks of their respective holders and should be treated as such.
 */
package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private final HelloWorldService hws;



    public CompletableFutureHelloWorld(HelloWorldService hws) {
        this.hws = hws;
    }

    public static void main(String[] args) {
        HelloWorldService hws = new HelloWorldService();
        CompletableFuture.supplyAsync(() -> hws.helloWorld())
                .thenApply((result) -> result.toUpperCase())
                .thenAccept((result) -> log(result));
//                .join();
        log("done");
        delay(2000);
    }

    public CompletableFuture<String> helloworld() {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloworldWithSize() {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
                .thenApply(s -> s + s.length());
    }

    public CompletableFuture<String> helloworldMultipleAsyncCalls(){
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);

        return hello.thenCombine(world, (a, b) -> a + b)
                .thenApply(String::toUpperCase);
    }

    public String helloworld3AsyncCalls(){
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> " hi");

        return hello.thenCombine(world, (a, b) -> a + b)
                .thenCombine(hi, (ab, c) -> ab + c)
                .thenApply(String::toUpperCase)
                .join();
    }

    public CompletableFuture<String> helloworldComposeAsyncCalls(){
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        //worldfuture waits for hello finished
        return hello.thenCompose(hws::worldFuture)
                .thenApply(String::toUpperCase);
    }
}