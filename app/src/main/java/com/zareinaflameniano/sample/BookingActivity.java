package com.zareinaflameniano.sample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private long selectedCheckInDate = 0;
    private long selectedCheckOutDate = 0;
    private ImageView tvImage;
    private TextView tvName;
    private  TextView tvLocation;
    private TextView tvDescription;
    private  TextView tvPrice;
    private double accPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent intent = getIntent();
        if (intent != null) {
            String accName = intent.getStringExtra("accName");
            String accLocation = intent.getStringExtra("accLocation");
            String accImage = intent.getStringExtra("accImage");
            accPrice = Double.parseDouble(intent.getStringExtra("accPrice"));
            String accDescription = intent.getStringExtra("accDescription");


            tvImage = findViewById(R.id.tvImage);
            tvName = findViewById(R.id.tvName);
            tvLocation = findViewById(R.id.tvLocation);
            tvDescription= findViewById(R.id.tvDescription);
            tvPrice = findViewById(R.id.tvPrice);


            int imageResourceId = getResources().getIdentifier(accImage, "drawable", getPackageName());
            if (imageResourceId != 0) {
                tvImage.setImageResource(imageResourceId);
            } else {
                tvImage.setImageResource(R.drawable.depthpool);
            }
            tvName.setText(accName);
            tvLocation.setText(accLocation);
            tvDescription.setText(accDescription);
            tvPrice.setText("Price: "+ accPrice);

            intent.putExtra("accImage", accImage);
            intent.putExtra("accName", accName);
            intent.putExtra("accLocation", accLocation);
        }

        Button btDetails = findViewById(R.id.btDetails);
        Button btCheckin = findViewById(R.id.btCheckin);
        btCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckInDatePicker();
            }
        });

        Button btCheckout = findViewById(R.id.btCheckout);
        btCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckOutDatePicker();
            }
        });

        Button btBook = findViewById(R.id.btBook);
        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btDetails.setVisibility(View.VISIBLE);
                if (selectedCheckInDate > 0 && selectedCheckOutDate > 0) {
                    btDetails.setVisibility(View.VISIBLE);
                    updateReservationText();
                    btBook.setText("Booked");
                } else {
                    // Show an alert message if no date is picked
                    showSnackbar("Please select check-in and check-out dates.");
                }
            }
        });

        btDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }



    private void showCheckInDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        selectedCheckInDate = selectedCalendar.getTimeInMillis();
                        String checkInMessage = "Check-in date selected: " + getFormattedDate(selectedCheckInDate);
                        showSnackbar(checkInMessage);

                    }
                }, year, month, dayOfMonth);

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }
    private void showCheckOutDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        selectedCheckOutDate = selectedCalendar.getTimeInMillis();
                        String checkOutMessage = "Check-out date selected: " + getFormattedDate(selectedCheckOutDate);
                        showSnackbar(checkOutMessage);

                        updateReservationText();
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.getDatePicker().setMinDate(selectedCheckInDate);

        datePickerDialog.show();
    }
    private void updateReservationText() {
        if (selectedCheckInDate > 0 && selectedCheckOutDate > 0) {
            int numberOfDays = getNumberOfDays();
            double amountDue = numberOfDays * accPrice;  // Use accPrice for computation
            String reservationMessage = "Reservation settled\nCheck-in: " +
                    getFormattedDate(selectedCheckInDate) +
                    "\nCheck-out: " + getFormattedDate(selectedCheckOutDate) +
                    "\nNumber of days: " + numberOfDays + " days" +
                    "\nAmount Due: " + amountDue;  // Include amount due in the message
            showReservationAlertDialog(reservationMessage);
        }
    }

    private void showReservationAlertDialog(String reservationMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reservation Details")
                .setMessage(reservationMessage)  // Fix here: use reservationMessage instead of message
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private int getNumberOfDays() {
        if (selectedCheckInDate > 0 && selectedCheckOutDate > 0) {
            long timeDifference = selectedCheckOutDate - selectedCheckInDate;
            long daysDifference = timeDifference / (24 * 60 * 60 * 1000);
            return (int) daysDifference;
        } else {
            return 0;
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. dd, yyyy", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }


}

