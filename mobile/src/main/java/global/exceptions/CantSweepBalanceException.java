package global.exceptions;

/**
 * Created by ras on 10/7/17.
 */

public class CantSweepBalanceException extends Throwable {
    public CantSweepBalanceException(String s,Exception e) {
        super(s,e);
    }
}
