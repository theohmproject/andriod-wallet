package global.store;

import global.OhmRate;

/**
 * Created by ras on 3/3/18.
 */

public interface RateDbDao<T> extends AbstractDbDao<T>{

    OhmRate getRate(String coin);


    void insertOrUpdateIfExist(OhmRate ohmRate);

}
