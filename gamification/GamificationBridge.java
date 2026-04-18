package gamification;

import anonymous.AnonymousQuestion;
import anonymous.AnonymousQuestionManager;
import anonymous.DuplicateVoteException;
import anonymous.QuestionNotFoundException;

public class GamificationBridge {

    private GamificationEngine engine;
    private AnonymousQuestionManager questionManager;

    public GamificationBridge(GamificationEngine engine,
            AnonymousQuestionManager questionManager) {
        this.engine = engine;
        this.questionManager = questionManager;
    }

    public void onUpvote(int questionId, String studentId) {
        try {
            questionManager.upvoteQuestion(questionId, studentId);
            engine.addPoints(studentId, 5, "upvoted a question");
            System.out.println("+ " + studentId
                    + " upvoted question #" + questionId
                    + " and earned 5 pts");
        } catch (QuestionNotFoundException e) {
            System.out.println("Question not found: " + e.getMessage());
        } catch (DuplicateVoteException e) {
            System.out.println("Duplicate vote: " + e.getMessage());
        } catch (InvalidScoreException e) {
            System.out.println("Score error: " + e.getMessage());
        }
    }

    public void onQuestionSubmit(int questionId,
            String content,
            String studentId) {
        try {
            questionManager.addQuestion(questionId, content);
            engine.addPoints(studentId, 3, "submitted anonymous question");
            System.out.println("+ " + studentId
                    + " submitted a question and earned 3 pts");
        } catch (InvalidScoreException e) {
            System.out.println("Score error: " + e.getMessage());
        }
    }

    public void onQuestionAnswered(int questionId) {
        try {
            questionManager.markQuestionAsAnswered(questionId);
            System.out.println("Question #" + questionId
                    + " marked as answered");
        } catch (QuestionNotFoundException e) {
            System.out.println("Question not found: " + e.getMessage());
        }
    }
}
