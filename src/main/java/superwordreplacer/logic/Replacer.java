package superwordreplacer.logic;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import superwordreplacer.interfaces.ReplaceFunction;

public class Replacer {

    public static void runReplacer(String sourcePath, String outputPath, String wordToReplace, String replacementWord, boolean caseSensitive, boolean wholeWords, boolean skipErrors) throws IOException {
        // Open selected files/folders
        File sourceFile = new File(sourcePath);
        File outputFolder = new File(outputPath);
        // Assigning the regular expression to replace a word depending on the "case sensitivity" and "whole words" flags
        String regularExpression = obtainTargetRegEx(wordToReplace,caseSensitive,wholeWords);
        // Assigning the lambda function used
        ReplaceFunction replaceFunction = obtainReplaceFunction();
        if(!outputFolder.exists()){
            outputFolder.mkdir();
        }
        if (sourceFile.isDirectory()) {
            processFolder(sourceFile, outputFolder, regularExpression, replacementWord, replaceFunction, skipErrors);
        } else {
            processFile(sourceFile, outputFolder, regularExpression, replacementWord, replaceFunction);
        }
    }

    private static String obtainTargetRegEx(String wordToReplace, boolean caseSensitive, boolean wholeWords) {
        String regularExpression = wordToReplace;
        if (wholeWords) {
            regularExpression = "\\b" + regularExpression + "\\b";
        }
        if (!caseSensitive) {
            regularExpression = "(?i)" + regularExpression;
        }
        return regularExpression;
    }

    private static void processFolder(File sourceFolder, File outputFolder, String regularExpression, String replacementWord, ReplaceFunction replaceFunction, boolean skipErrors) throws IOException {
        Iterator<File> files = FileUtils.iterateFiles(sourceFolder, new String[]{"txt"}, false);
        while (files.hasNext()) {
            File nextFile = files.next();
            try{
                processFile(nextFile, outputFolder, regularExpression, replacementWord, replaceFunction);
            }
            catch(IOException ex){
                EventLogger.println("An error ocurred when processing the file named \"" + nextFile +"\"");
                if(skipErrors){
                    EventLogger.println("Skipping...");
                }
                else{
                    return;
                }
            }
        }
    }

    private static void processFile(File sourceFile, File outputFolder, String regularExpression, String replacementWord, ReplaceFunction replaceFunction) throws IOException {
        // Reading the lines of a text file and storing them
        List<String> readLines = FileUtils.readLines(sourceFile, "UTF-8");
        // Replacing the target word using a lambda function, storing the result
        List<String> processedLines = readLines
                .stream()
                .map(line -> replaceFunction.apply(line, regularExpression, replacementWord)).collect(Collectors.toList());
        // Writing the stored result into a new text file in the selected output folder
        File outputFile = new File(outputFolder.getCanonicalPath() + "\\" + sourceFile.getName());
        FileUtils.writeLines(outputFile, processedLines);
        EventLogger.println("Finished processing file \""+ sourceFile.getName() + "\"");
    }
    
    private static ReplaceFunction obtainReplaceFunction() {
        return (line, regularExpression, replacementWord) -> {
            return replaceFunction(line, regularExpression, replacementWord);
        };
    }

    private static String replaceFunction(String line, String regularExpression, String replacementWord) {
        line = line.replaceAll(regularExpression, replacementWord);
        return line;
    }

}
