package ohm.org.ohmwallet.ui.settings_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import global.OhmModuleImp;
import ohm.org.ohmwallet.BuildConfig;
import ohm.org.ohmwallet.R;
import ohm.org.ohmwallet.module.Coin2PlayContext;
import ohm.org.ohmwallet.ui.base.BaseDrawerActivity;
import ohm.org.ohmwallet.ui.base.dialogs.SimpleTwoButtonsDialog;
import ohm.org.ohmwallet.ui.export_account.ExportKeyActivity;
import ohm.org.ohmwallet.ui.import_watch_only.SettingsWatchOnly;
import ohm.org.ohmwallet.ui.restore_activity.RestoreActivity;
import ohm.org.ohmwallet.ui.settings_backup_activity.SettingsBackupActivity;
import ohm.org.ohmwallet.ui.settings_network_activity.SettingsNetworkActivity;
import ohm.org.ohmwallet.ui.settings_pincode_activity.SettingsPincodeActivity;
import ohm.org.ohmwallet.ui.settings_rates.SettingsRatesActivity;
import ohm.org.ohmwallet.ui.start_node_activity.StartNodeActivity;
import ohm.org.ohmwallet.ui.tutorial_activity.TutorialActivity;
import ohm.org.ohmwallet.utils.CrashReporter;
import ohm.org.ohmwallet.utils.DialogsUtil;
import ohm.org.ohmwallet.utils.IntentsUtils;
import ohm.org.ohmwallet.utils.NavigationUtils;
import ohm.org.ohmwallet.utils.ReportIssueDialogBuilder;

import static ohm.org.ohmwallet.ui.tutorial_activity.TutorialActivity.INTENT_EXTRA_INFO_TUTORIAL;

/**
 * Created by Neoperol on 5/11/17.
 */

public class SettingsActivity extends BaseDrawerActivity implements View.OnClickListener {
    private Switch videoSwitch;
    private Button buttonBackup;
    private Button buttonRestore;
    private Button btn_export_pub_key;
    private Button btn_import_xpub;
    private Button buttonChange;
    private Button btn_change_node;
    private Button btn_reset_blockchain;
    private Button btn_report;
    private Button btn_support;
    private Button buttonTutorial;
    private TextView textAbout, text_rates;
    private TextView txt_network_info;

    @Override
    protected void onCreateView(Bundle savedInstanceState, ViewGroup container) {
        getLayoutInflater().inflate(R.layout.fragment_settings, container);
        setTitle("Settings");

        TextView app_version = (TextView) findViewById(R.id.app_version);
        app_version.setText(BuildConfig.VERSION_NAME);

        txt_network_info = (TextView) findViewById(R.id.txt_network_info);

        textAbout = (TextView)findViewById(R.id.text_about);
        String text = "Made by<br> <font color=#55476c>ras</font> <br>(c) OHM Community (c) PIVX Devs";
        textAbout.setText(Html.fromHtml(text));
        // Open Backup Wallet
        buttonBackup = (Button) findViewById(R.id.btn_backup_wallet);
        buttonBackup.setOnClickListener(this);

        // Open Restore Wallet
        buttonRestore = (Button) findViewById(R.id.btn_restore_wallet);
        buttonRestore.setOnClickListener(this);

        btn_export_pub_key = (Button) findViewById(R.id.btn_export_pub_key);
        btn_export_pub_key.setOnClickListener(this);

        btn_import_xpub = (Button) findViewById(R.id.btn_import_xpub);
        btn_import_xpub.setOnClickListener(this);

        // Open Change Pincode
        buttonChange = (Button) findViewById(R.id.btn_change_pincode);
        buttonChange.setOnClickListener(this);

        btn_change_node = (Button) findViewById(R.id.btn_change_node);
        btn_change_node.setOnClickListener(this);

        btn_reset_blockchain = (Button) findViewById(R.id.btn_reset_blockchain);
        btn_reset_blockchain.setOnClickListener(this);

        // rates
        findViewById(R.id.btn_rates).setOnClickListener(this);
        text_rates = (TextView) findViewById(R.id.text_rates);
        text_rates.setText(ohmApplication.getAppConf().getSelectedRateCoin());

        // Open Network Monitor
        buttonChange = (Button) findViewById(R.id.btn_network);
        buttonChange.setOnClickListener(this);

        btn_report = (Button) findViewById(R.id.btn_report);
        btn_report.setOnClickListener(this);

        btn_support = (Button) findViewById(R.id.btn_support);
        btn_support.setOnClickListener(this);

        // Open Tutorial
        buttonTutorial = (Button) findViewById(R.id.btn_tutorial);
        buttonTutorial.setOnClickListener(this);

        // Video Switch
        videoSwitch = (Switch) findViewById(R.id.videoSwitch);
        videoSwitch.setChecked(ohmApplication.getAppConf().isSplashSoundEnabled());
        videoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                ohmApplication.getAppConf().setSplashSound(checked);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        setNavigationMenuItemChecked(2);
        updateNetworkStatus();
        text_rates.setText(ohmApplication.getAppConf().getSelectedRateCoin());
    }

