package finalver.model;

public class Question {
    private final int questionid;
    private String questiontext;
    private double points;
    private QuestionType questiontype;
    private String topic;
    private String difficultylevel;

    public enum QuestionType {
        MULTIPLE_CHOICE,
        TRUE_FALSE,
        SHORT_ANSWER
    }

    public Question(int questionid, String questiontext, double points,
                    QuestionType questiontype, String topic, String difficultylevel) {
        this.questionid=questionid;
        this.questiontext=questiontext;
        this.points=points;
        this.questiontype=questiontype;
        this.topic=topic;
        this.difficultylevel=difficultylevel;
    }

    public int getQuestionid() {
        return questionid;
    }

    public String getQuestiontext() {
        return questiontext;
    }

    public void setQuestiontext(String questiontext) {
        this.questiontext=questiontext;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        if (points >=0) {
            this.points=points;
        }
    }

    public QuestionType getQuestiontype() {
        return questiontype;
    }

    public void setQuestionType(QuestionType questiontype) {
        this.questiontype=questiontype;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic=topic;
    }

    public String getDifficultylevel() {
        return difficultylevel;
    }

    public void setDifficultyLevel(String difficultylevel) {
        this.difficultylevel=difficultylevel;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionid +
                ", questionText='" + questiontext + '\'' +
                ", points=" + points +
                ", questionType=" + questiontype +
                ", topic='" + topic + '\'' +
                ", difficultyLevel='" + difficultylevel + '\'' +
                '}';
    }
}
