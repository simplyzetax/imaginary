package com.github.simplyzetax.imaginary.specifications.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class Specification {

    private LivingEntity entity;

    /**
     * Constructs an EntitySpecification with the given LivingEntity.
     *
     * @param entity the LivingEntity to specify
     */
    public Specification(LivingEntity entity) {
        this.entity = entity;
    }

    /**
     * Serializes the LivingEntity by storing its UUID as a string.
     *
     * @return the UUID of the entity as a string
     */
    public String serialize() {
        if (entity == null) {
            throw new IllegalStateException("Entity is null and cannot be serialized.");
        }
        return entity.getUniqueId().toString();
    }

    /**
     * Deserializes the given string to retrieve the corresponding LivingEntity.
     *
     * @param entityUUID the UUID string of the entity to deserialize
     * @return an EntitySpecification containing the LivingEntity
     * @throws IllegalArgumentException if the UUID is invalid or the entity is not found or not a LivingEntity
     */
    public static Specification deserialize(String entityUUID) {
        if (entityUUID == null || entityUUID.isEmpty()) {
            throw new IllegalArgumentException("The entity UUID string cannot be null or empty.");
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(entityUUID);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID string: " + entityUUID, e);
        }

        Entity foundEntity = Bukkit.getEntity(uuid);
        if (foundEntity == null) {
            throw new IllegalArgumentException("No entity found with UUID: " + entityUUID);
        }

        if (!(foundEntity instanceof LivingEntity)) {
            throw new IllegalArgumentException("Entity with UUID " + entityUUID + " is not a LivingEntity.");
        }

        return new Specification((LivingEntity) foundEntity);
    }

    /**
     * Retrieves the LivingEntity associated with this specification.
     *
     * @return the LivingEntity
     */
    public LivingEntity getEntity() {
        return entity;
    }
}