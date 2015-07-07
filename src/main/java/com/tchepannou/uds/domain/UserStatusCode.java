package com.tchepannou.uds.domain;

public class UserStatusCode extends PersistentEnum {
    private boolean active;
    private boolean defaultStatus;

    public boolean isDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(boolean defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
