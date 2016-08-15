/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.productstats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;

import java.util.List;

public class DemoProductStatsActivity extends AppCompatActivity implements DemoProductStatsContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private DemoProductStatsContract.UserActionsListener bulkRatingsUserActionListener;

    private CardView productRowHeader;
    private TextView productName;
    private RatingBar productRating;

    private RecyclerView productStatsRecyclerView;
    private DemoProductStatsAdapter productStatsAdapter;
    private ProgressBar reviewsLoading;

    private String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_reviews);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        setupToolbarViews();
        setupHeaderViews();
        setupRecyclerView();

        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        bulkRatingsUserActionListener = new DemoProductStatsPresenter(this, demoConfigUtils, demoDataUtil, productId);
    }

    private void setupToolbarViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupHeaderViews() {
        productRowHeader = (CardView) findViewById(R.id.product_row_header);
        (productRowHeader.findViewById(R.id.product_image)).setVisibility(View.INVISIBLE);

        productName = (TextView) productRowHeader.findViewById(R.id.product_name);
        productRating = (RatingBar) productRowHeader.findViewById(R.id.product_rating);

        productName.setText("Testing product id: " + productId);
        productRating.setVisibility(View.INVISIBLE);

    }

    private void setupRecyclerView() {
        productStatsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        productStatsAdapter = new DemoProductStatsAdapter();
        productStatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        productStatsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        productStatsRecyclerView.setAdapter(productStatsAdapter);
        productStatsRecyclerView.setNestedScrollingEnabled(false);
        reviewsLoading = (ProgressBar) findViewById(R.id.reviews_loading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bulkRatingsUserActionListener.loadProductStats();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showProductStats(List<Product> bazaarStatistics) {
        productStatsAdapter.refreshReviews(bazaarStatistics);
    }

    @Override
    public void showLoadingProductStats(boolean show) {
        reviewsLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoProductStats() {

    }

    @Override
    public void showProductStatsMessage(String message) {
        Toast.makeText(DemoProductStatsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void transitionToProductStats() {
        // no-op
    }

    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoProductStatsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }
}
