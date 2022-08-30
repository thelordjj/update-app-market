package cordova.plugin.market.update;

import org.apache.cordova.*;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.gms.tasks.Task;

/*import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity; /*

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * This class echoes a string called from JavaScript.
 */
public class AppMarketUpdate extends CordovaPlugin {

    static int  REQUEST_CODE_UPDATE= 0;
    static String V_RESULT_CANCELED = "CANCELED";
    static String V_RESULT_FAILED_INSTALL = "FAILED_INSTALL";
    static String V_RESULT_INSTALLED = "INSTALLED";
    static String V_DOWNLOADING = "DOWNLOADING";

    static int DAYS_FOR_FLEXIBLE_UPDATE = 5;
    static int PRIORITY = 0;

    CallbackContext callbackContextG;

    AppUpdateManager appUpdateManager;
    int updateType = 0;

    //Bundle outState;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

       // Log.d("execute","execute");

        callbackContextG = callbackContext;
        appUpdateManager = AppUpdateManagerFactory.create(this.cordova.getActivity());

       /*if (action.equals("coolMethod")) {
            String message = args.getString(0);
            return true;
        }*/
        if (action.equals("updateDevice")) {
            int param = args.getInt(0);
            this.updateDevice(param,callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    private void updateDevice(int pTipoUpdate, CallbackContext callbackContext) {
    //if (pTipoUpdate != null && pTipoUpdate.length() > 0) {
        updateType = pTipoUpdate;
        //callbackContext.success("param = "+ updateType);
        checkUpdate(callbackContext);
    //} else {
      //  callbackContext.error("Parametro tipo update no encontrado");
   // }
    }

     private void checkUpdate(CallbackContext callbackContext) {
        try{
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
         // Checks whether the platform allows the specified type of update,
        // and current version staleness.
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                    if (checkResume(appUpdateInfo)){       
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
                            checkTipoActualizacion(appUpdateInfo);
                        }
                    }
            });

        }catch(Exception e){
            callbackContext.error("001-Error");
        }
    }

    private void checkTipoActualizacion(AppUpdateInfo appUpdateInfo){
                if (updateType == AppUpdateType.IMMEDIATE)  {
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){ 
                        callUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
                    }
                }
              /*  if (updateType == AppUpdateType.FLEXIBLE)  {
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                         callUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE);
                    }
                } */
    }
     /*
    private void ListenerFlexible(){
        // Create a listener to track request state updates.
        InstallStateUpdatedListener listener = state -> {
            // (Optional) Provide a download progress bar.
            if (state.installStatus() == InstallStatus.DOWNLOADING) {
            }

            if (state.installStatus() == InstallStatus.FAILED) {
                
            }

            if (state.installStatus() == InstallStatus.INSTALLED) {
            }

            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
            // Log state or install the update.
        };
        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(listener);
    } */

  /*  private void popupSnackbarForCompleteUpdate() {
        View layoutSnackbar = this.cordova.getActivity().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(layoutSnackbar,
                                          "Se ha completado la descarga",
                                          Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("INSTALAR", view -> appUpdateManager.completeUpdate()
        );
       //snackbar.setActionTextColor(getResources().getColor(R.color.teal_700));
        snackbar.show();
    } */
   
    /*
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        cordova.setActivityResultCallback(this);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                if (callbackContextG != null) {
                    switch(resultCode) {
                        case RESULT_CANCELED:
                            callbackContextG.error(V_RESULT_CANCELED);
                        break;
                    }
                }
            }
        }    
      super.onActivityResult(requestCode, resultCode, intent);
    }

    private boolean checkResume(AppUpdateInfo appUpdateInfo){
       /* if (updateType == AppUpdateType.FLEXIBLE) { 
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
                return false;
            } 
        } */
        if (updateType == AppUpdateType.IMMEDIATE) {
                 if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {                            
                     callUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
                     return false;
                }                  
        }
        return true;
    }

    private void callUpdate(AppUpdateInfo appUpdateInfo, int vUpdateType){
                try {
                    this.cordova.setActivityResultCallback(this);
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                                                              vUpdateType,
                                                              this.cordova.getActivity(),
                                                              REQUEST_CODE_UPDATE);

                   /* if (vUpdateType == AppUpdateType.FLEXIBLE){
                        ListenerFlexible();
                    } */
                } catch (Exception e) {
                    e.printStackTrace();
                }
    } 
}

