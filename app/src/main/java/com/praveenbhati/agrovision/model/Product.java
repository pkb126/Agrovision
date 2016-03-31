package com.praveenbhati.agrovision.model;

/**
 * Created by Bhati on 11/9/2015.
 */
public class Product {
    int productID;
    String productName;

    public Product() {
    }

    public Product(String productName) {
        this.productName = productName;
    }

    public Product(int productID, String productName) {
        this.productID = productID;
        this.productName = productName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
