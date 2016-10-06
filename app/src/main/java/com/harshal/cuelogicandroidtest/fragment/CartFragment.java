package com.harshal.cuelogicandroidtest.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harshal.cuelogicandroidtest.BaseActivity;
import com.harshal.cuelogicandroidtest.R;
import com.harshal.cuelogicandroidtest.adapter.CartItemsAdapter;
import com.harshal.cuelogicandroidtest.database._CartItemsDB;
import com.harshal.cuelogicandroidtest.decoration.GridSpacingItemDecoration;
import com.harshal.cuelogicandroidtest.entity.Product;
import com.harshal.cuelogicandroidtest.util.Util;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends BaseFragment {
    private TextView txtDummy;
    private RecyclerView recyclerView;
    private List<Product> listCartItems = new ArrayList<Product>();
    private CartItemsAdapter cartItemsAdapter;
    private TextView txtTotalPrice;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_cart, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                new _CartItemsDB(getActivity()).deleteAllProductsFromCart();
                refreshCartItems();
                ((BaseActivity) getActivity()).showSnackBarLong(getView(), getString(R.string.clear_cart_items), Color.WHITE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inItAllUiViews(view);

        cartItemsAdapter = new CartItemsAdapter(getActivity(), listCartItems, cartListener);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Util.dpToPx(getActivity(), 5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartItemsAdapter);

        refreshCartItems();
    }

    @Override
    public void refreshCartItems() {
        listCartItems.clear();
        listCartItems.addAll(new _CartItemsDB(getActivity()).getAllCartItems());
        cartItemsAdapter.notifyDataSetChanged();
        if(listCartItems.size() > 0){
            txtDummy.setText(getString(R.string.blank));
            txtDummy.setVisibility(View.GONE);
        }else{
            txtDummy.setText(getString(R.string.no_items_in_cart));
            txtDummy.setVisibility(View.VISIBLE);
        }
        float totalPrice = 0;
        for (Product cartItem:
             listCartItems) {
            try {
                totalPrice += cartItem.getQuantity() * Float.parseFloat(cartItem.getPrice());
            }catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }
        txtTotalPrice.setText(getString(R.string.total_price)+" : "+getString(R.string.Rs) +" "+ Util.getFormattedAmount(totalPrice));
    }

    private void inItAllUiViews(View view) {
        txtDummy = (TextView) view.findViewById(R.id.txtDummy);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        txtTotalPrice = (TextView) view.findViewById(R.id.txtTotalPrice);
    }
}
