package com.example.android.ilovenougat;
import android.net.Uri;

import java.net.URL;

public class Data {

    private String brandName;
    private String price;
    private String dprice;
    private String discount;
    private String productName;

    private String iurl;

    Data(String mbrandName, String mprice, String mdprice, String mdiscount, String miurl, String mproductName){
        brandName=mbrandName;
        price=mprice;
        dprice=mdprice;
        discount=mdiscount;
        iurl=miurl;
        productName=mproductName;
    }

    public String getBrandName(){
        return brandName;
    }

    public String getPrice(){
        return  price;
    }

    public String getDprice(){
        return dprice;
    }

    public String getDiscount(){
        return discount;
    }

    public String getIurl(){
        return iurl;
    }

    public String getProductName(){
        return productName;
    }

}
