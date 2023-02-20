package com.cameronm.scheduleconsult.models;

/**
 * The NamedEntity adds common attributes to all database entities
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public abstract class NamedEntity {

    /**
     * The id of the named entity
     */
    private int id;

    /**
     * The name of the named entity
     */
    private String name;

    /**
     * The getId method returns the id of the named entity
     *
     * @return returns the id
     */
    public int getId() {
        return id;
    }

    /**
     * The setId method sets the id of the named entity
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
}
