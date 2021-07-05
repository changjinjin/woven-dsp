package com.info.baymax.common.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * create by pengchuan.chen on 2020/3/9
 */
@Getter
@Setter
public class HdfsFile {
  private String path;
  private String name;
  private String owner;
  private String permission;
  private long len;
  private long modificationTime;
  private boolean dir;

  public HdfsFile(String path, String name, String owner, String permission, long len, long modificationTime, boolean dir) {
    this.path = path;
    this.name = name;
    this.owner = owner;
    this.permission = permission;
    this.len = len;
    this.modificationTime = modificationTime;
    this.dir = dir;
  }

  @Override
  public String toString() {
    return "HdfsFile{path='" + this.path + '\'' + ", name='" + this.name + ", owner='" + this.owner
            + '\'' + ", permission='" + this.permission + ", len=" + this.len
            + ", modificationTime=" + this.modificationTime + ", dir=" + this.dir + '}';
  }
}
