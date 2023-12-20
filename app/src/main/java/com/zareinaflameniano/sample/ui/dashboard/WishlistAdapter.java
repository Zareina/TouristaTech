package com.zareinaflameniano.sample.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zareinaflameniano.sample.R;
import com.zareinaflameniano.sample.data.DatabaseHelper;
import com.zareinaflameniano.sample.data.FavoritesModel;

import java.util.List;



import java.util.List;

// WishlistAdapter.java
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<FavoritesModel> localDataSet;
    private Context context;
    private boolean isEditMode;
    private OnDeleteClickListener onDeleteClickListener;

    public WishlistAdapter(List<FavoritesModel> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
        this.isEditMode = false; // Default to not in edit mode
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView favName;
        private final ImageView favImage;
        private final CardView cardView;
        private final ImageButton btDelete;


        public ViewHolder(View view, OnDeleteClickListener onDeleteClickListener) {
            super(view);
            favName = view.findViewById(R.id.favName);
            favImage = view.findViewById(R.id.favImage);
            cardView = view.findViewById(R.id.cardView);
            btDelete = view.findViewById(R.id.btDelete);


            // Set the click listener for the delete button
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(getAdapterPosition());
                    }
                }
            });
        }

        public TextView getFavName() {
            return favName;
        }

        public ImageView getFavImage() {
            return favImage;
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getBtDelete() {
            return btDelete;
        }

    }

    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wishlist_row_item, viewGroup, false);
        return new ViewHolder(view, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(WishlistAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.getFavName().setText(localDataSet.get(position).favName);
        int img = context.getResources().getIdentifier(localDataSet.get(position).favImage, "drawable", "com.zareinaflameniano.sample");
        viewHolder.getFavImage().setImageResource(img);

        viewHolder.getBtDelete().setVisibility(isEditMode ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    public List<FavoritesModel> getLocalDataSet() {
        return localDataSet;
    }

    public void deleteFavorite(int position) {
        localDataSet.remove(position);
        notifyItemRemoved(position);
    }
}