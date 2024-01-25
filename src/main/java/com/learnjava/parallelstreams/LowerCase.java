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
package com.learnjava.parallelstreams;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.*;

public class LowerCase {

    public static void main(String[] args) {
        List<String> namesList = List.of("Bob", "Jamie", "Jill", "Rick");
        System.out.println(namesList);
        startTimer();
        LowerCase lowerCase = new LowerCase();
        lowerCase.convertStringsToLowerCase(namesList, true);
        timeTaken();
    }

    public List<String> convertStringsToLowerCase(List<String> namesList, boolean parallel) {
        Stream<String> stream = namesList.stream();
        if (parallel) {
            stream.parallel();
        }
//        stream.sequential();
        return stream
                .map(this::transform)
                .collect(Collectors.toList());
    }

    private String transform(String s) {
        delay(500);
        return s.toLowerCase();
    }
}