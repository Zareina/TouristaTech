package com.zareinaflameniano.sample.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.zareinaflameniano.sample.R;
import com.zareinaflameniano.sample.data.DatabaseHelper;
import com.zareinaflameniano.sample.data.FavoritesModel;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<FavoritesModel> localDataSet;
    private Context context;
    private boolean isEditMode;
    private DatabaseHelper db;

    public WishlistAdapter(List<FavoritesModel> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
        this.isEditMode = false; // Default to not in edit mode
        this.db = new DatabaseHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Other views...
        private final ImageView btDelete;

        public ViewHolder(View view) {
            super(view);
            // Other view bindings...
            btDelete = view.findViewById(R.id.btDelete);
        }

        public ImageView getBtDelete() {
            return btDelete;
        }
    }

    @Override
    public void onBindViewHolder(WishlistAdapter.ViewHolder viewHolder, final int position) {
        // Other bindings...

        viewHolder.getBtDelete().setVisibility(isEditMode ? View.VISIBLE : View.INVISIBLE);

        viewHolder.getBtDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    deleteItem(position);
                }
            }
        });
    }

    private void deleteItem(int position) {
        FavoritesModel itemToDelete = localDataSet.get(position);
        localDataSet.remove(position);
        notifyItemRemoved(position);
        db.deleteFavorite(itemToDelete.getFavID());
        Snackbar.make(binding.getRoot(), itemToDelete.getFavName() + " has been removed from your Wishlist.", Snackbar.LENGTH_LONG).show();
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
