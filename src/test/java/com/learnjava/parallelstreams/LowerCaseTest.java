package com.learnjava.parallelstreams;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.learnjava.util.CommonUtil.*;
import static java.lang.Character.isLowerCase;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LowerCaseTest {

    @BeforeEach
    void beforeEach() {
        startTimer();
    }
    @AfterEach
    void afterEach(){
        timeTaken();
        stopWatchReset();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void convertStringsToLowerCase(boolean isParallel) {
        List<String> namesList = List.of("Bob", "Jamie", "Jill", "Rick");
        LowerCase lowerCase = new LowerCase();
        List<String> result = lowerCase.convertStringsToLowerCase(namesList, isParallel);
        result.forEach(s -> assertTrue(isLowerCase(s.charAt(0))));
    }
}