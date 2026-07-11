package com.netflix.contentservice.model;

public enum MaturityRating {

    G("General Audiences"),
    PG("Parental Guidance Suggested"),
    PG_13("Parents Strongly Cautioned"),
    R("Restricted"),
    NC_17("Adults Only"),
    TV_Y("All Children"),
    TV_Y7("Older Children"),
    TV_G("General Audience"),
    TV_PG("Parental Guidance"),
    TV_14("Parents Strongly Cautioned"),
    TV_MA("Mature Audience");

    private final String description;

    MaturityRating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
