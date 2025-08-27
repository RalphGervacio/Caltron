package com.caltron.caltron.services;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class AIAdviceService {

//    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
//    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
//    private static final MediaType JSON = MediaType.parse("application/json");
//
//    public String generateAdvice(double ww, double pt, double exam, double finalGrade) {
//        // Graceful fallback if env not set or API is down.
//        if (API_KEY == null || API_KEY.isBlank()) {
//            return fallbackAdvice(ww, pt, exam, finalGrade, "No OPENAI_API_KEY configured.");
//        }
//
//        OkHttpClient client = new OkHttpClient();
//
//        String prompt = String.format(
//                "A student has these scores:\n"
//                + "- Written Works: %.2f%%\n"
//                + "- Performance Tasks: %.2f%%\n"
//                + "- Exams: %.2f%%\n"
//                + "Predicted Final Grade: %.2f\n\n"
//                + "Write a long, personalized study plan. "
//                + "Give detailed suggestions for weak areas, daily and weekly habits, "
//                + "time management strategies, and motivational advice in a supportive teacher-like tone.",
//                ww, pt, exam, finalGrade
//        );
//
//        JSONObject body = new JSONObject()
//                .put("model", "gpt-4o-mini")
//                .put("messages", new org.json.JSONArray()
//                        .put(new JSONObject().put("role", "system").put("content", "You are a helpful teacher."))
//                        .put(new JSONObject().put("role", "user").put("content", prompt))
//                )
//                .put("temperature", 0.8);
//
//        RequestBody requestBody = RequestBody.create(body.toString(), JSON);
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .post(requestBody)
//                .addHeader("Authorization", "Bearer " + API_KEY)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful() || response.body() == null) {
//                return fallbackAdvice(ww, pt, exam, finalGrade, "AI service unavailable (" + response.code() + ").");
//            }
//            String res = response.body().string();
//            JSONObject json = new JSONObject(res);
//            return json.getJSONArray("choices")
//                    .getJSONObject(0)
//                    .getJSONObject("message")
//                    .getString("content");
//        } catch (Exception e) {
//            return fallbackAdvice(ww, pt, exam, finalGrade, "AI request failed: " + e.getMessage());
//        }
//    }
//
//    private String fallbackAdvice(double ww, double pt, double exam, double finalGrade, String reason) {
//        String weak = "Written Works";
//        double min = ww;
//
//        if (pt < min) {
//            min = pt;
//            weak = "Performance Tasks";
//        }
//        if (exam < min) {
//            min = exam;
//            weak = "Exams";
//        }
//
//        return "**AI advice temporarily unavailable** (" + reason + ").\n\n"
//                + "Quick actionable plan based on your inputs:\n"
//                + "- Focus area: " + weak + "\n"
//                + "- Daily: 45–60 minutes targeted practice for " + weak + " with spaced repetition.\n"
//                + "- Weekly: 1 full review session; simulate exam conditions for 30 minutes.\n"
//                + "- Time management: Pomodoro 25/5, 4 cycles; log errors and re-try next day.\n"
//                + "- Ask teacher/peer for feedback once per week.\n";
//    }

}
