package com.flotta.entity.switchTable;

import com.flotta.entity.record.BasicEntity;

public class GenSW <V1 extends BasicEntity, V2 extends BasicEntity> {
  
  private V1 v1;
  
  private V2 v2;

  public V1 getV1() {
    return v1;
  }

  public void setV1(V1 v1) {
    this.v1 = v1;
  }

  public V2 getV2() {
    return v2;
  }

  public void setV2(V2 v2) {
    this.v2 = v2;
  }

}
