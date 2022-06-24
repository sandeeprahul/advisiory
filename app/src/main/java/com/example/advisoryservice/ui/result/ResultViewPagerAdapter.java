package com.example.advisoryservice.ui.result;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.advisoryservice.data.model.revieveDetail.RevieveDetail;
import com.example.advisoryservice.data.model.testresult.Summary;
import com.example.advisoryservice.util.WrappingViewPager;

import java.util.List;

public class ResultViewPagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    private int totalTabs,tabPosition;
    private List<Summary> data;
    private String custTransId;
    private boolean isSelfieTaken= false;
    private RevieveDetail revieveDetail;

    public ResultViewPagerAdapter(Context context, FragmentManager fm, int totalTabs, List<Summary> data, String custTransId, boolean isSelfieTaken, int tabPosition, RevieveDetail revieveDetail) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.data = data;
        this.custTransId= custTransId;
        this.isSelfieTaken= isSelfieTaken;
        this.tabPosition = tabPosition;
        this.revieveDetail = revieveDetail;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        return TabFragment.newInstance(data.get(position), position,custTransId,isSelfieTaken,revieveDetail);
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

    private int mCurrentPosition = -1; // Keep track of the current position

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);

        if (!(container instanceof WrappingViewPager)) {
            return; // Do nothing if it's not a compatible ViewPager
        }

        if (position != mCurrentPosition) { // If the position has changed, tell WrappingViewPager
            Fragment fragment = (Fragment) object;
            WrappingViewPager pager = (WrappingViewPager) container;
            if (fragment.getView() != null) {
                mCurrentPosition = position;
                pager.onPageChanged(fragment.getView());
            }
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getTitle();
    }
}
