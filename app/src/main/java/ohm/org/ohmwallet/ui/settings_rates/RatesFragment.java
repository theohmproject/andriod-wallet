package ohm.org.ohmwallet.ui.settings_rates;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ohm.org.ohmwallet.R;
import global.OhmRate;
import ohm.org.ohmwallet.ui.base.BaseRecyclerFragment;
import ohm.org.ohmwallet.ui.base.tools.adapter.BaseRecyclerAdapter;
import ohm.org.ohmwallet.ui.base.tools.adapter.BaseRecyclerViewHolder;
import ohm.org.ohmwallet.ui.base.tools.adapter.ListItemListeners;

/**
 * Created by ras on 7/2/17.
 */

public class RatesFragment extends BaseRecyclerFragment<OhmRate> implements ListItemListeners<OhmRate> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setEmptyText("No rate available");
        setEmptyTextColor(Color.parseColor("#cccccc"));
        return view;
    }

    @Override
    protected List<OhmRate> onLoading() {
        return ohmModule.listRates();
    }

    @Override
    protected BaseRecyclerAdapter<OhmRate, ? extends OhmRateHolder> initAdapter() {
        BaseRecyclerAdapter<OhmRate, OhmRateHolder> adapter = new BaseRecyclerAdapter<OhmRate, OhmRateHolder>(getActivity()) {
            @Override
            protected OhmRateHolder createHolder(View itemView, int type) {
                return new OhmRateHolder(itemView,type);
            }

            @Override
            protected int getCardViewResource(int type) {
                return R.layout.rate_row;
            }

            @Override
            protected void bindHolder(OhmRateHolder holder, OhmRate data, int position) {
                holder.txt_name.setText(data.getCode());
                if (list.get(0).getCode().equals(data.getCode()))
                    holder.view_line.setVisibility(View.GONE);
            }
        };
        adapter.setListEventListener(this);
        return adapter;
    }

    @Override
    public void onItemClickListener(OhmRate data, int position) {
        ohmApplication.getAppConf().setSelectedRateCoin(data.getCode());
        Toast.makeText(getActivity(),R.string.rate_selected,Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onLongItemClickListener(OhmRate data, int position) {

    }

    private  class OhmRateHolder extends BaseRecyclerViewHolder{

        private TextView txt_name;
        private View view_line;

        protected OhmRateHolder(View itemView, int holderType) {
            super(itemView, holderType);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }
}
