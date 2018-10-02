import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class to parse test coverage from jacoco csv report
 * and calculate the total percentage of coverage for different metrics
 */
public class TestCoverageReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCoverageReporter.class);

    public static void main(String[] args) {
        //the file path to jacoco csv
        File inputCoverage = new File("/.../jacoco/jacoco.csv");
        List<TestCoverageCounter> coverageByClass = new ArrayList<>();
        TestTotalCoverageCounter totalCoveragePercentage = new TestTotalCoverageCounter();
        try {
            coverageByClass = readCoverageFromCsv(inputCoverage);
        } catch (IOException exception) {
            LOGGER.info("Unable to read test coverage from csv file, return no coverage" + exception);
        }
        if (!coverageByClass.isEmpty()) {
            totalCoveragePercentage = countTotalCoveragePercentage(coverageByClass);
        }
        return totalCoveragePercentage;
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
            //skips the first line because it contains column names
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
