package com.info.baymax.dsp.data.sys.initialize;

import com.info.baymax.dsp.data.sys.entity.security.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "roots")
@XmlType(name = "PermRoots", propOrder = {"roots"})
public class PermRoots {

    @XmlElement(name = "root")
    private List<Permission> roots;
}
