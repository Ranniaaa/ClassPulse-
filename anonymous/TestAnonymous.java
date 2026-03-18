package anonymous;

public class TestAnonymous {

    public static void main(String[] args) {

        // Create a new question object
        AnonymousQuestion q1 = new AnonymousQuestion(1, "What is OOP?");

        // Print question information
        System.out.println("Question: " + q1.getContent());
        System.out.println("Votes: " + q1.getVotes());

        // Upvote the question
        q1.upvote();

        // Display votes after upvote
        System.out.println("Votes after upvote: " + q1.getVotes());

        // Mark the question as answered
        q1.markAsAnswered();

        // Display whether the question is answered
        System.out.println("Answered: " + q1.isAnswered());
    }
}