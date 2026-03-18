package anonymous; 
// This line defines the package name.
// It must match the folder name exactly.

public class AnonymousQuestion { 
// This class represents the blueprint of a question.

    private int id; 
    // Unique identifier for each question.

    private String content; 
    // The text content of the question.

    private int votes; 
    // Number of upvotes the question received.

    private boolean answered; 
    // Indicates whether the question has been answered (true = answered).

    // -------------------------
    // Constructor
    // -------------------------

    public AnonymousQuestion(int id, String content) {
        // Constructor used to create a new question object.

        this.id = id;
        // Assign the parameter value to the class variable.

        this.content = content;
        // Store the question text.

        this.votes = 0;
        // When created, the question starts with 0 votes.

        this.answered = false;
        // When created, the question is not answered yet.
    }

    // -------------------------
    // Methods
    // -------------------------

    public void upvote() {
        // This method increases the number of votes.

        votes++;
        // Increment votes by 1.
    }

    public void markAsAnswered() {
        // This method marks the question as answered.

        answered = true;
        // Change status to answered.
    }

    // -------------------------
    // Getters
    // -------------------------

    public int getId() {
        return id;
        // Returns the question ID.
    }

    public String getContent() {
        return content;
        // Returns the question content.
    }

    public int getVotes() {
        return votes;
        // Returns the number of votes.
    }

    public boolean isAnswered() {
        return answered;
        // Returns whether the question is answered.
    }
}