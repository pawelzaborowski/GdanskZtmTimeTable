package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import si.uni_lj.student.pz8285.ztmtimetable.R;

/**
 * Created by Pawel on 01.06.2018.
 */

public class NoConnectionFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_internet);
        builder.setMessage(R.string.no_internet2)
                .setPositiveButton(R.string.turn_wifi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(
                                Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });

        return builder.create();
    }
}