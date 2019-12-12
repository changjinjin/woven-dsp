package com.merce.woven.dsp.data.sys.initialize;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.merce.woven.dsp.data.sys.entity.security.Permission;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "roots")
@XmlType(name = "PermRoots", propOrder = {"roots"})
public class PermRoots {

    @XmlElement(name = "root")
    private List<Permission> roots;

    public PermRoots() {
    }

    public PermRoots(List<Permission> roots) {
        this.roots = roots;
    }
}
