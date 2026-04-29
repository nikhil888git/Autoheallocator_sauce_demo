package com.demo.utils.autoheal.models;

public class HealResult {
    public String original;
    public String healed;
    public String strategy; // "ai" or "heuristic"
    public Double aiConfidenceScore;
    public Double heurConfidenceScore;

    public HealResult() {}

    public HealResult(String original, String healed, String strategy, Double aiConfidenceScore, Double heurConfidenceScore) {
        this.original = original;
        this.healed = healed;
        this.strategy = strategy;
        this.aiConfidenceScore = aiConfidenceScore;
        this.heurConfidenceScore = heurConfidenceScore;
    }
}
