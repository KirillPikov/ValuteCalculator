/**
 * Исключение означает, что в ходе вычислений было получено отрицательное число.
 */
public class WrongValueException extends Exception {
    public WrongValueException(String message) {
        super(message);
    }
}
