package com.ecucaristia.EcucaristiaAppmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecucaristia.EcucaristiaAppmobile.R;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListIglesias;

import java.util.ArrayList;

public class AdapterListIg extends BaseAdapter {
    private Context context;
    private ArrayList<modelListIglesias> arrayList;

    public AdapterListIg(Context context, ArrayList<modelListIglesias> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.item_list_iglesias, null);
        }
        TextView nombre = (TextView) convertView.findViewById(R.id.i_tv_txtName);
        nombre.setText(arrayList.get(position).getNombre());
        return convertView;
    }
}
