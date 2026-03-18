package anonymous;

import java.util.ArrayList;

public class AnonymousQuestionManager {

    private ArrayList<AnonymousQuestion> questions;

    // Constructor
    public AnonymousQuestionManager() {
        questions = new ArrayList<>();
    }

    // Add question
    public void addQuestion(int id, String content) {
        AnonymousQuestion newQuestion =
                new AnonymousQuestion(id, content);

        questions.add(newQuestion);
    }

    // Get all questions
    public ArrayList<AnonymousQuestion> getQuestions() {
        return questions;
    }

    // Find question (using Exception)
    public AnonymousQuestion findQuestionById(int id)
            throws QuestionNotFoundException {

        for (AnonymousQuestion q : questions) {
            if (q.getId() == id) {
                return q;
            }
        }

        throw new QuestionNotFoundException(
                "Question with ID " + id + " not found.");
    }

    // Upvote question
    public void upvoteQuestion(int id, String studentId)
            throws QuestionNotFoundException,
                   DuplicateVoteException {

        AnonymousQuestion q = findQuestionById(id);
        q.upvote(studentId);
    }

    // Mark question as answered
    public void markQuestionAsAnswered(int id)
            throws QuestionNotFoundException {

        AnonymousQuestion q = findQuestionById(id);
        q.markAsAnswered();
    }
}