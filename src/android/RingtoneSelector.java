package org.RingtoneSelector;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * This class echoes a string called from JavaScript.
 */
public class RingtoneSelector extends CordovaPlugin {

    public Intent Mringtone;
    RingtoneManager mRingtoneManager;
    private ContentResolver contentResolver;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {
            String message = args.getString(0);
            this.open(message, callbackContext);
            return true;
        }
        return false;
    }

    CallbackContext contex;

    private void open(String message, CallbackContext callbackContext) {
        contex = callbackContext;
        if (message != null && message.length() > 0) {
            //Starts the intent or Activity of the ringtone manager, opens popup box
            Mringtone = new Intent(mRingtoneManager.ACTION_RINGTONE_PICKER);

            //specifies what type of tone we want, in this case "ringtone", can be notification if you want
            Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);

            //gives the title of the RingtoneManager picker title
            Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_TITLE, "Pick a Ringtone");


            String uri = null;
            //chooses and keeps the selected item as a uri
            if (uri != null) {
                Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(uri));
            } else {
                Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
            }
            cordova.startActivityForResult(this, Mringtone, 0);

        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    Uri uri;

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 0:

                try {
                    //sents the ringtone that is picked in the Ringtone Picker Dialog
                    uri = intent.getParcelableExtra(mRingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Log.d("PLUGIN", "startActivityForResult: Success " + uri);
                    contex.success(uri.toString());

                } catch (Exception e) {
                    contex.error("There was an error.");
                }

                break;

            default:

                Log.d("PLUGIN", "startActivityForResult: Error !!!!!!!!!!!!!!!!!");


        }
    }

}
