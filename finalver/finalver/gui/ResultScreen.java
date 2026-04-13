package finalver.gui;

import javax.swing.*;
import java.awt.*;
public class ResultScreen extends JFrame{
    private JTextArea resultArea;
    private JButton closeButton;
    public ResultScreen(){
        setTitle("Result Screen - ClassPulse+");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        resultArea=new JTextArea();
        resultArea.setEditable(false);
        closeButton=new JButton("Close");
        add(new JScrollPane(resultArea),BorderLayout.CENTER);
        add(closeButton,BorderLayout.SOUTH);
        closeButton.addActionListener(e ->setVisible(false));
    }
    public void setResults(String results){
        resultArea.setText(results);
    }
}
