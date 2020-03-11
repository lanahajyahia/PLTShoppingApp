package com.example.plt.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.plt.Common.Common;
import com.example.plt.Models.Order;
import com.example.plt.Models.WishlistItem;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "PltDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getBags(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone", "ItemId", "ItemName", "Quantity", "Price", "Discount", "Size", "Image"};
        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "UserPhone=?", new String[]{userPhone},
                null, null, null, null); //add null
        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("ItemId")),
                        c.getString(c.getColumnIndex("ItemName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("Size")),
                        c.getString(c.getColumnIndex("Image"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToBag(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO OrderDetail(UserPhone,ItemId,ItemName,Quantity,Price,Discount,Size,Image) VALUES('%s','%s','%s','%s','%s','%s','%s','%s')",
                order.getUserPhone(),
                order.getItemId(),
                order.getItemName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getSize(),
                order.getImage());
        db.execSQL(query);
    }

    public void cleanBag(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s';", userPhone);
        db.execSQL(query);
    }

    public void removeFromBag(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ItemId='%s' and UserPhone='%s' and Size='%s';",
                order.getItemId(), order.getUserPhone(), order.getSize());
        db.execSQL(query);
    }

    public void updateBag(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity='%s' WHERE UserPhone ='%s' and ItemId='%s' and Size='%s'"
                , order.getQuantity(), order.getUserPhone(), order.getItemId(), order.getSize());
        db.execSQL(query);

    }

    public void increaseBag(String userPhone, String itemId, String itemSize) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(
                "UPDATE OrderDetail SET Quantity=Quantity+1 WHERE UserPhone ='%s' AND ItemId='%s' AND Size='%s'"
                , userPhone, itemId, itemSize);
        db.execSQL(query);
    }

    public boolean checkItemExist(String itemId, String userPhone, String itemSize) {
        boolean flag = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String SQLQuery = String.format("SELECT * From OrderDetail WHERE UserPhone='%s' AND ItemId='%s' AND Size='%s'"
                , userPhone, itemId, itemSize);
        Log.d("pttt", "size: " + itemSize);
        cursor = db.rawQuery(SQLQuery, null);
        if (cursor.getCount() > 0)
            flag = true;
        else
            flag = false;
        cursor.close();
        return flag;
    }

    public int getCountBag() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return count;

    }

    //wishlist
    public void addToWishlist(WishlistItem wishlistItem) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Wishlist(ItemId,ItemName,Price,Image,UserPhone) VALUES('%s','%s','%s','%s','%s')",
                wishlistItem.getItemId(),
                wishlistItem.getItemName(),
                wishlistItem.getPrice(),
                wishlistItem.getImage(),
                wishlistItem.getUserPhone());
        db.execSQL(query);
    }

    public void removeFromWishlist(WishlistItem wishlistItem) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Wishlist WHERE  itemId='%s' and UserPhone='%s';",
                wishlistItem.getItemId(), wishlistItem.getUserPhone());
        db.execSQL(query);
    }

    public boolean isFavorite(String itemId, String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Wishlist WHERE itemId='%s' and UserPhone='%s';", itemId, userPhone);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void cleanWishlist() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Wishlist");
        db.execSQL(query);
    }

    public List<WishlistItem> getWishlist(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ItemId", "ItemName", "Price", "Image", "UserPhone"};
        String sqlTable = "Wishlist";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "UserPhone=?", new String[]{userPhone}, null, null, null, null); //add null
        final List<WishlistItem> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Log.d("ptttt", c.getString(c.getColumnIndex("UserPhone")) + " " + Common.currentUser.getPhone());
                if (c.getString(c.getColumnIndex("UserPhone")).equals(Common.currentUser.getPhone())) {
                    Log.d("ptttt", c.getString(c.getColumnIndex("UserPhone")) + " " + Common.currentUser.getPhone());
                    result.add(new WishlistItem(c.getString(c.getColumnIndex("ItemId")),
                            c.getString(c.getColumnIndex("ItemName")),
                            c.getString(c.getColumnIndex("Price")),
                            c.getString(c.getColumnIndex("Image")),
                            c.getString(c.getColumnIndex("UserPhone"))
                    ));
                }
            } while (c.moveToNext());
        }
        return result;
    }


}
