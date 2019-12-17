package com.info.baymax.dsp.data.sys.initialize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.info.baymax.common.utils.JaxbUtils;
import com.info.baymax.dsp.data.sys.entity.security.Permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "clients")
@XmlType(name = "PermClients", propOrder = {"clients"})
public class PermClients {

    @XmlElement(name = "client")
    private List<PermClient> clients;

    public static void main(String[] args) throws FileNotFoundException, JAXBException {
        PermClients clients = new PermClients(
            Arrays.asList(new PermClient("baymax", Arrays.asList(new Permission("p1", "/p1", 1, null)))));
        JaxbUtils.java2xml(new File("d:/1.xml"), clients, PermClients.class);
    }
}
