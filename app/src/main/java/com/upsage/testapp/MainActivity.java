/*
 * Copyright (c) 2019 Artem Martus
 */

package com.upsage.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Button addToCart;
    private Spinner groceriesSpinner;
    private Spinner cartContentSpinner;
    private Button removeFromCartButton;

    private List<String> selectedItems = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addToCart = findViewById(R.id.addToCartButton);
        groceriesSpinner = findViewById(R.id.groceriesSpinner);
        cartContentSpinner = findViewById(R.id.cartContentsSpinner);
        removeFromCartButton = findViewById(R.id.removeFromCartButton);

        ArrayAdapter<?> groceriesAdapter = ArrayAdapter.createFromResource(this,
                R.array.groceries, android.R.layout.simple_spinner_item);
        groceriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groceriesSpinner.setAdapter(groceriesAdapter);

        ArrayAdapter<?> cartAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, selectedItems);
        cartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cartContentSpinner.setAdapter(cartAdapter);

        groceriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] items = getResources().getStringArray(R.array.groceries);
                String selected = items[position];
                addToCart.setEnabled(!selectedItems.contains(selected));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
    }

    public void addToCart(View view) {
        String[] items = getResources().getStringArray(R.array.groceries);
        String selected = items[groceriesSpinner.getSelectedItemPosition()];
        if (!selectedItems.contains(selected)) {
            selectedItems.add(selected);
            ((ArrayAdapter) cartContentSpinner.getAdapter()).notifyDataSetChanged();
        }
        removeFromCartButton.setEnabled(true);
        addToCart.setEnabled(false);
    }

    public void removeFromCart(View view) {
        selectedItems.remove(cartContentSpinner.getSelectedItemPosition());
        ((ArrayAdapter) cartContentSpinner.getAdapter()).notifyDataSetChanged();
        removeFromCartButton.setEnabled(selectedItems.size() > 0);
    }

    public void showReceipt(View view) {
        StringBuilder message;
        if (selectedItems.size() > 0) {
            message = new StringBuilder("Вы выбрали ");
            for (String s : selectedItems)
                message.append(s).append(",");
            message.deleteCharAt(message.length() - 1);
        } else {
            message = new StringBuilder("Корзина пуста!");
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
