package net.api.forge.entity;

public interface ExtEntity {

    default boolean shouldRiderSit() {
        return true;
    }

}
