package ohm.org.ohmwallet.ui.transaction_send_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.ohmj.core.Coin;

import java.math.BigDecimal;

import ohm.org.ohmwallet.R;
import global.OhmRate;
import ohm.org.ohmwallet.ui.base.BaseFragment;

/**
 * Created by ras on 2/9/18.
 */

public class AmountInputFragment extends BaseFragment implements View.OnClickListener {

    private View root;

    private EditText edit_amount, editCurrency;
    private TextView txt_currency_amount, txtShowPiv,txt_local_currency;
    private ImageButton btnSwap;
    private ViewFlipper amountSwap;
    private OhmRate ohmRate;
    private boolean inOHM = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.amount_input,container,false);
        edit_amount = (EditText) root.findViewById(R.id.edit_amount);
        //Sending amount currency
        editCurrency = (EditText) root.findViewById(R.id.edit_amount_currency);
        txt_currency_amount = (TextView) root.findViewById(R.id.txt_currency_amount);
        txt_local_currency = (TextView) root.findViewById(R.id.txt_local_currency);
        txtShowPiv = (TextView) root.findViewById(R.id.txt_show_piv) ;
        //Swap type of ammounts
        amountSwap = (ViewFlipper) root.findViewById( R.id.viewFlipper );
        amountSwap.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_in_left));
        amountSwap.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_out_right));
        btnSwap = (ImageButton) root.findViewById(R.id.btn_swap);
        btnSwap.setOnClickListener(this);

        ohmRate = ohmModule.getRate(ohmApplication.getAppConf().getSelectedRateCoin());

        if (ohmRate != null)
            txt_local_currency.setText("0 " + ohmRate.getCode());
        else
            txt_local_currency.setText("0");

        editCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ohmRate != null) {
                    if (s.length() > 0) {
                        String valueStr = s.toString();
                        if (valueStr.charAt(0) == '.') {
                            valueStr = "0" + valueStr;
                        }
                        BigDecimal result = new BigDecimal(valueStr).divide(ohmRate.getRate(), 6, BigDecimal.ROUND_DOWN);
                        txtShowPiv.setText(result.toPlainString() + " OHM");
                    } else {
                        txtShowPiv.setText("0 " + ohmRate.getCode());
                    }
                }else {
                    txtShowPiv.setText(R.string.no_rate);
                }
            }
        });

        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0) {
                    if (ohmRate != null) {
                        String valueStr = s.toString();
                        if (valueStr.charAt(0) == '.') {
                            valueStr = "0" + valueStr;
                        }
                        Coin coin = Coin.parseCoin(valueStr);
                        txt_local_currency.setText(
                                ohmApplication.getCentralFormats().format(
                                        new BigDecimal(coin.getValue() * ohmRate.getRate().doubleValue()).movePointLeft(8)
                                )
                                        + " " + ohmRate.getCode()
                        );
                    }else {
                        // rate null -> no connection.
                        txt_local_currency.setText(R.string.no_rate);
                    }
                }else {
                    if (ohmRate !=null)
                        txt_local_currency.setText("0 "+ ohmRate.getCode());
                    else
                        txt_local_currency.setText(R.string.no_rate);
                }
            }
        });

        return root;
    }

    public String getAmountStr() throws Exception {
        if (edit_amount == null && editCurrency == null){
            throw new Exception("Fragment is not attached");
        }
        String amountStr = "0";
        if (inOHM) {
            amountStr = edit_amount.getText().toString();
        }else {
            // the value is already converted
            String valueStr = txtShowPiv.getText().toString();
            amountStr = valueStr.replace(" OHM","");
            if(valueStr.length() > 0) {
                if (valueStr.charAt(0) == '.') {
                    amountStr = "0" + valueStr;
                }
            }
        }
        return amountStr;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_swap) {
            inOHM = !inOHM;
            amountSwap.showNext();
        }
    }
}
