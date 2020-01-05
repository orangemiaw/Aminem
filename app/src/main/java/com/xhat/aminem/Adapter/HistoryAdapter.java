package com.xhat.aminem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.xhat.aminem.Model.AlllostitemItem;
import com.xhat.aminem.Module.GlideApp;
import com.xhat.aminem.R;

import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.LostItemHolder> {

    Context mContext;
    List<AlllostitemItem> allLostItemList;

    public HistoryAdapter(Context context, List<AlllostitemItem> itemList) {
        this.mContext = context;
        allLostItemList = itemList;
    }

    @Override
    public HistoryAdapter.LostItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new LostItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.LostItemHolder holder, int position) {
        final AlllostitemItem alllostitemItem = allLostItemList.get(position);
        holder.tvItemName.setText(alllostitemItem.getItemName());
        holder.tvItemFound.setText("Penemuan: " + alllostitemItem.getPlaceFound());
        holder.tvItemSave.setText("Pengambilan: " + alllostitemItem.getPlaceSaveName());
        holder.btnDate.setText(alllostitemItem.getDateFound());

        GlideApp
                .with(mContext)
                .load(alllostitemItem.getImage())
                .into(holder.ivItemDrawable);

        String status = alllostitemItem.getStatus();
        switch (status) {
            case "1":
                holder.btnStatus.setText("Lost");
                break;
            case "2":
                holder.btnStatus.setText("FOUND");
                break;
            case "3":
                holder.btnStatus.setText("Neglected");
                break;
            case "4":
                holder.btnStatus.setText("Not Valid");
                break;
        }

        String category = alllostitemItem.getCategory();

        switch (category) {
            case "Kartu Identitas":
                holder.ivTextDrawable.setImageResource(R.drawable.ic_lightbulb_outline_black_24dp);
                holder.ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundyello);
                break;
            case "Dokumen":
                holder.ivTextDrawable.setImageResource(R.drawable.ic_attach_file_black_24dp);
                holder.ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundpink);
                break;
            case "Kunci":
                holder.ivTextDrawable.setImageResource(R.drawable.ic_key_white);
                holder.ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundgreen);
                break;
            case "Elektronik":
                holder.ivTextDrawable.setImageResource(R.drawable.ic_electronic_white);
                holder.ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundgreen);
                break;
            case "Dompet":
                holder.ivTextDrawable.setImageResource(R.drawable.ic_attach_money_black_24dp);
                holder.ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundyello);
                break;
            case "Lainnya":
                holder.ivTextDrawable.setImageResource(R.drawable.ic_other_white);
                holder.ivTextDrawable.setBackgroundResource(R.drawable.cerclebackgroundpink);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return allLostItemList.size();
    }

    public class LostItemHolder extends RecyclerView.ViewHolder{
        ImageView ivTextDrawable;
        ImageView ivItemDrawable;
        Button btnDate, btnStatus;
        TextView tvItemName;
        TextView tvItemFound, tvItemSave;

        public LostItemHolder(View itemView) {
            super(itemView);

            ivTextDrawable = itemView.findViewById(R.id.iv_text_drawable);
            ivItemDrawable = itemView.findViewById(R.id.iv_item_drawable);
            btnDate = itemView.findViewById(R.id.btn_date);
            btnStatus = itemView.findViewById(R.id.btn_status);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemFound = itemView.findViewById(R.id.tv_item_found);
            tvItemSave = itemView.findViewById(R.id.tv_item_save);

            ButterKnife.bind(this, itemView);
        }
    }
}
