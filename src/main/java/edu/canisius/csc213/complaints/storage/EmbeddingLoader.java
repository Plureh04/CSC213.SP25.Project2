package edu.canisius.csc213.complaints.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class EmbeddingLoader {

    /**
     * Loads complaint embeddings from a JSONL (newline-delimited JSON) file.
     * Each line must be a JSON object with:
     * {
     *   "complaintId": <long>,
     *   "embedding": [<double>, <double>, ...]
     * }
     *
     * @param jsonlStream InputStream to the JSONL file
     * @return A map from complaint ID to its embedding vector
     * @throws IOException if the file cannot be read or parsed
     */
    public static Map<Long, double[]> loadEmbeddings(InputStream jsonlStream) throws IOException {
        Map<Long, double[]> embeddings = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(jsonlStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank lines

                try {
                    Map<String, Object> obj = mapper.readValue(line, Map.class);
                    Long id = ((Number) obj.get("complaintId")).longValue();
                    List<?> list = (List<?>) obj.get("embedding");
                    double[] vector = list.stream().mapToDouble(e -> ((Number) e).doubleValue()).toArray();
                    embeddings.put(id, vector);
                } catch (Exception e) {
                    System.err.println("Skipping invalid JSON line: " + line);
                }
            }
        }

        return embeddings;
    }



}
