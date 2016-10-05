package com.harshal.cuelogicandroidtest.fragment;

import android.support.v4.app.Fragment;

import com.harshal.cuelogicandroidtest.listener.CartListener;

/**
 * Created by ceroroot on 1/2/16.
 */
public abstract class BaseFragment extends Fragment{
    protected CartListener cartListener;

    public CartListener getCartListener() {
        return cartListener;
    }

    public void setCartListener(CartListener cartListener) {
        this.cartListener = cartListener;
    }

    public abstract void refreshCartItems();
}
