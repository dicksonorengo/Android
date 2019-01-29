package com.himebaugh.calculator;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.Math;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {
    String answer="",firstnumber="",currentoperator="",calculation = "",prev_ans="",secondnumber="",function="";
    Double result=0.0,temp=0.0,numberone=0.0,numbertwo=0.0;
    Boolean dot=false,root=false,power1=false;
    TextView mCalculatorDisplay,history;
    DecimalFormat df = new DecimalFormat("@###########");
    NumberFormat format, longformate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalculatorDisplay = (TextView) findViewById(R.id.textViewDisplay);
        format = new DecimalFormat("#.####");
        longformate = new DecimalFormat("0.#E0");
        history = (TextView)findViewById(R.id.history);
        history.setMovementMethod(new ScrollingMovementMethod());
        mCalculatorDisplay.setMovementMethod(new ScrollingMovementMethod());
        if(savedInstanceState!=null){
            answer = savedInstanceState.getString("answer");
            calculation = savedInstanceState.getString("calculation");
            mCalculatorDisplay.setText(answer);
            history.setText(calculation);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("answer",answer);
        outState.putString("calculation",calculation);
    }

    public void pressedNumber(View v){
            Button textView = (Button) v;
            calculation += textView.getText();
            firstnumber += textView.getText();
            numberone = Double.parseDouble(firstnumber);
            if (root) {
                numberone = Math.sqrt(numberone);
            }
            switch (currentoperator) {
                case "":
                    if(power1){
                        temp = result + Math.pow(numbertwo,numberone);
                        answer = format.format(temp).toString();
                    }
                    else{
                        temp = result + numberone;
                        answer = format.format(temp).toString();}
                    break;
                case "+":
                    if(power1){
                        temp = result + Math.pow(numbertwo,numberone);
                        answer = format.format(temp).toString();
                    }
                    else {
                        temp = result + numberone;
                        answer = format.format(temp).toString(); }
                    break;
                case "-":
                    if(power1){
                        temp = result - Math.pow(numbertwo,numberone);
                        answer = format.format(temp).toString();
                    }
                    else {
                        temp = result - numberone;
                        answer = format.format(temp).toString(); }
                    break;
                case "*":
                    if(power1){
                        temp = result * Math.pow(numbertwo,numberone);
                        answer = format.format(temp).toString();
                    }
                    else {
                        temp = result * numberone;
                        answer = format.format(temp).toString(); }
                    break;
                case "/":
                    try {
                        if(power1){
                            temp = result / Math.pow(numbertwo,numberone);
                            answer = format.format(temp).toString();
                        }
                        else {
                            temp = result / numberone;
                            answer = format.format(temp).toString(); }
                    } catch (Exception e) {
                        answer = e.getMessage();
                    }
                    break;
            }
            updateallofthem();
    }
    public void updateallofthem () {
        history.setText(calculation);
        mCalculatorDisplay.setText(answer);
    }
    public void pressedoperator(View v) {
        Button text1 = (Button) v;
        if (answer != "") {
            if (currentoperator != "") {
                char c = getcharfromLast(calculation, 2);
                //System.out.println(calculation);
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    calculation = calculation.substring(0, calculation.length() - 3);
                }
            }
            calculation = calculation + " " + text1.getText() + " ";
            firstnumber = "";
            numberone = 0.0;
            result = temp;
            currentoperator = text1.getText().toString();
            updateallofthem();
            dot= false;
            root = false;
            power1 =false;

        }
    }
    private char getcharfromLast (String s,int i){
        char c = s.charAt(s.length() - i);
        return c;
    }
    public void dotpressed(View v){
        if(!dot){
            if(firstnumber.length() == 0){
                firstnumber = "0.";
                calculation += "0.";
                answer = "0.";
                dot =true;
                updateallofthem();
            }
            else {
                firstnumber += ".";
                calculation += ".";
                answer += ".";
                dot =true;
                updateallofthem();
            }
        }
    }
    public void equalpressed(View v){
        if(answer!="" && answer!=prev_ans){
            calculation += "\n= "+answer+"\n";
            firstnumber = "";
            result = temp;
            numberone = 0.0;
            prev_ans = answer;
            updateallofthem();
            dot=true;
        }
    }
    public void cleartextview (View v){
        answer = "";
        currentoperator = "";
        firstnumber = "";
        result = 0.0;
        temp = 0.0;
        numberone = 0.0;
        calculation = "";
        numbertwo=0.0;
        secondnumber="";
        updateallofthem();
        dot = false;
        root = false;
        power1 = false;
    }
    public  void minusfuntion(View v){
        if(answer!="" && getcharfromLast(calculation,1)!=' '){
            numberone = numberone*(-1);
            firstnumber = numberone.toString();
            switch (currentoperator){
                case "":
                    temp = numberone;
                    calculation = firstnumber;
                    break;
                case "+":
                    temp = result + numberone;
                    removeuntilchar(calculation, ' ');
                    calculation += firstnumber;
                    break;
                case "-":
                    temp = result - numberone;
                    removeuntilchar(calculation, ' ');
                    calculation += firstnumber;
                    break;
                case "*":
                    temp = result * numberone;
                    removeuntilchar(calculation, ' ');
                    calculation += firstnumber;
                    break;
                case "/":
                    try {
                        temp = result / numberone;
                        removeuntilchar(calculation, ' ');
                        calculation += firstnumber;
                    } catch (Exception e) {
                        answer = e.getMessage();
                    }
                    break;
            }
            answer = format.format(temp).toString();
        }
    }
    public void removeuntilchar(String str, char chr) {
        char c = getcharfromLast(str, 1);
        if (c != chr) {
            str = removechar(str, 1);
            calculation = str;
            updateallofthem();
            removeuntilchar(str, chr);
        }
    }
    public String removechar(String str, int i) {
        char c = str.charAt(str.length() - i);
        if (c == '.' && !dot) {
            dot = false;
        }
        if (c == '^') {
          power1 = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }
    public void rootpressed(View v) {
        Button root1 = (Button) v;
        if (answer == "" && result == 0 && !root) {
            calculation += root1.getText().toString();
            root = true;
            updateallofthem();
        } else if (getcharfromLast(calculation, 1) == ' ' && currentoperator != "" && !root) {
            calculation += root1.getText().toString();
            root = true;
            updateallofthem();
        }
    }

    public void powerpressed(View v) {
        Button power = (Button)v;
        if (calculation != "" && !root && !power1) {
            if (getcharfromLast(calculation, 1) != ' ') {
                calculation += power.getText().toString();
                secondnumber = firstnumber;
                numbertwo = numberone;
                firstnumber = "";
                power1 = true;
                updateallofthem();
            }
        }
    }
    public void modulopressed(View v) {
        if (answer != "" && getcharfromLast(calculation, 1) != ' ') {
            calculation += "% ";
            switch (currentoperator) {
                case "":
                    temp = temp / 100;
                    break;
                case "+":
                    temp = result + ((result * numberone) / 100);
                    break;
                case "-":
                    temp = result - ((result * numberone) / 100);
                    break;
                case "x":
                    temp = result * (numberone / 100);
                    break;
                case "/":
                    try {
                        temp = result / (numberone / 100);
                    } catch (Exception e) {
                        answer = e.getMessage();
                    }
                    break;
            }
            answer = format.format(temp).toString();
            if (answer.length() > 9) {
                answer = longformate.format(temp).toString();
            }
            result = temp;
            updateallofthem();
        }
    }
    public void anotherfuntionpressed(View v) {
        Button func = (Button)v;
        function = func.getText().toString();
        switch (currentoperator) {
            case "":
                switch (function) {
                    case "sin":
                        temp = result + Math.sin(numberone);
                        //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + Math.sin(50.0));
                        calculation = "sin(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "cos":
                        temp = result + Math.cos(Math.toDegrees(numberone));
                        calculation = "cos(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "tan":
                        temp = result + Math.tan(Math.toDegrees(numberone));
                        calculation = "tan(" + numberone;
                        answer = temp.toString();
                        break;
                }
                updateallofthem();
                break;
            case "+":
                switch (function) {
                    case "sin":
                        temp = result + Math.sin(Math.toDegrees(numberone));
                        calculation += "sin(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "cos":
                        temp = result + Math.cos(Math.toDegrees(numberone));
                        calculation += "cos(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "tan":
                        temp = result + Math.tan(Math.toDegrees(numberone));
                        calculation += "tan(" + firstnumber;
                        answer = temp.toString();
                        break;
                }
                updateallofthem();
                break;

            case "-":
                switch (function) {
                    case "sin":
                        temp = result + Math.sin(Math.toDegrees(numberone));
                        calculation += "sin(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "cos":
                        temp = result + Math.cos(Math.toDegrees(numberone));
                        calculation += "cos(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "tan":
                        temp = result + Math.tan(Math.toDegrees(numberone));
                        calculation += "tan(" + firstnumber;
                        answer = temp.toString();
                        break;
                }
                updateallofthem();
                break;
            case "*":
                switch (function) {
                    case "sin":
                        temp = result + Math.sin(Math.toDegrees(numberone));
                        calculation += "sin(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "cos":
                        temp = result + Math.cos(Math.toDegrees(numberone));
                        calculation += "cos(" + firstnumber;
                        answer = temp.toString();
                        break;
                    case "tan":
                        temp = result + Math.tan(Math.toDegrees(numberone));
                        calculation += "tan(" + firstnumber;
                        answer = temp.toString();
                        break;
                }
                updateallofthem();
                break;
            case "/":
                removeuntilchar(calculation, ' ');
                switch (function) {
                    case "sin":
                        try {
                            temp = result / Math.sin(Math.toDegrees(numberone));
                            calculation += "sin(" + firstnumber;
                            answer = temp.toString();
                        } catch (Exception e) {
                            answer = e.getMessage();
                        }
                        break;
                    case "cos":
                        try {
                            temp = result / Math.cos(Math.toDegrees(numberone));
                            calculation += "cos(" + firstnumber;
                            answer = temp.toString();
                        } catch (Exception e) {
                            answer = e.getMessage();
                        }
                        break;
                    case "tan":
                        try {
                            temp = result / Math.tan(Math.toDegrees(numberone));
                            calculation += "tan(" + firstnumber;
                            answer = temp.toString();
                        } catch (Exception e) {
                            answer = e.getMessage();
                        }
                        break;
                }
                updateallofthem();
                break;
        }
    }
}
