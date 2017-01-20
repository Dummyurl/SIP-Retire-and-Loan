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
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import me.grantland.widget.AutofitTextView;


public class RetirementFragment extends Fragment implements View.OnClickListener {

    EditText edtMonthlyExpense, edtInflation, edtReturn, edtAnnuityRate ;
    DiscreteSeekBar seekRetireAge, seekCurrentAge ;
    TextView txtResult, txtCurrentAge , txtRetireAge ;

    LinearLayout llDetail ;
    AutofitTextView txtSavingYear, txtMonthlyExpInflation, txtAnnualPension, txtCorpus,txtMonthlyInvestment, txtAnnualInvestment ;

    public RetirementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_retirement, container, false);

        llDetail = (LinearLayout) view.findViewById(R.id.llDetail);
        txtCurrentAge = (TextView) view.findViewById(R.id.txtCurrentAge);
        txtRetireAge = (TextView) view.findViewById(R.id.txtRetireAge);

        seekRetireAge = (DiscreteSeekBar) view.findViewById(R.id.seekRetireAge);
        seekRetireAge.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                txtRetireAge.setText(value+"");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        seekCurrentAge = (DiscreteSeekBar) view.findViewById(R.id.seekCurrentAge);
        seekCurrentAge.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                txtCurrentAge.setText(value+"");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        edtMonthlyExpense = (EditText) view.findViewById(R.id.edtMonthlyExpense);
        edtInflation = (EditText) view.findViewById(R.id.edtInflation);
        edtReturn = (EditText) view.findViewById(R.id.edtReturn);
        edtAnnuityRate = (EditText) view.findViewById(R.id.edtAnnuityRate);

        txtSavingYear = (AutofitTextView) view.findViewById(R.id.txtSavingYear);
        txtResult = (TextView) view.findViewById(R.id.txtResult);
        txtResult.setOnClickListener(this);
        txtMonthlyExpInflation = (AutofitTextView) view.findViewById(R.id.txtMonthlyExpInflation);
        txtAnnualPension = (AutofitTextView) view.findViewById(R.id.txtAnnualPension);
        txtCorpus = (AutofitTextView) view.findViewById(R.id.txtCorpus);
        txtMonthlyInvestment = (AutofitTextView) view.findViewById(R.id.txtMonthlyInvestment);
        txtAnnualInvestment = (AutofitTextView) view.findViewById(R.id.txtAnnualInvestment);

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

                int retireAge = Integer.parseInt(txtRetireAge.getText().toString()) ;
                int currentAge = Integer.parseInt(txtCurrentAge.getText().toString()) ;
                if (currentAge == 0){
                    Toast.makeText(getActivity(),"Current Age cannot be 0",Toast.LENGTH_LONG).show();
                }else if (retireAge == 0){
                    Toast.makeText(getActivity(),"Retirement Age cannot be 0",Toast.LENGTH_LONG).show();
                }else if (currentAge > retireAge ){
                    Toast.makeText(getActivity(),"Current Age cannot be greater than Retirement Age",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(edtMonthlyExpense.getText().toString())){
                    edtMonthlyExpense.setError("Please enter Monthly Expense");
                }else if (TextUtils.isEmpty(edtInflation.getText().toString())){
                    edtInflation.setError("Please enter Expected Inflation");
                }else if (TextUtils.isEmpty(edtReturn.getText().toString())){
                    edtReturn.setError("Please enter Expected Return");
                }else {
                    int savingYear = ( Integer.parseInt(txtRetireAge.getText().toString()) - Integer.parseInt(txtCurrentAge.getText().toString()) ) ;
                    txtSavingYear.setText(savingYear + "");

                    // monthly exp
                    double inflationRate = (Double.parseDouble(edtInflation.getText().toString().trim()));
                    double inflationRate1 = (1 + (inflationRate / 100)) ;
                    double power = Math.pow(inflationRate1,Double.parseDouble(txtSavingYear.getText().toString()));
                    double result = Double.parseDouble(edtMonthlyExpense.getText().toString()) * power;
                    txtMonthlyExpInflation.setText(NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(result));

                    double annualPension = result * 12;
                    txtAnnualPension.setText("" + NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(annualPension));

                    double corpus = annualPension * 15;
                    txtCorpus.setText("" + NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(corpus));

                    double monthlyRate = (Double.parseDouble(edtReturn.getText().toString()) / 1200);
                    double totalYear = Double.parseDouble(txtSavingYear.getText().toString()) * 12 ;

                    double ans = FinanceLib.pmt(monthlyRate,totalYear,0,-corpus,false);
                    txtMonthlyInvestment.setText("" + NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(ans));

                    double annualInvest = ans * 12;
                    txtAnnualInvestment.setText("" + NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(annualInvest));
                    Log.e("Format : ", NumberFormat.getNumberInstance(new Locale("en", "in")).format(annualInvest));
                    Log.e("Format 1 : ", NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(annualInvest));

                    llDetail.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
                    llDetail.setVisibility(View.VISIBLE);
                }



                break;
        }
    }

    public static double calc(double rate, double term, double amount) {
//		r = 10; n = 36; p = 100000; f = 0; t = true;
//        y = FinanceLib.pmt(0.00740260861, 180, -984698, 0, false);
		/*
		    Symbols used in the formulae that follow:
			p: present value
			f: future value
			n: number of periods
			y: payment (in each period)
			r: rate
			^: the power operator (NOT the java bitwise XOR operator!)
	    */
        double r = rate/12;
        double t = (term)*12;
        return FinanceLib.pmt(r, t, -amount, 0, false);
    }
    /*public class PMT {

        public static double calc(double rate, double term, double amount) {
//		r = 10; n = 36; p = 100000; f = 0; t = true;
//        y = FinanceLib.pmt(0.00740260861, 180, -984698, 0, false);

		    Symbols used in the formulae that follow:
			p: present value
			f: future value
			n: number of periods
			y: payment (in each period)
			r: rate
			^: the power operator (NOT the java bitwise XOR operator!)

            double r = rate/12;
            double t = (term/12)*12;
            return FinanceLib.pmt(r, t, -amount, 0, false);
        }

        public static double pmtJava(double rate, double term, double amount) {
            double r = rate/12;
            double t = (term/12)*12;
            return (amount*r)/(1 - Math.pow(1+r,-t));
        }

        public static void main(String[] args) {

            System.out.println("pmtJava: " + pmtJava(0.10, 36, 100000));

            System.out.println("pmt = " + PMT.calc(0.10, 36, 100000));
        }

    }*/
}
