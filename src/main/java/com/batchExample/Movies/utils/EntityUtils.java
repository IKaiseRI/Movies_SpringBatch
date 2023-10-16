package com.batchExample.Movies.utils;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class EntityUtils {

    @SneakyThrows
    public static LocalDate convertToMySQLDate(String inputDate) {
        DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");

        LocalDate localDate = LocalDate.parse(inputDate, inputDateFormat);

        return localDate;
    }

    public static void addUniqueElements(Set<String> targetSet, Set<String> sourceSet) {
        for (String element : sourceSet) {
            targetSet.add(element);
        }
    }

    public static void printer(Set<String> set) {
        System.out.println("The actual set start");
        set.forEach(System.out::println);
        System.out.println("The actual set end");
    }
}
