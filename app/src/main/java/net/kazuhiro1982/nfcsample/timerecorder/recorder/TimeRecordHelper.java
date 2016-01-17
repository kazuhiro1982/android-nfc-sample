package net.kazuhiro1982.nfcsample.timerecorder.recorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class TimeRecordHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "TimeRecorder";

    static final String TABLE_NAME = "record";

    static enum COLUMN {
        ID("id", "integer", "primary key autoincrement"),
        DATE("date", "integer", "not null"),
        START("start", "text", ""),
        END("end", "text", "");

        String name;
        String type;
        String attribute;

        private COLUMN(String name, String type, String attribute) {
            this.name = name;
            this.type = type;
            this.attribute = attribute;
        }

        public static String[] names(COLUMN... list) {
            String[] cols = new String[list.length];

            for (int i = 0 ; i < list.length; i++) {
                cols[i] = list[i].name;
            }

            return cols;
        }
    }

    private static final int DB_VERSION = 1;


    public TimeRecordHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder("CREATE table ");
        builder.append(TABLE_NAME).append("(");
        COLUMN[] cols = COLUMN.values();
        for (int i = 0; i < cols.length; i++) {
            COLUMN col = cols[i];
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(col.name).append(" ").append(col.type).append(" ").append(col.attribute);
        }
        builder.append(")");

        db.execSQL(builder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nop
    }

}
