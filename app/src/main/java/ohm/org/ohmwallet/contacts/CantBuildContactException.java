package ohm.org.ohmwallet.contacts;

/**
 * Created by ras on 7/1/17.
 */
public class CantBuildContactException extends RuntimeException {
    public CantBuildContactException(Exception e) {
        super(e);
    }
}
