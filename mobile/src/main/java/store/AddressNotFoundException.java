package store;

/**
 * Created by ras on 6/14/17.
 */

public class AddressNotFoundException extends Exception {

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
