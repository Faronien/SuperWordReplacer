package superwordreplacer.interfaces;

public interface ReplaceFunction {
    String apply(String line, String regularExpression, String replacementWord);
}
