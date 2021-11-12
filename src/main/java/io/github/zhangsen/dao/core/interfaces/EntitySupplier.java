package io.github.zhangsen.dao.core.interfaces;

@FunctionalInterface
public interface EntitySupplier<Id,T> {
    T get(Id id);
}
