package com.ecucaristia.EcucaristiaAppmobile.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ecucaristia.EcucaristiaAppmobile.R;

import java.util.ArrayList;


public class listIglesiasFragment extends Fragment {
    private ListView listview;
    private ArrayList<String> names;

    public listIglesiasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_iglesias, container, false);
        // Inflate the layout for this fragment

        names = new ArrayList<String>();
        names.add("Veracruz");
        names.add("Tabasco");
        names.add("Chiapas");
        names.add("Campeche");
        names.add("Quintana Roo");

        ListView listView = (ListView) view.findViewById(R.id.listaIglesias);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);

        listview.setAdapter(itemsAdapter);
        return view;
    }
}
