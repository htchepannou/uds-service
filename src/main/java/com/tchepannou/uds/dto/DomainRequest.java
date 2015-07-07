package com.tchepannou.uds.dto;

import org.hibernate.validator.constraints.NotBlank;

public class DomainRequest {
    @NotBlank (message = "name")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
