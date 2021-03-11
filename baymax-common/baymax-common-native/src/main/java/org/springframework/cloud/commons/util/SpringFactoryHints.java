package org.springframework.cloud.commons.util;

import org.springframework.nativex.extension.NativeImageConfiguration;
import org.springframework.nativex.extension.NativeImageHint;
import org.springframework.nativex.extension.TypeInfo;

@NativeImageHint(trigger = SpringFactoryImportSelector.class, follow = true, typeInfos = {
		@TypeInfo(types = { Class.class, ClassLoader.class }) })
public class SpringFactoryHints implements NativeImageConfiguration {
}
