package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;



public class MainActivity extends AppCompatActivity {

    //Declaramos los botones numéricos

    private int[] numericButtons = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree,
    R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine};

    //Declaramos los botones que realizan operaciones
    private int[] operatorButtons = {R.id.btnEqual, R.id.btnPlus, R.id.btnMinus, R.id.btnPlusMinus,
    R.id.btnDivision, R.id.btnMMinus, R.id.btn1x, R.id.btnC, R.id.btnx2, R.id.btnRoot, R.id.btnMultiplication,
    R.id.btnPercentage, R.id.btnCE, R.id.btnMc, R.id.btnMr, R.id.btnMS, R.id.btnMx, R.id.btnMPlus};

    //Declaramos la pantalla
    private TextView txtScreen;

    //Representa si la última tecla presionada es numérica o no
    private boolean lastNumeric;

    //Representa si el estado actual es erróneo o no
    private boolean stateError;

    //Si es true no permite añadir otro punto
    private boolean lastDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Encuentra el TextView
        this.txtScreen = (TextView) findViewById(R.id.txtScreen);

        //Encuentra y añade un OnClickListener a los botones numéricos
            setNumericOnClickListener();

        //Encuentra y añade un OnClickListener a los botones de operaciones
            setOperatorOnClickListener();
    }

    private void setNumericOnClickListener(){
        //Creo un onClickListener común
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Solo añade el texto de un botón que ha sido pulsado
                Button button =(Button) view;
                //Si el estado actual es Error, reemplaza el mensaje de error.
                if (stateError){
                    txtScreen.setText(button.getText());
                    stateError = false;
                    //Si no, todavía hay una expresión válida, así que la añade
                } else {
                    txtScreen.append(button.getText());
                }
                lastNumeric = true;
            }
        };

        //Añade un listener a todos los botones numéricos
        for (int id : numericButtons){
            findViewById(id).setOnClickListener(listener);
        }
    }

    //Encuentra y añade un onClickListener a todos los botones operadores, botón de igual y boton de punto decimal.
    private void setOperatorOnClickListener(){
        //Creo un onClickListener común para los operadores
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si el estado actual es un error, no añadas
                //Solo si el ultimo boton es un número, añade el operador
                if (lastNumeric && !stateError){
                    Button button = (Button) view;
                    txtScreen.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;  //Reseteo el dot flag
                }
            }
        };

        for (int id : operatorButtons){
            findViewById(id).setOnClickListener(listener);
        }

        //Punto decimal

        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastNumeric && !stateError && !lastDot){
                    txtScreen.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });

        //Boton C

        findViewById(R.id.btnC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtScreen.setText(""); //limpia la pantalla
                //reseteo todos los estados y flags
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });

        //Boton igual

        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEqual();
            }
        });

    }

    private void onEqual(){
        //Si el estado actual es error, no hace nada
        //Si únicamente el ultimo input es un número, la calculadora da solución
        if (lastNumeric && !stateError){

            //lee la expresión
            String txt = txtScreen.getText().toString();
            //Crea una Expression (libreria externa)
            Expression expression = new ExpressionBuilder(txt).build();
            try{
                //Calcula el resultado y lo muestra
                double result = expression.evaluate();
                txtScreen.setText(Double.toString(result));
                lastDot = true; //El resultado contiene un punto
            }catch (ArithmeticException e){
                //Muestra un mensaje de error.
                txtScreen.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }




}