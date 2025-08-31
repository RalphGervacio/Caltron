package com.caltron.caltron.controller;

import com.caltron.caltron.ml.GradePredictor;
import com.caltron.caltron.services.AIAdviceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/caltron")
public class HomeController {

    private final GradePredictor predictor;
    private final AIAdviceService aiAdviceService;

    public HomeController(GradePredictor predictor, AIAdviceService aiAdviceService) {
        this.predictor = predictor;
        this.aiAdviceService = aiAdviceService;
    }

    @GetMapping(value = "/homepage")
    public ModelAndView showHomePage() {
        return new ModelAndView("pages/homepage");
    }

    // Transmutation logic using cutoff table
    private int transmute(double rawPercent) {
        // Each entry = { cutoff, transmutedGrade }
        double[][] table = {
            {100.00, 100},
            {98.40, 99},
            {96.80, 98},
            {95.20, 97},
            {93.60, 96},
            {92.00, 95},
            {90.40, 94},
            {88.80, 93},
            {87.20, 92},
            {85.60, 91},
            {84.00, 90},
            {82.40, 89},
            {80.80, 88},
            {79.20, 87},
            {77.60, 86},
            {76.00, 85},
            {74.40, 84},
            {72.80, 83},
            {71.20, 82},
            {69.60, 81},
            {68.00, 80},
            {66.40, 79},
            {64.80, 78},
            {63.20, 77},
            {61.60, 76},
            {60.00, 75},
            {56.00, 74},
            {52.00, 73},
            {48.00, 72},
            {44.00, 71},
            {40.00, 70},
            {36.00, 69},
            {32.00, 68},
            {28.00, 67},
            {24.00, 66},
            {20.00, 65},
            {16.00, 64},
            {12.00, 63},
            {8.00, 62},
            {4.00, 61},
            {0.00, 60}
        };

        for (double[] entry : table) {
            double cutoff = entry[0];
            int grade = (int) entry[1];
            if (rawPercent >= cutoff) {
                return grade;
            }
        }
        return 60;
    }

    private void requirePositive(String name, double v) {
        if (Double.isNaN(v) || Double.isInfinite(v) || v <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + " must be > 0");
        }
    }

    @PostMapping("/compute")
    public Map<String, Object> computeGrade(
            @RequestParam double wwScore,
            @RequestParam double wwTotal,
            @RequestParam double ptScore,
            @RequestParam double ptTotal,
            @RequestParam double examScore,
            @RequestParam double examTotal,
            @RequestParam double wwWeight,
            @RequestParam double ptWeight,
            @RequestParam double examWeight
    ) {

        // 0. Validate to avoid divide-by-zero and weird weights
        requirePositive("wwTotal", wwTotal);
        requirePositive("ptTotal", ptTotal);
        requirePositive("examTotal", examTotal);

        double weightSum = wwWeight + ptWeight + examWeight;
        if (weightSum <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sum of weights must be > 0");
        }

        boolean weightsNormalized = false;
        if (Math.abs(weightSum - 100.0) > 0.0001) {
            // Auto-normalize to keep UX smooth (still tell client what happened)
            wwWeight = (wwWeight / weightSum) * 100.0;
            ptWeight = (ptWeight / weightSum) * 100.0;
            examWeight = (examWeight / weightSum) * 100.0;
            weightsNormalized = true;
        }

        Map<String, Object> result = new HashMap<>();

        // 1. Compute Percentage Scores (PS)
        double wwPercent = (wwScore / wwTotal) * 100.0;
        double ptPercent = (ptScore / ptTotal) * 100.0;
        double examPercent = (examScore / examTotal) * 100.0;

        // 2. Compute Weighted Scores (no transmutation yet)
        double initialGrade = (wwPercent * wwWeight / 100.0)
                + (ptPercent * ptWeight / 100.0)
                + (examPercent * examWeight / 100.0);

        double roundedInitial = Math.round(initialGrade * 100.0) / 100.0;

        // 3. Transmute only once (DepEd official rule)
        int finalGrade = transmute(roundedInitial);

        // 4. Descriptor
        String descriptorGrade;
        if (finalGrade < 75) {
            descriptorGrade = "Poor";
        } else if (finalGrade <= 79) {
            descriptorGrade = "Fairly Satisfactory";
        } else if (finalGrade <= 84) {
            descriptorGrade = "Satisfactory";
        } else if (finalGrade <= 89) {
            descriptorGrade = "Very Satisfactory";
        } else if (finalGrade <= 94) {
            descriptorGrade = "Outstanding";
        } else { // 95 - 100
            descriptorGrade = "Excellent";
        }

        // 5. Predict (safe since predictor is bootstrapped)
        double mlPrediction = predictor.predict(wwPercent, ptPercent, examPercent);

        // 6. JSON response
        result.put("writtenWorks", Map.of("percent", wwPercent));
        result.put("performanceTasks", Map.of("percent", ptPercent));
        result.put("exam", Map.of("percent", examPercent));
        result.put("initialGrade", initialGrade);
        result.put("finalGrade", finalGrade);
        result.put("prediction", mlPrediction);
        result.put("status", finalGrade >= 75 ? "PASSED" : "FAILED");
        result.put("color", finalGrade >= 75 ? "green" : "red");
        result.put("desc", descriptorGrade);
        result.put("weights", Map.of(
                "ww", wwWeight,
                "pt", ptWeight,
                "exam", examWeight,
                "normalized", weightsNormalized
        ));

        return result;
    }

}
