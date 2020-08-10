package com.flotta.entity.viewEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import com.flotta.invoice.FeeItem;

public class OneCategoryOfUserFinance {
  
  private long userId;
  
  private String categoryName;
  
  private double totalGross;
  
  List<FeeItem> feeItems = new LinkedList<FeeItem>();
  
  public OneCategoryOfUserFinance() {}
  
  public OneCategoryOfUserFinance(long userId, String categoryName) {
    this.userId = userId;
    this.categoryName = categoryName;
    this.totalGross = 0;
    }

  public OneCategoryOfUserFinance(String categoryName, long totalGross, List<FeeItem> feeItems) {
    this.categoryName = categoryName;
    this.totalGross = totalGross;
    this.feeItems = feeItems;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public double getTotalGross() {
    return totalGross;
  }

  public void setTotalGross(double totalGross) {
    this.totalGross = totalGross;
  }

  public List<FeeItem> getFeeItems() {
    return feeItems;
  }

  public void setFeeItems(List<FeeItem> feeItems) {
    this.feeItems = feeItems;
  }
  
  public void addFeeItem(FeeItem fee) {
    feeItems.add(fee);
    totalGross = round(totalGross + fee.getUserGrossAmount(), 2);
  }
  
//https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
  private static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
