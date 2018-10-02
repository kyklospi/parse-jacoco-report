package com.bonial.api.destinations.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by atika.rachmawati on 13.09.18.
 * <p>
 * Test coverage data read from jacoco report
 */
@AllArgsConstructor
@Getter
public class TestCoverageCounter {
    private String group;
    private String packageName;
    private String className;
    private int instructionMissed;
    private int instructionCovered;
    private int branchMissed;
    private int branchCovered;
    private int lineMissed;
    private int lineCovered;
    private int complexityMissed;
    private int complexityCovered;
    private int methodMissed;
    private int methodCovered;
}
