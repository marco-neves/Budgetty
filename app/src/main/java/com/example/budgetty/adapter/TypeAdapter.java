package com.example.budgetty.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetty.R;
import com.example.budgetty.database.ExpenseType;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {


    public interface TypeDelegate{
        void chooseType(ExpenseType type);
        Drawable getDrawable();
    }

    private ExpenseType[] types = ExpenseType.values();
    private TypeDelegate delegate;
    private int selected = -1;
    private Drawable background;

    public TypeAdapter(TypeDelegate delegate) {
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public TypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.types_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.ViewHolder holder, int position) {
        holder.tvType.setText(types[position].toString());
        background = delegate.getDrawable();
        holder.itemView.setOnClickListener(v -> {
            delegate.chooseType(types[position]);
            selected = position;
            holder.itemView.setBackground(background);
        });
        if(selected==position){
            holder.itemView.setBackground(background);
        }else{
            holder.itemView.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return types.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.type_textview_recyclerview);
        }
    }
}
