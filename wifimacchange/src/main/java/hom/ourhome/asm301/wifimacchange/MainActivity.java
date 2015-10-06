package hom.ourhome.asm301.wifimacchange;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    public TextView txtResult;
    ImageButton btnExecute;
    String command;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//RootTools debugMode
        RootTools.debugMode = true;
//RootTools debugMode

// Call su on app open Start
        final Runtime runtime = Runtime.getRuntime();
        try {
            RootTools.isAccessGiven();
            toggleWifi(true);
            toggleBluetooth(false);
            CopyAssets();
            //InstallToSystemCommand();
        } catch (Exception e) {
            e.printStackTrace();
        }
// Call su on app open End

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // input = (EditText) findViewById(R.id.txt);
        btnExecute = (ImageButton) findViewById(R.id.btn);
        txtResult = (TextView) findViewById(R.id.result);
        btnExecute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // command = input.getText().toString();
                CopyAssets();
                toggleWifi(true);
                new WifiMacChangeActivity(MainActivity.this).execute(command);
                // new getOutput().execute(command);
            }
        });

// Install To System Button START
        Button InstallToSystemButton = (Button) this.findViewById(R.id.InstallToSystemButton);
        InstallToSystemButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            InstallToSystemCommand();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (RootDeniedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
// Install To System Button END

/** // Uninstall From System Button START
        Button UninstallFromSystemButton = (Button) this.findViewById(R.id.UninstallFromSystemButton);
        UninstallFromSystemButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            UninstallFromSystemCommand();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (RootDeniedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
// Uninstall From System Button END **/

// Exit Button START
        Button ExitButton = (Button) this.findViewById(R.id.ExitButton);
        ExitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                        System.exit(0);
                    }
                }
        );
// Exit Button END

// Wifi Switch START
        Switch toggle_wifi = (Switch) findViewById(R.id.wifi_switch);
        toggle_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleWifi(true);
                } else {
                    toggleWifi(false);
                }
            }
        });
// Wifi Switch END

// Bluetooth Switch START
        Switch toggle_bluetooth = (Switch) findViewById(R.id.bluetooth_switch);
        toggle_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleBluetooth(true);
                } else {
                    toggleBluetooth(false);
                }
            }
        });
// Bluetooth Switch END

// RootTools Buttons START
        Button isRootAvailable = (Button) findViewById(R.id.isRootAvailable);
        isRootAvailable.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (RootTools.isRootAvailable()) {
                    makeToast("Root is available!");
                } else {
                    makeToast("Root is NOT available!");
                }
            }
        });
        Button isAccessGiven = (Button) findViewById(R.id.isAccessGiven);
        isAccessGiven.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (RootTools.isAccessGiven()) {
                    makeToast("Root access has been granted!");
                } else {
                    makeToast("Root access has not been granted!");
                }
            }
        });
        Button isBusyboxAvailable = (Button) findViewById(R.id.isBusyboxAvailable);
        isBusyboxAvailable.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (RootTools.isBusyboxAvailable()) {
                    makeToast("BusyBox is available!");
                } else {
                    makeToast("BusyBox is not available!");
                }
            }
        });
        Button offerBusyBox = (Button) findViewById(R.id.offerBusyBox);
        offerBusyBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RootTools.offerBusyBox(MainActivity.this);
            }
        });
        Button offerSuperUser = (Button) findViewById(R.id.offerSuperUser);
        offerSuperUser.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RootTools.offerSuperUser(MainActivity.this);
            }
        });
// RootTools Buttons END

// Version Name START
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        TextView versionText = (TextView) findViewById(R.id.versionName);
        versionText.setText("Version: " + version);
