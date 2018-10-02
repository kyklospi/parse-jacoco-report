/**
 * Test coverage data read from jacoco report
 */
class TestCoverageCounter {
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
    
    TestCoverageCounter(String group, String packageName, String className, 
                               int instructionMissed, int instructionCovered, 
                               int branchMissed, int branchCovered, 
                               int lineMissed, int lineCovered, 
                               int complexityMissed, int complexityCovered, 
                               int methodMissed, int methodCovered) {
        this.group = group;
        this.packageName = packageName;
        this.className = className;
        this.instructionMissed = instructionMissed;
        this.instructionCovered = instructionCovered;
        this.branchMissed = branchMissed;
        this.branchCovered = branchCovered;
        this.lineMissed = lineMissed;
        this.lineCovered = lineCovered;
        this.complexityMissed = complexityMissed;
        this.complexityCovered = complexityCovered;
        this.methodMissed = methodMissed;
        this.methodCovered = methodCovered;
    }
    
    String getGroup() {
        return group;
    }

    String getPackageName() {
        return packageName;
    }

    String getClassName() {
        return className;
    }

    int getInstructionMissed() {
        return instructionMissed;
    }

    int getInstructionCovered() {
        return instructionCovered;
    }

    int getBranchMissed() {
        return branchMissed;
    }

    int getBranchCovered() {
        return branchCovered;
    }

    int getLineMissed() {
        return lineMissed;
    }

    int getLineCovered() {
        return lineCovered;
    }

    int getComplexityMissed() {
        return complexityMissed;
    }

    int getComplexityCovered() {
        return complexityCovered;
    }

    int getMethodMissed() {
        return methodMissed;
    }

    int getMethodCovered() {
        return methodCovered;
    }
}
