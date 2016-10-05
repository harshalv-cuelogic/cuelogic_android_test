package com.harshal.cuelogicandroidtest.listener;

import com.harshal.cuelogicandroidtest.entity.Product;

/**
 * Created by ceroroot on 8/8/16.
 */
public interface CartListener {
    void onProductAddToCart(Product product);
    void onProductRemovedFromCart(Product product);
}
