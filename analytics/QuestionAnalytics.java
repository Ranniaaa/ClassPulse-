package analytics;

import java.util.*;

public class QuestionAnalytics {

    // Detect difficult questions
    public List<String> getDifficultQuestions(Map<String, Integer> correctAnswers,
                                              Map<String, Integer> totalAnswers) {

        List<String> difficult = new ArrayList<>();

        for (String question : totalAnswers.keySet()) {

            int total = totalAnswers.get(question);
            int correct = correctAnswers.getOrDefault(question, 0);

            double successRate = (total == 0) ? 0 : (correct * 100.0) / total;

            // if success rate < 50% → difficult
            if (successRate < 50) {
                difficult.add(question);
            }
        }

        return difficult;
    }
}
