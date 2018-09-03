package com.in.ac.srit.books;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BooksRecyclerAdapter extends
        RecyclerView.Adapter<BooksRecyclerAdapter.ViewHolderClass> {
    private Context context;

    public BooksRecyclerAdapter(Context context, List<BookData> data) {
        this.context = context;
        this.data = data;
    }

    private List<BookData> data;
    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderClass(LayoutInflater.from(context).
                inflate(R.layout.book_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        Picasso.with(context).load(data.get(position).getImageUrl()).into(holder.image);
        holder.textView.setText(data.get(position).getBookname());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView textView;
        public ViewHolderClass(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.title_view_recycle);
            image=itemView.findViewById(R.id.image_view_recycle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id=data.get(getAdapterPosition()).getBookId();
            Intent intent=new Intent(context,BookDetail.class);
            intent.putExtra("ID",id);
            context.startActivity(intent);

        }
    }
}
