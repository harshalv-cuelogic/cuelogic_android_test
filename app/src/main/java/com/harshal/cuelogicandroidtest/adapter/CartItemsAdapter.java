package com.harshal.cuelogicandroidtest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.harshal.cuelogicandroidtest.BaseActivity;
import com.harshal.cuelogicandroidtest.R;
import com.harshal.cuelogicandroidtest.entity.Product;
import com.harshal.cuelogicandroidtest.listener.CartListener;
import com.harshal.cuelogicandroidtest.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Product> cartItemsList;
    private CartListener cartListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProductName, txtProductQuantity, txtProductPrice, txtVendorName, txtVendorAddress;
        public ImageView imgProduct;
        public Button btnCallVendor, btnRemoveFromCart;

        public MyViewHolder(View view) {
            super(view);
            txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            txtProductQuantity = (TextView) view.findViewById(R.id.txtProductQuantity);
            txtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            txtVendorName = (TextView) view.findViewById(R.id.txtVendorName);
            txtVendorAddress = (TextView) view.findViewById(R.id.txtVendorAddress);
            imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
            btnCallVendor = (Button) view.findViewById(R.id.btnCallVendor);
            btnRemoveFromCart = (Button) view.findViewById(R.id.btnRemoveFromCart);
        }
    }


    public CartItemsAdapter(Context mContext, List<Product> cartItemsList, CartListener cartListener) {
        this.mContext = mContext;
        this.cartItemsList = cartItemsList;
        this.cartListener = cartListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Product product = cartItemsList.get(position);
        holder.txtProductName.setText(product.getProductName());
        try {
            holder.txtProductPrice.setText(mContext.getString(R.string.price)+" : "+ mContext.getString(R.string.Rs) +" "+ Util.getFormattedAmount(Float.parseFloat(product.getPrice())));
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        holder.txtVendorName.setText(product.getVendorName());
        holder.txtVendorAddress.setText(product.getVendorAddress());
        holder.txtProductQuantity.setText(mContext.getString(R.string.qty)+" : "+product.getQuantity());

        // loading album cover using Glide library
        Picasso.with(mContext).load(product.getProductImg())
                .fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgProduct);

        holder.btnCallVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(product.getPhoneNumber().trim())){
                    Util.dialPhoneNumber(mContext, product.getPhoneNumber());
                }else{
                    ((BaseActivity)mContext).showSnackBarLong(view, mContext.getString(R.string.phone_no_not_provided), Color.WHITE);
                }
            }
        });
        holder.btnRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartListener!=null){
                    cartListener.onProductRemovedFromCart(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }
}
