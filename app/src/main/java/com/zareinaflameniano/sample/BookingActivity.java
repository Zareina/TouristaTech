package com.zareinaflameniano.sample;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
    private TextView tvLocation;
    private TextView tvDescription;
    private TextView tvPrice;
    private Button btBook;
    private Button btCheckin;
    private Button btCheckout;
    private Button btDetails;
    private int accPrice;
    private boolean isReservationCancelled;

    private static final String PREF_NAME = "BookingPreferences";
    private static final String KEY_RESERVATION_CANCELLED = "isReservationCancelled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent intent = getIntent();
        if (intent != null) {
            String accName = intent.getStringExtra("accName");
            String accLocation = intent.getStringExtra("accLocation");
            String accImage = intent.getStringExtra("accImage");
            accPrice = Integer.parseInt(intent.getStringExtra("accPrice"));
            String accDescription = intent.getStringExtra("accDescription");

            tvImage = findViewById(R.id.tvImage);
            tvName = findViewById(R.id.tvName);
            tvLocation = findViewById(R.id.tvLocation);
            tvDescription = findViewById(R.id.tvDescription);
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
            tvPrice.setText("PHP " + accPrice);
        }

        btBook = findViewById(R.id.btBook);
        btCheckin = findViewById(R.id.btCheckin);
        btCheckout = findViewById(R.id.btCheckout);
        btDetails = findViewById(R.id.btDetails);

        isReservationCancelled = getReservationState();

        // Initial setup of button visibility based on reservation state
        if (isReservationCancelled) {
            showBookButton();
        } else {
            hideCheckinCheckoutButtons();
        }

        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReservationCancelled) {
                    showCheckinCheckoutButtons();
                    isReservationCancelled = false;
                    saveReservationState(isReservationCancelled);
                } else {
                    hideCheckinCheckoutButtons();
                    isReservationCancelled = true;
                    saveReservationState(isReservationCancelled);
                }
            }
        });

        btCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckInDatePicker();
            }
        });

        btCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCheckInDate > 0) {
                    showCheckOutDatePicker();
                    if (selectedCheckOutDate > 0) {
                        showReservationAlertDialog();
                    } else {
                        showSnackbar("Please select check-out date");
                    }
                } else {
                    showSnackbar("Please select check-in date");
                }
            }
        });


        btDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isReservationCancelled && selectedCheckInDate > 0 && selectedCheckOutDate > 0) {
                    updateReservationText();
                } else {
                    showSnackbar("Please complete the reservation details first.");
                }
            }
        });

        // Set initial visibility of btDetails based on reservation state
        if (isReservationCancelled || selectedCheckInDate == 0 || selectedCheckOutDate == 0) {
            btDetails.setVisibility(View.GONE);
        } else {
            btDetails.setVisibility(View.VISIBLE);
        }
    }
    private void showReservationAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reservation Complete")
                .setMessage("Reservation complete")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showCancelReservationButton();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateReservationText() {
        if (selectedCheckInDate > 0 && selectedCheckOutDate > 0) {
            int numberOfDays = getNumberOfDays();
            double amountDue = numberOfDays * accPrice;
            String reservationMessage = "Reservation settled\nCheck-in: " +
                    getFormattedDate(selectedCheckInDate) +
                    "\nCheck-out: " + getFormattedDate(selectedCheckOutDate) +
                    "\nNumber of days: " + numberOfDays + " days" +
                    "\nAmount Due: " + amountDue;

            // Display the reservation details
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Reservation Details")
                    .setMessage(reservationMessage)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            showCancelReservationButton();
        }
    }

    private void showCheckinCheckoutButtons() {
        btCheckin.setVisibility(View.VISIBLE);
        btCheckout.setVisibility(View.VISIBLE);
        btBook.setVisibility(View.GONE);
    }

    private void hideCheckinCheckoutButtons() {
        btCheckin.setVisibility(View.GONE);
        btCheckout.setVisibility(View.GONE);
        btBook.setVisibility(View.VISIBLE);
        btBook.setText("MAKE A RESERVATION");
    }

    private void showBookButton() {
        btBook.setVisibility(View.VISIBLE);
        btCheckin.setVisibility(View.GONE);
        btCheckout.setVisibility(View.GONE);
        btBook.setText("MAKE A RESERVATION");
    }

    private void showCancelReservationButton() {
        btBook.setVisibility(View.VISIBLE);
        btCheckin.setVisibility(View.GONE);
        btCheckout.setVisibility(View.GONE);
        btDetails.setVisibility(View.VISIBLE);
        btBook.setText("CANCEL RESERVATION");
    }

    private void saveReservationState(boolean isCancelled) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_RESERVATION_CANCELLED, isCancelled);
        editor.apply();
    }

    private boolean getReservationState() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getBoolean(KEY_RESERVATION_CANCELLED, false);
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

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. dd, yyyy", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
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

    private boolean doesReservationOverlap(long checkInDate, long checkOutDate) {
        // Logic to check if the selected dates overlap with existing reservations
        // You can implement this based on your specific requirements
        return false;
    }
}
