package com.harshal.cuelogicandroidtest.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.harshal.cuelogicandroidtest.BaseActivity;
import com.harshal.cuelogicandroidtest.R;
import com.harshal.cuelogicandroidtest.adapter.ProductsAdapter;
import com.harshal.cuelogicandroidtest.decoration.GridSpacingItemDecoration;
import com.harshal.cuelogicandroidtest.entity.Product;
import com.harshal.cuelogicandroidtest.entity.ProductsList;
import com.harshal.cuelogicandroidtest.util.Constants;
import com.harshal.cuelogicandroidtest.util.Url;
import com.harshal.cuelogicandroidtest.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ShopFragment extends BaseFragment {

    private TextView txtDummy;
    private RecyclerView recyclerView;
    private List<Product> listProducts = new ArrayList<Product>();
    private ProductsAdapter productsAdapter;
    private NetworkChangedReceiver networkChangedReceiver = new NetworkChangedReceiver();

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BaseActivity) getActivity()).registerReceiver(networkChangedReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inItAllUiViews(view);
        productsAdapter = new ProductsAdapter(getActivity(), listProducts, cartListener);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Util.dpToPx(getActivity(), 5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsAdapter);

        makeRequestToFetchData();
    }

    private void makeRequestToFetchData() {
        if (Util.isNetworkAvailable(getActivity())) {
            txtDummy.setText(getString(R.string.loading_please_wait));
            txtDummy.setVisibility(View.VISIBLE);
            new AsyncHttpClient().get(Url.GET_DATA, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (cartListener != null) {
                        String strResponse = new String(responseBody);
                        if (Constants.LOG)
                            Log.d("Products List", strResponse);
                        ProductsList productsList = new Gson().fromJson(strResponse, ProductsList.class);
                        if (productsList.getProducts().size() > 0) {
                            txtDummy.setText(getString(R.string.blank));
                            txtDummy.setVisibility(View.GONE);
                            listProducts.clear();
                            listProducts.addAll(productsList.getProducts());
                            //Server not providing id's with product, So managed id's locally
                            for (int i = 0; i < listProducts.size(); i++) {
                                listProducts.get(i).setId(i + 1);
                            }
                            productsAdapter.notifyDataSetChanged();
                        } else {
                            txtDummy.setText(getString(R.string.no_data_found));
                            txtDummy.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (cartListener != null) {
                        txtDummy.setText(getString(R.string.no_data_found));
                        txtDummy.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            txtDummy.setText(getString(R.string.please_enable_network));
            txtDummy.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_shop, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                if (!getString(R.string.loading_please_wait).equals(txtDummy.getText().toString())) {
                    listProducts.clear();
                    productsAdapter.notifyDataSetChanged();
                    makeRequestToFetchData();
                } else {
                    ((BaseActivity) getActivity()).showSnackBarLong(getView(), getString(R.string.loading_in_progress), Color.WHITE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inItAllUiViews(View view) {
        txtDummy = (TextView) view.findViewById(R.id.txtDummy);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    public void refreshCartItems() {

    }

    public class NetworkChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Util.isNetworkAvailable(context)) {
//                ((BaseActivity)mContext).showSnackBarLong(getString(R.string.network_enabled), Color.WHITE);
                if (listProducts.size() == 0) {
                    makeRequestToFetchData();
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (networkChangedReceiver != null) {
            ((BaseActivity) getActivity()).unregisterReceiver(networkChangedReceiver);
        }
    }
}
