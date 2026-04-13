package finalver.gui;

import finalver.model.LiveQuestion;
import finalver.model.Question;
import finalver.model.QuizSession;
import finalver.manager.ResponseManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherScreen extends JFrame {

    private final QuizSession quizSession;
    private final ResponseManager responseManager;
    private final StudentScreen studentScreen;
    private final ResultScreen resultScreen;

    private JTextArea questionArea;
    private JTextField correctAnswerField;
    private JTextField pointsField;
    private JTextField topicField;
    private JTextField difficultyField;
    private JTextField timeLimitField;

    private JComboBox<String> questionTypeBox;

    private JTextField optionField;
    private JButton addOptionButton;
    private JTextArea optionsDisplay;

    private JButton createButton;
    private JButton openButton;
    private JButton closeButton;
    private JButton nextButton;
    private JButton showResultsButton;
    private JButton clearFormButton;

    private JLabel messageLabel;

    private List<String> optionsList;
    private int questionCounter;

    public TeacherScreen(QuizSession quizSession, ResponseManager responseManager, StudentScreen studentScreen,ResultScreen resultScreen){
        this.quizSession=quizSession;
        this.responseManager=responseManager;
        this.studentScreen=studentScreen;
        this.resultScreen=resultScreen;

        this.optionsList=new ArrayList<>();
        this.questionCounter=1;

        setTitle("Teacher Screen - ClassPulse+");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(12, 2, 10, 10));

        formPanel.add(new JLabel("Question Text:"));
        questionArea = new JTextArea(4, 20);
        formPanel.add(new JScrollPane(questionArea));

        formPanel.add(new JLabel("Question Type:"));
        questionTypeBox = new JComboBox<>(new String[]{
                "MULTIPLE_CHOICE",
                "TRUE_FALSE",
                "SHORT_ANSWER"
        });
        formPanel.add(questionTypeBox);

        formPanel.add(new JLabel("Correct Answer:"));
        correctAnswerField=new JTextField();
        formPanel.add(correctAnswerField);

        formPanel.add(new JLabel("Points:"));
        pointsField=new JTextField();
        formPanel.add(pointsField);

        formPanel.add(new JLabel("Topic:"));
        topicField=new JTextField();
        formPanel.add(topicField);

        formPanel.add(new JLabel("Difficulty:"));
        difficultyField=new JTextField();
        formPanel.add(difficultyField);

        formPanel.add(new JLabel("Time Limit (seconds):"));
        timeLimitField=new JTextField();
        formPanel.add(timeLimitField);

        formPanel.add(new JLabel("New Option:"));
        optionField=new JTextField();
        formPanel.add(optionField);

        addOptionButton=new JButton("Add Option");
        formPanel.add(addOptionButton);

        optionsDisplay=new JTextArea(5, 20);
        optionsDisplay.setEditable(false);
        formPanel.add(new JScrollPane(optionsDisplay));

        createButton=new JButton("Create Question");
        formPanel.add(createButton);

        openButton=new JButton("Open Current Question");
        formPanel.add(openButton);

        closeButton=new JButton("Close Current Question");
        formPanel.add(closeButton);

        nextButton=new JButton("Next Question");
        formPanel.add(nextButton);

        showResultsButton=new JButton("Show Results");
        formPanel.add(showResultsButton);

        clearFormButton=new JButton("Clear Form");
        formPanel.add(clearFormButton);

        messageLabel=new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(messageLabel, BorderLayout.SOUTH);

        add(mainPanel);

        questionTypeBox.addActionListener(e -> updateOptionControls());

        addOptionButton.addActionListener(e -> addOption());

        createButton.addActionListener(e -> createQuestion());

        openButton.addActionListener(e -> openCurrentQuestion());

        closeButton.addActionListener(e -> closeCurrentQuestion());

        nextButton.addActionListener(e -> goToNextQuestion());

        showResultsButton.addActionListener(e -> showResults());

        clearFormButton.addActionListener(e -> clearForm());

        updateOptionControls();

        setVisible(true);
    }

    private void updateOptionControls(){
        String selectedType=(String) questionTypeBox.getSelectedItem();
        boolean isMCQ ="MULTIPLE_CHOICE".equals(selectedType);

        optionField.setEnabled(isMCQ);
        addOptionButton.setEnabled(isMCQ);
        optionsDisplay.setEnabled(isMCQ);

        if (!isMCQ){
            optionField.setText("");
            optionsList.clear();
            optionsDisplay.setText("");
        }
    }

    private void addOption(){
        String selectedType=(String) questionTypeBox.getSelectedItem();

        if (!"MULTIPLE_CHOICE".equals(selectedType)) {
            messageLabel.setText("Options are only used for MULTIPLE_CHOICE questions.");
            return;
        }

        String option=optionField.getText();

        if (option==null || option.trim().isEmpty()) {
            messageLabel.setText("Option cannot be empty.");
            return;
        }

        optionsList.add(option.trim());
        updateOptionsDisplay();
        optionField.setText("");
        messageLabel.setText("Option added successfully.");
    }

    private void updateOptionsDisplay(){
        optionsDisplay.setText("");

        for (int i = 0; i < optionsList.size(); i++){
            optionsDisplay.append((i + 1) + ". " + optionsList.get(i) + "\n");
        }
    }

    private void createQuestion(){
        try{
            String questionText=questionArea.getText().trim();
            String correctAnswer=correctAnswerField.getText().trim();
            double points=Double.parseDouble(pointsField.getText().trim());
            String topic=topicField.getText().trim();
            String difficulty=difficultyField.getText().trim();
            int timeLimit=Integer.parseInt(timeLimitField.getText().trim());

            String selectedTypeText=(String) questionTypeBox.getSelectedItem();
            Question.QuestionType type=Question.QuestionType.valueOf(selectedTypeText);

            if (questionText.isEmpty()){
                messageLabel.setText("Question text is required.");
                return;
            }

            if (correctAnswer.isEmpty()){
                messageLabel.setText("Correct answer is required.");
                return;
            }

            if (timeLimit <=0) {
                messageLabel.setText("Time limit must be greater than 0.");
                return;
            }

            if (points<0) {
                messageLabel.setText("Points cannot be negative.");
                return;
            }

            if (type==Question.QuestionType.MULTIPLE_CHOICE && optionsList.size()<2){
                messageLabel.setText("MCQ must have at least 2 options.");
                return;
            }

            LiveQuestion question=new LiveQuestion(
                    questionCounter,
                    questionText,
                    points,
                    type,
                    topic,
                    difficulty,
                    correctAnswer,
                    timeLimit
            );

            if (type==Question.QuestionType.MULTIPLE_CHOICE) {
                for (String option : optionsList) {
                    question.addOption(option);
                }
            }

            quizSession.addQuestion(question);
            questionCounter++;

            if (!quizSession.isActive()) {
                quizSession.startSession();
            }

            studentScreen.refreshQuestionView();

            messageLabel.setText("Question created and added to session successfully.");
            clearForm();
        } catch (NumberFormatException ex) {
            messageLabel.setText("Points and time limit must be valid numbers.");
        } catch (IllegalArgumentException ex) {
            messageLabel.setText("Invalid question type.");
        } catch (Exception ex) {
            messageLabel.setText("Error while creating question: " + ex.getMessage());
        }
    }

    private void openCurrentQuestion() {
        LiveQuestion currentQuestion=quizSession.getCurrentQuestion();

        if (currentQuestion==null) {
            messageLabel.setText("No current question available.");
            return;
        }

        currentQuestion.openQuestion();
        responseManager.clearAnswers();
        studentScreen.refreshQuestionView();

        messageLabel.setText("Current question is now open.");
    }

    private void closeCurrentQuestion() {
        LiveQuestion currentQuestion=quizSession.getCurrentQuestion();

        if (currentQuestion==null) {
            messageLabel.setText("No current question available.");
            return;
        }

        currentQuestion.closeQuestion();
        studentScreen.refreshQuestionView();

        messageLabel.setText("Current question is now closed.");
    }

    private void goToNextQuestion() {
        quizSession.nextQuestion();
        responseManager.clearAnswers();
        studentScreen.refreshQuestionView();

        LiveQuestion currentQuestion=quizSession.getCurrentQuestion();

        if (currentQuestion==null) {
            messageLabel.setText("No more questions in the session.");
        } else {
            messageLabel.setText("Moved to next question.");
        }
    }

    private void showResults() {
        LiveQuestion currentQuestion=quizSession.getCurrentQuestion();

        if (currentQuestion==null) {
            messageLabel.setText("No current question to show results for.");
            return;
        }

        StringBuilder sb=new StringBuilder();

        sb.append("Question: ").append(currentQuestion.getQuestiontext()).append("\n");
        sb.append("Type: ").append(currentQuestion.getQuestiontype()).append("\n");
        sb.append("Correct Answer: ").append(currentQuestion.getCorrectAnswer()).append("\n");
        sb.append("Answers Count: ").append(responseManager.getAnswerCount()).append("\n\n");

        if (responseManager.getAllAnswers().isEmpty()) {
            sb.append("No answers submitted yet.");
        } else {
            responseManager.getAllAnswers().forEach(answer -> {
                sb.append("Student: ").append(answer.getStudent().getName()).append("\n");
                sb.append("Answer: ").append(answer.getAnswerText()).append("\n");
                sb.append("Correct: ").append(answer.isCorrect() ? "Yes" : "No").append("\n");
                sb.append("Submitted At: ").append(answer.getSubmissionTime()).append("\n");
                sb.append("------------------------------------\n");
            });
        }

        resultScreen.setResults(sb.toString());
        resultScreen.setVisible(true);

        messageLabel.setText("Results displayed.");
    }

    private void clearForm() {
        questionArea.setText("");
        correctAnswerField.setText("");
        pointsField.setText("");
        topicField.setText("");
        difficultyField.setText("");
        timeLimitField.setText("");
        optionField.setText("");
        optionsList.clear();
        optionsDisplay.setText("");
        questionTypeBox.setSelectedIndex(0);
        updateOptionControls();
    }
}
