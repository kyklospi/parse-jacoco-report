package com.bonial.api.destinations.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by atika.rachmawati on 13.09.18.
 * <p>
 * Total test coverage data as sum of collected coverage from jacoco
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TestTotalCoverageCounter {
    private float instruction;
    private float branch;
    private float line;
    private float complexity;
    private float method;
}
