package finalver.model;
import java.time.LocalDateTime;

public class Answer {
    private Student student;
    private LiveQuestion question;
    private String answertext;
    private boolean correct;
    private LocalDateTime submissiontime;

    public Answer(Student student, LiveQuestion question, String answertext) {
        this.student=student;
        this.question=question;
        this.answertext=answertext;
        this.correct=false;
        this.submissiontime=LocalDateTime.now();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student=student;
    }

    public LiveQuestion getQuestion() {
        return question;
    }

    public void setQuestion(LiveQuestion question) {
        this.question=question;
    }

    public String getAnswerText() {
        return answertext;
    }

    public void setAnswerText(String answertext) {
        this.answertext=answertext;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct=correct;
    }

    public LocalDateTime getSubmissionTime() {
        return submissiontime;
    }

    public void setSubmissionTime(LocalDateTime submissiontime) {
        this.submissiontime = submissiontime;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "student=" + student +
                ", question=" + question.getQuestiontext() +
                ", answerText='" + answertext + '\'' +
                ", correct=" + correct +
                ", submissionTime=" + submissiontime +
                '}';
    }
}
