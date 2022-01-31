package superwordreplacer.logic;

import javax.swing.JTextArea;

public class EventLogger {
    private static JTextArea outputArea = null;
    
    public static void setOutputArea(JTextArea outputArea){
        EventLogger.outputArea = outputArea;
    }
    
    public static void println(String line){
        if(outputArea != null){
            outputArea.setText(outputArea.getText() + line + "\n");
        }
    }
}
