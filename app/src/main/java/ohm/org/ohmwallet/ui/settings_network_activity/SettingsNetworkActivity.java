package ohm.org.ohmwallet.ui.settings_network_activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import ohm.org.ohmwallet.R;
import ohm.org.ohmwallet.ui.base.BaseActivity;

/**
 * Created by Neoperol on 6/8/17.
 */

public class SettingsNetworkActivity extends BaseActivity {

    View root;

    @Override
    protected void onCreateView(Bundle savedInstanceState, ViewGroup container) {
        root = getLayoutInflater().inflate(R.layout.fragment_network, container);
        setTitle("Network Monitor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
