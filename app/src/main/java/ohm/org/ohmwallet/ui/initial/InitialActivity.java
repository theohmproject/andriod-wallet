package ohm.org.ohmwallet.ui.initial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ohm.org.ohmwallet.OhmApplication;
import ohm.org.ohmwallet.ui.splash_activity.SplashActivity;
import ohm.org.ohmwallet.ui.wallet_activity.WalletActivity;
import ohm.org.ohmwallet.utils.AppConf;

/**
 * Created by ras on 8/19/17.
 */

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OhmApplication ohmApplication = OhmApplication.getInstance();
        AppConf appConf = ohmApplication.getAppConf();
        // show report dialog if something happen with the previous process
        Intent intent;
        if (!appConf.isAppInit() || appConf.isSplashSoundEnabled()){
            intent = new Intent(this, SplashActivity.class);
        }else {
            intent = new Intent(this, WalletActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
