package com.caltron.caltron.ml;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class GradePredictor {

    // Super-simple baseline: single “weight” we treat as the average predicted grade.
    private volatile double[] weights;

    @PostConstruct
    public void bootstrap() {
        // Ensure predict() always has a value even if /train was never called.
        // Choose a sensible default (e.g., 80) so the app never throws.
        this.weights = new double[]{80.0};
    }

    // Train with input features (X) and outputs (y)
    public synchronized void train(double[][] X, double[] y) {
        if (y == null || y.length == 0) {
            throw new IllegalArgumentException("y (labels) must not be empty.");
        }
        double sum = 0;
        for (double value : y) {
            sum += value;
        }
        double avg = sum / y.length;

        // Store baseline average
        this.weights = new double[]{avg};
    }

    // Predict using trained "weights" (baseline average)
    public double predict(double... inputs) {
        // No exception anymore; @PostConstruct guarantees weights != null
        return weights[0];
    }
}