// Version Name END

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.isRootAvailable) {
            if (RootTools.isRootAvailable()) {
                makeToast("Root is available!");
            } else {
                makeToast("Root is NOT available!");
            }
            return true;
        }
        if (id == R.id.isAccessGiven) {
            if (RootTools.isAccessGiven()) {
                makeToast("Root access has been granted!");
            } else {
                makeToast("Root access has not been granted!");
            }
            return true;
        }
        if (id == R.id.isBusyboxAvailable) {
            if (RootTools.isBusyboxAvailable()) {
                makeToast("BusyBox is available!");
            } else {
                makeToast("BusyBox is not available!");
            }
            return true;
        }
        if (id == R.id.offerBusyBox) {
            RootTools.offerBusyBox(MainActivity.this);
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.ExitButton) {
            makeToast("Exit Button is Selected");
            finish();
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Copy Assets START
    private void CopyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for (String filename : files) {
            //System.out.println("File name => "+filename);
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("WifiMacChange.XBIN");   // if files resides inside the "Files" directory itself
                //out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + "WifiMacChange");
                out = new FileOutputStream(Environment.getDataDirectory().getPath() + "/data/hom.ourhome.asm301.wifimacchange" + "/" + "WifiMacChange");
                copyFile(in, out);
                copyFileToSystem(Environment.getDataDirectory().getPath() + "/data/hom.ourhome.asm301.wifimacchange" + "/" + "WifiMacChange", "/system/xbin/WifiMacChange", "0700");
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void copyFileToSystem(String source, String destination, String permissions) throws IOException, TimeoutException, RootDeniedException {
        RootTools.remount("/system", "rw");
        Command copyFileToSystemCommand = new Command(0,
                "cp " + source + " " + destination,
                "/system/xbin/busybox chmod " + permissions + " " + destination);
        RootTools.getShell(true).add(copyFileToSystemCommand);
        RootTools.remount("/system", "ro");
    }
// Copy Assets END

    // Install To System Command START
    private void InstallToSystemCommand() throws TimeoutException, RootDeniedException, IOException {
        RootTools.remount("/system", "rw");
        copyFileToSystem(Environment.getDataDirectory().getPath() + "/app/hom.ourhome.asm301.wifimacchange-*.apk", "/system/priv-app/WifiMacChange.apk", "0644");
        RootTools.remount("/system", "ro");
        makeToast("Installed WifiMacChange script to /system/xbin/WifiMacChange");
        Command InstallToSystemCommand = new Command(0,
                "/system/bin/reboot");
        RootTools.getShell(true).add(InstallToSystemCommand);
    }
// Install To System Command END

/**    // Uninstall From System Command START
    private void UninstallFromSystemCommand() throws TimeoutException, RootDeniedException, IOException {
        RootTools.remount("/system", "rw");
        Command UninstallFromSystemCommand = new Command(0,
                "/system/xbin/busybox find /data -type f -iname '*WifiMacChange*' -delete",
                "/system/xbin/busybox find /system -type f -iname '*WifiMacChange*' -delete",
                "/system/xbin/busybox find /system -type f -iname '*WifiMacChange*.apk' -delete",
                "pm uninstall hom.ourhome.asm301.wifimacchange",
                "/system/xbin/busybox rm -rf /data/data/hom.ourhome.asm301.wifimacchange",
                "/system/xbin/busybox find /data -type f -iname '*WifiMacChange*' -delete");
        RootTools.getShell(true).add(UninstallFromSystemCommand);
        RootTools.remount("/system", "ro");
    }
// Uninstall From System Command END **/

    // Wifi Switch START
    public void toggleWifi(boolean status) {
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            makeToast("Wifi Enabled!");
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            makeToast("Wifi Disabled!");
        }
    }
// Wifi Switch END

    // Bluetooth Switch START
    public void toggleBluetooth(boolean status) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (status == true && !mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            makeToast("Bluetooth Enabled!");
        } else if (status == false && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            makeToast("Bluetooth Disabled!");
        }
    }
// Bluetooth Switch END

    // Toast Messages START
    public void makeToast(String msg) {
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
// Toast Messages END

}