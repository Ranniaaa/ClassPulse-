package finalver.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LiveQuestion extends Question {
    private String correctanswer;
    private boolean open;
    private int timeLimitSeconds;
    private LocalDateTime questionStartTime;
    private LocalDateTime questionEndTime;
    private List<String> options;

    public LiveQuestion(int questionid, String questiontext, double points,
                        QuestionType questiontype, String topic, String difficultylevel,
                        String correctanswer, int timeLimitSeconds) {
        super(questionid,questiontext,points,questiontype,topic,difficultylevel);
        this.correctanswer=correctanswer;
        this.timeLimitSeconds=timeLimitSeconds;
        this.open=false;
        this.options=new ArrayList<>();
    }

    public String getCorrectAnswer() {
        return correctanswer;
    }

    public void setCorrectAnswer(String correctanswer) {
        this.correctanswer=correctanswer;
    }

    public boolean isOpen() {
        return open;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public void setTimeLimitSeconds(int timeLimitSeconds) {
        if (timeLimitSeconds>0) {
            this.timeLimitSeconds=timeLimitSeconds;
        }
    }

    public LocalDateTime getQuestionStartTime() {
        return questionStartTime;
    }

    public LocalDateTime getQuestionEndTime() {
        return questionEndTime;
    }

    public List<String> getOptions() {
        return options;
    }

    public void addOption(String option) {
        if (option != null && !option.trim().isEmpty()) {
            options.add(option.trim());
        }
    }

    public void openQuestion(){
        this.open=true;
        this.questionStartTime=LocalDateTime.now();
        this.questionEndTime=questionStartTime.plusSeconds(timeLimitSeconds);
    }

    public void closeQuestion(){
        this.open=false;
    }

    public boolean isTimeOver(){
        if (questionEndTime == null){
            return false;
        }
        return LocalDateTime.now().isAfter(questionEndTime);
    }

    public boolean checkAnswer(String studentanswer){
        if (studentanswer == null){
            return false;
        }
        return studentanswer.trim().equalsIgnoreCase(correctanswer.trim());
    }
}
