package com.anju.residence.enums;

/**
 * 业务类型枚举类
 *
 * @author cygao
 * @date 2021/2/20 6:31 下午
 **/
public enum OperationType {

  /**
   *
   */
  OPERATION(0),

  LOGIN(1),

  ADD(2),

  UPDATE(3),

  DELETE(4);

  private final int type;

  OperationType(int type) {
    this.type = type;
  }


  public int getType() {
    return type;
  }
}
