package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    HelloWorldService hws = new HelloWorldService();
    CompletableFutureHelloWorld cfhw = new CompletableFutureHelloWorld(hws);

    @Test
    void helloworld() {
        //given

        //when
        CompletableFuture<String> future = cfhw.helloworld();

        //then
        future.thenAccept((result) -> assertEquals("HELLO WORLD", result))
                .join();
    }

    @Test
    void helloworldWithSize() {
        //given

        //when
        CompletableFuture<String> future = cfhw.helloworldWithSize();

        //then
        future.thenAccept((result) -> assertEquals("HELLO WORLD11", result))
                .join();
    }

    @Test
    void helloworldMultipleAsyncCalls() {

        startTimer();
        CompletableFuture<String> future = cfhw.helloworldMultipleAsyncCalls();

        future.thenAccept((result) -> assertEquals("hello world!", result)).join();
        timeTaken();
    }
}