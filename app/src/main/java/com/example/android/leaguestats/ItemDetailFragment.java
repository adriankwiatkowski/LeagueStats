package com.example.android.leaguestats;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.ItemLoader;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class ItemDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, ItemAdapter.ItemClickListener {

    private static final String LOG_TAG = ItemDetailFragment.class.getSimpleName();
    private static final String DB_SIGN = " = ?";
    private String mPatchVersion;
    private TextView mBaseGoldLabel, mTotalGoldLabel, mSellGoldLabel, mDescriptionLabel,
            mPlainTextLabel, mFromLabel, mIntoLabel;
    private TextView mItemNameTv, mBaseGoldTv, mTotalGoldTv, mSellGoldTv, mDescriptionTv,
            mPlainTextTv;
    private ImageView mImage;
    private String[] mFromArray;
    private String[] mIntoArray;
    private ItemAdapter mItemFromAdapter;
    private ItemAdapter mItemIntoAdapter;
    private RecyclerView mFromRecycler;
    private RecyclerView mIntoRecycler;
    public static final String ITEM_ID_KEY = "ITEM_ID_KEY";
    public static final String FROM_ARRAY_KEY = "FROM_ARRAY_KEY";
    public static final String INTO_ARRAY_KEY = "INTO_ARRAY_KEY";
    private ItemLoader mItemLoader;
    private int mItemId;
    private String CURRENT_ID_KEY = "CURRENT_ID_KEY";
    private String FROM_LAYOUT_MANAGER_STATE_KEY = "fromLayoutManagerStateKey";
    private String INTO_LAYOUT_MANAGER_STATE_KEY = "intoLayoutManagerStateKey";

    public ItemDetailFragment() {
    }

    public static ItemDetailFragment newInstance(int championId) {
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_ID_KEY, championId);
        itemDetailFragment.setArguments(args);
        return itemDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");

        Bundle args = getArguments();
        if (args != null && args.containsKey(ITEM_ID_KEY)) {
            mItemId = args.getInt(ITEM_ID_KEY);
        } else {
            Log.w(LOG_TAG, "itemId null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        Log.i(LOG_TAG, "onCreateView");

        mBaseGoldLabel = rootView.findViewById(R.id.base_gold_label);
        mTotalGoldLabel = rootView.findViewById(R.id.total_gold_label);
        mSellGoldLabel = rootView.findViewById(R.id.sell_gold_label);
        mDescriptionLabel = rootView.findViewById(R.id.item_description_label);
        mPlainTextLabel = rootView.findViewById(R.id.plain_text_label);
        mFromLabel = rootView.findViewById(R.id.item_from_label);
        mIntoLabel = rootView.findViewById(R.id.item_into_label);

        mItemNameTv = rootView.findViewById(R.id.item_detail_name);
        mBaseGoldTv = rootView.findViewById(R.id.base_gold_tv);
        mTotalGoldTv = rootView.findViewById(R.id.total_gold_tv);
        mSellGoldTv = rootView.findViewById(R.id.sell_gold_tv);
        mDescriptionTv = rootView.findViewById(R.id.item_description_tv);
        mPlainTextTv = rootView.findViewById(R.id.plain_text_tv);
        mImage = rootView.findViewById(R.id.item_detail_image);

        mFromRecycler = rootView.findViewById(R.id.item_from_recycler);
        mIntoRecycler = rootView.findViewById(R.id.item_into_recycler);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(LOG_TAG, "onActivityCreated");

        mPatchVersion = PreferencesUtils.getPatchVersion(getContext());

        int gridLayoutColumnCount;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 2;
        } else {
            gridLayoutColumnCount = 1;
        }

        mFromRecycler.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mFromRecycler.setHasFixedSize(true);
        mFromRecycler.setItemAnimator(new DefaultItemAnimator());
        mItemFromAdapter = new ItemAdapter(getContext(), null, ItemDetailFragment.this, mPatchVersion);
        mFromRecycler.setAdapter(mItemFromAdapter);

        mIntoRecycler.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mIntoRecycler.setHasFixedSize(true);
        mIntoRecycler.setItemAnimator(new DefaultItemAnimator());
        mItemIntoAdapter = new ItemAdapter(getContext(), null, ItemDetailFragment.this, mPatchVersion);
        mIntoRecycler.setAdapter(mItemIntoAdapter);

        mItemLoader = new ItemLoader(getContext(), mItemFromAdapter, mItemIntoAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FROM_LAYOUT_MANAGER_STATE_KEY)) {
                mFromRecycler.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(FROM_LAYOUT_MANAGER_STATE_KEY));
            }
            if (savedInstanceState.containsKey(INTO_LAYOUT_MANAGER_STATE_KEY)) {
                mIntoRecycler.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(INTO_LAYOUT_MANAGER_STATE_KEY));
            }
            if (savedInstanceState.containsKey(CURRENT_ID_KEY)) {
                mItemId = savedInstanceState.getInt(CURRENT_ID_KEY);
            }
        }

        mFromRecycler.setNestedScrollingEnabled(false);
        mIntoRecycler.setNestedScrollingEnabled(false);

        getActivity().getSupportLoaderManager().initLoader(mItemId, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader");
        final String[] PROJECTION = {
                Contract.ItemEntry._ID,
                Contract.ItemEntry.COLUMN_NAME,
                Contract.ItemEntry.COLUMN_BASE_GOLD,
                Contract.ItemEntry.COLUMN_TOTAL_GOLD,
                Contract.ItemEntry.COLUMN_SELL_GOLD,
                Contract.ItemEntry.COLUMN_IMAGE,
                Contract.ItemEntry.COLUMN_PURCHASABLE,
                Contract.ItemEntry.COLUMN_SANITIZED_DESCRIPTION,
                Contract.ItemEntry.COLUMN_PLAIN_TEXT,
                Contract.ItemEntry.COLUMN_FROM,
                Contract.ItemEntry.COLUMN_INTO};

        return new CursorLoader(getContext(),
                Contract.ItemEntry.CONTENT_URI,
                PROJECTION,
                Contract.ItemEntry._ID + DB_SIGN,
                new String[]{String.valueOf(mItemId)},
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
        if (cursor == null || cursor.getCount() < 1) {
            Log.d(LOG_TAG, "cursor null");
            return;
        }

        if (cursor.moveToFirst()) {
            Log.d(LOG_TAG, "cursor not null");
            int baseGold = cursor.getInt(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_BASE_GOLD));
            int totalGold = cursor.getInt(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_TOTAL_GOLD));
            int sellGold = cursor.getInt(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_SELL_GOLD));

            String image = cursor.getString(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_IMAGE));
            Picasso.get()
                    .load("http://ddragon.leagueoflegends.com/cdn/" + mPatchVersion + "/img/item/" + image)
                    .resize(200, 200)
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(mImage);

            String name = cursor.getString(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_SANITIZED_DESCRIPTION));
            String plainText = cursor.getString(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_PLAIN_TEXT));

            DataUtils.setTextWithLabel(baseGold, mBaseGoldTv, mBaseGoldLabel);
            DataUtils.setTextWithLabel(totalGold, mTotalGoldTv, mTotalGoldLabel);
            DataUtils.setTextWithLabel(sellGold, mSellGoldTv, mSellGoldLabel);

            DataUtils.setTextWithLabel(description, mDescriptionTv, mDescriptionLabel);
            DataUtils.setTextWithLabel(plainText, mPlainTextTv, mPlainTextLabel);

            mItemNameTv.setText(name);

            mItemId = cursor.getInt(cursor.getColumnIndex(Contract.ItemEntry._ID));
            Log.i(LOG_TAG, "onLoadFinishedItemId" + " " + String.valueOf(mItemId));

            String from = cursor.getString(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_FROM));
            mFromArray = DataUtils.stringToStringArray(from);
            Log.i(LOG_TAG, "mFromArray" + " " + Arrays.toString(mFromArray));

            String into = cursor.getString(cursor.getColumnIndex(Contract.ItemEntry.COLUMN_INTO));
            mIntoArray = DataUtils.stringToStringArray(into);
            Log.i(LOG_TAG, "mIntoArray" + " " + Arrays.toString(mIntoArray));

            mItemFromAdapter.swapCursor(null);
            mItemIntoAdapter.swapCursor(null);
            Bundle args = new Bundle();
            args.putInt(ITEM_ID_KEY, mItemId);
            if (mFromArray.length != 0) {
                mFromLabel.setVisibility(View.VISIBLE);
                args.putStringArray(FROM_ARRAY_KEY, mFromArray);
                mItemLoader.setFromArrayId(mItemId);
                getActivity().getSupportLoaderManager().initLoader(mItemLoader.getFromArrayId(), args, mItemLoader);
            } else {
                mFromLabel.setVisibility(View.INVISIBLE);
            }
            if (mIntoArray.length != 0) {
                mIntoLabel.setVisibility(View.VISIBLE);
                args.putStringArray(INTO_ARRAY_KEY, mIntoArray);
                mItemLoader.setIntoArrayId(mItemId);
                getActivity().getSupportLoaderManager().initLoader(mItemLoader.getIntoArrayId(), args, mItemLoader);
            } else {
                mIntoLabel.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.i(LOG_TAG, "onLoaderReset");
        mFromArray = null;
        mIntoArray = null;
        mBaseGoldTv.setText("");
        mTotalGoldTv.setText("");
        mSellGoldTv.setText("");
        mDescriptionTv.setText("");
        mPlainTextTv.setText("");
        mItemNameTv.setText("");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");
        mFromRecycler.setAdapter(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

        outState.putParcelable(FROM_LAYOUT_MANAGER_STATE_KEY, mFromRecycler.getLayoutManager().onSaveInstanceState());
        outState.putInt(CURRENT_ID_KEY, mItemId);
    }

    @Override
    public void onItemClick(int id) {
        Log.i(LOG_TAG, "onItemClick");
        mItemId = id;
        Log.i(LOG_TAG, "onItemClickId" + " " + String.valueOf(mItemId));
        getActivity().getSupportLoaderManager().initLoader(id, null, this);
    }
}
