package com.info.baymax.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * create by pengchuan.chen on 2020/3/11
 */
@Getter
@Setter
public class HdfsFilePermission {
  private String owner;
  private boolean execute;
  private boolean read;
  private boolean write;

  public HdfsFilePermission() {
  }

  public HdfsFilePermission(String owner, boolean read, boolean write, boolean execute) {
    this.owner = owner;
    this.execute = execute;
    this.read = read;
    this.write = write;
  }

}

