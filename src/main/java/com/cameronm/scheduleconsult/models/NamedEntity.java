package com.cameronm.scheduleconsult.models;

/**
 * The NamedEntity adds common attributes to all database entities
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public abstract class NamedEntity {

    /**
     * The ID of the named entity
     */
    private int id;

    /**
     * The name of the named entity
     */
    private String name;

    /**
     * The constructor for the NameEntity class
     *
     * @param id   The ID of the named entity
     * @param name The name of the named entity
     */
    NamedEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * The getId method returns the id of the named entity
     *
     * @return returns the ID
     */
    public int getId() {
        return id;
    }

    /**
     * The setId method sets the ID of the named entity
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * The getName method returns the name of the named entity
     *
     * @return returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * The setName method sets the name of the named entity
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The equals method determines whether two objects are equal
     *
     * @param obj The object
     * @return Return boolean result determining if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NamedEntity other)) {
            return false;
        }
        return this.getId() == other.getId() && this.name.equals(other.name);
    }
}
