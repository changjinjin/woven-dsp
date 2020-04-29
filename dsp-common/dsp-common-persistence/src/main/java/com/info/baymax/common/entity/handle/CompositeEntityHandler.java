package com.info.baymax.common.entity.handle;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CompositeEntityHandler implements EntityHandler {

    private final List<EntityHandler> handlers;

    public CompositeEntityHandler(List<EntityHandler> handlers) {
        this.handlers = new ArrayList<EntityHandler>(handlers);
        AnnotationAwareOrderComparator.sort(this.handlers);
    }

    public CompositeEntityHandler(EntityHandler... handlers) {
        this(Arrays.asList(handlers));
    }

    @Override
    public void handle(Object t) {
        for (EntityHandler handler : handlers) {
            if (handler.supports(t)) {
                handler.handle(t);
                return;
            }
        }
    }

    @Override
    public boolean supports(Object t) {
        return true;
    }

    public void addEntityHandler(EntityHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Entity handler is null.");
        }
        addEntityHandlers(Arrays.asList(handler));
    }

    public void addEntityHandlers(EntityHandler... handlers) {
        addEntityHandlers(Arrays.asList(handlers));
    }

    public void addEntityHandlers(Collection<? extends EntityHandler> handlers) {
        if (handlers == null || handlers.isEmpty()) {
            throw new IllegalArgumentException("Entity handlers is null or empty.");
        }
        this.handlers.addAll(handlers);
        AnnotationAwareOrderComparator.sort(this.handlers);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
