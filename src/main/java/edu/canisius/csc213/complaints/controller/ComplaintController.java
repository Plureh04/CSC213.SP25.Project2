package edu.canisius.csc213.complaints.controller;

import edu.canisius.csc213.complaints.model.Complaint;
import edu.canisius.csc213.complaints.service.ComplaintSimilarityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ComplaintController {

    private final List<Complaint> complaints;
    private final ComplaintSimilarityService similarityService;

    public ComplaintController(List<Complaint> complaints, ComplaintSimilarityService similarityService) {
        this.complaints = complaints;
        this.similarityService = similarityService;
    }

    @GetMapping("/complaint")
    public String showComplaint(@RequestParam(defaultValue = "0") int index, Model model) {
        int max = complaints.size();
        if (index < 0) index = 0;
        if (index >= max) index = max - 1;

        Complaint current = complaints.get(index);
        List<Complaint> similar = similarityService.findTop3Similar(current);

        model.addAttribute("complaint", current);
        model.addAttribute("similarComplaints", similar);
        model.addAttribute("prevIndex", index > 0 ? index - 1 : 0);
        model.addAttribute("nextIndex", index < max - 1 ? index + 1 : max - 1);

        return "complaint"; // ← This maps to complaint.html
    }

    @GetMapping("/search")
    public String searchComplaints(@RequestParam(required = false) String company, Model model) {
        if (company == null || company.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a company name.");
            return "search";
        }

        List<Complaint> searchResults = complaints.stream()
                .filter(c -> c.getCompany() != null &&
                        c.getCompany().toLowerCase().contains(company.toLowerCase()))
                .toList();

        if (searchResults.isEmpty()) {
            model.addAttribute("error", "No complaints found for that company.");
        } else {
            model.addAttribute("searchResults", searchResults);
        }

        model.addAttribute("companySearch", company);
        return "search";
    }
}
