package com.harshal.cuelogicandroidtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


/**
 * Created by ceroroot on 8/8/16.
 */
public class ProductsList implements Parcelable {
    List<Product> products;

    public ProductsList() {
    }

    public ProductsList(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductsList{" +
                "products=" + products +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(products);
    }

    protected ProductsList(Parcel in) {
        this.products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Parcelable.Creator<ProductsList> CREATOR = new Parcelable.Creator<ProductsList>() {
        @Override
        public ProductsList createFromParcel(Parcel source) {
            return new ProductsList(source);
        }

        @Override
        public ProductsList[] newArray(int size) {
            return new ProductsList[size];
        }
    };
}
