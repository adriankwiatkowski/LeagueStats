package com.example.android.leaguestats.utilities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.leaguestats.ItemDetailFragment;
import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.database.Contract;

import java.util.Arrays;

public class ItemLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ItemLoader.class.getSimpleName();
    private static final String DB_SIGN = " = ?";
    private final String[] PROJECTION = {
            Contract.ItemEntry._ID,
            Contract.ItemEntry.COLUMN_NAME,
            Contract.ItemEntry.COLUMN_TOTAL_GOLD,
            Contract.ItemEntry.COLUMN_IMAGE,
            Contract.ItemEntry.COLUMN_PURCHASABLE,
            Contract.ItemEntry.COLUMN_FROM,
            Contract.ItemEntry.COLUMN_INTO};

    private Context mContext;
    private ItemAdapter mItemAdapter;
    private ItemAdapter mFromAdapter;
    private ItemAdapter mIntoAdapter;
    private int mFromArrayId;
    private int mIntoArrayId;
    private final int FROM_ARRAY_ID_ADDITION = 20000;
    private final int INTO_ARRAY_ID_ADDITION = 10000;

    public ItemLoader(Context context, ItemAdapter itemAdapter) {
        mContext = context;
        mItemAdapter = itemAdapter;
    }

    public ItemLoader(Context context, ItemAdapter fromAdapter, ItemAdapter intoAdapter) {
        mContext = context;
        mFromAdapter = fromAdapter;
        mIntoAdapter = intoAdapter;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (args != null) {
            int itemId = args.getInt(ItemDetailFragment.ITEM_ID_KEY);
            Log.i(LOG_TAG, "Loader id " + String.valueOf(id) + "    " + "itemId " + String.valueOf(itemId));
            if (id == getFromArrayId()) {
                String[] selectionArgs = args.getStringArray(ItemDetailFragment.FROM_ARRAY_KEY);
                Log.i(LOG_TAG, "from array" + " " + Arrays.toString(selectionArgs));
                String inClause = DataUtils.buildInClause(selectionArgs);
                return new CursorLoader(mContext,
                        Contract.ItemEntry.CONTENT_URI,
                        PROJECTION,
                        Contract.ItemEntry._ID + inClause,
                        selectionArgs,
                        Contract.ItemEntry.COLUMN_TOTAL_GOLD);
            } else if (id == getIntoArrayId()){
                String[] selectionArgs = args.getStringArray(ItemDetailFragment.INTO_ARRAY_KEY);
                Log.i(LOG_TAG, "into array" + " " + Arrays.toString(selectionArgs));
                String inClause = DataUtils.buildInClause(selectionArgs);
                return new CursorLoader(mContext,
                        Contract.ItemEntry.CONTENT_URI,
                        PROJECTION,
                        Contract.ItemEntry._ID + inClause,
                        selectionArgs,
                        Contract.ItemEntry.COLUMN_TOTAL_GOLD);
            } else {
                return new CursorLoader(mContext,
                        Contract.ItemEntry.CONTENT_URI,
                        PROJECTION,
                        Contract.ItemEntry.COLUMN_PURCHASABLE + DB_SIGN,
                        new String[]{"true"},
                        Contract.ItemEntry.COLUMN_TOTAL_GOLD);
            }
        } else {
            return new CursorLoader(mContext,
                    Contract.ItemEntry.CONTENT_URI,
                    PROJECTION,
                    Contract.ItemEntry.COLUMN_PURCHASABLE + DB_SIGN,
                    new String[]{"true"},
                    Contract.ItemEntry.COLUMN_TOTAL_GOLD);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (mItemAdapter != null) {
            mItemAdapter.swapCursor(data);
        } else if (loader.getId() == getFromArrayId()){
            mFromAdapter.swapCursor(data);
            Log.i(LOG_TAG, "mFromAdapter" + " count " + String.valueOf(data.getCount()));
        } else if (loader.getId() == getIntoArrayId()) {
            mIntoAdapter.swapCursor(data);
            Log.i(LOG_TAG, "mIntoAdapter" + " count " + String.valueOf(data.getCount()));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (mItemAdapter != null) {
            mItemAdapter.swapCursor(null);
        } else if (loader.getId() == getFromArrayId()) {
            mFromAdapter.swapCursor(null);
        } else if (loader.getId() == getIntoArrayId()) {
            mIntoAdapter.swapCursor(null);
        }
    }

    public int getFromArrayId() {
        return mFromArrayId;
    }

    public void setFromArrayId(int fromArrayId) {
        this.mFromArrayId = fromArrayId + FROM_ARRAY_ID_ADDITION;
    }

    public int getIntoArrayId() {
        return mIntoArrayId;
    }

    public void setIntoArrayId(int intoArrayId) {
        this.mIntoArrayId = intoArrayId + INTO_ARRAY_ID_ADDITION;
    }
}
