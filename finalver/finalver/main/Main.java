package finalver.main;

import finalver.gui.ResultScreen;
import finalver.gui.StudentScreen;
import finalver.gui.TeacherScreen;
import finalver.manager.ResponseManager;
import finalver.model.QuizSession;

public class Main {

    public static void main(String[] args) {
        QuizSession quizSession = new QuizSession(1, "ClassPulse Live Session", "Teacher");
        ResponseManager responseManager = new ResponseManager();

        ResultScreen resultScreen = new ResultScreen();
        StudentScreen studentScreen = new StudentScreen(quizSession, responseManager);
        TeacherScreen teacherScreen = new TeacherScreen(
                quizSession,
                responseManager,
                studentScreen,
                resultScreen
        );

        teacherScreen.setVisible(true);
        studentScreen.setVisible(true);
    }
}
