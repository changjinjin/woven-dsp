package com.info.baymax.dsp.access.platform.web.controller.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import com.info.baymax.dsp.data.sys.entity.security.Role;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import com.info.baymax.dsp.data.sys.service.security.RestOperationService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 菜单信息查询接口
 *
 * @author jingwei.yang
 * @date 2019年11月14日 下午3:32:23
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "系统管理：菜单管理", value = "菜单管理接口")
public class MenuController implements Serializable {
    private static final long serialVersionUID = -8130399587077298234L;

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RestOperationService restOperationService;

    @ApiOperation(value = "加载登录用户的菜单列表")
    @PostMapping("/loadMenus")
    public Response<List<Menu>> loadMenus() {
        User currentUser = userService.findByTenantAndUsername(SaasContext.getCurrentTenantName(),
            SaasContext.getCurrentUsername());
        List<Permission> roots = Lists.newArrayList();
        if (currentUser.admin()) {
            roots = permissionService.findRootsTree();
        } else {
            List<Role> roles = currentUser.getRoles();
            Set<String> permIds = Sets.newHashSet();
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

    @ApiOperation(value = "查询所有的系统接口信息", hidden = true)
    @PostMapping("/fetchRestOperations")
    @ApiIgnore
    public List<RestOperation> fetchRestOperations(@ApiParam(value = "查询条件") @RequestBody RestOperation t) {
        return restOperationService.select(t);
    }

}
