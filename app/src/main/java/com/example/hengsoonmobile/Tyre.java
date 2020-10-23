package com.example.hengsoonmobile;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

/**
 * Created by Jex on 21/11/2016.
 */

public class Tyre implements Parcelable{
    //name and address string
    private String uniqueID;
    private String tyreBrand;
    private String tyreModel;
    private String tyreSize;
    private double price;
    private int quantity;
    private String manufactureDate;
    private double sellingPoint;
    private String remark;
    private String special;


    //Getters and setters
    public String getUniqueID() {
        return uniqueID;
    }
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getTyreBrand() {
        return tyreBrand;
    }
    public void setTyreBrand(String tyreBrand) {
        this.tyreBrand = tyreBrand;
    }

    public String getTyreModel() {
        return tyreModel;
    }
    public void setTyreModel(String tyreModel) {
        this.tyreModel = tyreModel;
    }

    public String getTyreSize() {
        return tyreSize;
    }
    public void setTyreSize(String tyreSize) {
        this.tyreSize = tyreSize;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }
    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;    }

    public double getSellingPoint() { return sellingPoint; }
    public void setSellingPoint(double sellingPoint) {
        this.sellingPoint = sellingPoint;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSpecial() {
        return special;
    }
    public void setSpecial(String special) {
        this.special = special;
    }


    public Tyre() {
      /*Blank default constructor essential for Firebase*/
        uniqueID = "";
        tyreBrand = "";
        tyreModel = "";
        tyreSize = "";
        price = 0.00;
        quantity = 0;
        manufactureDate = "";
        sellingPoint = 0.00;
        remark = "";
        special = "";
    }

    private Tyre(Parcel parcel){
        uniqueID = parcel.readString();
        tyreBrand = parcel.readString();
        tyreModel = parcel.readString();
        tyreSize = parcel.readString();
        price = parcel.readDouble();
        quantity = parcel.readInt();
        manufactureDate = parcel.readString();
        tyreBrand = parcel.readString();
        uniqueID = parcel.readString();
        tyreBrand = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(uniqueID);
        parcel.writeString(tyreBrand);
        parcel.writeString(tyreModel);
        parcel.writeString(tyreSize);
        parcel.writeDouble(price);
        parcel.writeInt(quantity);
        parcel.writeString(manufactureDate);
        parcel.writeString(remark);
        parcel.writeString(special);
    }

    public static final Parcelable.Creator<Tyre> CREATOR = new
            Parcelable.Creator<Tyre>() {
                public Tyre createFromParcel(Parcel in) {
                    return new Tyre(in);
                }

                public Tyre[] newArray(int size) {
                    return new Tyre[size];
                }};

    public Tyre(String tyreBrand, String tyreModel, String tyreSize, double price, int quantity, String manufactureDate) {
        this.tyreBrand = tyreBrand;
        this.tyreModel = tyreModel;
        this.tyreSize = tyreSize;
        this.price = price;
        this.quantity = quantity;
        this.manufactureDate = manufactureDate;
    }

    public Tyre(String tyreBrand, String tyreModel, String tyreSize, double price, int quantity, String manufactureDate, double sellingPoint) {
        this.tyreBrand = tyreBrand;
        this.tyreModel = tyreModel;
        this.tyreSize = tyreSize;
        this.price = price;
        this.quantity = quantity;
        this.manufactureDate = manufactureDate;
        this.sellingPoint = sellingPoint;
    }

    public Tyre(String uniqueID, String tyreBrand, String tyreModel, String tyreSize, double price, int quantity, String manufactureDate) {
        this.uniqueID = uniqueID;
        this.tyreBrand = tyreBrand;
        this.tyreModel = tyreModel;
        this.tyreSize = tyreSize;
        this.price = price;
        this.quantity = quantity;
        this.manufactureDate = manufactureDate;
    }

    public Tyre(String uniqueID, String tyreBrand, String tyreModel, String tyreSize, double price, int quantity, String manufactureDate, double sellingPoint, String remark, String special) {
        this.uniqueID = uniqueID;
        this.tyreBrand = tyreBrand;
        this.tyreModel = tyreModel;
        this.tyreSize = tyreSize;
        this.price = price;
        this.quantity = quantity;
        this.manufactureDate = manufactureDate;
        this.sellingPoint = sellingPoint;
        this.remark = remark;
        this.special = special;
    }





}
