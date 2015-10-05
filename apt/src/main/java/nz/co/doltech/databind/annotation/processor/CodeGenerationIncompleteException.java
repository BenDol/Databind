package nz.co.doltech.databind.annotation.processor;

public class CodeGenerationIncompleteException extends RuntimeException {
    public CodeGenerationIncompleteException(String message) {
        super(message);
    }

    public CodeGenerationIncompleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeGenerationIncompleteException(Throwable cause) {
        super(cause);
    }
}
