package com.example.project;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    WarehouseDatabase inventory = new WarehouseDatabase(MainActivity.this);
    LoginDatabase logins = new LoginDatabase(MainActivity.this);

    ArrayList Items, Descriptions, Quantity, ID;
    RecycleAdapter adapter;
    boolean getNotifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login);
        Button forgotPass = findViewById(R.id.forgotpassword);
        EditText Username = findViewById(R.id.username);
        EditText Password = findViewById(R.id.password);
        getNotifications = false;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Username.getText().toString().trim().equals("") || !Password.getText().toString().equals("")) {
                    if (logins.Login(Username.getText().toString().trim(), Password.getText().toString().trim())) {
                        viewWarehouse();
                    } else if (!logins.usernameExists(Username.getText().toString().trim())) {
                        setContentView(R.layout.pin);
                        EditText pin = findViewById(R.id.Pin);
                        Button register = findViewById(R.id.registerPin);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(pin.getText().toString().length() == 4) {
                                    logins.addUser(Username.getText().toString().trim(), Password.getText().toString().trim(), Integer.parseInt(pin.getText().toString()));
                                    viewWarehouse();
                                }else{
                                    Toast.makeText(MainActivity.this, "Pin must e 4 digits long.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.forgotpassword);
                EditText Pin = findViewById(R.id.pin2);
                EditText Username = findViewById(R.id.inputUsername);
                Button Login = findViewById(R.id.LoginWithPin);
                Login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Pin.getText().toString().equals("") || Username.getText().toString().equals("")){
                            Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        }else {
                            if (logins.verifyPin(Username.getText().toString(), Integer.parseInt(Pin.getText().toString().trim()))) {
                                viewWarehouse();
                            } else {
                                Toast.makeText(MainActivity.this, "incorrect username or pins", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    private void viewWarehouse() {
        ID = new ArrayList<>();
        Items = new ArrayList<>();
        Descriptions = new ArrayList<>();
        Quantity = new ArrayList<>();
        setContentView(R.layout.activity_main);
        Cursor cursor = inventory.getInventory();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Empty.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                ID.add(cursor.getString(0));
                Items.add(cursor.getString(1));
                Descriptions.add(cursor.getString(2));
                Quantity.add(Integer.parseInt(cursor.getString(3)));
            }
            cursor.close();
        }
        sendNotification(getNotifications);

        //Create inventory grid
        adapter = new RecycleAdapter(MainActivity.this, Items, Descriptions, Quantity, ID);
        RecyclerView InventoryGrid = findViewById(R.id.InventoryGrid);
        InventoryGrid.setAdapter(adapter);
        InventoryGrid.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //create button references
        Button add = findViewById(R.id.add_button);
        Button notifications = findViewById(R.id.notifications);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_additemsscreen);
                Button additem = findViewById(R.id.add_button2);
                Button back = findViewById(R.id.backButton);
                additem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText item = findViewById(R.id.item);
                        EditText description = findViewById(R.id.description);
                        EditText quantity = findViewById(R.id.quantity);
                        if(!quantity.getText().toString().trim().equals("") && !description.getText().toString().trim().equals("") && !item.getText().toString().trim().equals("")) {
                            inventory.addInventory(item.getText().toString().trim(), description.getText().toString().trim(), quantity.getText().toString().trim());
                            viewWarehouse();
                        }else{
                            Toast.makeText(MainActivity.this, "Please ensure all fields are entered.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewWarehouse();
                    }
                });
            }

        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Would you like to turn on low quantity notifications?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getNotifications = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                            }

                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getNotifications = false;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void sendNotification(boolean getNotifications) {
        if (getNotifications) {
            for (Object i : Quantity) {
                if (Integer.parseInt(i.toString().trim()) < 5) {
                    String channel = "LOW_QUANTITY";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel)
                            .setSmallIcon(R.drawable.baseline_store_24)
                            .setContentTitle("Low Quantity.")
                            .setContentText("One or more of your items have Quantities below 5.")
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    builder.setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(channel, "Low Quantity", NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }

                    notificationManager.notify(0, builder.build());
                }
            }
        }
    }
}
