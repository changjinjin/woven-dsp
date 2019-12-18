package com.info.baymax.dsp.access.platform.web.controller.menu;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.entity.security.Role;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import com.info.baymax.dsp.data.sys.service.security.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 菜单信息查询接口
 *
 * @author jingwei.yang
 * @date 2019年11月14日 下午3:32:23
 */
@RestController
@RequestMapping("/api/menu")
@Api(tags = "系统管理：菜单管理", value = "菜单管理接口")
public class MenuController implements Serializable {
    private static final long serialVersionUID = -8130399587077298234L;

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "加载登录用户的菜单列表")
    @PostMapping("/loadMenus")
    public Response<List<Menu>> save() {
        User currentUser = userService.findByTenantAndUsername(SaasContext.getCurrentTenantName(),
            SaasContext.getCurrentUsername());
        List<Permission> roots = Lists.newArrayList();
        if (currentUser.admin()) {
            roots = permissionService.findRootsTree();
        } else {
            List<Role> roles = currentUser.getRoles();
            Set<Long> permIds = Sets.newHashSet();
            if (ICollections.hasElements(roles)) {
                roles.forEach(t -> {
                    permIds.addAll(t.getPermIds());
                });
                roots = permissionService.fetchTree(permissionService.selectByPrimaryKeys(permIds));
            }
        }

        List<Menu> menus = Lists.newArrayList();
        if (ICollections.hasElements(roots)) {
            for (Permission p : roots) {
                Menu from = Menu.from(p);
                if (from != null) {
                    menus.add(from);
                }
            }
        }
        return Response.ok(menus);
    }

}
