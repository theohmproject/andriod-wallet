package wallet.exceptions;

/**
 * Created by ras on 8/10/17.
 */

public class TxNotFoundException extends Exception {
    public TxNotFoundException(String s) {
        super(s);
    }
}
