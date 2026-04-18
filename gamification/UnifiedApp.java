package gamification;

import analytics.QuestionAnalytics;
import anonymous.AnonymousQuestion;
import anonymous.AnonymousQuestionManager;
import finalver.model.QuizSession;
import finalver.model.Student;
import finalver.model.LiveQuestion;
import finalver.model.Question;
import finalver.manager.ResponseManager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class UnifiedApp extends Application {

    // ── ALL MODULES ───────────────────────────────────────────
    private GamificationEngine engine;
    private ParticipationCalculator calculator;
    private AnonymousQuestionManager questionManager;
    private GamificationBridge gamBridge;
    private ResponseManager responseManager;
    private QuizSession quizSession;
    private QuizBridge quizBridge;
    private AnalyticsBridge analyticsBridge;

    private BorderPane root;
    private final int[] questionIdCounter = {1};
    private final int[] quizQuestionCounter = {1};

    public UnifiedApp() {
        engine = new GamificationEngine();
        calculator = new ParticipationCalculator();
        questionManager = new AnonymousQuestionManager();
        gamBridge = new GamificationBridge(engine, questionManager);
        responseManager = new ResponseManager();
        quizSession = new QuizSession(1, "ClassPulse Live Session", "Teacher");
        quizBridge = new QuizBridge(engine, responseManager);
        analyticsBridge = new AnalyticsBridge(engine);

        // seed some initial data
        try {
            engine.addPointsWithSpeedBonus("Alice", 10, 4, "quiz answer");
            engine.addPoints("Alice", 110, "correct answer");
            engine.addPointsWithSpeedBonus("Bob", 10, 15, "quiz answer");
            engine.addPoints("Bob", 70, "correct answer");
            engine.addPoints("Diana", 130, "correct answer");
            engine.addPoints("Eve", 50, "participation");
        } catch (InvalidScoreException e) {
            System.out.println("Seed error: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setTop(buildNavBar());
        root.setCenter(buildLeaderboard());

        Scene scene = new Scene(root, 660, 560);
        stage.setTitle("ClassPulse+ Unified App");
        stage.setScene(scene);
        stage.show();
    }

    // ─── NAV BAR ──────────────────────────────────────────────
    private HBox buildNavBar() {
        Button b1 = navBtn("🏆 Leaderboard");
        Button b2 = navBtn("🎖 Badges");
        Button b3 = navBtn("👤 Profile");
        Button b4 = navBtn("📊 Grades");
        Button b5 = navBtn("🔗 Anonymous");
        Button b6 = navBtn("❓ Quiz");
        Button b7 = navBtn("📁 Reports");

        b1.setOnAction(e -> root.setCenter(buildLeaderboard()));
        b2.setOnAction(e -> root.setCenter(buildBadges()));
        b3.setOnAction(e -> root.setCenter(buildProfile()));
        b4.setOnAction(e -> root.setCenter(buildGrades()));
        b5.setOnAction(e -> root.setCenter(buildAnonymous()));
        b6.setOnAction(e -> root.setCenter(buildQuiz()));
        b7.setOnAction(e -> root.setCenter(buildReports()));

        HBox nav = new HBox(6, b1, b2, b3, b4, b5, b6, b7);
        nav.setPadding(new Insets(12, 16, 12, 16));
        nav.setAlignment(Pos.CENTER);
        nav.setStyle("-fx-background-color: #16213e;");
        return nav;
    }

    // ─── SCREEN 1: LEADERBOARD ────────────────────────────────
    private VBox buildLeaderboard() {
        Label title = makeTitle("🏆 Leaderboard");
        HBox header = makeRow("Rank", "Student", "Points", true);
        VBox rows = new VBox(8);
        try {
            List<Map.Entry<String, Integer>> ranked
                    = engine.getLeaderboard().getRankedList();
            String[] medals = {"🥇", "🥈", "🥉"};
            for (int i = 0; i < ranked.size(); i++) {
                String rank = i < 3 ? medals[i] : "#" + (i + 1);
                String name = ranked.get(i).getKey();
                String points = ranked.get(i).getValue() + " pts";
                rows.getChildren().add(makeRow(rank, name, points, false));
            }
        } catch (RankingGenerationException e) {
            rows.getChildren().add(errLabel(e.getMessage()));
        }
        return wrap(title, header, rows);
    }

    // ─── SCREEN 2: BADGES ─────────────────────────────────────
    private VBox buildBadges() {
        String sid = "Alice";
        Label title = makeTitle("🎖 Badges & Level");
        VBox cards = new VBox(8);
        cards.getChildren().addAll(
                makeCard("👤  Student", sid),
                makeCard("⭐  Points", engine.getTotalPoints(sid) + " pts"),
                makeCard("🎯  Level", engine.getCurrentLevel(sid).getName())
        );
        Label bt = sectionLabel("Earned Badges:");
        VBox badgeList = new VBox(8);
        for (Badge b : engine.getEarnedBadges(sid)) {
            HBox row = new HBox(10);
            row.setPadding(new Insets(8, 14, 8, 14));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
            Label icon = new Label("🏅");
            icon.setFont(Font.font(16));
            Label name = new Label(b.getName());
            name.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            name.setTextFill(Color.web("#f5a623"));
            name.setMinWidth(120);
            Label desc = new Label("— " + b.getDescription());
            desc.setFont(Font.font("Arial", 13));
            desc.setTextFill(Color.LIGHTGRAY);
            row.getChildren().addAll(icon, name, desc);
            badgeList.getChildren().add(row);
        }
        return wrap(title, cards, bt, badgeList);
    }

    // ─── SCREEN 3: PROFILE ────────────────────────────────────
    private VBox buildProfile() {
        String sid = "Alice";
        Label title = makeTitle("👤 Student Profile");
        int total = engine.getTotalPoints(sid);
        double grade = 0;
        try {
            grade = calculator.calculateGrade(total);
        } catch (InvalidScoreException ignored) {
        }
        VBox cards = new VBox(8);
        cards.getChildren().addAll(
                makeCard("👤  Name", sid),
                makeCard("⭐  Points", total + " pts"),
                makeCard("🎯  Level", engine.getCurrentLevel(sid).getName()),
                makeCard("🏅  Badges", engine.getEarnedBadges(sid).size() + " badge(s)"),
                makeCard("📊  Grade", grade + " / 20")
        );
        Label ht = sectionLabel("Point History:");
        VBox history = new VBox(6);
        for (PointRecord r : engine.getRecordsForStudent(sid)) {
            Label l = new Label("  +" + r.getPoints() + " pts  —  " + r.getReason());
            l.setFont(Font.font("Arial", 13));
            l.setTextFill(Color.web("#a0c4ff"));
            l.setPadding(new Insets(6, 12, 6, 12));
            l.setStyle("-fx-background-color: #16213e; -fx-background-radius: 6;");
            history.getChildren().add(l);
        }
        return wrap(title, cards, ht, history);
    }

    // ─── SCREEN 4: GRADES ─────────────────────────────────────
    private VBox buildGrades() {
        Label title = makeTitle("📊 Participation Grades");
        HBox header = makeRow("Student", "Points", "Grade /20", true);
        VBox rows = new VBox(8);
        double total = 0;
        int count = 0;
        try {
            for (Map.Entry<String, Integer> e
                    : engine.getLeaderboard().getRankedList()) {
                double g = calculator.calculateGrade(e.getValue());
                total += g;
                count++;
                rows.getChildren().add(makeRow(
                        e.getKey(), e.getValue() + " pts", g + " / 20", false));
            }
        } catch (RankingGenerationException | InvalidScoreException e) {
            rows.getChildren().add(errLabel(e.getMessage()));
        }
        double avg = count > 0
                ? Math.round((total / count) * 100.0) / 100.0 : 0;
        Label avgL = new Label("Class Average:  " + avg + " / 20");
        avgL.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        avgL.setTextFill(Color.web("#f5a623"));
        avgL.setPadding(new Insets(10, 16, 10, 16));
        avgL.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        return wrap(title, header, rows, avgL);
    }

    // ─── SCREEN 5: ANONYMOUS ──────────────────────────────────
    private VBox buildAnonymous() {
        Label title = makeTitle("🔗 Anonymous Questions");

        TextField qInput = styledField("Type your question...");
        TextField sInput = styledField("Your name...");
        Button submitBtn = navBtn("➕ Submit");

        Label upLabel = sectionLabel("Upvote a Question:");
        TextField idInput = styledField("Question ID (e.g. 1)");
        TextField s2Input = styledField("Your name...");
        Button upvoteBtn = navBtn("👍 Upvote");

        Label qTitle = sectionLabel("Questions Board:");
        VBox qList = new VBox(8);

        Runnable refresh = () -> {
            qList.getChildren().clear();
            for (AnonymousQuestion q : questionManager.getQuestions()) {
                HBox row = new HBox(10);
                row.setPadding(new Insets(8, 14, 8, 14));
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
                Label id = makeLabel("#" + q.getId(), 40, false);
                id.setTextFill(Color.web("#e94560"));
                Label content = makeLabel(q.getContent(), 210, false);
                Label votes = makeLabel("👍 " + q.getVotes(), 60, false);
                votes.setTextFill(Color.web("#f5a623"));
                Label status = new Label(q.isAnswered() ? "✔ Answered" : "Pending");
                status.setFont(Font.font("Arial", 13));
                status.setTextFill(q.isAnswered()
                        ? Color.web("#4caf50") : Color.GRAY);
                row.getChildren().addAll(id, content, votes, status);
                qList.getChildren().add(row);
            }
        };

        submitBtn.setOnAction(e -> {
            String q = qInput.getText().trim();
            String s = sInput.getText().trim();
            if (!q.isEmpty() && !s.isEmpty()) {
                gamBridge.onQuestionSubmit(questionIdCounter[0]++, q, s);
                qInput.clear();
                sInput.clear();
                refresh.run();
            }
        });

        upvoteBtn.setOnAction(e -> {
            String id = idInput.getText().trim();
            String s = s2Input.getText().trim();
            if (!id.isEmpty() && !s.isEmpty()) {
                try {
                    gamBridge.onUpvote(Integer.parseInt(id), s);
                    idInput.clear();
                    s2Input.clear();
                    refresh.run();
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid ID");
                }
            }
        });

        refresh.run();
        return wrap(title,
                sectionLabel("Submit Question:"),
                qInput, sInput, submitBtn,
                upLabel, idInput, s2Input, upvoteBtn,
                qTitle, qList);
    }

    // ─── SCREEN 6: QUIZ ───────────────────────────────────────
    private VBox buildQuiz() {
        Label title = makeTitle("❓ Live Quiz");

        // Teacher side — create question
        Label createLabel = sectionLabel("Teacher: Create Question");
        TextField qtField = styledField("Question text...");
        TextField caField = styledField("Correct answer...");
        TextField tlField = styledField("Time limit (seconds)...");
        Button createBtn = navBtn("➕ Create Question");

        // Student side — answer question
        Label answerLabel = sectionLabel("Student: Answer Question");
        TextField nameField = styledField("Your name...");
        TextField answerField = styledField("Your answer...");
        Button answerBtn = navBtn("✔ Submit Answer");

        Label feedback = new Label("");
        feedback.setFont(Font.font("Arial", 13));
        feedback.setTextFill(Color.web("#4caf50"));

        Label currentQ = new Label("No question yet.");
        currentQ.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        currentQ.setTextFill(Color.WHITE);
        currentQ.setPadding(new Insets(8, 14, 8, 14));
        currentQ.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");

        createBtn.setOnAction(e -> {
            String qText = qtField.getText().trim();
            String ca = caField.getText().trim();
            String tl = tlField.getText().trim();
            if (!qText.isEmpty() && !ca.isEmpty() && !tl.isEmpty()) {
                try {
                    int timeLimit = Integer.parseInt(tl);
                    LiveQuestion lq = new LiveQuestion(
                            quizQuestionCounter[0]++,
                            qText, 10,
                            Question.QuestionType.SHORT_ANSWER,
                            "General", "Medium", ca, timeLimit);
                    lq.openQuestion();
                    quizSession.addQuestion(lq);
                    if (!quizSession.isActive()) {
                        quizSession.startSession();
                    }
                    currentQ.setText("Q: " + qText);
                    responseManager.clearAnswers();
                    qtField.clear();
                    caField.clear();
                    tlField.clear();
                    feedback.setText("Question created and opened!");
                } catch (NumberFormatException ex) {
                    feedback.setText("Time limit must be a number.");
                }
            }
        });

        answerBtn.setOnAction(e -> {
            LiveQuestion lq = quizSession.getCurrentQuestion();
            if (lq == null) {
                feedback.setText("No question available.");
                return;
            }
            String name = nameField.getText().trim();
            String answer = answerField.getText().trim();
            if (!name.isEmpty() && !answer.isEmpty()) {
                Student student = new Student(
                        name.hashCode(), name, "Group A");
                String result = quizBridge.onAnswerSubmit(
                        student, lq, answer, 8);
                feedback.setText(result);
                nameField.clear();
                answerField.clear();
            }
        });

        return wrap(title,
                createLabel, qtField, caField, tlField, createBtn,
                sectionLabel("Current Question:"), currentQ,
                answerLabel, nameField, answerField, answerBtn,
                feedback);
    }

    // ─── SCREEN 7: REPORTS ────────────────────────────────────
    private VBox buildReports() {
        Label title = makeTitle("📁 Analytics & Reports");

        Label top3Label = sectionLabel("Top 3 Students:");
        VBox top3 = new VBox(8);
        List<String> topStudents = analyticsBridge.getTopStudents();
        String[] medals = {"🥇", "🥈", "🥉"};
        for (int i = 0; i < topStudents.size(); i++) {
            Label l = new Label(medals[i] + "  " + topStudents.get(i));
            l.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            l.setTextFill(Color.WHITE);
            l.setPadding(new Insets(8, 14, 8, 14));
            l.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
            top3.getChildren().add(l);
        }

        Label reportLabel = sectionLabel("Generate Report:");
        TextField totalField = styledField("Total students in class (e.g. 20)");
        Button genBtn = navBtn("📋 Generate Report");
        Button saveBtn = navBtn("💾 Save to File");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(160);
        reportArea.setStyle(
                "-fx-control-inner-background: #16213e; "
                + "-fx-text-fill: white; "
                + "-fx-background-radius: 8;");

        Label feedback = new Label("");
        feedback.setTextFill(Color.web("#4caf50"));
        feedback.setFont(Font.font("Arial", 13));

        genBtn.setOnAction(e -> {
            try {
                int total = Integer.parseInt(totalField.getText().trim());
                String report = analyticsBridge.generateReport(total);
                reportArea.setText(report);
            } catch (NumberFormatException ex) {
                reportArea.setText("Please enter a valid number.");
            }
        });

        saveBtn.setOnAction(e -> {
            try {
                int total = Integer.parseInt(totalField.getText().trim());
                analyticsBridge.saveReport("report.txt", total);
                feedback.setText("Report saved to report.txt ✔");
            } catch (NumberFormatException ex) {
                feedback.setText("Please enter a valid number.");
            }
        });

        return wrap(title, top3Label, top3,
                reportLabel, totalField,
                new HBox(10, genBtn, saveBtn),
                reportArea, feedback);
    }

    // ─── HELPERS ──────────────────────────────────────────────
    private Button navBtn(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: #e94560; "
                + "-fx-background-radius: 8; -fx-padding: 7 12 7 12;");
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #c73652; "
                + "-fx-background-radius: 8; -fx-padding: 7 12 7 12;"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #e94560; "
                + "-fx-background-radius: 8; -fx-padding: 7 12 7 12;"));
        return btn;
    }

    private Label makeTitle(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        l.setTextFill(Color.WHITE);
        l.setPadding(new Insets(0, 0, 8, 0));
        return l;
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        l.setTextFill(Color.LIGHTGRAY);
        l.setPadding(new Insets(6, 0, 2, 0));
        return l;
    }

    private HBox makeRow(String a, String b, String c, boolean header) {
        Label la = makeLabel(a, 90, header);
        Label lb = makeLabel(b, 180, header);
        Label lc = makeLabel(c, 120, header);
        HBox row = new HBox(la, lb, lc);
        row.setPadding(new Insets(9, 14, 9, 14));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(header
                ? "-fx-background-color: #e94560; -fx-background-radius: 8;"
                : "-fx-background-color: #16213e; -fx-background-radius: 8;");
        return row;
    }

    private HBox makeCard(String label, String value) {
        Label l = new Label(label);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        l.setTextFill(Color.LIGHTGRAY);
        l.setMinWidth(180);
        Label v = new Label(value);
        v.setFont(Font.font("Arial", 13));
        v.setTextFill(Color.WHITE);
        HBox card = new HBox(l, v);
        card.setPadding(new Insets(9, 14, 9, 14));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        return card;
    }

    private Label makeLabel(String text, double width, boolean bold) {
        Label l = new Label(text);
        l.setMinWidth(width);
        l.setTextFill(Color.WHITE);
        l.setFont(bold
                ? Font.font("Arial", FontWeight.BOLD, 13)
                : Font.font("Arial", 13));
        return l;
    }

    private TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; "
                + "-fx-prompt-text-fill: gray; -fx-background-radius: 8; "
                + "-fx-padding: 8;");
        return f;
    }

    private Label errLabel(String msg) {
        Label l = new Label(msg);
        l.setTextFill(Color.RED);
        return l;
    }

    private VBox wrap(javafx.scene.Node... nodes) {
        VBox box = new VBox(8);
        box.getChildren().addAll(nodes);
        box.setPadding(new Insets(24));
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
