package com.caltron.caltron.controller;

import com.caltron.caltron.beans.TrainingData;
import com.caltron.caltron.ml.GradePredictor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AIController {

    private final GradePredictor gradePredictor;

    public AIController(GradePredictor gradePredictor) {
        this.gradePredictor = gradePredictor;
    }

    @PostMapping("/train")
    @ResponseBody
    public String trainModel(@RequestBody TrainingData data) {
        gradePredictor.train(data.getX(), data.getY());
        return "Model trained successfully!";
    }

    @PostMapping("/predict")
    @ResponseBody
    public double predictGrade(@RequestBody double[] inputs) {
        return gradePredictor.predict(inputs[0], inputs[1], inputs[2]);
    }
}
