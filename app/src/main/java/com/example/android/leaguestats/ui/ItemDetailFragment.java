package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.ItemModel;
import com.example.android.leaguestats.viewmodels.ItemModelFactory;

import java.util.List;

public class ItemDetailFragment extends Fragment implements IdClickListener {

    private static final String LOG_TAG = ItemDetailFragment.class.getSimpleName();
    private NestedScrollView mScrollView;
    private TextView mBaseGoldLabel, mTotalGoldLabel, mSellGoldLabel, mDescriptionLabel,
            mFromLabel, mIntoLabel;
    private TextView mItemNameTv, mBaseGoldTv, mTotalGoldTv, mSellGoldTv, mDescriptionTv,
            mPlainTextTv;
    private ImageView mImage;
    private ItemAdapter mItemFromAdapter;
    private ItemAdapter mItemIntoAdapter;
    private RecyclerView mFromRecycler;
    private RecyclerView mIntoRecycler;
    private ItemModel mViewModel;
    private String mPatchVersion;

    private TextView mEmptyViewTv;
    private ViewGroup mContentView;

    public ItemDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        mScrollView = rootView.findViewById(R.id.nested_scroll_view);
        mBaseGoldLabel = rootView.findViewById(R.id.base_gold_label);
        mTotalGoldLabel = rootView.findViewById(R.id.total_gold_label);
        mSellGoldLabel = rootView.findViewById(R.id.sell_gold_label);
        mDescriptionLabel = rootView.findViewById(R.id.item_description_label);
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
        mEmptyViewTv = rootView.findViewById(R.id.empty_view_tv);
        mContentView = rootView.findViewById(R.id.item_content_layout);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        mFromRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mFromRecycler.setHasFixedSize(true);
        mFromRecycler.setItemAnimator(new DefaultItemAnimator());
        mItemFromAdapter = new ItemAdapter(getContext(), this, mPatchVersion);
        mFromRecycler.setAdapter(mItemFromAdapter);

        mIntoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mIntoRecycler.setHasFixedSize(true);
        mIntoRecycler.setItemAnimator(new DefaultItemAnimator());
        mItemIntoAdapter = new ItemAdapter(getContext(), this, mPatchVersion);
        mIntoRecycler.setAdapter(mItemIntoAdapter);

        mFromRecycler.setNestedScrollingEnabled(false);
        mIntoRecycler.setNestedScrollingEnabled(false);

        setupViewModel();
    }

    private void setupViewModel() {
        ItemModelFactory factory =
                InjectorUtils.provideItemModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ItemModel.class);
        mViewModel.getItem().observe(this, new Observer<ItemEntry>() {
            @Override
            public void onChanged(@Nullable ItemEntry itemEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(itemEntry);
            }
        });
        mViewModel.getItemFrom().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> itemList) {
                updateFromUi(itemList);
            }
        });
        mViewModel.getItemInto().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> itemList) {
                updateIntoUi(itemList);
            }
        });
    }

    private void updateUi(@Nullable ItemEntry itemEntry) {
        mScrollView.fullScroll(View.FOCUS_UP);
        if (itemEntry == null) {
            hideViews();
            return;
        }
        showViews();

        GlideApp.with(this)
                .load(GlideUtils.getItemUrl(mPatchVersion, itemEntry.getItemId()))
                .roundedImage()
                .into(mImage);

        int baseGold = itemEntry.getBaseGold();
        int totalGold = itemEntry.getTotalGold();
        int sellGold = itemEntry.getSellGold();

        String name = itemEntry.getName();
        String description = itemEntry.getDescription();
        String plainText = itemEntry.getPlainText();

        setTextWithLabel(baseGold, mBaseGoldTv, mBaseGoldLabel);
        setTextWithLabel(totalGold, mTotalGoldTv, mTotalGoldLabel);
        setTextWithLabel(sellGold, mSellGoldTv, mSellGoldLabel);

        setTextWithLabel(description, plainText, mDescriptionTv, mPlainTextTv, mDescriptionLabel);

        mItemNameTv.setText(name);

        mFromRecycler.setVisibility(View.INVISIBLE);
        mIntoRecycler.setVisibility(View.INVISIBLE);
    }

    private void updateFromUi(@Nullable List<Item> itemList) {
        mItemFromAdapter.setData(itemList);
        if (itemList != null && !itemList.isEmpty()) {
            mFromLabel.setVisibility(View.VISIBLE);
            mFromRecycler.setVisibility(View.VISIBLE);
        } else {
            mFromLabel.setVisibility(View.INVISIBLE);
            mFromRecycler.setVisibility(View.INVISIBLE);
        }
    }

    private void updateIntoUi(@Nullable List<Item> itemList) {
        mItemIntoAdapter.setData(itemList);
        if (itemList != null && !itemList.isEmpty()) {
            mIntoLabel.setVisibility(View.VISIBLE);
            mIntoRecycler.setVisibility(View.VISIBLE);
        } else {
            mIntoLabel.setVisibility(View.INVISIBLE);
            mIntoRecycler.setVisibility(View.INVISIBLE);
        }
    }

    private void showViews() {
        mEmptyViewTv.setVisibility(View.INVISIBLE);
        mContentView.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        mEmptyViewTv.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.INVISIBLE);
    }

    private void setTextWithLabel(String description, String plainText, TextView descriptionTv,
                                  TextView plainTextTv, TextView labelTv) {
        if (TextUtils.isEmpty(description) && TextUtils.isEmpty(plainText)) {
            labelTv.setVisibility(View.GONE);
            descriptionTv.setVisibility(View.GONE);
            plainTextTv.setVisibility(View.GONE);
        } else {
            labelTv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(description)) {
                descriptionTv.setVisibility(View.VISIBLE);
                descriptionTv.setText(description);
            } else {
                descriptionTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(plainText)) {
                plainTextTv.setVisibility(View.VISIBLE);
                plainTextTv.setText(plainText);
            } else {
                plainTextTv.setVisibility(View.GONE);
            }
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
    public void onClick(int id) {
        initItem(id);
    }

    private void initItem(long id) {
        mViewModel.initItem(id);
    }
}
