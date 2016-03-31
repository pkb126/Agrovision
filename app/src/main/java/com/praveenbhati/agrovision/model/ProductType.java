package com.praveenbhati.agrovision.model;

/**
 * Created by Bhati on 11/9/2015.
 */
public class ProductType{
    int productTypeId;
    String productTypeName;
    int productId;
    double productRate;

    public ProductType() {
    }



    public ProductType(int productTypeId, String productTypeName, int productId) {
        this.productTypeId = productTypeId;
        this.productTypeName = productTypeName;
        this.productId = productId;
    }

    public double getProductRate() {
        return productRate;
    }

    public void setProductRate(double productRate) {
        this.productRate = productRate;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
