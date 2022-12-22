package com.coderdojo.zen.award;

public class AwardNotFoundException extends RuntimeException {

    AwardNotFoundException(Long id) {
        super("Could not find award " + id);
    }
}
