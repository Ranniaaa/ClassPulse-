package finalver.exception;


public class DuplicateAnswerException extends Exception {

    public DuplicateAnswerException() {
        super("This student has already submitted an answer for this question.");
    }

    public DuplicateAnswerException(String message) {
        super(message);
    }
}
