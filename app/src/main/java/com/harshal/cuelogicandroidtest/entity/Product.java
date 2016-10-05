package com.harshal.cuelogicandroidtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ceroroot on 8/8/16.
 */
public class Product implements Parcelable {
    private long id;
    @SerializedName("productname")
    private String productName;
    private String price;
    @SerializedName("vendorname")
    private String vendorName;
    @SerializedName("vendoraddress")
    private String vendorAddress;
    private String productImg;
    private ArrayList<String> productGallery;
    private String phoneNumber;
    private int quantity;

    public Product() {
    }

    public Product(String productName, String price, String vendorName, String vendorAddress, String productImg, ArrayList<String> productGallery, String phoneNumber) {
        this.productName = productName;
        this.price = price;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.productImg = productImg;
        this.productGallery = productGallery;
        this.phoneNumber = phoneNumber;
    }

    public Product(long id, String productName, String price, String vendorName, String vendorAddress, String productImg, ArrayList<String> productGallery, String phoneNumber, int quantity) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.productImg = productImg;
        this.productGallery = productGallery;
        this.phoneNumber = phoneNumber;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public ArrayList<String> getProductGallery() {
        return productGallery;
    }

    public void setProductGallery(ArrayList<String> productGallery) {
        this.productGallery = productGallery;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", vendorAddress='" + vendorAddress + '\'' +
                ", productImg='" + productImg + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeString(this.price);
        dest.writeString(this.vendorName);
        dest.writeString(this.vendorAddress);
        dest.writeString(this.productImg);
        dest.writeStringList(this.productGallery);
        dest.writeString(this.phoneNumber);
    }

    protected Product(Parcel in) {
        this.productName = in.readString();
        this.price = in.readString();
        this.vendorName = in.readString();
        this.vendorAddress = in.readString();
        this.productImg = in.readString();
        this.productGallery = in.createStringArrayList();
        this.phoneNumber = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
