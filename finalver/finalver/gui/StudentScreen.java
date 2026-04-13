package finalver.gui;


import finalver.exception.DuplicateAnswerException;
import finalver.exception.InvalidAnswerException;
import finalver.exception.QuestionClosedException;
import finalver.manager.ResponseManager;
import finalver.model.LiveQuestion;
import finalver.model.QuizSession;
import finalver.model.Student;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentScreen extends JFrame{
    private final QuizSession quizSession;
    private final ResponseManager responseManager;
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextField studentGroupField;
    private JTextArea questionArea;
    private JTextArea optionsArea;
    private JTextField answerField;
    private JButton submitButton;
    private JButton refreshButton;
    private JLabel messageLabel;
    public StudentScreen(QuizSession quizSession,ResponseManager responseManager) {
        this.quizSession=quizSession;
        this.responseManager=responseManager;

        setTitle("Student Screen - ClassPulse+");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel=new JPanel(new GridLayout(8,2,10,10));

        panel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        panel.add(studentIdField);

        panel.add(new JLabel("Student Name:"));
        studentNameField=new JTextField();
        panel.add(studentNameField);

        panel.add(new JLabel("Student Group:"));
        studentGroupField=new JTextField();
        panel.add(studentGroupField);

        panel.add(new JLabel("Current Question:"));
        questionArea=new JTextArea(4,20);
        questionArea.setEditable(false);
        panel.add(new JScrollPane(questionArea));

        panel.add(new JLabel("Options (if MCQ):"));
        optionsArea=new JTextArea(5, 20);
        optionsArea.setEditable(false);
        panel.add(new JScrollPane(optionsArea));

        panel.add(new JLabel("Your Answer:"));
        answerField=new JTextField();
        panel.add(answerField);

        refreshButton=new JButton("Refresh Question");
        panel.add(refreshButton);

        submitButton=new JButton("Submit Answer");
        panel.add(submitButton);

        messageLabel=new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel);

        add(panel);

        refreshButton.addActionListener(e -> refreshQuestionView());

        submitButton.addActionListener(e -> submitAnswer());

        refreshQuestionView();

        setVisible(true);
    }

    public void refreshQuestionView(){
        LiveQuestion currentQuestion=quizSession.getCurrentQuestion();

        if (currentQuestion==null){
            questionArea.setText("No question available.");
            optionsArea.setText("");
            return;
        }

        questionArea.setText(currentQuestion.getQuestiontext());

        List<String> options = currentQuestion.getOptions();
        optionsArea.setText("");

        if (options != null && !options.isEmpty()){
            for (int i = 0; i < options.size(); i++) {
                optionsArea.append((i + 1) + ". " + options.get(i) + "\n");
            }
        }
    }

    private void submitAnswer(){
        try {
            LiveQuestion currentQuestion = quizSession.getCurrentQuestion();

            if (currentQuestion == null) {
                messageLabel.setText("No current question available.");
                return;
            }

            int studentId=Integer.parseInt(studentIdField.getText().trim());
            String studentName=studentNameField.getText().trim();
            String studentGroup=studentGroupField.getText().trim();
            String answerText=answerField.getText().trim();

            if (studentName.isEmpty()) {
                messageLabel.setText("Student name is required.");
                return;
            }

            Student student=new Student(studentId,studentName,studentGroup);

            responseManager.submitAnswer(student,currentQuestion,answerText);

            messageLabel.setText("Answer submitted successfully.");
            answerField.setText("");
        } catch (NumberFormatException ex){
            messageLabel.setText("Student ID must be a valid number.");
        } catch (DuplicateAnswerException ex){
            messageLabel.setText(ex.getMessage());
        } catch (QuestionClosedException ex){
            messageLabel.setText(ex.getMessage());
        } catch (InvalidAnswerException ex){
            messageLabel.setText(ex.getMessage());
        } catch (Exception ex){
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }
}
