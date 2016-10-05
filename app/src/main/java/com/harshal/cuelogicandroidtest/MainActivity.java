package com.harshal.cuelogicandroidtest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.harshal.cuelogicandroidtest.database._CartItemsDB;
import com.harshal.cuelogicandroidtest.entity.Product;
import com.harshal.cuelogicandroidtest.fragment.BaseFragment;
import com.harshal.cuelogicandroidtest.fragment.CartFragment;
import com.harshal.cuelogicandroidtest.fragment.ShopFragment;
import com.harshal.cuelogicandroidtest.listener.CartListener;
import com.harshal.cuelogicandroidtest.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements CartListener {
    private TextView toolbarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BaseFragment shopFragment, cartFragment;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        refreshCartItemsCount();
    }


    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.products));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_products_tab_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.cart));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cart_tab_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        shopFragment = new ShopFragment();
        shopFragment.setCartListener(this);
        adapter.addFrag(shopFragment, getString(R.string.shop));

        cartFragment = new CartFragment();
        cartFragment.setCartListener(this);
        adapter.addFrag(cartFragment, getString(R.string.cart));

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbarTitle.setText(getString(R.string.shop));
                        break;

                    case 1:
                        toolbarTitle.setText(getString(R.string.cart));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onProductAddToCart(Product product) {
        new _CartItemsDB(this).checkAndInsertProductInCart(product);
        if (cartFragment != null) {
            cartFragment.refreshCartItems();
        }
        refreshCartItemsCount();
        showToastShortCenter(getString(R.string.product_added_to_cart));
//        showSnackBarLong(coordinatorLayout, getString(R.string.product_added_to_cart), Color.GREEN);
    }

    private void refreshCartItemsCount() {
        int cartItemsCount = new _CartItemsDB(this).getCartItemsCount();
        TextView tabTwo = (TextView) tabLayout.getTabAt(1).getCustomView();
        if (cartItemsCount > 0) {
            Drawable drawable = Util.buildBadgeActionDrawable(this, cartItemsCount, R.drawable.ic_cart_tab_24dp);
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cart_tab_24dp, 0, 0);
        }
    }

    @Override
    public void onProductRemovedFromCart(Product product) {
        new _CartItemsDB(this).deleteProductFromCart(product);
        if (cartFragment != null) {
            cartFragment.refreshCartItems();
        }
        refreshCartItemsCount();
        showToastShortCenter(getString(R.string.product_removed_from_cart));
//        showSnackBarLong(coordinatorLayout, getString(R.string.product_removed_from_cart), Color.GREEN);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}