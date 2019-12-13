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

public class LostItemAdapter extends RecyclerView.Adapter<LostItemAdapter.LostItemHolder> {

    Context mContext;
    List<AlllostitemItem> allLostItemList;

    public String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };

    public LostItemAdapter(Context context, List<AlllostitemItem> itemList) {
        this.mContext = context;
        allLostItemList = itemList;
    }

    @Override
    public LostItemAdapter.LostItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost, parent, false);
        return new LostItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LostItemAdapter.LostItemHolder holder, int position) {
        final AlllostitemItem alllostitemItem = allLostItemList.get(position);
        holder.tvItemName.setText(alllostitemItem.getItemName());
        holder.tvItemFound.setText("Penemuan: " + alllostitemItem.getPlaceFound());
        holder.tvItemSave.setText("Pengambilan: " + alllostitemItem.getPlaceSaveName());
        //holder.btnCategory.setText(alllostitemItem.getCategory());
        holder.btnCategory.setText(alllostitemItem.getDateFound());

        GlideApp
                .with(mContext)
                .load(alllostitemItem.getImage())
                .into(holder.ivItemDrawable);

//        String itemName = alllostitemItem.getCategory();
//        String firstCharItemNama = itemName.substring(0,1).toUpperCase();
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(firstCharItemNama, getColor());
//        holder.ivTextDrawable.setImageDrawable(drawable);

        String category = alllostitemItem.getCategory();
        Log.d("CATEGORY", category);

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
        Button btnCategory;
        TextView tvItemName;
        TextView tvItemFound, tvItemSave;

        public LostItemHolder(View itemView) {
            super(itemView);

            ivTextDrawable = itemView.findViewById(R.id.iv_text_drawable);
            ivItemDrawable = itemView.findViewById(R.id.iv_item_drawable);
            btnCategory = itemView.findViewById(R.id.btn_category);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemFound = itemView.findViewById(R.id.tv_item_found);
            tvItemSave = itemView.findViewById(R.id.tv_item_save);

            ButterKnife.bind(this, itemView);
        }
    }

    public int getColor() {
        String color;

        // Randomly select a fact
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);

        color = mColors[randomNumber];
        int colorAsInt = Color.parseColor(color);

        return colorAsInt;
    }
}
