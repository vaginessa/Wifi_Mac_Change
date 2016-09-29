package hom.ourhome.asm301.wifimacchange;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.rootshell.exceptions.RootDeniedException;
import com.stericson.rootshell.execution.Command;
import com.stericson.roottools.RootTools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public TextView txtResult;
    ImageButton btnExecute;
    String command;
    EditText input;

    // Timer START
    TextView textViewTime;
// Timer END

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// Timer START
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewTime.setText("00:60:00");
        final CounterClass timer = new CounterClass(3600000, 1000);
// Timer END

        btnExecute = (ImageButton) findViewById(R.id.btn);
        txtResult = (TextView) findViewById(R.id.result);
        btnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // command = input.getText().toString();
                CopyAssets();
                toggleWifi(true);
                new WifiMacChangeActivity(MainActivity.this).execute(command);
                // new getOutput().execute(command);
                // Timer START
                timer.start();
                // Timer END
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
                System.exit(0);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/**
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
 **/
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

// Tools START
        if (id == R.id.isRootAvailable) {
            if (RootTools.isRootAvailable()) {
                makeToast("Root is available!");
            } else {
                makeToast("Root is NOT available!");
            }
        } else if (id == R.id.isAccessGiven) {
            if (RootTools.isAccessGiven()) {
                makeToast("Root access has been granted!");
            } else {
                makeToast("Root access has not been granted!");
            }
        } else if (id == R.id.isBusyboxAvailable) {
            if (RootTools.isBusyboxAvailable()) {
                makeToast("BusyBox is available!");
            } else {
                makeToast("BusyBox is not available!");
            }
        } else if (id == R.id.offerBusyBox) {
            RootTools.offerBusyBox(MainActivity.this);
        } else if (id == R.id.offerSuperUser) {
            RootTools.offerSuperUser(MainActivity.this);
        } else if (id == R.id.InstallToSystemCommand) {
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
// Tools END
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
// Uninstall From System Command START
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
// Uninstall From System Command END

    // Toast Messages START
    public void makeToast(String msg) {
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
// Toast Messages END

    // Timer START
    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);
            textViewTime.setText(hms);
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            textViewTime.setText("Completed.");
        }
    }
// Timer END

}
