package superwordreplacer.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import superwordreplacer.logic.EventLogger;
import superwordreplacer.logic.Replacer;

public class MainWindow extends JFrame implements ActionListener {

    private JLabel labelWordReplacement;
    private JLabel labelReplace;
    private JTextField fieldReplace;
    private JLabel labelWith;
    private JTextField fieldWith;
    private JSeparator separator1;

    private JLabel labelFileSettings;
    private JLabel labelSource;
    private JTextField fieldSource;
    private JButton buttonSource;
    private JLabel labelOutput;
    private JTextField fieldOutput;
    private JButton buttonCopyDestination;
    private JSeparator separator2;

    private JLabel labelReplacementSettings;
    private JCheckBox checkSkip;
    private JCheckBox checkWhole;
    private JCheckBox checkCase;
    private JSeparator separator3;

    private JButton buttonStart;
    private JLabel labelProgress;
    private JProgressBar progressBar;
    private JLabel labelEventLog;
    private JTextArea areaEventLog;
    private JScrollPane scrollEventLog;

    public MainWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        SwingUtilities.updateComponentTreeUI(this);
        updateComponentTreeUI(this);

        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(510, 560));
        this.setTitle("Super Word Replacer");
        this.setResizable(false);

        // Word Replacement
        labelWordReplacement = new JLabel("Word Replacement");
        labelWordReplacement.setBounds(200, 4, 100, 30);
        this.add(labelWordReplacement);

        labelReplace = new JLabel("Replace:");
        labelReplace.setBounds(10, 30, 50, 30);
        this.add(labelReplace);

        fieldReplace = new JTextField();
        fieldReplace.setBounds(60, 35, 180, 20);
        this.add(fieldReplace);

        labelWith = new JLabel("With:");
        labelWith.setBounds(265, 30, 50, 30);
        this.add(labelWith);

        fieldWith = new JTextField();
        fieldWith.setBounds(300, 35, 180, 20);
        this.add(fieldWith);

        separator1 = new JSeparator(JSeparator.HORIZONTAL);
        separator1.setBounds(10, 70, 475, 2);
        this.add(separator1);

        // File Settings
        labelFileSettings = new JLabel("File Settings");
        labelFileSettings.setBounds(215, 80, 100, 30);
        this.add(labelFileSettings);

        // Source file
        labelSource = new JLabel("Source file/folder:");
        labelSource.setBounds(10, 100, 100, 30);
        this.add(labelSource);

        fieldSource = new JTextField("");
        fieldSource.setBounds(115, 105, 280, 20);
        this.add(fieldSource);

        buttonSource = new JButton("Browse...");
        buttonSource.setBounds(400, 105, 80, 20);
        buttonSource.addActionListener(this);
        this.add(buttonSource);

        // Copy destination
        labelOutput = new JLabel("Output folder:");
        labelOutput.setBounds(10, 135, 103, 30);
        this.add(labelOutput);

        fieldOutput = new JTextField("");
        fieldOutput.setBounds(115, 140, 280, 20);
        this.add(fieldOutput);

        buttonCopyDestination = new JButton("Browse...");
        buttonCopyDestination.setBounds(400, 140, 80, 20);
        buttonCopyDestination.addActionListener(this);
        this.add(buttonCopyDestination);

        separator2 = new JSeparator(JSeparator.HORIZONTAL);
        separator2.setBounds(10, 175, 475, 2);
        this.add(separator2);

        // Replacement Settings
        labelReplacementSettings = new JLabel("Replacement Settings");
        labelReplacementSettings.setBounds(195, 185, 120, 30);
        this.add(labelReplacementSettings);

        checkSkip = new JCheckBox("Skip file if an error occurs when altering it");
        checkSkip.setBounds(10, 215, 300, 20);
        this.add(checkSkip);

        checkCase = new JCheckBox("Case sensitive replacement");
        checkCase.setBounds(10, 240, 300, 20);
        this.add(checkCase);

        checkWhole = new JCheckBox("Only replace whole words");
        checkWhole.setBounds(10, 265, 300, 20);
        this.add(checkWhole);

        separator3 = new JSeparator(JSeparator.HORIZONTAL);
        separator3.setBounds(10, 300, 475, 2);
        this.add(separator3);

        // Final section
        buttonStart = new JButton("Start replacing");
        buttonStart.setBounds(185, 310, 120, 30);
        buttonStart.addActionListener(this);
        this.add(buttonStart);

        progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(false);
        progressBar.setBounds(10, 355, 470, 20);
        this.add(progressBar);

        labelEventLog = new JLabel("Event Log");
        labelEventLog.setBounds(220, 380, 100, 30);
        this.add(labelEventLog);

        areaEventLog = new JTextArea();
        areaEventLog.setEditable(false);

        scrollEventLog = new JScrollPane(areaEventLog);
        scrollEventLog.setBounds(10, 410, 470, 100);
        this.add(scrollEventLog);

        EventLogger.setOutputArea(areaEventLog);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void createFileChooser(JTextField field, String title, boolean acceptTextFiles) {
        JFileChooser fcSource = new JFileChooser();
        fcSource.setCurrentDirectory(new java.io.File("."));
        fcSource.setDialogTitle(title);
        if (acceptTextFiles) {
            fcSource.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fcSource.setFileFilter(new FileNameExtensionFilter("Text Files (.txt)", "txt"));
        } else {
            fcSource.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        if (fcSource.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field.setText(fcSource.getSelectedFile().getAbsolutePath());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(buttonSource)) {
            createFileChooser(fieldSource, "Select file/folder", true);
        } else if (e.getSource().equals(buttonCopyDestination)) {
            createFileChooser(fieldOutput, "Select folder", false);
        } else if (e.getSource().equals(buttonStart)) {
            //areaEventLog.setText("benus\nbenus\nbenus\nbenus\nbenus\nbenus\nbenus\nbenus\nbenus\nbenus\nbenus\nbenus\nbenus\n");
            String sourcePath = fieldSource.getText();
            String outputPath = fieldOutput.getText();
            String wordToReplace = fieldReplace.getText();
            String replacementWord = fieldWith.getText();
            boolean caseSensitive = checkCase.isSelected();
            boolean wholeWords = checkWhole.isSelected();
            boolean skipErrors = checkSkip.isSelected();     
            try {
                if (validateFileFields(sourcePath, outputPath)) {
                    progressBar.setIndeterminate(true);
                    Replacer.runReplacer(sourcePath, outputPath, wordToReplace, replacementWord, caseSensitive, wholeWords, skipErrors);
                    progressBar.setIndeterminate(false);
                    JOptionPane.showConfirmDialog(null, "Done processing!", "Finished!", JOptionPane.DEFAULT_OPTION);
                }

            } catch (IOException ex) {
                progressBar.setIndeterminate(false);
                EventLogger.println("An error ocurred: " + ex);
                JOptionPane.showMessageDialog(null, "An error ocurred. Check the event log for details.", "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static boolean validateFileFields(String sourcePath, String outputPath) throws IOException {
        if (sourcePath.isBlank() || outputPath.isBlank()) {
            JOptionPane.showMessageDialog(null, "Both file path fields need to be filled to run the replacer.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            File sourceFile = new File(sourcePath);
            File outputFile = new File(outputPath);
            if (!outputFile.isDirectory()) {
                JOptionPane.showMessageDialog(null, "The output path needs to be a folder.", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            String sourceFolder = "";
            String outputFolder = outputFile.getCanonicalPath();
            if (sourceFile.isDirectory()) {
                sourceFolder = sourceFile.getCanonicalPath();
            } else {
                sourceFolder = sourceFile.getParentFile().getCanonicalPath();
            }
            if (sourceFolder.equals(outputFolder)) {
                int dialogResult = JOptionPane.showConfirmDialog(null, "The input and output are in the same folder. This will overwrite all the files within it. Proceed?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.NO_OPTION) {
                    return false;
                }
            }
        }
        return true;
    }
}
