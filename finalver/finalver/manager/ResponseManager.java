package finalver.manager;

import finalver.exception.DuplicateAnswerException;
import finalver.exception.InvalidAnswerException;
import finalver.exception.QuestionClosedException;
import finalver.model.Answer;
import finalver.model.LiveQuestion;
import finalver.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResponseManager{

    private Map<Integer,Answer> answers;

    public ResponseManager() {
        answers=new HashMap<>();
    }

    public void submitAnswer(Student student, LiveQuestion question, String answertext)
            throws DuplicateAnswerException, QuestionClosedException, InvalidAnswerException {

        if (student==null) {
            throw new InvalidAnswerException("Student information is missing.");
        }

        if (question==null) {
            throw new InvalidAnswerException("Question is missing.");
        }

        if (answertext==null || answertext.trim().isEmpty()) {
            throw new InvalidAnswerException("Answer cannot be empty.");
        }

        if (!question.isOpen() || question.isTimeOver()) {
            throw new QuestionClosedException("The question is closed or time is over.");
        }

        if (answers.containsKey(student.getId())) {
            throw new DuplicateAnswerException("This student has already answered this question.");
        }

        Answer answer = new Answer(student, question, answertext);
        boolean isCorrect=question.checkAnswer(answertext);
        answer.setCorrect(isCorrect);

        answers.put(student.getId(), answer);
    }

    public Answer getAnswerByStudentId(int id) {
        return answers.get(id);
    }

    public boolean hasStudentAnswered(int id) {
        return answers.containsKey(id);
    }

    public Collection<Answer> getAllAnswers() {
        return answers.values();
    }

    public int getAnswerCount() {
        return answers.size();
    }

    public void clearAnswers() {
        answers.clear();
    }
}
