package com.example.fallcode;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> implements Filterable {
    private ArrayList<Article> articlesData;
    private ArrayList<Article> articlesDataAll;
    private Context context;
    private int lastPosition = -1;

    ArticleAdapter(Context context, ArrayList<Article> itemsData) {
        this.articlesData = itemsData;
        this.articlesDataAll = itemsData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.article, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ViewHolder holder, int position) {
        Article current = articlesData.get(position);

        holder.bindTo(current);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.glide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return articlesData.size();
    }

    @Override
    public Filter getFilter() {
        return articleFilter;
    }

    private Filter articleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Article> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.count = articlesDataAll.size();
                results.values = articlesDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Article item : articlesDataAll) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            articlesData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;
        //private TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemTitle);
            content = itemView.findViewById(R.id.itemContent);
            //id = itemView.findViewById(R.id.itemId);

            /*
            itemView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Activity", "Törlés gomb lenyomva");
                }
            });
            */



        }

        public void bindTo(Article current) {
            title.setText(current.getTitle());
            content.setText(current.getContent());
            //id.setText(current._getId());

            itemView.findViewById(R.id.btnDelete).setOnClickListener(view -> ((ListActivity)context).delete(current));
            itemView.findViewById(R.id.btnEdit).setOnClickListener(view -> ((ListActivity)context).update(current));
        }
    }

    ;

};
