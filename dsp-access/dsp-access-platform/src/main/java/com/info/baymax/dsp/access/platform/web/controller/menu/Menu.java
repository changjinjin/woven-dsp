package com.info.baymax.dsp.access.platform.web.controller.menu;

import com.google.common.collect.Lists;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单信息
 *
 * @author jingwei.yang
 * @date 2019年11月14日 下午3:32:23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu implements Serializable {
    private static final long serialVersionUID = -8130399587077298234L;

    private String id;

    private String menuName;

    private String menuIcon;

    private String reg;

    private List<Menu> submenus;

    public Menu(String id, String menuName, String menuIcon, String reg) {
        this.id = id;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.reg = reg;
    }

    public static Menu from(Permission permission) {
        if (permission == null || permission.getEnabled() == null || permission.getEnabled() == 0) {
            return null;
        }
        Menu menu = new Menu(permission.getRoute(), permission.getName(), permission.getIcon(), permission.getUrl());
        List<Permission> children = permission.getChildren();

        List<Menu> menus = menu.getSubmenus();
        if (ICollections.hasElements(children)) {
            if (ICollections.hasNoElements(menus)) {
                menus = Lists.newArrayList();
                menu.setSubmenus(menus);
            }

            for (Permission p : children) {
                Menu from = Menu.from(p);
                if (from != null) {
                    menus.add(from);
                }
            }
        }
        return menu;
    }
}
