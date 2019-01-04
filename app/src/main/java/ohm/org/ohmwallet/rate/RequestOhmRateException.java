package ohm.org.ohmwallet.rate;

/**
 * Created by ras on 7/5/17.
 */
public class RequestOhmRateException extends Exception {
    public RequestOhmRateException(String message) {
        super(message);
    }

    public RequestOhmRateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestOhmRateException(Exception e) {
        super(e);
    }
}
