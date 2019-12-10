package io.github.afifaniks.heimerx;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.erz.joysticklibrary.JoyStick;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements JoyStick.JoyStickListener {

    private final String UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB";
    public static BluetoothSocket mmSocket;
    public static BluetoothDevice mmDevice;
    public static InputStream mmInputStream;
    public static OutputStream mmOutputStream;
    private static boolean isOn = false;

    private TextView deviceName;
    private TextView txtDirection;
    private ImageButton btnA;
    private ImageButton btnB;
    private ImageButton btnC;
    private ImageButton btnD;
    private ImageButton btnBTDevices;
    private ImageButton btnOnOff;
    private JoyStick joyStick;

    public static void setConnectedDevice (BluetoothDevice device, BluetoothSocket socket, InputStream inputStream, OutputStream outputStream) {
        mmDevice = device;
        mmSocket = socket;
        mmInputStream = inputStream;
        mmOutputStream = outputStream;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mmDevice != null) {
            deviceName.setText(mmDevice.getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceName = findViewById(R.id.deviceName);
        txtDirection = findViewById(R.id.direction);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        btnBTDevices = findViewById(R.id.btDevies);
        btnOnOff = findViewById(R.id.btnOnOff);
        joyStick = findViewById(R.id.joyStick);

        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("a");
            }
        });

        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("b");
            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("c");
            }
        });

        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("d");
            }
        });

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mmDevice == null) {
                    Toast.makeText(MainActivity.this, "No connected device found!", Toast.LENGTH_SHORT).show();
                } else {
                    if (isOn) {
                        isOn = false;
                        btnOnOff.setImageResource(R.drawable.off_btn_state);
                        send("f");
                    } else {
                        isOn = true;
                        btnOnOff.setImageResource(R.drawable.on_btn_state);
                        send("o");
                    }
                }
            }
        });

        btnBTDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BluetoothDevicesActivity.class);
                startActivity(intent);
            }
        });

        joyStick.setListener(this);

        if (mmDevice != null) {
            deviceName.setText(mmDevice.getName());
        }
    }

    public void send(String msg) {
        msg += "\n";
       if (mmDevice != null) {
            try {
                if (mmSocket.isConnected()) {
                    mmOutputStream.write(msg.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
//            Toast.makeText(MainActivity.this, "No connected device found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMove(JoyStick joyStick, double angle, double power, int direction) {
        String msg = "" + direction;
        System.out.println(direction);
//        switch (direction) {
//            case 0:
//                txtDirection.setText("Left");
//                break;
//            case 1:
//                txtDirection.setText("Left-Forward");
//                break;
//            case 2:
//                txtDirection.setText("Forward");
//                break;
//            case 3:
//                txtDirection.setText("Right-Forward");
//                break;
//            case 4:
//                txtDirection.setText("Right");
//                break;
//            case 5:
//                txtDirection.setText("Right-Backward");
//                break;
//            case 6:
//                txtDirection.setText("Backward");
//                break;
//            case 7:
//                txtDirection.setText("Left-Backward");
//                break;
//        }
        send(msg);
    }

    @Override
    public void onTap() {

    }

    @Override
    public void onDoubleTap() {

    }
}
