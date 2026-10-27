package org.hedgetech.fairylightsredux.server.fastener;

@FunctionalInterface
public interface IFastenerHolder {
    Fastener<?> getFastener();
}
