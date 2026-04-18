package gamification;

import finalver.model.LiveQuestion;
import finalver.model.Student;
import finalver.model.Answer;
import finalver.manager.ResponseManager;
import finalver.exception.DuplicateAnswerException;
import finalver.exception.QuestionClosedException;
import finalver.exception.InvalidAnswerException;

public class QuizBridge {

    private GamificationEngine engine;
    private ResponseManager responseManager;

    public QuizBridge(GamificationEngine engine,
            ResponseManager responseManager) {
        this.engine = engine;
        this.responseManager = responseManager;
    }

    // Called when a student submits an answer
    public String onAnswerSubmit(Student student,
            LiveQuestion question,
            String answerText,
            long responseTimeSeconds) {
        try {
            // submit to Malak's system
            responseManager.submitAnswer(student, question, answerText);

            // check if correct
            boolean correct = question.checkAnswer(answerText);

            if (correct) {
                // give points with speed bonus
                engine.addPointsWithSpeedBonus(
                        student.getName(),
                        10,
                        responseTimeSeconds,
                        "correct quiz answer"
                );
                return "Correct! Points awarded to " + student.getName();
            } else {
                // small participation point even for wrong answer
                engine.addPoints(student.getName(), 2,
                        "quiz participation");
                return "Wrong answer. +2 participation pts.";
            }

        } catch (DuplicateAnswerException e) {
            return "Already answered: " + e.getMessage();
        } catch (QuestionClosedException e) {
            return "Question closed: " + e.getMessage();
        } catch (InvalidAnswerException e) {
            return "Invalid answer: " + e.getMessage();
        } catch (InvalidScoreException e) {
            return "Score error: " + e.getMessage();
        }
    }
}
