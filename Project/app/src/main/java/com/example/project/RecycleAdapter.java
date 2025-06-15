package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterFactory;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RViewHolder> {
    Context context;
    ArrayList Items, Descriptions, Quantities, ID;
    WarehouseDatabase db;



    public RecycleAdapter(Context context, ArrayList item, ArrayList description, ArrayList quantity, ArrayList ID) {
        this.context = context;
        this.Items = item;
        this.Descriptions = description;
        this.Quantities = quantity;
        this.ID = ID;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(R.layout.grid_layout, parent, false);
        return new RViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        db = new WarehouseDatabase(context);
        holder.Item.setText(String.valueOf(Items.get(position)));
        String item = holder.Item.getText().toString();
        holder.Item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                db.updateTable(item, holder.Item.getText().toString(),holder.Description.getText().toString(),holder.Quantity.getText().toString() );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.Description.setText(String.valueOf(Descriptions.get(position)));
        holder.Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                db.updateTable(item, holder.Item.getText().toString(),holder.Description.getText().toString(),holder.Quantity.getText().toString() );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.Quantity.setText(String.valueOf(Quantities.get(position)));
        holder.Quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                db.updateTable(item, holder.Item.getText().toString(),holder.Description.getText().toString(),holder.Quantity.getText().toString() );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteRow(String.valueOf(ID.get(position)));
                ID.remove(position);
                Items.remove(position);
                Descriptions.remove(position);
                Quantities.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class RViewHolder extends RecyclerView.ViewHolder {
        EditText Item, Description, Quantity;
        Button Delete;
        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            Item = itemView.findViewById(R.id.Item);
            Description = itemView.findViewById(R.id.Description);
            Quantity = itemView.findViewById(R.id.Quantity);
            Delete = itemView.findViewById(R.id.Delete);
        }
    }
}
