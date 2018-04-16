package com.zhr.test;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        for (ApplicationInfo info : installedApplications) {
            String string =info.name + info.packageName +info.backupAgentName+info.manageSpaceActivityName;
            System.out.println("string = " + string);
        }
    }
}
