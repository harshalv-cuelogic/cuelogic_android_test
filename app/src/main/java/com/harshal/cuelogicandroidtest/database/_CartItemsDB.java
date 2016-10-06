package com.harshal.cuelogicandroidtest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harshal.cuelogicandroidtest.entity.Product;
import com.harshal.cuelogicandroidtest.manager.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class _CartItemsDB extends DatabaseManager {
    private Context mContext;

    public _CartItemsDB(Context context) {
        super(context);
        this.mContext = context;
    }

    public interface _Product {
        String TABLE = "CartItems";

        String ID = "id";
        String PRODUCT_NAME = "product_name";
        String PRICE = "price";
        String VENDOR_NAME = "vendor_name";
        String VENDOR_ADDRESS = "vendor_address";
        String PRODUCT_IMAGE = "product_image";
        String PRODUCT_GALLERY = "product_gallery";
        String PHONE_NUMBER = "phone_number";
        String QUANTITY = "quantity";

        String CREATE_TABLE = "create table " + TABLE
                + " (" + ID + " INTEGER, "
                + PRODUCT_NAME + " TEXT, "
                + PRICE + " TEXT, "
                + VENDOR_NAME + " TEXT, "
                + VENDOR_ADDRESS + " TEXT, "
                + PRODUCT_IMAGE + " TEXT, "
                + PRODUCT_GALLERY + " TEXT, "
                + PHONE_NUMBER + " TEXT, "
                + QUANTITY + " INTEGER);";
    }

    public int getCartItemsCount() {
        SQLiteDatabase db = open();
        return (int)DatabaseUtils.queryNumEntries(db, _Product.TABLE);
    }

    public List<Product> getAllCartItems() {
        SQLiteDatabase db = open();
        List<Product> listCartItems = new ArrayList<Product>();
        Cursor cur = db.query(_Product.TABLE, null, null, null, null, null, null);
        if (cur.moveToFirst()) {
            do {
                Product product = makeProduct(cur);
                listCartItems.add(product);
            } while (cur.moveToNext());
        }
        cur.close();
        return listCartItems;
    }

    public Product makeProduct(Cursor cur) {
        ArrayList<String> productGallery = new Gson().fromJson(cur.getString(cur.getColumnIndex(_Product.PRODUCT_GALLERY)), new TypeToken<ArrayList<String>>() {
        }.getType());
        return new Product(cur.getLong(cur.getColumnIndex(_Product.ID)),
                cur.getString(cur.getColumnIndex(_Product.PRODUCT_NAME)),
                cur.getString(cur.getColumnIndex(_Product.PRICE)),
                cur.getString(cur.getColumnIndex(_Product.VENDOR_NAME)),
                cur.getString(cur.getColumnIndex(_Product.VENDOR_ADDRESS)),
                cur.getString(cur.getColumnIndex(_Product.PRODUCT_IMAGE)),
                productGallery,
                cur.getString(cur.getColumnIndex(_Product.PHONE_NUMBER)),
                cur.getInt(cur.getColumnIndex(_Product.QUANTITY)));
    }

    public long checkAndInsertProductInCart(Product product) {
        SQLiteDatabase db = open();
        Product tempProduct = null;
        Cursor cur = db.query(_Product.TABLE, null, _Product.ID + "=?", new String[]{"" + product.getId()}, null, null, null);
        if (cur.moveToFirst()) {
            tempProduct = makeProduct(cur);
        }
        long count = 0;
        if (tempProduct == null) {
            product.setQuantity(1);
            count = insertProductInCart(product);
        } else {
            int newQty = tempProduct.getQuantity()+1;
            product.setQuantity(newQty);
            count = updateProductInCart(product);
        }
        return count;
    }

    public long insertProductInCart(Product product) {
        SQLiteDatabase db = open();
        ContentValues values = makeContentValuesToInsert(product);
        long count = db.insert(_Product.TABLE, null, values);
        return count;
    }

    private ContentValues makeContentValuesToInsert(Product product) {
        ContentValues values = new ContentValues();
        values.put(_Product.ID, product.getId());
        values.put(_Product.PRODUCT_NAME, product.getProductName());
        values.put(_Product.PRICE, product.getPrice());
        values.put(_Product.VENDOR_NAME, product.getVendorName());
        values.put(_Product.VENDOR_ADDRESS, product.getVendorAddress());
        values.put(_Product.PRODUCT_IMAGE, product.getProductImg());
        values.put(_Product.PRODUCT_GALLERY, new Gson().toJson(product.getProductGallery()));
        values.put(_Product.PHONE_NUMBER, product.getPhoneNumber());
        values.put(_Product.QUANTITY, product.getQuantity());
        return values;
    }

    public long updateProductInCart(Product product) {
        SQLiteDatabase db = open();
        ContentValues values = makeContentValuesToUpdateProduct(product);
        long count = db.update(_Product.TABLE, values, _Product.ID + "=?",
                new String[]{"" + product.getId()});
        return count;
    }

    private ContentValues makeContentValuesToUpdateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(_Product.QUANTITY, product.getQuantity());
        return values;
    }

    public long deleteProductFromCart(Product product) {
        SQLiteDatabase db = open();
        long count = db.delete(_Product.TABLE, _Product.ID + "=?", new String[]{"" + product.getId()});
        return count;
    }
    public long deleteAllProductsFromCart() {
        SQLiteDatabase db = open();
        long count = db.delete(_Product.TABLE, null, null);
        return count;
    }
}
