package com.demo.utils.autoheal.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.utils.autoheal.models.ExecutionJson;
import org.testng.Reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutoHealTraceAggregator {
    private static final List<ExecutionJson> traces = Collections.synchronizedList(new ArrayList<>());
    private static final String TRACE_FILE = "target/execution_evidence.json";

    public static void logTrace(ExecutionJson trace) {
        if (trace.testCaseId == null) {
            try {
                if (Reporter.getCurrentTestResult() != null) {
                    trace.testCaseId = Reporter.getCurrentTestResult().getTestContext().getName() + "_" + Reporter.getCurrentTestResult().getName();
                } else {
                    trace.testCaseId = "unknown-test-id";
                }
            } catch (Exception e) {
                trace.testCaseId = "autoheal-trace";
            }
        }
        traces.add(trace);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonLog = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(trace);
            AutoHealLogger.logExecution("Emitting ISO Trace Schema:\n" + jsonLog);
        } catch (Exception e) {
            AutoHealLogger.logError("[EXECUTION]", "Could not serialize json trace");
        }
    }

    public static void flushTraces() {
        AutoHealLogger.logExecution("Aggregating central trace points for execution evidence limit...");
        try {
            if (!traces.isEmpty()) {
                File reportFile = new File(TRACE_FILE);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, traces);
                AutoHealLogger.logExecution("Execution Evidence generated at " + reportFile.getAbsolutePath());
            }
        } catch (Exception e) {
            AutoHealLogger.logError("[EXECUTION]", "Failed generating execution evidence trace file.");
        }
    }
}
