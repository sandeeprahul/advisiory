package com.example.advisoryservice.ui.result;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.data.model.testresult.RestProduct;
import com.example.advisoryservice.data.model.testresult.SubCategory;
import com.example.advisoryservice.data.model.testresult.TopProduct;
import com.example.advisoryservice.util.Log;

import java.util.HashMap;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class ExpandableCategorySection extends Section {

    private String TAG = ExpandableCategorySection.class.getSimpleName();

    private final String title;
    private final List<SubCategory> list;
    private final ClickListener clickListener;
    private Context context;
    private HashMap<String, String> subcategoryMap = new HashMap<>();

    private String categoryId;
    private boolean expanded = false;
    int count = 0;
    private CircularProgressDrawable circularProgressDrawable;

    ExpandableCategorySection(Context context, @NonNull final String title, @NonNull final List<SubCategory> list,
                              @NonNull final ClickListener clickListener, String categoryId) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.section_item)
                .headerResourceId(R.layout.section_header)
                .build());

        this.context = context;
        this.title = title;
        this.list = list;
        this.clickListener = clickListener;
        this.categoryId = categoryId;

        int[] COLORS = new int[]{
                context.getResources().getColor(R.color.colorAccent)};

        circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();
    }

    public String getCategoryTitle() {
        return title;
    }

    public HashMap<String, String> getSubcategoryMap() {
        return subcategoryMap;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? list.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        final SubCategory category = list.get(position);

        subcategoryMap.put(category.getSubCategoryName(), category.getSubCategoryId());
        itemHolder.tvItem.setText(category.getSubCategoryName());

        //itemHolder.llSubCategory.setVisibility(category.getTopProducts().size() > 0 ? View.VISIBLE : View.GONE);

        Log.d(TAG, "Category Name " + category.getSubCategoryName());
        Log.d(TAG, "TopProduct List size <<<::>>> " + category.getTopProducts().size());

        Log.d(TAG, "CategoryId <<::>> " + categoryId);

        itemHolder.llSubCategory.removeAllViews();
        if (category.getTopProducts().size() == 0) {

            Log.e(TAG, "Top product list is empty");
            Log.d(TAG, "Rest Product Size <<::>> " + category.getRestProducts().size());
            count = 0;

            for (RestProduct restProduct : category.getRestProducts()) {

                Log.d(TAG, "Count <<::>> " + count);
                if (categoryId.isEmpty())
                    subListItem(restProduct, itemHolder);
                else if (restProduct.getFilterCategory().equalsIgnoreCase(categoryId)) {
                    subListItem(restProduct, itemHolder);
                }

                if (count == 3)
                    break;
                count++;
            }

        } else {
            count = 0;
            Log.e(TAG, "Top product list is displayed " + category.getTopProducts().size());

            for (TopProduct topProduct : category.getTopProducts()) {

                Log.d(TAG, "Top Products Filtercategort " + topProduct.getFilterCategory());
                if (categoryId.isEmpty())
                    subListItem(topProduct, itemHolder);
                else if (topProduct.getFilterCategory().equalsIgnoreCase(categoryId)) {
                    subListItem(topProduct, itemHolder);
                }

                if (count == 3)
                    break;
                count++;
            }
        }

        itemHolder.rootView.setOnClickListener(v ->
                clickListener.onItemRootViewClicked(this, itemHolder.getAdapterPosition(), category.getSubCategoryName(),
                        categoryId.isEmpty() ? "all" : categoryId)
        );
    }

    private void subListItem(RestProduct restProduct, ItemViewHolder itemHolder) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = vi.inflate(R.layout.subcategory_list_item, null);

        ImageView ivProductImage = v1.findViewById(R.id.ivProductImage);
        TextView tvCategoryName = v1.findViewById(R.id.tvCategoryName);
        TextView tvCategoryPrice = v1.findViewById(R.id.tvCategoryPrice);
        TextView tvCategoryRating = v1.findViewById(R.id.tvCategoryRating);
        RatingBar ratingBar = v1.findViewById(R.id.ratingBar);

        Glide.with(context).load(restProduct.getProductImageUrl())
                .placeholder(circularProgressDrawable)
                .into(ivProductImage);
        tvCategoryName.setText(restProduct.getSkuName());
        tvCategoryPrice.setText(new StringBuilder().append(context.getResources().getString(R.string.rupee_symbol))
                .append(restProduct.getMrp()));
        tvCategoryRating.setText(restProduct.getSkinExpertRating());
        if (!restProduct.getSkinExpertRating().isEmpty())
            ratingBar.setRating(Float.parseFloat(restProduct.getSkinExpertRating()));

        if (isTablet()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    325, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15, 10, 15, 10);

            itemHolder.llSubCategory.addView(v1, layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15, 10, 15, 10);

            itemHolder.llSubCategory.addView(v1, layoutParams);
        }
    }

    private void subListItem(TopProduct topProduct, ItemViewHolder itemHolder) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.subcategory_list_item, null);

        ImageView ivProductImage = v.findViewById(R.id.ivProductImage);
        TextView tvCategoryName = v.findViewById(R.id.tvCategoryName);
        TextView tvCategoryPrice = v.findViewById(R.id.tvCategoryPrice);
        TextView tvCategoryRating = v.findViewById(R.id.tvCategoryRating);
        RatingBar ratingBar = v.findViewById(R.id.ratingBar);

        Log.d(TAG, "Top ");
        Glide.with(context).load(topProduct.getProductImageUrl())
                .placeholder(circularProgressDrawable)
                .into(ivProductImage);
        tvCategoryName.setText(topProduct.getSkuName());
        tvCategoryPrice.setText(new StringBuilder().append(context.getResources().getString(R.string.rupee_symbol)).append(topProduct.getMrp()));
        tvCategoryRating.setText(topProduct.getSkinExpertRating());
        if (!topProduct.getSkinExpertRating().isEmpty())
            ratingBar.setRating(Float.parseFloat(topProduct.getSkinExpertRating()));

        if (isTablet()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    325, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15, 10, 15, 10);

            Log.d(TAG, "Prouduct added in tablet");
            itemHolder.llSubCategory.addView(v, layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15, 10, 15, 10);

            Log.d(TAG, "Prouduct added in phone");
            itemHolder.llSubCategory.addView(v, layoutParams);
        }
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
        /*headerHolder.imgArrow.setImageResource(
                expanded ? R.drawable.ic_keyboard_arrow_up_black_18dp : R.drawable.ic_keyboard_arrow_down_black_18dp
        );*/

        headerHolder.rootView.setOnClickListener(v -> clickListener.onHeaderRootViewClicked(this));
    }

    boolean isExpanded() {
        return expanded;
    }

    void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    interface ClickListener {

        void onHeaderRootViewClicked(@NonNull final ExpandableCategorySection section);

        void onItemRootViewClicked(@NonNull final ExpandableCategorySection section, final int itemAdapterPosition, String name, String categoryId);
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        final View rootView;
        final TextView tvTitle;
        final ImageView imgArrow;

        HeaderViewHolder(@NonNull View view) {
            super(view);

            rootView = view;
            tvTitle = view.findViewById(R.id.tvTitle);
            imgArrow = view.findViewById(R.id.imgArrow);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        final View rootView;
        final TextView tvItem, tvViewAll;
        final LinearLayout llSubCategory;

        ItemViewHolder(@NonNull View view) {
            super(view);

            rootView = view;
            tvViewAll = view.findViewById(R.id.tvViewAll);
            tvItem = view.findViewById(R.id.tvSubTitle);
            llSubCategory = view.findViewById(R.id.llSubCategory);
        }
    }

    public boolean isTablet() {
        try {
            // Compute screen size
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            // Tablet devices should have a screen size greater than 6 inches
            return size >= 6;
        } catch (Throwable t) {
            android.util.Log.e(TAG, "Failed to compute screen size", t);
            return false;
        }
    }
}