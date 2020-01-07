package com.example.budgetty.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetty.R;
import com.example.budgetty.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BudgetFragment extends Fragment {

    @BindView(R.id.cancel_button_budgetFrag)
    Button cancel;
    @BindView(R.id.confirm_button_budgetFrag)
    Button confirm;
    @BindView(R.id.set_budget_edittext_budgetFrag)
    EditText budget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.budget_setter_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        confirm.setOnClickListener(v -> {
            try {
                double budget = Double.parseDouble(this.budget.getText().toString().trim());
                budget = Math.round(budget*100)/100.0;
                broadcast(budget);
                getFragmentManager().popBackStack();
            }catch (Exception e){
                Log.d("TAG_X", "onClick: "+e);
            }
        });
        cancel.setOnClickListener(v -> {
            budget.setText("");
            getFragmentManager().popBackStack();
        });
    }

    private void broadcast(double budgetNum){
        Intent intent = new Intent(Constants.SET_BUDGET_ACTION);
        intent.setAction(Constants.SET_BUDGET_ACTION);
        intent.putExtra(Constants.SET_BUDGET_KEY,budgetNum);
        getContext().sendBroadcast(intent);
    }
}
