package ohm.org.ohmwallet.ui.wallet_activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ohmj.core.Coin;
import org.ohmj.utils.MonetaryFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ohm.org.ohmwallet.R;
import global.OhmRate;
import ohm.org.ohmwallet.ui.base.BaseRecyclerFragment;
import ohm.org.ohmwallet.ui.base.tools.adapter.BaseRecyclerAdapter;
import ohm.org.ohmwallet.ui.base.tools.adapter.BaseRecyclerViewHolder;
import ohm.org.ohmwallet.ui.base.tools.adapter.ListItemListeners;
import ohm.org.ohmwallet.ui.transaction_detail_activity.TransactionDetailActivity;
import global.wrappers.TransactionWrapper;

import static ohm.org.ohmwallet.ui.transaction_detail_activity.FragmentTxDetail.IS_DETAIL;
import static ohm.org.ohmwallet.ui.transaction_detail_activity.FragmentTxDetail.TX_WRAPPER;
import static ohm.org.ohmwallet.utils.TxUtils.getAddressOrContact;

/**
 * Created by ras on 6/29/17.
 */

public class TransactionsFragmentBase extends BaseRecyclerFragment<TransactionWrapper> {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsFragmentBase.class);

    private OhmRate ohmRate;
    private MonetaryFormat coinFormat = MonetaryFormat.BTC;
    private int scale = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setEmptyView(R.drawable.img_transaction_empty);
        setEmptyText(getString(R.string.no_transactions));
        setEmptyTextColor(Color.parseColor("#cccccc"));
        return view;
    }

    @Override
    protected List<TransactionWrapper> onLoading() {
        List<TransactionWrapper> list = ohmModule.listTx();
        Collections.sort(list, new Comparator<TransactionWrapper>(){
            public int compare(TransactionWrapper o1, TransactionWrapper o2){
                if(o1.getTransaction().getUpdateTime().getTime() == o2.getTransaction().getUpdateTime().getTime())
                    return 0;
                return o1.getTransaction().getUpdateTime().getTime() > o2.getTransaction().getUpdateTime().getTime() ? -1 : 1;
            }
        });
        return list;
    }

    @Override
    protected BaseRecyclerAdapter<TransactionWrapper, ? extends BaseRecyclerViewHolder> initAdapter() {
        BaseRecyclerAdapter<TransactionWrapper, TransactionViewHolderBase> adapter = new BaseRecyclerAdapter<TransactionWrapper, TransactionViewHolderBase>(getActivity()) {
            @Override
            protected TransactionViewHolderBase createHolder(View itemView, int type) {
                return new TransactionViewHolderBase(itemView);
            }

            @Override
            protected int getCardViewResource(int type) {
                return R.layout.transaction_row;
            }

            @Override
            protected void bindHolder(TransactionViewHolderBase holder, TransactionWrapper data, int position) {
                String amount = data.getAmount().toFriendlyString();
                if (amount.length()<=10){
                    holder.txt_scale.setVisibility(View.GONE);
                    holder.amount.setText(amount);
                }else {
                    // format amount
                    holder.txt_scale.setVisibility(View.VISIBLE);
                    holder.amount.setText(parseToCoinWith4Decimals(data.getAmount().toPlainString()).toFriendlyString());
                }

                String localCurrency = null;
                if (ohmRate !=null) {
                    localCurrency = ohmApplication.getCentralFormats().format(
                                    new BigDecimal(data.getAmount().getValue() * ohmRate.getRate().doubleValue()).movePointLeft(8)
                                    )
                                    + " " + ohmRate.getCode();
                    holder.amountLocal.setText(localCurrency);
                    holder.amountLocal.setVisibility(View.VISIBLE);
                }else {
                    holder.amountLocal.setVisibility(View.INVISIBLE);
                }


                holder.description.setText(data.getTransaction().getMemo());

                if (data.isSent()){
                    //holder.cv.setBackgroundColor(Color.RED);Color.GREEN
                    holder.imageView.setImageResource(R.mipmap.ic_transaction_send);
                    holder.amount.setTextColor(ContextCompat.getColor(context, R.color.red));
                }else if (data.isZcSpend()) {
                    holder.imageView.setImageResource(R.drawable.ic_transaction_incognito);
                    holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green));
                } else if (!data.isStake()){
                    holder.imageView.setImageResource(R.mipmap.ic_transaction_receive);
                    holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green));
                } else {
                    holder.imageView.setImageResource(R.drawable.ic_transaction_mining);
                    holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green));
                }
                holder.title.setText(getAddressOrContact(ohmModule,data));

                /*if (data.getOutputLabels()!=null && !data.getOutputLabels().isEmpty()){
                    AddressLabel contact = data.getOutputLabels().get(0);
                    if (contact!=null) {
                        if (contact.getName() != null)
                            holder.title.setText(contact.getName());
                        else
                            holder.title.setText(contact.getAddresses().get(0));
                    }else {
                        holder.title.setText(data.getTransaction().getOutput(0).getScriptPubKey().getToAddress(ohmModule.getConf().getNetworkParams()).toBase58());
                    }
                }else {
                    holder.title.setText(data.getTransaction().getOutput(0).getScriptPubKey().getToAddress(ohmModule.getConf().getNetworkParams()).toBase58());
                }*/
                String memo = data.getTransaction().getMemo();
                holder.description.setText(memo!=null?memo:"No description");
            }
        };
        adapter.setListEventListener(new ListItemListeners<TransactionWrapper>() {
            @Override
            public void onItemClickListener(TransactionWrapper data, int position) {
                Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TX_WRAPPER,data);
                bundle.putBoolean(IS_DETAIL,true);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongItemClickListener(TransactionWrapper data, int position) {

            }
        });
        return adapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        ohmRate = ohmModule.getRate(ohmApplication.getAppConf().getSelectedRateCoin());
    }

    /**
     * Converts to a coin with max. 4 decimal places. Last place gets rounded.
     * 0.01234 -> 0.0123
     * 0.01235 -> 0.0124
     *
     * @param input
     * @return
     */
    public Coin parseToCoinWith4Decimals(String input) {
        try {
            return Coin.valueOf(new BigDecimal(parseToCoin(cleanInput(input)).value).setScale(-scale - 1,
                    BigDecimal.ROUND_HALF_UP).setScale(scale + 1).toBigInteger().longValue());
        } catch (Throwable t) {
            if (input != null && input.length() > 0)
                logger.warn("Exception at parseToCoinWith4Decimals: " + t.toString());
            return Coin.ZERO;
        }
    }

    public  Coin parseToCoin(String input) {
        if (input != null && input.length() > 0) {
            try {
                return coinFormat.parse(cleanInput(input));
            } catch (Throwable t) {
                logger.warn("Exception at parseToBtc: " + t.toString());
                return Coin.ZERO;
            }
        } else {
            return Coin.ZERO;
        }
    }

    private  String cleanInput(String input) {
        input = input.replace(",", ".");
        // don't use String.valueOf(Double.parseDouble(input)) as return value as it gives scientific
        // notation (1.0E-6) which screw up coinFormat.parse
        //noinspection ResultOfMethodCallIgnored
        Double.parseDouble(input);
        return input;
    }
}
