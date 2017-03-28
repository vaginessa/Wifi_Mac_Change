package hom.ourhome.macaddresschange;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stericson.rootshell.RootShell;
import com.stericson.rootshell.exceptions.RootDeniedException;
import com.stericson.rootshell.execution.Command;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab0 = (FloatingActionButton) findViewById(R.id.fab0);
        fab0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MacAddressChangeCommand();
                Snackbar.make(view, "Replace fab0 with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace fab1 with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitApp();
                Snackbar.make(view, "Replace fab2 with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        versionName();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void MacAddressChangeCommand() {
        Command command = new Command(0,
                "busybox ifconfig wlan0 down",
                "echo Shutdown the wlan0 adapter: DONE",
                "WIFI_MAC=`(date; cat /proc/interrupts) | md5sum | sed -r 's/^(.{10}).*$/\\1/; s/([0-9a-f]{2})/\\1:/g; s/:$//;'`",
                "echo 00:${WIFI_MAC} > /efs/wifi/.mac.cob",
                "echo 00:${WIFI_MAC} > /efs/wifi/.mac.info",
                "echo Reconfigureing the wlan0 adapter: DONE",
                "echo New Wifi mac address is 00:${WIFI_MAC}",
                "busybox ifconfig wlan0 hw ether 0:${WIFI_MAC}",
                "busybox ifconfig wlan0 up",
                "echo Starting the wlan0 adapter: DONE"
        );
        try {
            RootShell.getShell(true).add(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        }
    }

    public void versionName() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = "Version: " + pInfo.versionName + "." + pInfo.versionCode;
        TextView versionText = (TextView) findViewById(R.id.versionName);
        versionText.setText(version);
    }

    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
        System.exit(0);
    }
}
