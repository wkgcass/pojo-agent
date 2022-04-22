package io.vproxy.pojoagent.api.template;

public interface PojoUpdateFrom<T extends PojoUpdateFrom<T>> {
    void updateFrom(T another);

    default void preUpdateFrom(T another) {
    }

    default void postUpdateFrom(T another) {
    }
}
