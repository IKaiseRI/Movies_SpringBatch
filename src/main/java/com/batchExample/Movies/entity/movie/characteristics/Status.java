package com.batchExample.Movies.entity.movie.characteristics;

import java.util.HashMap;
import java.util.Map;

public enum Status {
    RELEASED,
    POST_PRODUCTION,
    IN_PRODUCTION,
    PLANNED;
    private static final Map<String, Status> stringToEnum = new HashMap<>();

    static {
        stringToEnum.put("Released", RELEASED);
        stringToEnum.put("Post Production", POST_PRODUCTION);
        stringToEnum.put("In Production", IN_PRODUCTION);
        stringToEnum.put("Planned", PLANNED);
    }

    public static Status fromString(String value) {
        return stringToEnum.get(value);
    }
}
