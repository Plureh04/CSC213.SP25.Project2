package edu.canisius.csc213.complaints.storage;

import edu.canisius.csc213.complaints.model.Complaint;

import java.util.List;
import java.util.Map;

public class ComplaintMerger {

    /**
     * Matches complaints to their corresponding embedding vectors by complaint ID.
     *
     * @param complaints List of complaints (from CSV)
     * @param embeddings Map from complaintId to embedding vector (from JSONL)
     */
    public static void mergeEmbeddings(List<Complaint> complaints, Map<Long, double[]> embeddings) {
        for (Complaint complaint : complaints) {
            double[] embedding = embeddings.get(complaint.getComplaintId());
            if (embedding != null) {
                complaint.setEmbedding(embedding);
            }
        }
    }



}
