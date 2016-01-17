package net.kazuhiro1982.nfcsample.timerecorder;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import net.kazuhiro1982.nfcsample.timerecorder.recorder.TimeRecorder;

public class CheckInActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        Intent intent = getIntent();
        detectNFCTag(intent);

        Button checkInButton = (Button) findViewById(R.id.button_check_in);
        checkInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimeRecorder(CheckInActivity.this).clockIn();
                finish();
            }
        });
        Button checkOutButton = (Button) findViewById(R.id.button_check_out);
        checkOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimeRecorder(CheckInActivity.this).clockOut();
                finish();
            }
        });
    }

    private void detectNFCTag(Intent intent) {

        String action = intent.getAction();
        if (!NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            finish();
        }

        final SharedPreferences prefs = getSharedPreferences(getString(R.string.tag_preference_name), Context.MODE_PRIVATE);
        String savedTagId = prefs.getString(getString(R.string.tag_id_key), null);

        final String tagId = getIdmString(intent);
        if (savedTagId == null) {

            registerTag(prefs, tagId);
        } else if (!savedTagId.equals(tagId)) {
            finish();
        }
    }

    private void registerTag(final SharedPreferences prefs, final String tagId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.title_tag_register));
        alertDialogBuilder.setMessage(getString(R.string.message_tag_register));
        alertDialogBuilder.setPositiveButton(getString(R.string.message_tag_register_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editor edit = prefs.edit();
                edit.putString(getString(R.string.tag_id_key), tagId);
                edit.commit();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.message_tag_register_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialogBuilder.setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String getIdmString(Intent intent) {
        byte[] rawIdm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        if (rawIdm == null) {
            return null;
        }

        StringBuffer idmByte = new StringBuffer();
        for (int i = 0; i < rawIdm.length; i++) {
            idmByte.append(Integer.toHexString(rawIdm[i] & 0xff));
        }
        return idmByte.toString();
    }
}
