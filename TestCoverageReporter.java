package com.bonial.api.destinations.test;

import com.google.gson.Gson;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by atika.rachmawati on 13.09.18.
 * <p>
 * Class to report unit test coverage to datadog
 */
@Service
public class TestCoverageReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCoverageReporter.class);

    public static void main(String[] args) {
        File inputCoverage = new File("destinations-api-service/target/site/jacoco/jacoco.csv");
        List<TestCoverageCounter> coverageByClass = new ArrayList<>();
        TestTotalCoverageCounter totalCoveragePercentage = new TestTotalCoverageCounter();
        try {
            coverageByClass = readCoverageFromCsv(inputCoverage);
        } catch (IOException exception) {
            LOGGER.info("Unable to read test coverage from csv file, report no coverage" + exception);
        }
        if (!coverageByClass.isEmpty()) {
            totalCoveragePercentage = countTotalCoveragePercentage(coverageByClass);
        }
        reportCoverageToDatadog(totalCoveragePercentage);
    }

    private static void reportCoverageToDatadog(TestTotalCoverageCounter coveragePercent) {
        List<DatadogMetric> coverageMetrics = new ArrayList<>();
        String metricPrefix = "unit.test.coverage.";
        String branchName = System.getenv("BITBUCKET_BRANCH");
        if (branchName == null || branchName.isEmpty()) {
            branchName = "UNKNOWN";
        }
        String[] tag = {"branch", branchName};

        DatadogMetric instruction = DatadogMetric.builder().type("GAUGE").tag(tag)
                .name(metricPrefix + "instruction").value(String.valueOf(coveragePercent.getInstruction())).build();
        coverageMetrics.add(instruction);
        DatadogMetric branch = DatadogMetric.builder().type("GAUGE").tag(tag)
                .name(metricPrefix + "branch").value(String.valueOf(coveragePercent.getBranch())).build();
        coverageMetrics.add(branch);
        DatadogMetric line = DatadogMetric.builder().type("GAUGE").tag(tag)
                .name(metricPrefix + "line").value(String.valueOf(coveragePercent.getLine())).build();
        coverageMetrics.add(line);
        DatadogMetric complexity = DatadogMetric.builder().type("GAUGE").tag(tag)
                .name(metricPrefix + "complexity").value(String.valueOf(coveragePercent.getComplexity())).build();
        coverageMetrics.add(complexity);
        DatadogMetric method = DatadogMetric.builder().type("GAUGE").tag(tag)
                .name(metricPrefix + "method").value(String.valueOf(coveragePercent.getMethod())).build();
        coverageMetrics.add(method);

        sendMetricToDatadog(coverageMetrics);
    }

    private static void sendMetricToDatadog(List<DatadogMetric> coverageMetrics) {
        Gson gson = new Gson();
        StringEntity metricsString = null;
        try {
            metricsString = new StringEntity(gson.toJson(coverageMetrics));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Can't parse metric: ", e.getMessage());
        }

        if (metricsString != null) {
            HttpPost postReq = new HttpPost("https://xtracer.bonial.global/v1/api/messages");
            postReq.addHeader("Content-Type", "application/json");
            postReq.setEntity(metricsString);
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                httpClient.execute(postReq);
                httpClient.close();
            } catch (Exception e) {
                LOGGER.warn("Can't connect to datadog: ", e.getMessage());
            }
        }
    }

    private static TestTotalCoverageCounter countTotalCoveragePercentage(List<TestCoverageCounter> coverageByClass) {
        float totalInstructionCoverage = 0, totalInstructionMiss = 0;
        float totalBranchCoverage = 0, totalBranchMiss = 0;
        float totalLineCoverage = 0, totalLineMiss = 0;
        float totalComplexityCoverage = 0, totalComplexityMiss = 0;
        float totalMethodCoverage = 0, totalMethodMiss = 0;

        for (TestCoverageCounter coverage : coverageByClass) {
            totalBranchCoverage += coverage.getBranchCovered();
            totalBranchMiss += coverage.getBranchMissed();
            totalInstructionCoverage += coverage.getInstructionCovered();
            totalInstructionMiss += coverage.getInstructionMissed();
            totalLineCoverage += coverage.getLineCovered();
            totalLineMiss += coverage.getLineMissed();
            totalComplexityCoverage += coverage.getComplexityCovered();
            totalComplexityMiss += coverage.getComplexityMissed();
            totalMethodCoverage += coverage.getMethodCovered();
            totalMethodMiss += coverage.getMethodMissed();
        }
        float totalInstructionCoveragePercent = totalInstructionCoverage / (totalInstructionCoverage + totalInstructionMiss) * 100;
        float totalBranchCoveragePercent = totalBranchCoverage / (totalBranchCoverage + totalBranchMiss) * 100;
        float totalLineCoveragePercent = totalLineCoverage / (totalLineCoverage + totalLineMiss) * 100;
        float totalComplexityCoveragePercent = totalComplexityCoverage / (totalComplexityCoverage + totalComplexityMiss) * 100;
        float totalMethodCoveragePercent = totalMethodCoverage / (totalMethodCoverage + totalMethodMiss) * 100;

        return new TestTotalCoverageCounter(
                totalInstructionCoveragePercent,
                totalBranchCoveragePercent,
                totalLineCoveragePercent,
                totalComplexityCoveragePercent,
                totalMethodCoveragePercent
        );
    }

    private static List<TestCoverageCounter> readCoverageFromCsv(File csvFile) throws IOException {
        Pattern pattern = Pattern.compile(",");
        List<TestCoverageCounter> coverageByClass;
        try (BufferedReader in = new BufferedReader(new FileReader(csvFile))) {
            coverageByClass = in.lines().skip(1).map(line -> {
                String[] x = pattern.split(line);
                return new TestCoverageCounter(
                        x[0], x[1], x[2],
                        Short.parseShort(x[3]), Short.parseShort(x[4]),
                        Short.parseShort(x[5]), Short.parseShort(x[6]),
                        Short.parseShort(x[7]), Short.parseShort(x[8]),
                        Short.parseShort(x[9]), Short.parseShort(x[10]),
                        Short.parseShort(x[11]), Short.parseShort(x[12]));
            })
                    .collect(Collectors.toList());
        }
        return coverageByClass;
    }
}
