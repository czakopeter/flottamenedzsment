package com.flotta.invoice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.entity.switchTable.CompanyDataDescriptionCategoryCouplerST;

@Entity
@Table(name = "companyData")
public class CompanyData {
  
  @Id
  @GeneratedValue
  private long id;
  
  private String name;
  
  private String city;
  
  @OneToMany(mappedBy = "companyData", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, CompanyDataDescriptionCategoryCouplerST> descriptionCategoryCouplers = new HashMap<>();
  
  public CompanyData() {}

  public CompanyData(String name, String city) {
    this.name = name;
    this.city = city;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Map<LocalDate, CompanyDataDescriptionCategoryCouplerST> getDescriptionCategoryCoupler() {
    return descriptionCategoryCouplers;
  }

  public void setDescriptionCategoryCoupler(Map<LocalDate, CompanyDataDescriptionCategoryCouplerST> descriptionCategoryCoupler) {
    if(descriptionCategoryCoupler == null) {
      this.descriptionCategoryCouplers = new HashMap<>();
    } else {
      this.descriptionCategoryCouplers = descriptionCategoryCoupler;
    }
  }

}
