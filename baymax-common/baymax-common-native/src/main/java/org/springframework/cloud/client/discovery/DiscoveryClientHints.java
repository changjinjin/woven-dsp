package org.springframework.cloud.client.discovery;

import org.springframework.nativex.extension.NativeImageConfiguration;
import org.springframework.nativex.extension.NativeImageHint;
import org.springframework.nativex.extension.TypeInfo;

@NativeImageHint(trigger = EnableDiscoveryClientImportSelector.class, typeInfos = {
		@TypeInfo(types = { EnableDiscoveryClient.class }) }, applyToFunctional = false)
public class DiscoveryClientHints implements NativeImageConfiguration {
}