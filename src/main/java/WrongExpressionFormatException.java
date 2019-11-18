/**
 * Исключение означает, что выражение является синтаксически некорректным с точки зрения задачи.
 */
public class WrongExpressionFormatException extends Exception {
    public WrongExpressionFormatException(String message) {
        super(message);
    }
}
