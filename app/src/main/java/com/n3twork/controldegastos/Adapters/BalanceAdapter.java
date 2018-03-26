package com.n3twork.controldegastos.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import com.n3twork.controldegastos.R;

/**
 * Created by Juan Cruz Abregú on 13/11/2017.
 *
 * Apatador de Balance para mostrar en ListView
 */

public class BalanceAdapter extends SimpleCursorAdapter {

    private Cursor cursor;

    /**
     * Constructor
     *
     * @param context
     * @param cursor
     * @param from
     * @param to
     * @param flags
     */

    public BalanceAdapter(Context context, Cursor cursor, String[] from, int[] to, int flags){

        super(context, R.layout.list_historial_item_row, cursor, from, to, flags);
        this.cursor = cursor;

    }
}
