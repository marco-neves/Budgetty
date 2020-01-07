package com.example.budgetty.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetty.R;
import com.example.budgetty.adapter.TypeAdapter;
import com.example.budgetty.database.Expense;
import com.example.budgetty.database.ExpenseType;
import com.example.budgetty.util.Constants;
import com.example.budgetty.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFragment extends Fragment implements TypeAdapter.TypeDelegate {

    @BindView(R.id.type_recyclerview_addFrag)
    RecyclerView type;
    @BindView(R.id.location_edittext_addFrag)
    EditText location;
    @BindView(R.id.month_edittext_addFrag)
    EditText month;
    @BindView(R.id.et_day_addfrag)
    EditText day;
    @BindView(R.id.et_year_addfrag)
    EditText year;
    @BindView(R.id.cost_edittext_addFrag)
    EditText cost;
    @BindView(R.id.save_button_addFrag)
    Button save;

    private ExpenseType expenseType = ExpenseType.MISC;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expense_adder_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        type.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));
        type.setAdapter(new TypeAdapter(this));
    }

    @Override
    public void chooseType(ExpenseType type) {
        this.expenseType = type;
    }

    @Override
    public Drawable getDrawable() {
        return this.getResources().getDrawable(R.drawable.selected_type_bk);
    }

    @OnClick(R.id.save_button_addFrag)
    public void onClick(View view){
        String location = this.location.getText().toString().trim(),
                month = this.month.getText().toString().trim(),
                day = this.day.getText().toString().trim(),
                year = this.year.getText().toString().trim();
        double cost = 0;
        boolean valid = false;
        try{
            cost = Double.parseDouble(this.cost.getText().toString().trim());
        }catch (NumberFormatException e){
            Logger.logError(R.string.parse_double_error +cost+", "+e);
            Toast.makeText(this.getContext(), R.string.invalid_cost_text,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        try {
            double dMonth = Double.parseDouble(month),
                    dDay = Double.parseDouble(day);
            if( (dMonth > 12||dMonth==0) || year.equals("") || (dDay>31||dDay==0)){
                Toast.makeText(this.getContext(), R.string.incorrect_input,Toast.LENGTH_SHORT).show();
                valid = false;
            }else{valid=true;}
        }catch (NumberFormatException e){
            Toast.makeText(getContext(),R.string.incorrect_input,Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(valid) {
            Expense expense = new Expense(location, expenseType, cost, month + '/' + day + '/' + year);
            resetInput();
            broadcast(expense);
        }
    }

    @OnClick(R.id.cancel_button_addFrag)
    public void onCancel(View view){
        resetInput();
        getFragmentManager().popBackStack();
    }

    private void resetInput(){
        location.setText("");
        month.setText("");
        day.setText("");
        year.setText("");
        cost.setText("");
    }

    private void broadcast(Expense expense){
        Intent intent = new Intent(Constants.ADD_EXPENSE_ACTION);
        intent.setAction(Constants.ADD_EXPENSE_ACTION);
        intent.putExtra(Constants.RETRIEVE_EXPENSE_KEY,expense);
        getContext().sendBroadcast(intent);
        getFragmentManager().popBackStack();

        Logger.logIt("onClick: "+expense);
    }
}
