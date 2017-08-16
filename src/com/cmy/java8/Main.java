package com.cmy.java8;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by cmy on 2017/5/27
 */
public class Main {

    public static void main(String[] args) {
//        new Thread(() -> {
//            System.out.println("lambda practise");
//            System.out.println("Ha ha");
//        }).start();

        List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");

        List<String> features2 = Arrays.asList("Lambdas", "Default Method2", "Stream API3", "Date and Time API4");

        //features.forEach(User.user::test);
        //filter(features, (s -> s.length() > 10));

        //features.stream().map((s -> s + "test")).forEach(System.out::println);

        System.out.println(features.stream().reduce((a, b) -> a + b).get());

        String str = features.stream().filter(s -> s.length() > 1).map(String::toUpperCase).collect(Collectors.joining(","));
        System.out.println(str);

        Stream.concat(features.stream(), features2.stream()).distinct().forEach(System.out::println);
    }

    public static void filter(List<String> names, Predicate<String> condition) {
        names.stream().filter(condition).forEach(System.out::println);
    }
}
