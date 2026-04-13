package finalver.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizSession {
    private int sessionid;
    private String title;
    private String teacherName;
    private List<LiveQuestion> questions;
    private boolean active;
    private int currentQuestionIndex;
    private LocalDateTime sessionstarttime;
    private LocalDateTime sessionendtime;
    public QuizSession(int sessionid, String title, String teacherName) {
        this.sessionid=sessionid;
        this.title=title;
        this.teacherName=teacherName;
        this.questions = new ArrayList<>();
        this.active = false;
        this.currentQuestionIndex = -1;
    }
    public int getSessionid() {
        return sessionid;
    }
    public void setSessionid(int sessionid) {
        this.sessionid=sessionid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title=title;
    }
    public String getTeacherName() {
        return teacherName;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName=teacherName;
    }
    public List<LiveQuestion> getQuestions() {
        return questions;
    }
    public void setQuestions(List<LiveQuestion> questions) {
        this.questions=questions;
    }
    public boolean isActive() {
        return active;
    }
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    public LocalDateTime getStarttime() {
        return sessionstarttime;
    }
    public LocalDateTime getEndtime() {
        return sessionendtime;
    }
    public void addQuestion(LiveQuestion question) {
        questions.add(question);
    }
    public void removeQuestion(LiveQuestion question) {
        questions.remove(question);
    }
    public void startSession() {
        this.active=true;
        this.sessionstarttime=LocalDateTime.now();
        if (!questions.isEmpty()) {
            currentQuestionIndex = 0;
        }
    }

    public void endSession() {
        this.active=false;
        this.sessionendtime=LocalDateTime.now();
    }
    public LiveQuestion getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }
    public void nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
        }
    }
    @Override
    public String toString() {
        return "QuizSession{" +
                "sessionId=" + sessionid +
                ", title='" + title + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", questionsCount=" + questions.size() +
                ", active=" + active +
                ", currentQuestionIndex=" + currentQuestionIndex +
                ", startTime=" + sessionstarttime +
                ", endTime=" + sessionendtime +
                '}';
    }
}
