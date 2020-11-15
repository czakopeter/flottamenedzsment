package com.flotta.entity.record;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BasicEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BasicEntity other = (BasicEntity) obj;
    if (id != other.id)
      return false;
    return true;
  }
  
}
