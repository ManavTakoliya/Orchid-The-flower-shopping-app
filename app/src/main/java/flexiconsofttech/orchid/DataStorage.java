package flexiconsofttech.orchid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataStorage {

    private Context ctx;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static final int INTEGER = 1;
    public static final int STRING = 2;
    public static final int BOOLEAN = 3;
    public static final int FLOAT = 4;
    public static final int LONG = 5;

    public DataStorage(Context ctx) {

        this.ctx = ctx;
        preferences = ctx.getSharedPreferences("flower", ctx.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void write(String key, int value) {

        editor.putInt(key, value);
        editor.commit();
    }

    public void write(String key, String value) {

        editor.putString(key, value);
        editor.commit();
    }

    public void write(String key, Float value) {

        editor.putFloat(key, value);
        editor.commit();
    }

    public void write(String key, Boolean value) {

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void write(String key, Long value) {

        editor.putLong(key, value);
        editor.commit();
    }

    public Object read(String Key, int DataType) {

        Object temp = new Object();
        if (DataType == INTEGER)
            temp = preferences.getInt(Key, 0);
        else if (DataType == STRING)
            temp = preferences.getString(Key, "");
        else if (DataType == BOOLEAN)
            temp = preferences.getBoolean(Key, false);
        else if (DataType == FLOAT)
            temp = preferences.getFloat(Key, 0.0f);
        else if (DataType == LONG)
        temp = preferences.getLong(Key, 0);

        return temp;

    }

}
