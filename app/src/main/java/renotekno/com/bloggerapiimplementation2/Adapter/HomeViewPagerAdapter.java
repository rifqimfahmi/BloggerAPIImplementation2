package renotekno.com.bloggerapiimplementation2.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import renotekno.com.bloggerapiimplementation2.FeaturedPosts;

/**
 * Created by zcabez on 20/08/2017.
 */

public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FeaturedPosts();
        } else if (position == 1) {
            return new Fragment();
        }else if (position == 2) {
            return new Fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
