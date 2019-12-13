package com.info.baymax.dsp.auth.security.support.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.dsp.auth.security.exceptions.ClientIdDisabledException;
import com.info.baymax.dsp.auth.security.exceptions.ClientIdNotFoundException;
import com.info.baymax.dsp.auth.security.exceptions.LicenseNotActivatedException;
import com.info.baymax.dsp.auth.security.i18n.SecurityMessageSource;
import com.info.baymax.dsp.auth.security.license.LicenseInit;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.service.security.TenantService;

import de.schlichtherle.demo.LicenseVerifyDemo;
import de.schlichtherle.license.LicenseContent;

/**
 * 实现租户信息加载方法
 * 
 * @author: jingwei.yang
 * @date: 2019年4月22日 下午2:02:22
 */
@Service
public class TenantDetailsServiceImpl implements TenantDetailsService {

	@Autowired
	private TenantService tenantService;

	protected final MessageSourceAccessor messages = SecurityMessageSource.getAccessor();

	@Override
	public TenantDetails findByTenant(String tenantName) {
		LicenseContent content = new LicenseVerifyDemo().verify(LicenseInit.getParamers());
		if (content == null) {
			throw new LicenseNotActivatedException(
					this.messages.getMessage("ClientErr.licenseNotActivation", "License is not activation !"));
		}

		Tenant tenant = tenantService.findByName(tenantName);
		if (tenant == null) {
			throw new ClientIdNotFoundException(this.messages.getMessage("ClientErr.clientNotFound",
					new Object[] { tenantName }, "Client {0} not found"));
		}
		if (!YesNoType.YES.equalsTo(tenant.getEnabled())) {
			throw new ClientIdDisabledException(this.messages.getMessage("ClientErr.clientDisabled",
					new Object[] { tenantName }, "Client {0} disabled"));
		}
		return new SimpleTenantDetails(null, tenant.getName(), tenant.getVersion() + "");
	}

}
