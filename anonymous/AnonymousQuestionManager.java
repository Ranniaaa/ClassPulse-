package anonymous;

import java.util.ArrayList;

public class AnonymousQuestionManager {

    // List that stores all questions
    private ArrayList<AnonymousQuestion> questions;

    // Constructor
    public AnonymousQuestionManager() {
        questions = new ArrayList<>();
        // Initialize the ArrayList when the manager is created
    }

    // Add a new question
    public void addQuestion(int id, String content) {
        AnonymousQuestion newQuestion = new AnonymousQuestion(id, content);
        // Create a new question object

        questions.add(newQuestion);
        // Add the question to the list
    }

    // Return all questions
    public ArrayList<AnonymousQuestion> getQuestions() {
        return questions;
        // Return the full list
    }

    // Find a question by its ID
    public AnonymousQuestion findQuestionById(int id) {
        for (AnonymousQuestion q : questions) {
            // Loop through all questions

            if (q.getId() == id) {
                // Check if the ID matches

                return q;
                // Return the found question
            }
        }
        return null;
        // Return null if not found
    }

    // Upvote a specific question
    public void upvoteQuestion(int id) {
        AnonymousQuestion q = findQuestionById(id);
        // Search for the question

        if (q != null) {
            q.upvote();
            // Increase votes if the question exists
        }
    }

    // Mark a question as answered
    public void markQuestionAsAnswered(int id) {
        AnonymousQuestion q = findQuestionById(id);
        // Search for the question

        if (q != null) {
            q.markAsAnswered();
            // Change status to answered
        }
    }
}