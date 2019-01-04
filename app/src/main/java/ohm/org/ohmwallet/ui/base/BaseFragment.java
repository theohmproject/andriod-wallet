package ohm.org.ohmwallet.ui.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import ohm.org.ohmwallet.OhmApplication;
import global.OhmModule;

/**
 * Created by ras on 6/29/17.
 */

public class BaseFragment extends Fragment {

    protected OhmApplication ohmApplication;
    protected OhmModule ohmModule;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ohmApplication = OhmApplication.getInstance();
        ohmModule = ohmApplication.getModule();
    }

    protected boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getActivity(),permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
