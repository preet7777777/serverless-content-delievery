package com.serverless.contentdelivery.shared.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * One shared ObjectMapper keeps payload parsing consistent across the control
 * and processing Lambdas.
 */
public final class JsonMapperFactory {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonMapperFactory() {
    }

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }
}
