package com.info.baymax.dsp.data.sys.initialize;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.info.baymax.dsp.data.sys.entity.security.Permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "client")
@XmlType(name = "PermClient", propOrder = {"clientId", "roots"})
public class PermClient {

    @XmlElement
    private String clientId;

    @XmlElementWrapper(name = "roots")
    @XmlElement(name = "root")
    private List<Permission> roots;
}
