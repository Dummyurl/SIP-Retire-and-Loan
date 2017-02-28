package com.itechnotion.calculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class LoanFragment extends Fragment implements View.OnClickListener {

    EditText edtLoanAmt, edtYear, edtInterestRate ;
    TextView txtResult, txtEMI, txtTotalAmt ;
    LinearLayout llDetail ;
    ScrollView scrollView ;

    public LoanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loan, container, false);

        llDetail = (LinearLayout) view.findViewById(R.id.llDetail);

        edtLoanAmt = (EditText) view.findViewById(R.id.edtLoanAmt);
        edtYear = (EditText) view.findViewById(R.id.edtYear);
        edtInterestRate = (EditText) view.findViewById(R.id.edtInterestRate);

        txtEMI = (TextView) view.findViewById(R.id.txtEMI);
        txtResult = (TextView) view.findViewById(R.id.txtResult);
        txtResult.setOnClickListener(this);
        txtTotalAmt = (TextView) view.findViewById(R.id.txtTotalAmt);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);



        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtResult:

                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                if (TextUtils.isEmpty(edtLoanAmt.getText().toString())){
                    edtLoanAmt.setError("Please enter Loan amount");
                }else if (TextUtils.isEmpty(edtYear.getText().toString())){
                    edtYear.setError("Please enter Year");
                }else if (TextUtils.isEmpty(edtInterestRate.getText().toString())){
                    edtInterestRate.setError("Please enter Interest Rate");
                }else{

                    double loanAmount = Integer.parseInt(edtLoanAmt.getText().toString().trim());
                    double interestRate = (Double.parseDouble(edtInterestRate.getText().toString().trim()));
                    double loanPeriod = Integer.parseInt(edtYear.getText().toString().trim());

                    double r = interestRate/1200;
                    double r1 = Math.pow(r+1,loanPeriod * 12);

                    double emi = (double) ((r+(r/(r1-1))) * loanAmount);
                    double totalPayment = emi * (loanPeriod * 12);

                    txtEMI.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(emi));
                    txtTotalAmt.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(totalPayment));

                    llDetail.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
                    llDetail.setVisibility(View.VISIBLE);

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

                break;
        }
    }


}
