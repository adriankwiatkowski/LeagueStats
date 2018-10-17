package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.database.models.ListItemEntry;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PicassoUtils;
import com.example.android.leaguestats.viewmodels.ItemDetailModel;
import com.example.android.leaguestats.viewmodels.ItemDetailModelFactory;
import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.utilities.LeaguePreferences;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailFragment extends Fragment implements IdClickListener {

    public interface OnItemDetailSelected {
        void onItemDetailSelected(long id);
    }

    private static final String LOG_TAG = ItemDetailFragment.class.getSimpleName();
    private OnItemDetailSelected mCallback;
    private String mPatchVersion;
    private TextView mBaseGoldLabel, mTotalGoldLabel, mSellGoldLabel, mDescriptionLabel,
            mPlainTextLabel, mFromLabel, mIntoLabel;
    private TextView mItemNameTv, mBaseGoldTv, mTotalGoldTv, mSellGoldTv, mDescriptionTv,
            mPlainTextTv;
    private ImageView mImage;
    private ItemAdapter mItemFromAdapter;
    private ItemAdapter mItemIntoAdapter;
    private RecyclerView mFromRecycler;
    private RecyclerView mIntoRecycler;
    private String FROM_LAYOUT_MANAGER_STATE_KEY = "fromLayoutManagerStateKey";
    private String INTO_LAYOUT_MANAGER_STATE_KEY = "intoLayoutManagerStateKey";
    private ItemDetailModel mViewModel;

    public ItemDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        Log.d(LOG_TAG, "onCreateView");

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
        Log.d(LOG_TAG, "onActivityCreated");

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        int gridLayoutColumnCount;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 2;
        } else {
            gridLayoutColumnCount = 1;
        }

        mFromRecycler.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mFromRecycler.setHasFixedSize(true);
        mFromRecycler.setItemAnimator(new DefaultItemAnimator());
        mItemFromAdapter = new ItemAdapter(getContext(), new ArrayList<ListItemEntry>(), this, mPatchVersion);
        mFromRecycler.setAdapter(mItemFromAdapter);

        mIntoRecycler.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mIntoRecycler.setHasFixedSize(true);
        mIntoRecycler.setItemAnimator(new DefaultItemAnimator());
        mItemIntoAdapter = new ItemAdapter(getContext(), new ArrayList<ListItemEntry>(), this, mPatchVersion);
        mIntoRecycler.setAdapter(mItemIntoAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FROM_LAYOUT_MANAGER_STATE_KEY)) {
                mFromRecycler.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(FROM_LAYOUT_MANAGER_STATE_KEY));
            }
            if (savedInstanceState.containsKey(INTO_LAYOUT_MANAGER_STATE_KEY)) {
                mIntoRecycler.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(INTO_LAYOUT_MANAGER_STATE_KEY));
            }
        }

        mFromRecycler.setNestedScrollingEnabled(false);
        mIntoRecycler.setNestedScrollingEnabled(false);

        setupViewModel();
    }

    private void setupViewModel() {

        ItemDetailModelFactory factory =
                InjectorUtils.provideItemDetailModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ItemDetailModel.class);

        mViewModel.getItem().observe(getActivity(), new Observer<ItemEntry>() {
            @Override
            public void onChanged(@Nullable ItemEntry itemEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(itemEntry);
            }
        });

        mViewModel.getItemFrom().observe(getActivity(), new Observer<List<ListItemEntry>>() {
            @Override
            public void onChanged(@Nullable List<ListItemEntry> listItemEntries) {
                updateFromUi(listItemEntries);
            }
        });

        mViewModel.getItemInto().observe(getActivity(), new Observer<List<ListItemEntry>>() {
            @Override
            public void onChanged(@Nullable List<ListItemEntry> listItemEntries) {
                updateIntoUi(listItemEntries);
            }
        });
    }

    private void updateUi(@Nullable ItemEntry itemEntry) {
        Log.d(LOG_TAG, "updating Ui");
        String image = itemEntry.getImage();

        PicassoUtils.setItemImage(mImage, image, mPatchVersion,
                        R.dimen.item_thumbnail_width, R.dimen.item_thumbnail_height);
        int baseGold = itemEntry.getBaseGold();
        int totalGold = itemEntry.getTotalGold();
        int sellGold = itemEntry.getSellGold();

        String name = itemEntry.getName();
        String description = itemEntry.getDescription();
        String plainText = itemEntry.getPlainText();

        setTextWithLabel(baseGold, mBaseGoldTv, mBaseGoldLabel);
        setTextWithLabel(totalGold, mTotalGoldTv, mTotalGoldLabel);
        setTextWithLabel(sellGold, mSellGoldTv, mSellGoldLabel);

        setTextWithLabel(description, mDescriptionTv, mDescriptionLabel);
        setTextWithLabel(plainText, mPlainTextTv, mPlainTextLabel);

        mItemNameTv.setText(name);
        
        mFromRecycler.setVisibility(View.INVISIBLE);
        mIntoRecycler.setVisibility(View.INVISIBLE);
    }

    private void updateFromUi(List<ListItemEntry> listItemEntries) {
        mItemFromAdapter.setData(listItemEntries);
        if (listItemEntries != null && !listItemEntries.isEmpty()) {
            mFromLabel.setVisibility(View.VISIBLE);
            mFromRecycler.setVisibility(View.VISIBLE);
        } else {
            mFromLabel.setVisibility(View.INVISIBLE);
            mFromRecycler.setVisibility(View.INVISIBLE);
        }
    }

    private void updateIntoUi(@Nullable List<ListItemEntry> listItemEntries) {
        mItemIntoAdapter.setData(listItemEntries);
        if (listItemEntries != null && !listItemEntries.isEmpty()) {
            mIntoLabel.setVisibility(View.VISIBLE);
            mIntoRecycler.setVisibility(View.VISIBLE);
        } else {
            mIntoLabel.setVisibility(View.INVISIBLE);
            mIntoRecycler.setVisibility(View.INVISIBLE);
        }
    }

    private void setTextWithLabel(String string, TextView textView, TextView textViewLabel) {
        if (TextUtils.isEmpty(string)) {
            textViewLabel.setVisibility(View.GONE);
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textView.setText(string);
        }
    }

    private void setTextWithLabel(int value, TextView textView, TextView textViewLabel) {
        if (value == 0) {
            textViewLabel.setVisibility(View.GONE);
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(value));
        }
    }

    @Override
    public void onClickListener(long id) {
        mCallback.onItemDetailSelected(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnItemDetailSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnItemDetailSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFromRecycler.setAdapter(null);
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

        outState.putParcelable(FROM_LAYOUT_MANAGER_STATE_KEY, mFromRecycler.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(INTO_LAYOUT_MANAGER_STATE_KEY, mIntoRecycler.getLayoutManager().onSaveInstanceState());
    }
}
