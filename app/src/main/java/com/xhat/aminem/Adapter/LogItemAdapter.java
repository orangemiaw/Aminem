package com.xhat.aminem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.xhat.aminem.Model.LogItem;
import com.xhat.aminem.R;

import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class LogItemAdapter extends RecyclerView.Adapter<LogItemAdapter.LogItemHolder> {

    Context mContext;
    List<LogItem> logItemList;

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

    public LogItemAdapter(Context context, List<LogItem> logList) {
        this.mContext = context;
        logItemList = logList;
    }

    @Override
    public LogItemAdapter.LogItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
        return new LogItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LogItemAdapter.LogItemHolder holder, int position) {
        final LogItem alllogitemItem = logItemList.get(position);
        holder.tvLogDate.setText(alllogitemItem.getCreated());
        holder.tvLogIp.setText("Controller: " + alllogitemItem.getController() + " | Action: " + alllogitemItem.getAction() + " | IP: " + alllogitemItem.getIp());

        String controllerName = alllogitemItem.getController();
        String firstCharNamaDosen = controllerName.substring(0,1).toUpperCase();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstCharNamaDosen, getColor());
        holder.ivTextDrawable.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return logItemList.size();
    }

    public class LogItemHolder extends RecyclerView.ViewHolder{
        ImageView ivTextDrawable;
        TextView tvLogDate;
        TextView tvLogIp;

        public LogItemHolder(View itemView) {
            super(itemView);

            ivTextDrawable = itemView.findViewById(R.id.iv_text_drawable);
            tvLogDate = itemView.findViewById(R.id.tv_log_date);
            tvLogIp = itemView.findViewById(R.id.tv_log_ip);

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
