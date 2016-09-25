package com.example.limethecoder.eventkicker.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.limethecoder.eventkicker.R;
import com.example.limethecoder.eventkicker.model.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsListAdapter extends RecyclerView
    .Adapter<CommentsListAdapter.ViewHolder> {

  private List<Comment> mComments;

  public CommentsListAdapter(List<Comment> comments) {
    mComments = comments;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent,
                                       int viewType) {
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.comment_layout, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder,
                               int position) {
    Comment comment = mComments.get(position);
    holder.getAuthorView().setText(comment.authorName);
    holder.getContentView().setText(comment.content);
    holder.getDateView().setText(comment.getParsedDate());

  }

  @Override
  public int getItemCount() {
    return mComments.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_author)
    TextView authorView;
    @BindView(R.id.comment_content)
    TextView contentView;
    @BindView(R.id.comment_date)
    TextView dateView;


    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public TextView getAuthorView() {
      return authorView;
    }


    public TextView getContentView() {
      return contentView;
    }


    public TextView getDateView() {
      return dateView;
    }
  }

  }
