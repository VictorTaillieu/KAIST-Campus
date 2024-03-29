package com.example.kaistcampusv2.ui.facilities;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kaistcampusv2.OnCampusFacility;
import com.example.kaistcampusv2.OnCampusFacilityAdapter;
import com.example.kaistcampusv2.R;
import java.util.ArrayList;
import java.io.*;
import java.util.List;

public class FacilitiesFragment extends Fragment {
    private RecyclerView recyclerView;
    private OnCampusFacilityAdapter adapter;
    private List<OnCampusFacility> facilitiesList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_facilities, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        facilitiesList = new ArrayList<>();
        createListData();
        adapter = new OnCampusFacilityAdapter(this.getContext(), facilitiesList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void loadData(String file){
        String line;
        BufferedReader csvReader = null;
        AssetManager assetManager = getResources().getAssets();

        try {
            InputStream inputStream = assetManager.open(file);
            csvReader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = csvReader.readLine()) != null){
                String[] data = line.split(";");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String description = data[2];
                String type = data[3];
                String location = data[4];
                String[] days = data[5].split(",");
                String hours = data[6];
                boolean onCampus = (data[7].equals("1"));
                String contact = data[8];

                facilitiesList.add(new OnCampusFacility(id, name, description, type, location, days, hours, onCampus, contact));
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createListData()
    {
        loadData("facilities.csv");
    }
}