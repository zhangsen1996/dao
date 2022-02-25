package io.github.zhangsen.mongodb.core.interfaces;

@FunctionalInterface
public interface EntitySupplier<Id,T> {
    T get(Id id);
}
