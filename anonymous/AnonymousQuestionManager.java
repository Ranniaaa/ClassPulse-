package anonymous;

import java.util.ArrayList;

public class AnonymousQuestionManager {

    private ArrayList<AnonymousQuestion> questions;

    public AnonymousQuestionManager() {
        questions = new ArrayList<>();
    }

    public void addQuestion(int id, String content) {
        AnonymousQuestion newQuestion =
                new AnonymousQuestion(id, content);

        questions.add(newQuestion);
    }

    public ArrayList<AnonymousQuestion> getQuestions() {
        return questions;
    }

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

    public void upvoteQuestion(int id, String studentId)
            throws QuestionNotFoundException,
                   DuplicateVoteException {

        AnonymousQuestion q = findQuestionById(id);
        q.upvote(studentId);
    }

    public void markQuestionAsAnswered(int id)
            throws QuestionNotFoundException {

        AnonymousQuestion q = findQuestionById(id);
        q.markAsAnswered();
    }
}