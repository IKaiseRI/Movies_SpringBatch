package com.batchExample.Movies.validation;

import com.batchExample.Movies.entity.movie.characteristics.Language;
import com.batchExample.Movies.entity.movie.characteristics.Status;
import com.batchExample.Movies.utils.EntityUtils;

import java.time.LocalDate;

public class CustomValidation {

    public static boolean isValidLanguage(String originalLanguage) {
        try {
            Language.valueOf(originalLanguage);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidDate(LocalDate localDate) {
        return !localDate.isAfter(LocalDate.now());
    }

    public static boolean isValidStatus(String status) {
        try {
            Status.fromString(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String isValidMovie(String originalLanguage, String localDate, String status) {
        String errorMessage = null;
        if (!isValidStatus(status)) {
            errorMessage = status;
            System.out.println(errorMessage);
        } else if (!isValidLanguage(originalLanguage)) {
            errorMessage = originalLanguage;
        } else if (!isValidDate(EntityUtils.convertToMySQLDate(localDate))) {
            errorMessage = localDate;
        }
        return errorMessage;
    }
}
