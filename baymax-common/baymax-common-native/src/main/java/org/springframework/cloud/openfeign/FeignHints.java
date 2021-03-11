package org.springframework.cloud.openfeign;

import org.springframework.cloud.openfeign.FeignCircuitBreakerDisabledConditions.CircuitBreakerClassMissing;
import org.springframework.cloud.openfeign.FeignCircuitBreakerDisabledConditions.CircuitBreakerDisabled;
import org.springframework.nativex.extension.NativeImageConfiguration;
import org.springframework.nativex.extension.NativeImageHint;
import org.springframework.nativex.extension.TypeInfo;
import org.springframework.nativex.type.AccessBits;

@Deprecated
@NativeImageHint(trigger = FeignCircuitBreakerDisabledConditions.class, typeInfos = { @TypeInfo(types = {
		CircuitBreakerClassMissing.class,
		CircuitBreakerDisabled.class }, access = AccessBits.ALL) }, abortIfTypesMissing = true, applyToFunctional = false)
public class FeignHints implements NativeImageConfiguration {
}
