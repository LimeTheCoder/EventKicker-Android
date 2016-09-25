package com.example.limethecoder.eventkicker.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.limethecoder.eventkicker.model.EventItem;
import com.example.limethecoder.eventkicker.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<EventItem> mItems;
    private ViewHolder.ItemClickListener clickListener;
    private Context context;

    public CardAdapter(List<EventItem> events, ViewHolder.ItemClickListener itemClickListener, Context context) {
        mItems = events;
        this.clickListener = itemClickListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_event_item, viewGroup, false);

        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        EventItem event = mItems.get(i);

        viewHolder.title.setText(event.getName());
        viewHolder.dateSchedule.setText(event.getSchParsedDate());
        viewHolder.author.setText(event.getAuthorName());
        Picasso.with(context).load(event.getPictureUrl()).placeholder(R.drawable.baltoro_glacier).into(viewHolder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView author;
        public TextView dateSchedule;
        public CardView cv;
        public TextView title;

        private ItemClickListener clickListener;

        public ViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            // set listener
            this.clickListener = clickListener;

            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            title = (TextView)itemView.findViewById(R.id.tv_nature);
            author = (TextView)itemView.findViewById(R.id.name);
            dateSchedule = (TextView)itemView.findViewById(R.id.date);

            imgThumbnail.setOnClickListener(this);
            title.setOnClickListener(this);
        }

        public interface ItemClickListener {
            void onClick(View view, int position, boolean isLongClick);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }
}