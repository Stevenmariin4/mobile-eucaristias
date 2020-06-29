package com.ecucaristia.EcucaristiaAppmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecucaristia.EcucaristiaAppmobile.R;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListEucaristias;

import java.util.ArrayList;

public class AdapterListE extends BaseAdapter {
    private Context context;
    private ArrayList<modelListEucaristias> arrayList;

    public AdapterListE(Context context, ArrayList<modelListEucaristias> arrayList) {
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

            convertView = layoutInflater.inflate(R.layout.item_lis_euc, null);
        }
        TextView fecha = (TextView) convertView.findViewById(R.id.i_tv_txtFecha);
        TextView dia = (TextView) convertView.findViewById(R.id.i_tv_txtDia);
        fecha.setText(arrayList.get(position).getFecha());
        dia.setText(arrayList.get(position).getDia());
        return convertView;
    }
}
