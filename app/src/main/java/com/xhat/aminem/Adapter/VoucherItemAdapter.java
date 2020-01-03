package com.xhat.aminem.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhat.aminem.Model.VoucherItem;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class VoucherItemAdapter extends RecyclerView.Adapter<VoucherItemAdapter.VoucherItemHolder> {

    Context mContext;
    List<VoucherItem> voucherItemList;

    public VoucherItemAdapter(Context context, List<VoucherItem> itemList) {
        this.mContext = context;
        voucherItemList = itemList;
    }

    @Override
    public VoucherItemAdapter.VoucherItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VoucherItemAdapter.VoucherItemHolder holder, int position) {
        final VoucherItem voucherItem = voucherItemList.get(position);
        holder.tvVoucherName.setText(voucherItem.getName());
        holder.tvVoucherDesc.setText(voucherItem.getDescription());
        holder.btnVoucherPrice.setText(voucherItem.getPrice().toString() + " TKN");

        GlideApp
                .with(mContext)
                .load(voucherItem.getImage())
                .into(holder.ivVoucherDrawable);
    }

    @Override
    public int getItemCount() {
        return voucherItemList.size();
    }

    public class VoucherItemHolder extends RecyclerView.ViewHolder{
        ImageView ivVoucherDrawable;
        Button btnVoucherPrice;
        TextView tvVoucherName;
        TextView tvVoucherDesc;

        public VoucherItemHolder(View itemView) {
            super(itemView);

            ivVoucherDrawable = itemView.findViewById(R.id.iv_voucher_drawable);
            btnVoucherPrice = itemView.findViewById(R.id.btn_voucher_price);
            tvVoucherName = itemView.findViewById(R.id.tv_voucher_name);
            tvVoucherDesc = itemView.findViewById(R.id.tv_voucher_desc);

            ButterKnife.bind(this, itemView);
        }
    }
}
