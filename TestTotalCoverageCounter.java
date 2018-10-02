/**
 * Total test coverage data as sum of collected coverage from jacoco
 */
class TestTotalCoverageCounter {
    private float instruction;
    private float branch;
    private float line;
    private float complexity;
    private float method;
    
    TestTotalCoverageCounter() {
    }

    TestTotalCoverageCounter(float instruction, float branch, float line, float complexity, float method) {
        this.instruction = instruction;
        this.branch = branch;
        this.line = line;
        this.complexity = complexity;
        this.method = method;
    }

    float getInstruction() {
        return instruction;
    }

    float getBranch() {
        return branch;
    }

    float getLine() {
        return line;
    }

    float getComplexity() {
        return complexity;
    }

    float getMethod() {
        return method;
    }
}
