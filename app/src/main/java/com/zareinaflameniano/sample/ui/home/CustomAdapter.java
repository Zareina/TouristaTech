package com.zareinaflameniano.sample.ui.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zareinaflameniano.sample.R;
import com.zareinaflameniano.sample.data.AccommodationsModel;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<AccommodationsModel> localDataSet;
    private final OnItemClickListener localListener;
    private final OnFaveClickListener localListenerFave;

    private Context context;

    public interface OnItemClickListener {
        void onItemClick(AccommodationsModel item);
    }

    public interface OnFaveClickListener {
        void onItemClick(AccommodationsModel item, int position);
    }

    public void updateData(List<AccommodationsModel> newData) {
        localDataSet = newData;
        notifyDataSetChanged();
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        private final ImageView header_image;
        private final CardView cardView;
        private final Button btnFave;
        private final TextView subtitle;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title = view.findViewById(R.id.title);
            header_image = view.findViewById(R.id.header_image);
            cardView = view.findViewById(R.id.cardView);
            btnFave = view.findViewById(R.id.btnFave);
            subtitle = view.findViewById(R.id.subtitle);

        }

        public TextView getTitle() {
            return title;
        }
        public ImageView getHeaderImage() {
            return header_image;
        }
        public CardView getCardView() {
            return cardView;
        }
        public Button getBtnFave() {
            return btnFave;
        }

        public  TextView getSubtitle(){return subtitle;}
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(List<AccommodationsModel> dataSet, OnItemClickListener listener, OnFaveClickListener listenerFave, Context context) {

        localDataSet = dataSet;
        localListener = listener;
        localListenerFave = listenerFave;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTitle().setText(localDataSet.get(position).accName);
        viewHolder.getSubtitle().setText(localDataSet.get(position).accLocation);
        int img = context.getResources().getIdentifier(localDataSet.get(position).accImage, "drawable", "com.zareinaflameniano.sample");
        viewHolder.getHeaderImage().setImageResource(img);
        viewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localListener.onItemClick(localDataSet.get(position));
            }
        });
        viewHolder.getBtnFave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localListenerFave.onItemClick(localDataSet.get(position), position);
            }
        });
        if(localDataSet.get(position).accFavorite == 0){
            viewHolder.getBtnFave().setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }else{
            viewHolder.getBtnFave().setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
        }

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}