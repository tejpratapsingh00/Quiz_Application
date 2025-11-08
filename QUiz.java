import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class QuizApp extends JFrame implements ActionListener {
    private final String[] questions = {
            "1) What is the capital of India?",
            "2) What comes after Sunday?",
            "3) When is our Independence Day?"
    };

    private final String[][] options = {
            {"Kolkata", "Mumbai", "New Delhi", "Chennai"},
            {"Monday", "Tuesday", "Friday", "Saturday"},
            {"2 Oct", "26 Jan", "15 Aug", "23 Sept"}
    };

    // answers stored as 0-based index
    private final int[] answers = {2, 0, 2};

    private int index = 0;
    private int correct = 0;

    private final JLabel questionLabel = new JLabel("", SwingConstants.CENTER);
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionsGroup = new ButtonGroup();
    private final JButton nextButton = new JButton("Next");

    public QuizApp() {
        setTitle("Simple Quiz App");
        setSize(520, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(nextButton);
        add(bottom, BorderLayout.SOUTH);

        nextButton.addActionListener(this);

        loadQuestion();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Ensure user selected an option
            int selected = getSelectedOption();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Please select an option before proceeding.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selected == answers[index]) {
                correct++;
            }

            index++;
            if (index < questions.length) {
                loadQuestion();
            } else {
                showResult();
            }
        } catch (Exception ex) {
            // show stack trace in a dialog so you can copy-paste if something unexpected happens
            String message = "Unexpected error: " + ex.toString();
            JTextArea ta = new JTextArea();
            ta.setText(getStackTraceString(ex));
            ta.setEditable(false);
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(500, 200));
            JOptionPane.showMessageDialog(this, sp, message, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadQuestion() {
        optionsGroup.clearSelection();
        questionLabel.setText("<html><body style='width:450px; text-align:center;'>" + questions[index] + "</body></html>");
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[index][i]);
        }
    }

    private int getSelectedOption() {
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) return i;
        }
        return -1;
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "Quiz Finished!\nYour Score: " + correct + "/" + questions.length,
                "Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private String getStackTraceString(Throwable t) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement s : t.getStackTrace()) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // ensure GUI is created on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                QuizApp app = new QuizApp();
                app.setVisible(true);
            } catch (Throwable t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(null, "Fatal error starting application:\n" + t,
                        "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
