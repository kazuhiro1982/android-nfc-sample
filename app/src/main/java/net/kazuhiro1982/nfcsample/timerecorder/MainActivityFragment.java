package net.kazuhiro1982.nfcsample.timerecorder;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import net.kazuhiro1982.nfcsample.timerecorder.recorder.Record;
import net.kazuhiro1982.nfcsample.timerecorder.recorder.TimeRecorder;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TableLayout table = (TableLayout)getActivity().findViewById(R.id.tableLayout_record);
        List<Record> records = new TimeRecorder(getActivity()).getRecordsOnThisMonth();
        for (Record record : records) {
            TableRow row = new TableRow(getActivity());
            row.setBackgroundColor(Color.BLACK);
            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.bottomMargin = 1;
            params.topMargin = 1;
            params.leftMargin = 1;
            params.rightMargin = 1;

            row.addView(createCell(record.getHumanDate()), params);
            row.addView(createCell(record.getStart()), params);
            row.addView(createCell(record.getEnd()), params);

            table.addView(row);
        }

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private TextView createCell(String text) {
        TextView view = new TextView(getActivity());

        view.setGravity(Gravity.CENTER);
        view.setText(text);
        view.setBackgroundColor(Color.WHITE);
        return view;
    }

}
