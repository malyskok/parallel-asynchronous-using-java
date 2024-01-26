package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

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
}