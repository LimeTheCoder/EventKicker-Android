package com.example.limethecoder.eventkicker.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.limethecoder.eventkicker.R;
import com.example.limethecoder.eventkicker.model.EventItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SmallCardAdapter extends RecyclerView.Adapter<SmallCardAdapter.ViewHolder> {

    private List<EventItem> mItems;
    private ViewHolder.ItemClickListener clickListener;
    private Context context;

    public SmallCardAdapter(List<EventItem> events, ViewHolder.ItemClickListener listener, Context context) {
        mItems = events;
        clickListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_small, viewGroup, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        EventItem event = mItems.get(i);

        viewHolder.title.setText(event.getName());
        Picasso.with(context).load(event.getPictureUrl()).placeholder(R.drawable.baltoro_glacier).into(viewHolder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public CardView cv;
        public TextView title;

        private ItemClickListener clickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            // set listener
            this.clickListener = itemClickListener;

            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            title = (TextView)itemView.findViewById(R.id.tv_nature);

            imgThumbnail.setOnClickListener(this);
            title.setOnClickListener(this);
        }

        public interface ItemClickListener {
            void onClick(View view, int position, boolean isLongClick);
        }

        @Override
        public void onClick(View v) {
            Log.d("SmallCardAdapter" , "Adaptr's onclick called");
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("SmallCardAdapter" , "Adaptr's on long click called");
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }
}
