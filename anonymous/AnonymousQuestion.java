package anonymous;

import java.util.ArrayList;

public class AnonymousQuestion {

    private int id;
    private String content;
    private int votes;
    private boolean answered;

    // List to store students who voted
    private ArrayList<String> voters;

    // Constructor
    public AnonymousQuestion(int id, String content) {

        this.id = id;
        this.content = content;
        this.votes = 0;
        this.answered = false;

        // Initialize voters list
        voters = new ArrayList<>();
    }

    // Upvote with duplicate prevention
    public void upvote(String studentId)
            throws DuplicateVoteException {

        if (voters.contains(studentId)) {
            throw new DuplicateVoteException(
                    "Student already voted.");
        }

        votes++;
        voters.add(studentId);
    }

    // Mark as answered
    public void markAsAnswered() {
        answered = true;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getVotes() {
        return votes;
    }

    public boolean isAnswered() {
        return answered;
    }
}