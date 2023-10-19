package com.batchExample.Movies.utils;

import com.batchExample.Movies.entity.movie.characteristics.Language;
import com.batchExample.Movies.entity.movie.characteristics.Status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EntityUtils {

    public static LocalDate convertToMySQLDate(String input) {
        return LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public static Language convertToLanguage(String input) {
        return Language.valueOf(input);
    }

    public static Status convertToStatus(String input) {
        return Status.fromString(input);
    }

}
