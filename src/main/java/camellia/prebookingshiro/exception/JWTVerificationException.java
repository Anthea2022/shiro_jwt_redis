package camellia.prebookingshiro.exception;

/**
 * @author anthea
 * @date 2023/11/7 15:07
 */
public class JWTVerificationException extends RuntimeException {
    public JWTVerificationException(String message) {
        this(message, (Throwable)null);
    }

    public JWTVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
