package analytics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Dashboard {

    private JTable topStudentsTable;
    private JTable upcomingSessionsTable;
    private JTextArea reportArea;
    private JLabel participationLabel;
    private JLabel lastUpdatedLabel;
    private JPanel chartPanel;
    private JPanel gradeChartPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JList<String> studentList;
    private DefaultListModel<String> studentListModel;
    private JTextArea studentDetailsArea;
    private Timer autoRefreshTimer;
    
    // Real data
    private Map<String, Integer> studentScores;
    private int totalStudents = 0;
    private int activeStudents = 0;
    private String lastUpdatedTime;
    
    // Question data for analytics
    private Map<String, Integer> questionCorrect;
    private Map<String, Integer> questionTotal;
    
    // Upcoming sessions data
    private String[][] upcomingSessions = {
        {"Monday 10:00 AM", "Mathematics", "Algebra", "Group A"},
        {"Tuesday 2:00 PM", "Physics", "Mechanics", "Group B"},
        {"Wednesday 11:00 AM", "Programming", "Java Basics", "Group A"},
        {"Thursday 1:00 PM", "English", "Literature", "Group C"},
        {"Friday 9:00 AM", "History", "World Wars", "Group B"}
    };

    // Professional Color Scheme
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color SIDEBAR_COLOR = new Color(30, 38, 62);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(90, 92, 235);
    private static final Color SECONDARY_COLOR = new Color(16, 185, 129);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color TEXT_DARK = new Color(30, 38, 62);
    private static final Color TEXT_LIGHT = new Color(107, 114, 128);
    private static final Color CHART_A = new Color(90, 92, 235);
    private static final Color CHART_B = new Color(139, 92, 246);
    private static final Color CHART_C = new Color(236, 72, 153);

    public Dashboard() {
        loadRealData();
        loadQuestionData();
        updateLastUpdatedTime();
        initializeUI();
        startAutoRefresh();
    }
    
    private void loadRealData() {
        FileManager fm = new FileManager();
        studentScores = fm.loadScores("scores.txt");
        
        totalStudents = studentScores.size();
        activeStudents = 0;
        for (int score : studentScores.values()) {
            if (score > 0) activeStudents++;
        }
        
        if (studentScores.isEmpty()) {
            studentScores = new HashMap<>();
            studentScores.put("Ali", 100);
            studentScores.put("Sara", 120);
            studentScores.put("Yacine", 90);
            studentScores.put("Lina", 110);
            studentScores.put("Amira", 95);
            studentScores.put("Omar", 85);
            studentScores.put("Khaled", 75);
            studentScores.put("Nadia", 105);
            studentScores.put("Sofiane", 88);
            studentScores.put("Malak", 115);
            totalStudents = 10;
            activeStudents = 10;
        }
    }
    
    private void loadQuestionData() {
        questionCorrect = new HashMap<>();
        questionTotal = new HashMap<>();
        
        String[] questions = {"Q1: Algorithms", "Q2: Data Structures", "Q3: Recursion", 
                              "Q4: Sorting", "Q5: Searching", "Q6: Graphs", 
                              "Q7: Dynamic Programming", "Q8: OOP Basics"};
        
        int[] correct = {30, 45, 25, 60, 55, 35, 20, 75};
        int[] total = {100, 100, 100, 100, 100, 100, 100, 100};
        
        for (int i = 0; i < questions.length; i++) {
            questionCorrect.put(questions[i], correct[i]);
            questionTotal.put(questions[i], total[i]);
        }
    }
    
    private void updateLastUpdatedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        lastUpdatedTime = sdf.format(new Date());
    }
    
    private void startAutoRefresh() {
        autoRefreshTimer = new Timer(30000, e -> {
            loadRealData();
            updateLastUpdatedTime();
            refreshCurrentScreen("Dashboard");
        });
        autoRefreshTimer.start();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("ClassPulse+ Dashboard");
        frame.setSize(1300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(1200, 700));

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_COLOR);

        JPanel sidebarPanel = createSidebar();
        main.add(sidebarPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BG_COLOR);
        
        mainContentPanel.add(createDashboardScreen(), "Dashboard");
        mainContentPanel.add(createAnalyticsScreen(), "Analytics");
        mainContentPanel.add(createStudentsScreen(), "Students");
        mainContentPanel.add(createReportsScreen(), "Reports");
        mainContentPanel.add(createSettingsScreen(), "Settings");
        
        main.add(mainContentPanel, BorderLayout.CENTER);
        
        frame.setContentPane(main);
        frame.setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JLabel logoLabel = new JLabel("ClassPulse+", SwingConstants.LEFT);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 30, 10);
        sidebar.add(logoLabel, gbc);
        
        String[] menuItems = {"Dashboard", "Analytics", "Students", "Reports", "Settings"};
        
        gbc.insets = new Insets(5, 10, 5, 10);
        
        for (int i = 0; i < menuItems.length; i++) {
            final String item = menuItems[i];
            JButton btn = createSidebarButton(item);
            btn.addActionListener(e -> {
                cardLayout.show(mainContentPanel, item);
                refreshCurrentScreen(item);
            });
            gbc.gridy = i + 1;
            sidebar.add(btn, gbc);
        }
        
        return sidebar;
    }

    private void refreshCurrentScreen(String screen) {
        updateLastUpdatedTime();
        if (screen.equals("Dashboard")) {
            refreshDashboard();
        } else if (screen.equals("Analytics")) {
            refreshAnalytics();
        } else if (screen.equals("Students")) {
            refreshStudentsList();
        }
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(new Color(200, 200, 210));
        btn.setBackground(SIDEBAR_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(45, 55, 85));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR_COLOR);
                btn.setForeground(new Color(200, 200, 210));
            }
        });
        return btn;
    }

    // ========== DASHBOARD SCREEN ==========
    private JPanel createDashboardScreen() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(BG_COLOR);
        
        JPanel headerPanel = createHeader();
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        gridPanel.setOpaque(false);
        gridPanel.add(createTopStudentsCard());
        gridPanel.add(createReportCard());
        gridPanel.add(createChartCard());
        gridPanel.add(createParticipationCard());
        gridPanel.add(createUpcomingSessionsCard());
        gridPanel.add(createQuickActionsCard());
        
        panel.add(gridPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttonPanel.setOpaque(false);
        
        JButton generateBtn = createStyledButton("Refresh Dashboard", PRIMARY_COLOR);
        generateBtn.addActionListener(e -> refreshDashboard());
        
        JButton exportBtn = createStyledButton("Export Report", SECONDARY_COLOR);
        exportBtn.addActionListener(e -> exportReport());
        
        buttonPanel.add(generateBtn);
        buttonPanel.add(exportBtn);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        
        lastUpdatedLabel = new JLabel("Last updated: " + lastUpdatedTime, SwingConstants.CENTER);
        lastUpdatedLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lastUpdatedLabel.setForeground(TEXT_LIGHT);
        southPanel.add(lastUpdatedLabel, BorderLayout.SOUTH);
        
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void refreshDashboard() {
        loadRealData();
        updateLastUpdatedTime();
        updateTopStudentsTable();
        updateReportArea();
        updateParticipationLabel();
        if (chartPanel != null) chartPanel.repaint();
        if (lastUpdatedLabel != null) lastUpdatedLabel.setText("Last updated: " + lastUpdatedTime);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome back, Teacher", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_DARK);
        
        JLabel dateLabel = new JLabel(getCurrentDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(TEXT_LIGHT);
        
        leftPanel.add(welcomeLabel);
        leftPanel.add(dateLabel);
        header.add(leftPanel, BorderLayout.WEST);
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        statsPanel.setOpaque(false);
        
        statsPanel.add(createStatPill("Students: " + totalStudents, SECONDARY_COLOR));
        statsPanel.add(createStatPill("Active: " + activeStudents, PRIMARY_COLOR));
        
        double avgScore = studentScores.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        statsPanel.add(createStatPill("Avg: " + String.format("%.0f", avgScore), WARNING_COLOR));
        
        header.add(statsPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createUpcomingSessionsCard() {
        JPanel card = createStyledCard("Upcoming Sessions");
        card.setLayout(new BorderLayout());
        
        String[] cols = {"Time", "Subject", "Topic", "Group"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (String[] session : upcomingSessions) {
            model.addRow(session);
        }
        
        upcomingSessionsTable = new JTable(model);
        upcomingSessionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        upcomingSessionsTable.setRowHeight(26);
        upcomingSessionsTable.setShowGrid(false);
        
        JTableHeader header = upcomingSessionsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(CARD_COLOR);
        header.setForeground(TEXT_LIGHT);
        
        JScrollPane scrollPane = new JScrollPane(upcomingSessionsTable);
        scrollPane.setBorder(null);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createQuickActionsCard() {
        JPanel card = createStyledCard("Quick Actions");
        card.setLayout(new GridLayout(4, 1, 8, 8));
        
        JButton newQuizBtn = createSmallActionButton("Start Live Quiz", PRIMARY_COLOR);
        JButton viewReportsBtn = createSmallActionButton("View Analytics", SECONDARY_COLOR);
        JButton messageBtn = createSmallActionButton("Send Announcement", WARNING_COLOR);
        JButton gradeBtn = createSmallActionButton("Grade Submissions", DANGER_COLOR);
        
        card.add(newQuizBtn);
        card.add(viewReportsBtn);
        card.add(messageBtn);
        card.add(gradeBtn);
        
        return card;
    }
    
    private JButton createSmallActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        btn.addActionListener(e -> {
            if (text.equals("Start Live Quiz")) {
                JOptionPane.showMessageDialog(null, 
                    "Starting Live Quiz!\n\nTopics available:\n• Algorithms (30% correct)\n• Data Structures (45% correct)\n• Recursion (25% correct)");
            } else if (text.equals("View Analytics")) {
                JOptionPane.showMessageDialog(null,
                    "Class Analytics:\n• Average: 102 pts\n• Top: Sara (120)\n• Needs Review: Recursion");
            } else if (text.equals("Send Announcement")) {
                JOptionPane.showMessageDialog(null,
                    "Send to parents:\n• Students below 70%\n• Progress reports\n• Upcoming exams");
            } else if (text.equals("Grade Submissions")) {
                JOptionPane.showMessageDialog(null,
                    "Pending:\n• Quiz 3: 8 students\n• Homework: 3 students");
            }
        });
        return btn;
    }

    // ========== ANALYTICS SCREEN ==========
    private JPanel createAnalyticsScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(BG_COLOR);
        
        JLabel title = new JLabel("Detailed Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        tabbedPane.addTab("Performance Summary", createPerformanceSummaryTab());
        tabbedPane.addTab("Grade Distribution", createGradeDistributionTab());
        tabbedPane.addTab("Question Analytics", createQuestionAnalyticsTab());
        tabbedPane.addTab("Class Trends", createTrendsTab());
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        JButton refreshBtn = createStyledButton("Refresh Analytics", PRIMARY_COLOR);
        refreshBtn.addActionListener(e -> refreshAnalytics());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPerformanceSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        
        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        double avg = studentScores.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        int max = studentScores.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int min = studentScores.values().stream().mapToInt(Integer::intValue).min().orElse(0);
        double median = calculateMedian();
        double stdDev = calculateStdDev(avg);
        
        StringBuilder summary = new StringBuilder();
        summary.append("==============================================\n");
        summary.append("        CLASS PERFORMANCE SUMMARY\n");
        summary.append("==============================================\n\n");
        summary.append(String.format("Total Students:     %d\n", totalStudents));
        summary.append(String.format("Active Students:    %d\n", activeStudents));
        summary.append(String.format("Participation Rate: %.1f%%\n", (activeStudents * 100.0 / totalStudents)));
        summary.append("\n----------------------------------------------\n");
        summary.append("SCORE STATISTICS\n");
        summary.append("----------------------------------------------\n");
        summary.append(String.format("Average Score:      %.1f\n", avg));
        summary.append(String.format("Median Score:       %.1f\n", median));
        summary.append(String.format("Highest Score:      %d\n", max));
        summary.append(String.format("Lowest Score:       %d\n", min));
        summary.append(String.format("Standard Deviation: %.1f\n", stdDev));
        
        summaryArea.setText(summary.toString());
        panel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private double calculateMedian() {
        List<Integer> scores = new ArrayList<>(studentScores.values());
        scores.sort(Integer::compareTo);
        int size = scores.size();
        if (size % 2 == 0) {
            return (scores.get(size / 2 - 1) + scores.get(size / 2)) / 2.0;
        } else {
            return scores.get(size / 2);
        }
    }
    
    private double calculateStdDev(double mean) {
        double sum = 0;
        for (int score : studentScores.values()) {
            sum += Math.pow(score - mean, 2);
        }
        return Math.sqrt(sum / totalStudents);
    }
    
    private JPanel createGradeDistributionTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        
        gradeChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGradeChart(g);
            }
        };
        gradeChartPanel.setPreferredSize(new Dimension(350, 200));
        gradeChartPanel.setBackground(CARD_COLOR);
        gradeChartPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 240), 1));
        
        JTextArea gradeStats = new JTextArea();
        gradeStats.setEditable(false);
        gradeStats.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        int A = 0, B = 0, C = 0, D = 0, F = 0;
        for (int score : studentScores.values()) {
            if (score >= 90) A++;
            else if (score >= 80) B++;
            else if (score >= 70) C++;
            else if (score >= 60) D++;
            else F++;
        }
        
        gradeStats.setText(String.format(
            "GRADE BREAKDOWN:\n\n" +
            "A (90-100): %d students\n" +
            "B (80-89):  %d students\n" +
            "C (70-79):  %d students\n" +
            "D (60-69):  %d students\n" +
            "F (Below):  %d students\n\n" +
            "Total: %d students\n" +
            "Passing Rate: %.1f%%",
            A, B, C, D, F, totalStudents, ((A + B + C) * 100.0 / totalStudents)
        ));
        
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(CARD_COLOR);
        chartContainer.setBorder(BorderFactory.createTitledBorder("Grade Distribution"));
        chartContainer.add(gradeChartPanel, BorderLayout.CENTER);
        
        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.setBackground(CARD_COLOR);
        statsContainer.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsContainer.add(new JScrollPane(gradeStats), BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chartContainer, statsContainer);
        splitPane.setResizeWeight(0.6);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void drawGradeChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int A = 0, B = 0, C = 0, D = 0, F = 0;
        for (int score : studentScores.values()) {
            if (score >= 90) A++;
            else if (score >= 80) B++;
            else if (score >= 70) C++;
            else if (score >= 60) D++;
            else F++;
        }
        
        int[] counts = {A, B, C, D, F};
        Color[] colors = {CHART_A, CHART_B, CHART_C, WARNING_COLOR, DANGER_COLOR};
        String[] labels = {"A", "B", "C", "D", "F"};
        
        int width = gradeChartPanel.getWidth() - 80;
        int height = gradeChartPanel.getHeight() - 70;
        int barWidth = width / 5 - 10;
        int maxCount = Math.max(Math.max(A, B), Math.max(C, Math.max(D, F)));
        
        g2d.setColor(TEXT_LIGHT);
        g2d.drawLine(50, 25, 50, height + 35);
        g2d.drawLine(50, height + 35, width + 60, height + 35);
        
        for (int i = 0; i <= 5; i++) {
            int countValue = (maxCount / 5) * i;
            int y = height + 35 - (i * height / 5);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2d.drawString(String.valueOf(countValue), 30, y + 3);
        }
        
        for (int i = 0; i < 5; i++) {
            int barHeight = (counts[i] * height) / Math.max(1, maxCount);
            int x = 65 + i * (barWidth + 8);
            int y = height + 35 - barHeight;
            
            g2d.setColor(colors[i]);
            g2d.fillRoundRect(x, y, barWidth, barHeight, 6, 6);
            
            g2d.setColor(TEXT_DARK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2d.drawString(labels[i], x + barWidth / 2 - 4, height + 50);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2d.drawString(String.valueOf(counts[i]), x + barWidth / 2 - 5, y - 5);
        }
    }
    
    private JPanel createQuestionAnalyticsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        
        String[] cols = {"Question", "Correct", "Total", "Rate", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(questionCorrect.entrySet());
        sorted.sort((a, b) -> {
            double rateA = (a.getValue() * 100.0) / questionTotal.get(a.getKey());
            double rateB = (b.getValue() * 100.0) / questionTotal.get(b.getKey());
            return Double.compare(rateA, rateB);
        });
        
        for (Map.Entry<String, Integer> entry : sorted) {
            String question = entry.getKey();
            int correct = entry.getValue();
            int total = questionTotal.get(question);
            double rate = (correct * 100.0) / total;
            String status = rate < 50 ? "Difficult" : (rate < 70 ? "Medium" : "Easy");
            
            model.addRow(new Object[]{
                question, correct, total, String.format("%.0f%%", rate), status
            });
        }
        
        JTable questionTable = new JTable(model);
        questionTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        questionTable.setRowHeight(28);
        questionTable.setShowGrid(false);
        
        JTableHeader header = questionTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        JScrollPane scrollPane = new JScrollPane(questionTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JTextArea recommendations = new JTextArea();
        recommendations.setEditable(false);
        recommendations.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        recommendations.setBackground(new Color(255, 245, 235));
        recommendations.setBorder(BorderFactory.createTitledBorder("Recommendations"));
        
        StringBuilder recText = new StringBuilder();
        recText.append("Based on question performance:\n\n");
        for (Map.Entry<String, Integer> entry : sorted) {
            double rate = (entry.getValue() * 100.0) / questionTotal.get(entry.getKey());
            if (rate < 50) {
                recText.append("• Review ").append(entry.getKey()).append(" - ").append(String.format("%.0f%%", rate)).append(" correct\n");
            }
        }
        recText.append("\nSuggested actions:\n");
        recText.append("• Schedule extra help sessions\n");
        recText.append("• Provide additional practice\n");
        recText.append("• Consider peer tutoring");
        
        recommendations.setText(recText.toString());
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(new JScrollPane(recommendations), BorderLayout.CENTER);
        southPanel.setPreferredSize(new Dimension(0, 120));
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTrendsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        
        JTextArea trendsArea = new JTextArea();
        trendsArea.setEditable(false);
        trendsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        double avg = studentScores.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        
        StringBuilder trends = new StringBuilder();
        trends.append("==============================================\n");
        trends.append("           CLASS TRENDS ANALYSIS\n");
        trends.append("==============================================\n\n");
        
        trends.append("PERFORMANCE TRENDS:\n");
        trends.append("----------------------------------------------\n");
        
        if (avg >= 85) {
            trends.append("✓ Class is performing excellently!\n");
            trends.append("✓ Consider introducing advanced topics\n");
        } else if (avg >= 70) {
            trends.append("✓ Class is performing well\n");
            trends.append("→ Focus on improving difficult areas\n");
        } else {
            trends.append("⚠ Class needs additional support\n");
            trends.append("→ Schedule review sessions\n");
        }
        
        trends.append("\nSTUDENT ENGAGEMENT:\n");
        trends.append("----------------------------------------------\n");
        trends.append(String.format("Participation Rate: %.1f%%\n", (activeStudents * 100.0 / totalStudents)));
        
        trends.append("\nIMPROVEMENT AREAS:\n");
        trends.append("----------------------------------------------\n");
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(questionCorrect.entrySet());
        sorted.sort((a, b) -> {
            double rateA = (a.getValue() * 100.0) / questionTotal.get(a.getKey());
            double rateB = (b.getValue() * 100.0) / questionTotal.get(b.getKey());
            return Double.compare(rateA, rateB);
        });
        
        trends.append("Top 3 topics needing review:\n");
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            String q = sorted.get(i).getKey();
            double rate = (sorted.get(i).getValue() * 100.0) / questionTotal.get(q);
            trends.append("  ").append(i + 1).append(". ").append(q).append(" (").append(String.format("%.0f%%", rate)).append(")\n");
        }
        
        trendsArea.setText(trends.toString());
        panel.add(new JScrollPane(trendsArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshAnalytics() {
        loadRealData();
        loadQuestionData();
        if (gradeChartPanel != null) gradeChartPanel.repaint();
    }
    
    // ========== STUDENTS SCREEN ==========
    private JPanel createStudentsScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(BG_COLOR);
        
        JLabel title = new JLabel("Student Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        contentPanel.setOpaque(false);
        
        JPanel listCard = createAnalyticsCard("All Students");
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        studentList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentList.setFixedCellHeight(32);
        listCard.add(new JScrollPane(studentList), BorderLayout.CENTER);
        contentPanel.add(listCard);
        
        JPanel detailsCard = createAnalyticsCard("Student Details");
        studentDetailsArea = new JTextArea();
        studentDetailsArea.setEditable(false);
        studentDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsCard.add(new JScrollPane(studentDetailsArea), BorderLayout.CENTER);
        contentPanel.add(detailsCard);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        addPanel.setBackground(CARD_COLOR);
        addPanel.setBorder(BorderFactory.createTitledBorder("Add/Update Student"));
        
        JTextField nameField = new JTextField(12);
        JTextField scoreField = new JTextField(4);
        JButton addBtn = createStyledButton("Add", SECONDARY_COLOR);
        JButton updateBtn = createStyledButton("Update", PRIMARY_COLOR);
        
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String scoreText = scoreField.getText().trim();
            if (!name.isEmpty() && !scoreText.isEmpty()) {
                try {
                    int score = Integer.parseInt(scoreText);
                    studentScores.put(name, score);
                    totalStudents = studentScores.size();
                    refreshStudentsList();
                    nameField.setText("");
                    scoreField.setText("");
                    JOptionPane.showMessageDialog(panel, "Student added!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid score!");
                }
            }
        });
        
        updateBtn.addActionListener(e -> {
            String selected = studentList.getSelectedValue();
            if (selected != null) {
                String name = selected.split(" - ")[0];
                String scoreText = scoreField.getText().trim();
                if (!scoreText.isEmpty()) {
                    try {
                        int score = Integer.parseInt(scoreText);
                        studentScores.put(name, score);
                        refreshStudentsList();
                        scoreField.setText("");
                        JOptionPane.showMessageDialog(panel, "Score updated!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel, "Invalid score!");
                    }
                }
            }
        });
        
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Score:"));
        addPanel.add(scoreField);
        addPanel.add(addBtn);
        addPanel.add(updateBtn);
        
        panel.add(addPanel, BorderLayout.SOUTH);
        
        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = studentList.getSelectedValue();
                if (selected != null) {
                    String name = selected.split(" - ")[0];
                    int score = studentScores.getOrDefault(name, 0);
                    String grade = getGrade(score);
                    String status = getStatus(score);
                    
                    studentDetailsArea.setText(String.format(
                        "STUDENT PROFILE\n\n" +
                        "Name: %s\n" +
                        "Score: %d points\n" +
                        "Grade: %s\n" +
                        "Status: %s\n\n" +
                        "Rank: %d/%d\n" +
                        "Above Average: %s\n\n" +
                        "Recommendation:\n%s",
                        name, score, grade, status,
                        getRank(name), totalStudents,
                        score > getAverageScore() ? "Yes" : "No",
                        getRecommendation(score)
                    ));
                    scoreField.setText(String.valueOf(score));
                }
            }
        });
        
        refreshStudentsList();
        
        return panel;
    }
    
    private String getGrade(int score) {
        if (score >= 90) return "A (Excellent)";
        if (score >= 80) return "B (Good)";
        if (score >= 70) return "C (Average)";
        if (score >= 60) return "D (Below)";
        return "F (Failing)";
    }
    
    private String getStatus(int score) {
        if (score >= 90) return "Outstanding";
        if (score >= 75) return "Satisfactory";
        if (score >= 60) return "Needs Improvement";
        return "Critical Attention";
    }
    
    private String getRecommendation(int score) {
        if (score >= 90) return "Keep up the excellent work!";
        if (score >= 75) return "Good progress. Keep improving!";
        if (score >= 60) return "Schedule extra help sessions.";
        return "Immediate intervention needed.";
    }
    
    private int getRank(String studentName) {
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(studentScores.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getKey().equals(studentName)) {
                return i + 1;
            }
        }
        return sorted.size();
    }
    
    private double getAverageScore() {
        return studentScores.values().stream().mapToInt(Integer::intValue).average().orElse(0);
    }
    
    private void refreshStudentsList() {
        if (studentListModel != null) {
            studentListModel.clear();
            List<Map.Entry<String, Integer>> sorted = new ArrayList<>(studentScores.entrySet());
            sorted.sort((a, b) -> b.getValue() - a.getValue());
            for (Map.Entry<String, Integer> entry : sorted) {
                studentListModel.addElement(entry.getKey() + " - " + entry.getValue() + " pts");
            }
        }
    }
    
    // ========== REPORTS SCREEN ==========
    private JPanel createReportsScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(BG_COLOR);
        
        JLabel title = new JLabel("Reports & Export");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);
        
        JTextArea reportDisplay = new JTextArea();
        reportDisplay.setEditable(false);
        reportDisplay.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(reportDisplay);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        JButton generateReportBtn = createStyledButton("Generate Full Report", PRIMARY_COLOR);
        generateReportBtn.addActionListener(e -> {
            String report = generateFullReport();
            reportDisplay.setText(report);
        });
        
        JButton exportTxtBtn = createStyledButton("Export as TXT", SECONDARY_COLOR);
        exportTxtBtn.addActionListener(e -> {
            FileManager fm = new FileManager();
            fm.saveToFile(generateFullReport(), "class_report_" + System.currentTimeMillis() + ".txt");
            JOptionPane.showMessageDialog(panel, "Report exported!");
        });
        
        JButton exportCSVBtn = createStyledButton("Export as CSV", WARNING_COLOR);
        exportCSVBtn.addActionListener(e -> exportCSV());
        
        buttonPanel.add(generateReportBtn);
        buttonPanel.add(exportTxtBtn);
        buttonPanel.add(exportCSVBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void exportCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("Student Name,Score,Grade,Status\n");
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(studentScores.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());
        
        for (Map.Entry<String, Integer> entry : sorted) {
            csv.append(entry.getKey()).append(",");
            csv.append(entry.getValue()).append(",");
            csv.append(getGrade(entry.getValue())).append(",");
            csv.append(getStatus(entry.getValue())).append("\n");
        }
        
        FileManager fm = new FileManager();
        fm.saveToFile(csv.toString(), "students_data_" + System.currentTimeMillis() + ".csv");
        JOptionPane.showMessageDialog(null, "CSV exported!");
    }
    
    // ========== SETTINGS SCREEN ==========
    private JPanel createSettingsScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(BG_COLOR);
        
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);
        
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        settingsPanel.add(new JLabel("Auto-refresh (seconds):"), gbc);
        gbc.gridx = 1;
        JSpinner refreshSpinner = new JSpinner(new SpinnerNumberModel(30, 10, 120, 5));
        settingsPanel.add(refreshSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        settingsPanel.add(new JLabel("Default View:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> viewCombo = new JComboBox<>(new String[]{"Dashboard", "Analytics", "Students", "Reports"});
        settingsPanel.add(viewCombo, gbc);
        
        JButton saveBtn = createStyledButton("Save Settings", PRIMARY_COLOR);
        saveBtn.addActionListener(e -> {
            int refreshRate = (Integer) refreshSpinner.getValue();
            if (autoRefreshTimer != null) {
                autoRefreshTimer.stop();
                autoRefreshTimer = new Timer(refreshRate * 1000, ev -> {
                    loadRealData();
                    updateLastUpdatedTime();
                    refreshCurrentScreen("Dashboard");
                });
                autoRefreshTimer.start();
            }
            JOptionPane.showMessageDialog(panel, "Settings saved! Auto-refresh: " + refreshRate + " seconds");
        });
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        settingsPanel.add(saveBtn, gbc);
        
        panel.add(settingsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // ========== COMMON COMPONENTS ==========
    private JPanel createTopStudentsCard() {
        JPanel card = createStyledCard("Top Performers");
        card.setLayout(new BorderLayout());
        
        String[] cols = {"Student", "Score"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        topStudentsTable = new JTable(model);
        topStudentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        topStudentsTable.setRowHeight(28);
        topStudentsTable.setShowGrid(false);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        topStudentsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        JTableHeader header = topStudentsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(CARD_COLOR);
        header.setForeground(TEXT_LIGHT);
        
        JScrollPane scrollPane = new JScrollPane(topStudentsTable);
        scrollPane.setBorder(null);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    private void updateTopStudentsTable() {
        DefaultTableModel model = (DefaultTableModel) topStudentsTable.getModel();
        model.setRowCount(0);
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(studentScores.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());
        
        int count = 0;
        for (Map.Entry<String, Integer> entry : sorted) {
            if (count >= 5) break;
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            count++;
        }
    }

    private JPanel createReportCard() {
        JPanel card = createStyledCard("Class Report");
        card.setLayout(new BorderLayout());
        
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        reportArea.setBackground(CARD_COLOR);
        reportArea.setForeground(TEXT_DARK);
        reportArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 240), 1));
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }
    
    private void updateReportArea() {
        AnalyticsManager am = new AnalyticsManager();
        double participation = am.calculateParticipation(totalStudents, activeStudents);
        
        StringBuilder report = new StringBuilder();
        report.append("====================================\n");
        report.append("      CLASSPULSE+ REPORT\n");
        report.append("====================================\n\n");
        report.append("Participation: ").append(String.format("%.0f", participation)).append("%\n\n");
        
        report.append("TOP STUDENTS:\n");
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(studentScores.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            report.append(sorted.get(i).getKey()).append(": ").append(sorted.get(i).getValue()).append(" pts\n");
        }
        
        report.append("\nDIFFICULT QUESTIONS:\n");
        for (Map.Entry<String, Integer> entry : questionCorrect.entrySet()) {
            double rate = (entry.getValue() * 100.0) / questionTotal.get(entry.getKey());
            if (rate < 50) {
                report.append(entry.getKey()).append(" (").append(String.format("%.0f%%", rate)).append(")\n");
            }
        }
        
        reportArea.setText(report.toString());
    }

    private JPanel createChartCard() {
        JPanel card = createStyledCard("Performance Overview");
        card.setLayout(new BorderLayout());
        
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setPreferredSize(new Dimension(300, 200));
        chartPanel.setBackground(CARD_COLOR);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 240), 1));
        
        card.add(chartPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createParticipationCard() {
        JPanel card = createStyledCard("Attendance Rate");
        card.setLayout(new GridBagLayout());
        
        participationLabel = new JLabel("0%", SwingConstants.CENTER);
        participationLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        participationLabel.setForeground(SECONDARY_COLOR);
        
        JLabel subLabel = new JLabel("of students participated", SwingConstants.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLabel.setForeground(TEXT_LIGHT);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(participationLabel);
        textPanel.add(subLabel);
        
        card.add(textPanel);
        return card;
    }
    
    private void updateParticipationLabel() {
        AnalyticsManager am = new AnalyticsManager();
        double participation = am.calculateParticipation(totalStudents, activeStudents);
        participationLabel.setText(String.format("%.0f%%", participation));
    }

    private JPanel createStyledCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 240), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        return card;
    }
    
    private JPanel createAnalyticsCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 240), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        return card;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private JPanel createStatPill(String text, Color bgColor) {
        JPanel pill = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        pill.setBackground(bgColor);
        pill.setLayout(new BorderLayout());
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        pill.add(label);
        return pill;
    }

    private void drawChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(studentScores.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());
        
        int chartHeight = Math.min(5, sorted.size());
        String[] students = new String[chartHeight];
        int[] scores = new int[chartHeight];
        
        for (int i = 0; i < chartHeight; i++) {
            students[i] = sorted.get(i).getKey();
            scores[i] = sorted.get(i).getValue();
        }
        
        if (students.length == 0) {
            students = new String[]{"No Data"};
            scores = new int[]{0};
        }
        
        int width = chartPanel.getWidth() - 60;
        int height = chartPanel.getHeight() - 50;
        int barWidth = Math.max(25, width / students.length - 8);
        int maxScore = Math.max(150, getMaxScore(scores));
        
        g2d.setColor(TEXT_LIGHT);
        g2d.drawLine(50, 25, 50, height + 25);
        g2d.drawLine(50, height + 25, width + 50, height + 25);
        
        for (int i = 0; i <= 5; i++) {
            int scoreValue = (maxScore / 5) * i;
            int y = height + 25 - (i * height / 5);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2d.drawString(String.valueOf(scoreValue), 25, y + 3);
        }
        
        Color[] colors = {CHART_A, CHART_B, CHART_C, CHART_A, CHART_B};
        for (int i = 0; i < students.length; i++) {
            int barHeight = Math.max(4, (scores[i] * height) / maxScore);
            int x = 60 + i * (barWidth + 6);
            int y = height + 25 - barHeight;
            
            g2d.setColor(colors[i % colors.length]);
            g2d.fillRoundRect(x, y, barWidth, barHeight, 6, 6);
            
            g2d.setColor(TEXT_DARK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            String shortName = students[i].length() > 8 ? students[i].substring(0, 6) + ".." : students[i];
            g2d.drawString(shortName, x + barWidth / 4, height + 38);
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 9));
            g2d.drawString(String.valueOf(scores[i]), x + barWidth / 3, y - 3);
        }
    }
    
    private int getMaxScore(int[] scores) {
        int max = 0;
        for (int s : scores) {
            if (s > max) max = s;
        }
        return max;
    }

    private String generateFullReport() {
        AnalyticsManager am = new AnalyticsManager();
        double participation = am.calculateParticipation(totalStudents, activeStudents);
        double avgScore = studentScores.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        
        StringBuilder report = new StringBuilder();
        report.append("============================================================\n");
        report.append("              CLASSPULSE+ FULL REPORT\n");
        report.append("============================================================\n\n");
        report.append("Generated: ").append(getCurrentDateTime()).append("\n\n");
        
        report.append("------------------------------------------------------------\n");
        report.append("CLASS OVERVIEW\n");
        report.append("------------------------------------------------------------\n");
        report.append(String.format("Total Students:     %d\n", totalStudents));
        report.append(String.format("Active Students:    %d\n", activeStudents));
        report.append(String.format("Participation Rate: %.1f%%\n", participation));
        report.append(String.format("Class Average:      %.1f pts\n", avgScore));
        
        report.append("\n------------------------------------------------------------\n");
        report.append("STUDENT RANKINGS\n");
        report.append("------------------------------------------------------------\n");
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(studentScores.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());
        int rank = 1;
        for (Map.Entry<String, Integer> entry : sorted) {
            report.append(String.format("%d. %-15s %3d pts\n", rank, entry.getKey(), entry.getValue()));
            rank++;
        }
        
        report.append("\n------------------------------------------------------------\n");
        report.append("QUESTION PERFORMANCE\n");
        report.append("------------------------------------------------------------\n");
        
        for (Map.Entry<String, Integer> entry : questionCorrect.entrySet()) {
            double rate = (entry.getValue() * 100.0) / questionTotal.get(entry.getKey());
            String difficulty = rate < 50 ? "DIFFICULT" : (rate < 70 ? "MEDIUM" : "EASY");
            report.append(String.format("%-20s %3d/%d  (%.1f%%)  %s\n", 
                entry.getKey(), entry.getValue(), questionTotal.get(entry.getKey()), rate, difficulty));
        }
        
        report.append("\n============================================================\n");
        report.append("END OF REPORT\n");
        report.append("============================================================\n");
        
        return report.toString();
    }

    private void exportReport() {
        FileManager fm = new FileManager();
        String report = generateFullReport();
        String filename = "classpulse_report_" + System.currentTimeMillis() + ".txt";
        fm.saveToFile(report, filename);
        JOptionPane.showMessageDialog(null, "Report exported: " + filename);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        return sdf.format(new Date());
    }
    
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(Dashboard::new);
    }
}