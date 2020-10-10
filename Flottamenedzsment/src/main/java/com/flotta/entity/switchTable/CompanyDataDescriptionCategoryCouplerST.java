package com.flotta.entity.switchTable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.flotta.invoice.CompanyData;
import com.flotta.invoice.DescriptionCategoryCoupler;

@Entity
public class CompanyDataDescriptionCategoryCouplerST extends BasicSwitchTable {

  @ManyToOne
  private CompanyData companyData;
  
//  private DescriptionCategoryCoupler descriptionCategoryCoupler;

  public CompanyData getCompanyData() {
    return companyData;
  }
  
  
  public void setCompanyData(CompanyData companyData) {
    this.companyData = companyData;
  }

//  public DescriptionCategoryCoupler getDescriptionCategoryCoupler() {
//    return descriptionCategoryCoupler;
//  }
//
//
//
//  public void setDescriptionCategoryCoupler(DescriptionCategoryCoupler descriptionCategoryCoupler) {
//    this.descriptionCategoryCoupler = descriptionCategoryCoupler;
//  }



  @Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    return false;
  }

}