    private void updateNetworkStatus() {
        // Check if the activity is on foreground
        if (!isOnForeground)return;
        txt_network_info.setText(
                Html.fromHtml(
                        "Network<br><font color=#55476c>"+ ohmModule.getConf().getNetworkParams().getId()+
                                "</font><br>" +
                                "Height<br><font color=#55476c>"+ ohmModule.getChainHeight()+"</font><br>" +
                                "Protocol Version<br><font color=#55476c>"+
                                ohmModule.getProtocolVersion()+"</font>"

                )
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_backup_wallet){
            Intent myIntent = new Intent(v.getContext(), SettingsBackupActivity.class);
            startActivity(myIntent);
        }else if (id == R.id.btn_tutorial){
            Intent myIntent = new Intent(v.getContext(), TutorialActivity.class);
            myIntent.putExtra(INTENT_EXTRA_INFO_TUTORIAL,true);
            startActivity(myIntent);
        }else if (id == R.id.btn_restore_wallet){
            Intent myIntent = new Intent(v.getContext(), RestoreActivity.class);
            startActivity(myIntent);
        }else if (id == R.id.btn_change_pincode){
            Intent myIntent = new Intent(v.getContext(), SettingsPincodeActivity.class);
            startActivity(myIntent);
        }else if (id == R.id.btn_network){
            startActivity(new Intent(v.getContext(),SettingsNetworkActivity.class));
        }else if(id == R.id.btn_change_node) {
            startActivity(new Intent(v.getContext(), StartNodeActivity.class));
        }else if(id == R.id.btn_reset_blockchain){
            launchResetBlockchainDialog();
        }else if (id == R.id.btn_report){
            launchReportDialog();
        }else if(id == R.id.btn_support){
            IntentsUtils.startSend(
                    this,
                    getString(R.string.support_subject),
                    getString(R.string.report_issue_dialog_message_issue),
                    new ArrayList<Uri>()
            );
        }else if (id == R.id.btn_export_pub_key){
            startActivity(new Intent(v.getContext(), ExportKeyActivity.class));
        }else if (id == R.id.btn_import_xpub){
            startActivity(new Intent(v.getContext(), SettingsWatchOnly.class));
        }else if (id == R.id.btn_rates){
            startActivity(new Intent(v.getContext(), SettingsRatesActivity.class));
        }
    }

    private void launchResetBlockchainDialog() {
        SimpleTwoButtonsDialog dialog = DialogsUtil.buildSimpleTwoBtnsDialog(
                this,
                getString(R.string.dialog_reset_blockchain_title),
                getString(R.string.dialog_reset_blockchain_body),
                new SimpleTwoButtonsDialog.SimpleTwoBtnsDialogListener() {
                    @Override
                    public void onRightBtnClicked(SimpleTwoButtonsDialog dialog) {
                        ohmApplication.stopBlockchain();
                        Toast.makeText(SettingsActivity.this,R.string.reseting_blockchain,Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onLeftBtnClicked(SimpleTwoButtonsDialog dialog) {
                        dialog.dismiss();
                    }
                }
        );
        dialog.setLeftBtnText(R.string.button_cancel)
                .setRightBtnText(R.string.button_ok);
        dialog.show();
    }

    private void launchReportDialog() {
        ReportIssueDialogBuilder dialog = new ReportIssueDialogBuilder(
                this,
                "coin2play.org.coin2playwallet.myfileprovider",
                R.string.report_issuea_dialog_title,
                R.string.report_issue_dialog_message_issue)
        {
            @Nullable
            @Override
            protected CharSequence subject() {
                return Coin2PlayContext.REPORT_SUBJECT_ISSUE+" "+ ohmApplication.getVersionName();
            }

            @Nullable
            @Override
            protected CharSequence collectApplicationInfo() throws IOException {
                final StringBuilder applicationInfo = new StringBuilder();
                CrashReporter.appendApplicationInfo(applicationInfo, ohmApplication);
                return applicationInfo;
            }

            @Nullable
            @Override
            protected CharSequence collectStackTrace() throws IOException {
                return null;
            }

            @Nullable
            @Override
            protected CharSequence collectDeviceInfo() throws IOException {
                final StringBuilder deviceInfo = new StringBuilder();
                CrashReporter.appendDeviceInfo(deviceInfo, SettingsActivity.this);
                return deviceInfo;
            }

            @Nullable
            @Override
            protected CharSequence collectWalletDump() throws IOException {
                return ((OhmModuleImp) ohmModule).getWallet().toString(false,true,true,null);
            }
        };
        dialog.show();
    }
    @Override
    protected void onBlockchainStateChange() {
        updateNetworkStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationUtils.goBackToHome(this);
    }
}
