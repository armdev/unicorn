package io.project.app.common.utils;

import java.util.UUID;

public class RandomUUID {

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
