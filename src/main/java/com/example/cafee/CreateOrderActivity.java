package com.example.cafee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CreateOrderActivity extends AppCompatActivity {

    private TextView textViewHello;
    private TextView textViewAdditions;
    private CheckBox checkBoxMilk;
    private CheckBox checkBoxSugar;
    private CheckBox checkBoxLemon;
    private Spinner spinnerTea;
    private Spinner spinnerCoffee;

    private String drink;
    private String name;
    private String password;
    private StringBuilder builderAdditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        Intent intent = getIntent();
        //проверяем имеет ли intent данные ключи
        if (intent.hasExtra("name") && intent.hasExtra("password")) {
            name = intent.getStringExtra("name");
            password = intent.getStringExtra("password");
        }else { //или присваиваем им знач-я по умолчанию
            name = getString(R.string.default_name);
            password = getString(R.string.default_password);
        }

        drink = getString(R.string.tea);//присвоили перем-й знач-е по умолчанию
        textViewHello = findViewById(R.id.textViewHello);
        String hello = String.format(getString(R.string.hello_user),name);
        textViewHello.setText(hello);
        textViewAdditions = findViewById(R.id.textViewAdditions);

        String additions = String.format(getString(R.string.additions),drink);
        textViewAdditions.setText(additions);
        checkBoxMilk = findViewById(R.id.checkboxMilk);
        checkBoxSugar = findViewById(R.id.checkboxSugar);
        checkBoxLemon = findViewById(R.id.checkboxLemon);
        spinnerTea = findViewById(R.id.spinnerTea);
        spinnerCoffee = findViewById(R.id.spinnerCoffee);
        builderAdditions = new StringBuilder();
    }

    public void onClickChangeDrink(View view) {
        RadioButton button = (RadioButton) view;
        int id = button.getId();
        if (id == R.id.radioButtonTea){
            drink = getString(R.string.tea);
            spinnerTea.setVisibility(View.VISIBLE);
            spinnerCoffee.setVisibility(View.INVISIBLE);
            checkBoxLemon.setVisibility(View.VISIBLE);
        }else if (id == R.id.radioButtonCoffee){
            drink = getString(R.string.coffee);
            spinnerTea.setVisibility(View.INVISIBLE);
            spinnerCoffee.setVisibility(View.VISIBLE);
            checkBoxLemon.setVisibility(View.INVISIBLE);
        }
        String additions = String.format(getString(R.string.additions),drink);
        textViewAdditions.setText(additions);
    }

    public void onClickSendOrder(View view) {
        // формируем список добавок

        //если StringBuilder содержал какие-то знач-я, то сначала очищаем его
        builderAdditions.setLength(0);

        //пробегаемся по всем chekbox и проверяем отмечены они или нет,
        //если отмечены, то добавляем их в StringBuilder
        if (checkBoxMilk.isChecked()){
            builderAdditions.append(getString(R.string.milk)).append(" ");
        }
        if (checkBoxSugar.isChecked()){
            builderAdditions.append(getString(R.string.sugar)).append(" ");
        }
        if (checkBoxLemon.isChecked() && drink.equals(getString(R.string.tea))){
            builderAdditions.append(getString(R.string.lemon)).append(" ");
        }
        //собираем все наши данные в строку order
        String optionOfDrink = "";
        if (drink.equals(getString(R.string.tea))){
            optionOfDrink = spinnerTea.getSelectedItem().toString();
        }else {
            optionOfDrink = spinnerCoffee.getSelectedItem().toString();
        }
        String order = String.format(getString(R.string.order),name,password,drink,optionOfDrink);
        //создаем строку с добавками
        String additions;
        if (builderAdditions.length() > 0){
            additions =  "\n" + getString(R.string.need_additions) + builderAdditions.toString();
        }else {
            additions = "";//или пустое значение
        }
        //Строка полного запроса будет состоять из строки запроса и строки добавки
        String fullOrder = order + additions;
        Intent intent = new Intent(this,OrderDetailActivity.class);
        intent.putExtra("order",fullOrder);
        startActivity(intent);
    }
}
