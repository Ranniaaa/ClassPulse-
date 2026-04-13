package finalver.exception;

public class QuestionClosedException extends Exception {

    public QuestionClosedException() {
        super("This question is closed. No more answers are allowed.");
    }

    public QuestionClosedException(String message) {
        super(message);
    }
}

