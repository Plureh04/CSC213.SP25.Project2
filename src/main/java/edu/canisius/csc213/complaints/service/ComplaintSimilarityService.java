package edu.canisius.csc213.complaints.service;

import edu.canisius.csc213.complaints.model.Complaint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ComplaintSimilarityService {

    private final List<Complaint> complaints;

    public ComplaintSimilarityService(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<Complaint> findTop3Similar(Complaint target) {
        List<ComplaintWithScore> scored = new ArrayList<>();

        for (Complaint c : complaints) {
            if (c.getComplaintId() == target.getComplaintId()) continue;
            if (c.getEmbedding() == null || target.getEmbedding() == null) continue;

            double score = cosineSimilarity(target.getEmbedding(), c.getEmbedding());
            scored.add(new ComplaintWithScore(c, score));
        }

        return scored.stream()
                .sorted(Comparator.comparingDouble((ComplaintWithScore s) -> -s.score)) // descending
                .limit(3)
                .map(s -> s.complaint)
                .collect(Collectors.toList());
    }

    private double cosineSimilarity(double[] a, double[] b) {
        double dot = 0.0;
        double magA = 0.0;
        double magB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            magA += a[i] * a[i];
            magB += b[i] * b[i];
        }

        if (magA == 0 || magB == 0) return 0.0;

        return dot / (Math.sqrt(magA) * Math.sqrt(magB));
    }

    private static class ComplaintWithScore {
        Complaint complaint;
        double score;

        ComplaintWithScore(Complaint c, double s) {
            this.complaint = c;
            this.score = s;
        }
    }
}
