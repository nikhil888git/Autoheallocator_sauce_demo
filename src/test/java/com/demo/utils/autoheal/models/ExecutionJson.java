package com.demo.utils.autoheal.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionJson {
    public String testCaseId;
    public String step;
    public LocatorResolution locator_resolution;
    public String status;

    public static class LocatorResolution {
        public String original;
        public String resolved;
        public String strategy;
        public Confidence confidence;
    }

    public static class Confidence {
        public Double ai_confidence_score = 0.0;
        public Double heur_confidence_score = 0.0;
        public String ai_skipped_reason;
    }
}
