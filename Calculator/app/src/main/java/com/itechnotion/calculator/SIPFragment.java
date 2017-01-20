package com.itechnotion.calculator;

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
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SIPFragment extends Fragment implements View.OnClickListener {

    //#8BC34A
    EditText edtExpectedReturn, edtMonthlyInvestment, edtYear, edtLumpsum ;
    TextView txtResult, txtAmount;
    LinearLayout llDetail ;

    public SIPFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sip, container, false);

        llDetail = (LinearLayout) view.findViewById(R.id.llDetail);
        edtExpectedReturn = (EditText) view.findViewById(R.id.edtExpectedReturn);
        edtMonthlyInvestment = (EditText) view.findViewById(R.id.edtMonthlyInvestment);
        edtYear = (EditText) view.findViewById(R.id.edtYear);
        edtLumpsum = (EditText) view.findViewById(R.id.edtLumpsum);

        txtResult = (TextView) view.findViewById(R.id.txtResult);
        txtResult.setOnClickListener(this);
        txtAmount = (TextView) view.findViewById(R.id.txtAmount);

        return view ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtResult:

                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (TextUtils.isEmpty(edtExpectedReturn.getText().toString())){
                    edtExpectedReturn.setError("Please enter Expected Return");
                }else if (TextUtils.isEmpty(edtMonthlyInvestment.getText().toString())){
                    edtMonthlyInvestment.setError("Please enter Monthly Investment");
                }else if (TextUtils.isEmpty(edtYear.getText().toString())){
                    edtYear.setError("Please enter Year");
                }else{

                    double loanAmount = Integer.parseInt(edtMonthlyInvestment.getText().toString().trim());
                    Log.e("amt :", loanAmount + "");
                    double interestRate = (Double.parseDouble(edtExpectedReturn.getText().toString().trim()));
                    double loanPeriod = Integer.parseInt(edtYear.getText().toString().trim());

                    double r = interestRate/1200;
                    double r1 = Math.pow(r+1,loanPeriod * 12);

                    double emi = (double) ((r+(r/(r1-1))) * loanAmount);
                    double totalPayment = emi * (loanPeriod * 12);

                    double monthlyRate = (Double.parseDouble(edtExpectedReturn.getText().toString().trim()) / 1200);
                    Log.e("monthly rate :", monthlyRate + "");
                    double totalYear = Double.parseDouble(edtYear.getText().toString().trim()) * 12 ;
                    Log.e("year :", totalYear + "");

                    double finalAmount ;
                    if (TextUtils.isEmpty(edtLumpsum.getText().toString())){
                        finalAmount = FinanceLib.fv(monthlyRate,totalYear,-loanAmount,0,false);
                    }else{
                        double lumpsum = Integer.parseInt(edtLumpsum.getText().toString().trim());
                        finalAmount = FinanceLib.fv(monthlyRate,totalYear,-loanAmount,-lumpsum,false);
                    }


                    //txtEMI.setText(new DecimalFormat("##.##").format(emi));

                    txtAmount.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(finalAmount));

                    Pattern pattern = Pattern.compile("\\-?[0-9,]+\\.[0-9]{2}");
                    Matcher matcher = pattern.matcher(finalAmount+"");
                    Log.e("Matcher :",matcher+ "");
                    if (matcher.find())
                    {
                        System.out.println("match " + matcher.group(0));
                    }


                    llDetail.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
                    llDetail.setVisibility(View.VISIBLE);

                }

                break;
        }
    }

}
