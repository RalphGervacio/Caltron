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

    // Transmutation logic
    private double transmute(double rawPercent) {
        if (rawPercent == 100.0) {
            return 100;
        }
        if (rawPercent >= 99.99) {
            return 99;
        }
        if (rawPercent >= 98.39) {
            return 98;
        }
        if (rawPercent >= 96.79) {
            return 97;
        }
        if (rawPercent >= 93.6) {
            return 96;
        }
        if (rawPercent >= 92.0) {
            return 95;
        }
        if (rawPercent >= 90.4) {
            return 94;
        }
        if (rawPercent >= 88.8) {
            return 93;
        }
        if (rawPercent >= 87.2) {
            return 92;
        }
        if (rawPercent >= 85.6) {
            return 91;
        }
        if (rawPercent >= 84.0) {
            return 90;
        }
        if (rawPercent >= 82.4) {
            return 89;
        }
        if (rawPercent >= 80.8) {
            return 88;
        }
        if (rawPercent >= 79.2) {
            return 87;
        }
        if (rawPercent >= 77.6) {
            return 86;
        }
        if (rawPercent >= 76.0) {
            return 85;
        }
        if (rawPercent >= 74.4) {
            return 84;
        }
        if (rawPercent >= 72.8) {
            return 83;
        }
        if (rawPercent >= 71.2) {
            return 82;
        }
        if (rawPercent >= 69.6) {
            return 81;
        }
        if (rawPercent >= 68.0) {
            return 80;
        }
        if (rawPercent >= 66.4) {
            return 79;
        }
        if (rawPercent >= 64.8) {
            return 78;
        }
        if (rawPercent >= 63.2) {
            return 77;
        }
        if (rawPercent >= 61.6) {
            return 76;
        }
        if (rawPercent >= 60.0) {
            return 75;
        }
        if (rawPercent >= 56.0) {
            return 74;
        }
        if (rawPercent >= 52.0) {
            return 73;
        }
        if (rawPercent >= 48.0) {
            return 72;
        }
        if (rawPercent >= 44.0) {
            return 71;
        }
        if (rawPercent >= 40.0) {
            return 70;
        }
        if (rawPercent >= 36.0) {
            return 69;
        }
        if (rawPercent >= 32.0) {
            return 68;
        }
        if (rawPercent >= 28.0) {
            return 67;
        }
        if (rawPercent >= 24.0) {
            return 66;
        }
        if (rawPercent >= 20.0) {
            return 65;
        }
        if (rawPercent >= 16.0) {
            return 64;
        }
        if (rawPercent >= 12.0) {
            return 63;
        }
        if (rawPercent >= 8.0) {
            return 62;
        }
        if (rawPercent >= 4.0) {
            return 61;
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
            System.out.println("Written Works: " + wwWeight);
            ptWeight = (ptWeight / weightSum) * 100.0;
            System.out.println("Performance Task: " + ptWeight);
            examWeight = (examWeight / weightSum) * 100.0;
            System.out.println("Exam: " + examWeight);
            weightsNormalized = true;
        }

        Map<String, Object> result = new HashMap<>();

        // 1. Compute %
        double wwPercent = (wwScore / wwTotal) * 100.0;
        double ptPercent = (ptScore / ptTotal) * 100.0;
        double examPercent = (examScore / examTotal) * 100.0;

        // 2. Transmute
        double wwTransmuted = transmute(wwPercent);
        double ptTransmuted = transmute(ptPercent);
        double examTransmuted = transmute(examPercent);

        // 3. Apply weights
        double finalGrade = (wwTransmuted * wwWeight / 100.0)
                + (ptTransmuted * ptWeight / 100.0)
                + (examTransmuted * examWeight / 100.0);

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

        // 5. Predict (now always safe because predictor is bootstrapped)
        double mlPrediction = predictor.predict(wwPercent, ptPercent, examPercent);

        // 6. AI Suggestions (graceful fallback if API not configured)
        //String advice = aiAdviceService.generateAdvice(wwPercent, ptPercent, examPercent, finalGrade);
        // 7. JSON response
        result.put("writtenWorks", Map.of("percent", wwPercent, "transmuted", wwTransmuted));
        result.put("performanceTasks", Map.of("percent", ptPercent, "transmuted", ptTransmuted));
        result.put("exam", Map.of("percent", examPercent, "transmuted", examTransmuted));
        result.put("finalGrade", finalGrade);
        result.put("prediction", mlPrediction);
        result.put("status", finalGrade >= 75 ? "PASSED" : "FAILED");
        result.put("color", finalGrade >= 75 ? "green" : "red");
        result.put("desc", descriptorGrade);
        //result.put("advice", advice);
        result.put("weights", Map.of(
                "ww", wwWeight,
                "pt", ptWeight,
                "exam", examWeight,
                "normalized", weightsNormalized
        ));

        return result;
    }
}
