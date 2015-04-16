package com.butterflyeffect.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GpsDetector extends CordovaPlugin implements LocationListener {
    boolean alertShown = false;
    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) {
        
        PluginResult result = null;
        boolean gpsEnabled = false;
        String GPSDetectionAction = "gpsDetection";
        
        if (action.equals(GPSDetectionAction)) {
            //android.content.ContentResolver contentResolver = cordova.getActivity().getApplicationContext().getContentResolver();
            //gpsEnabled = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
                        
            Context ctx = cordova.getActivity().getApplicationContext();
            LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            
           

            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            try {
                if(!gpsEnabled && !args.getBoolean(0)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
                    showGPSDisabledAlertToUser();
                }
                else alertShown = false;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            result = new PluginResult(Status.OK, gpsEnabled);
            Log.i("gpsEnabled", (gpsEnabled) ? "yes" : "no");
        }
        else {
            result = new PluginResult(Status.INVALID_ACTION);
        }
        
        callbackContext.sendPluginResult(result);
        
        return true;
    }
    
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cordova.getActivity());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
        .setCancelable(false)
        .setPositiveButton("Goto Settings Page To Enable GPS",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                cordova.getActivity().startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    
    @Override
    public void onProviderEnabled(String s) {
        if(LocationManager.GPS_PROVIDER.equals(s)){
            Toast.makeText(cordova.getActivity(),"GPS on",Toast.LENGTH_SHORT).show();
            Log.i("onProviderEnabled", "enabled");
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        if(LocationManager.GPS_PROVIDER.equals(s)){
            Toast.makeText(cordova.getActivity(),"GPS off",Toast.LENGTH_SHORT).show();
            Log.i("onProviderDisabled", "disabled");
        }
    }
}