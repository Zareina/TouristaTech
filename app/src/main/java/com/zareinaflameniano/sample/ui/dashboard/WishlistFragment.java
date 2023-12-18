package com.zareinaflameniano.sample.ui.dashboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zareinaflameniano.sample.R;
import com.zareinaflameniano.sample.data.DatabaseHelper;
import com.zareinaflameniano.sample.data.FavoritesModel;
import com.zareinaflameniano.sample.databinding.FragmentWishlistBinding;

import java.util.List;


public class WishlistFragment extends Fragment {

    private FragmentWishlistBinding binding;
    private WishlistAdapter customAdapter;
    private RecyclerView rvWishlist;
    private Button btEdit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WishlistViewModel model =
                new ViewModelProvider(this).get(WishlistViewModel.class);

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rvWishlist = root.findViewById(R.id.rvWishlist);
        btEdit = root.findViewById(R.id.btEdit);

        DatabaseHelper db = new DatabaseHelper(requireContext());

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customAdapter != null) {
                    customAdapter.setEditMode(!customAdapter.isEditMode());
                }
            }
        });

        List<FavoritesModel> wishList = db.getAllFavorites();
        customAdapter = new WishlistAdapter(wishList, requireContext());

        rvWishlist.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvWishlist.setAdapter(customAdapter);

        return root;
    }
}