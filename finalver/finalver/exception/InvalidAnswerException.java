package finalver.exception;

public class InvalidAnswerException extends Exception {

    public InvalidAnswerException() {
        super("The submitted answer is invalid.");
    }

    public InvalidAnswerException(String message) {
        super(message);
    }
}