package com.example.gestco;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class adapterCompra extends BaseAdapter {

    private Context mContext;
    private Cursor mCursor;
    private List<Boolean> selectedItems;

    public adapterCompra(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        selectedItems = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            selectedItems.add(false); //Inicializamos todos los elementos como no seleccionados
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listaitemcompra, parent, false);
        }

        //Movemos el cusor a la posicion
        mCursor.moveToPosition(position);

        //Obtenemos el nombre del item del cursor
        String itemName = mCursor.getString(mCursor.getColumnIndexOrThrow("Nombre"));

        //Enviamos el nombre del item al textview
        //TextView textViewItemName = convertView.findViewById(R.id.checkBoxItem);
        CheckBox checkItemName = convertView.findViewById(R.id.checkBoxItem);
        checkItemName.setText(itemName);

        //Establecemos el estado del CheckBox según la lista de elementos seleccionados
        checkItemName.setChecked(selectedItems.get(position));


        return convertView;
    }
}