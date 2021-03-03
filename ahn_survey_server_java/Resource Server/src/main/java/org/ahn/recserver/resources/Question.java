/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.resources;

/**
 *
 * @author rgustafs
 */
public class Question {

    private final String low_text;
    private final String high_text;
    private final int minimum;
    private final int maximum;
    private final int interval;

    /**
     * Constructs a new question
     *
     * @param low_text
     * @param high_text
     * @param minimum
     * @param maximum
     * @param interval
     */
    public Question(String low_text, String high_text, int minimum, int maximum, int interval) {
        this.low_text = low_text;
        this.high_text = high_text;
        this.minimum = minimum;
        this.maximum = maximum;
        this.interval = interval;
    }

    public String getLow_text() {
        return low_text;
    }

    public String getHigh_text() {
        return high_text;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getInterval() {
        return interval;
    }

}
