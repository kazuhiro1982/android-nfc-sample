package net.kazuhiro1982.nfcsample.timerecorder;

import java.io.IOException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TagWriterActivity extends Activity {

    final private Charset UTF8 = Charset.forName("UTF-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_writer);

        Button button = (Button) findViewById(R.id.button_tag_writer);
        final Intent intent = getIntent();
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    writeTag(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TagWriterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                } catch (FormatException e) {
                    e.printStackTrace();
                    Toast.makeText(TagWriterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void writeTag(Intent intent) throws IOException, FormatException {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            NdefMessage msg = createNdefMessage();

            // Ndefメッセージの書き込み
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(msg);
            Log.d("Tag Write", getString(R.string.message_tag_write_completed));
            Toast.makeText(TagWriterActivity.this, getString(R.string.message_tag_write_completed), Toast.LENGTH_LONG);
            ndef.close();
        }
    }

    private NdefMessage createNdefMessage() {
        String packageName = getPackageName();
        byte[] messageBytes = getString(R.string.app_name).getBytes(UTF8);
        NdefRecord message = createMimeRecord("application/" + packageName, messageBytes);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{message});
        return ndefMessage;
    }

    private NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(UTF8);
        NdefRecord mimeRecord =  new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[]{0}, payload);
        return mimeRecord;
    }

}
