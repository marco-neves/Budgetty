package com.example.budgetty.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class ViewFragment extends Fragment implements TypeAdapter.TypeDelegate{
    @BindView(R.id.type_edittext_viewerFrag)
    EditText etType;
    @BindView(R.id.location_edittext_viewerFrag)
    EditText etLocation;
    @BindView(R.id.cost_edittext_viewerFrag)
    EditText etCost;
    @BindView(R.id.month_edittext_viewerFrag)
    EditText etMonth;
    @BindView(R.id.day_edittext_viewerFrag)
    EditText etDay;
    @BindView(R.id.year_edittext_viewerFrag)
    EditText etYear;
    @BindView(R.id.cancel_button_viewerFrag)
    Button btCancel;
    @BindView(R.id.confirm_button_viewerFrag)
    Button btConfirm;
    @BindView(R.id.types_recyclerview_viewerFrag)
    RecyclerView rvTypes;

    private Expense expense = null;
    private ExpenseType type;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expense_viewer_fragment,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        rvTypes.setLayoutManager(new LinearLayoutManager(this.getContext(),RecyclerView.HORIZONTAL,false));
        rvTypes.setAdapter(new TypeAdapter(this));
        rvTypes.setVisibility(View.INVISIBLE);
        super.onViewCreated(view, savedInstanceState);
        savedInstanceState = getArguments();
        try {
            expense = (Expense)(savedInstanceState.getParcelable(Constants.RETRIEVE_SELECTED_EXPENSE_KEY));
            type = ExpenseType.valueOf(expense.getType());
        }catch (NullPointerException n){
            Logger.logIt("onViewCreated: " + n);
        }
        if(expense==null){
            getFragmentManager().popBackStack();
        }else {
            setTexts();
            setClickListeners();
        }
    }
    private void setTexts(){
        etType.setText("Type: "+expense.getType());
        etType.setInputType(0);

        etLocation.setText("Location: "+expense.getLocation());
        etLocation.setInputType(0);

        etCost.setText("$"+expense.getCost());
        etCost.setInputType(0);

        String[] date = expense.getDate().split("/");
        etMonth.setText("Date: "+date[0]);
        etMonth.setInputType(0);
        etDay.setText(date[1]);
        etDay.setInputType(0);
        etYear.setText(date[2]);
        etYear.setInputType(0);
    }

    private void setClickListeners(){
        etType.setOnLongClickListener(new LongClickType());
        etLocation.setOnLongClickListener(new LongClickLocation());
        etMonth.setOnLongClickListener(new LongClickPrice());
        etDay.setOnLongClickListener(new LongClickPrice());
        etYear.setOnLongClickListener(new LongClickPrice());
        etCost.setOnLongClickListener(new LongClickDate());
        btCancel.setOnClickListener(new CancelButton());
        btConfirm.setOnClickListener(new ConfirmButton());
    }
    private class LongClickType implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setVisibility(View.INVISIBLE);
            rvTypes.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private class LongClickLocation implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setInputType(1);
            ((EditText)v).setText("");
            return true;
        }
    }

    private class LongClickPrice implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setInputType(2);
            ((EditText)v).setText("-");
            return true;
        }
    }

    private class LongClickDate implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setText("$");
            ((EditText)v).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            return true;
        }
    }

    private class CancelButton implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
        }
    }

    private class ConfirmButton implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Constants.VIEW_EXPENSE_ACTION);
            intent.setAction(Constants.VIEW_EXPENSE_ACTION);
            intent.putExtra(Constants.RETRIEVE_SELECTED_EXPENSE_KEY,getExpense());
            getContext().sendBroadcast(intent);
            getFragmentManager().popBackStack();
        }
    }
    @Override
    public void chooseType(ExpenseType etype) {
        this.type = etype;
        etType.setVisibility(View.VISIBLE);
        etType.setText(type.toString());
        rvTypes.setVisibility(View.INVISIBLE);
    }
    @Override
    public Drawable getDrawable() {
        return this.getResources().getDrawable(R.drawable.selected_type_bk);
    }
    private Expense getExpense(){
        Expense newExpense = new Expense();
        String date = "";
        date+= etMonth.getText().toString() +
                "/" + etDay.getText().toString() +
                "/" + etYear.getText().toString();

        newExpense.setKey(expense.getKey());
        newExpense.setType(type);
        newExpense.setLocation(etLocation.getText().toString());
        newExpense.setCost(Double.parseDouble(etCost.getText().toString()));
        newExpense.setDate(date);
        return newExpense;
    }
}
