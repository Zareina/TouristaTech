package com.zareinaflameniano.sample.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.zareinaflameniano.sample.BookingActivity;
import com.zareinaflameniano.sample.R;
import com.zareinaflameniano.sample.data.AccommodationsModel;
import com.zareinaflameniano.sample.data.DatabaseHelper;
import com.zareinaflameniano.sample.data.TownModel;
import com.zareinaflameniano.sample.databinding.FragmentHomeBinding;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private long currentSelectedTownID; // Variable to store the currently selected town ID

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Instantiate ViewModel
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize UI components
        final Spinner sp_town = root.findViewById(R.id.sp_town);
        recyclerView = root.findViewById(R.id.recyclerView);
        EditText searchBar = root.findViewById(R.id.searchBar);

        // Initialize DatabaseHelper
        DatabaseHelper db = new DatabaseHelper(root.getContext());

        // Get list of towns from the database
        List<TownModel> townList = db.getAllTowns();

        // Set up the spinner
        ArrayAdapter<TownModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, townList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_town.setAdapter(adapter);

        // Set up the spinner item selection listener
        sp_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle item selection
                TownModel selectedTown = (TownModel) sp_town.getSelectedItem();
                currentSelectedTownID = selectedTown.getTownID();
                updateRecyclerView(currentSelectedTownID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Set up the search bar text change listener
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No changes needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAccommodationsList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No changes needed here
            }
        });

        // Set up the initial state of the UI
        updateRecyclerView(townList.get(0).getTownID());

        return root;
    }

    // Method to filter accommodations based on search string
    private void filterAccommodationsList(String searchString) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<AccommodationsModel> filteredList = db.searchAccommodations(currentSelectedTownID, searchString);

        // Update the data in the existing adapter
        customAdapter.updateData(filteredList);
    }

    // Method to update the RecyclerView based on the selected town
    private void updateRecyclerView(long selectedTownID) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<AccommodationsModel> accommodationsList = db.getAccommodationsByTownID(selectedTownID);

        Log.i("HomeFragment", "Data size: " + accommodationsList.size());

        // Initialize the adapter with the updated data
        customAdapter = new CustomAdapter(
                accommodationsList,
                new CustomAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(AccommodationsModel item) {
                        // Handle item click (e.g., start BookingActivity)
                        Intent intent = new Intent(getActivity(), BookingActivity.class);
                        intent.putExtra("accName", item.accName);
                        intent.putExtra("accLocation", item.accLocation);
                        intent.putExtra("accImage", item.accImage);
                        startActivity(intent);
                    }
                },
                new CustomAdapter.OnFaveClickListener() {
                    @Override
                    public void onItemClick(AccommodationsModel item, int position) {
                        // Toggle the favorite status
                        int newFavoriteStatus = item.accFavorite == 0 ? 1 : 0;

                        // Update the SharedPreferences
                        db.updateFavorite(item.accID, newFavoriteStatus);

                        // Update the item's favorite status in the list
                        accommodationsList.get(position).accFavorite = newFavoriteStatus;

                        // Notify the adapter of the change
                        customAdapter.notifyItemChanged(position);

                        // Show a Snackbar indicating the change
                        Snackbar.make(binding.getRoot(), item.accName + " has been " +
                                (newFavoriteStatus == 1 ? "added" : "removed") + " to your Favorites.", Snackbar.LENGTH_LONG).show();
                    }
                },
                getContext());

        // Set the adapter to the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(customAdapter);

        // Check and update the initial state of the favorite button for each item
        for (AccommodationsModel item : accommodationsList) {
            item.accFavorite = db.isFavorite(item.accID) ? 1 : 0;
        }
    }
}

