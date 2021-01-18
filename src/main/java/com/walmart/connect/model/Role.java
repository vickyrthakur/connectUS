package com.walmart.connect.model;

public enum Role {

    IN1(1),
    IN2(2),
    IN3(3),
    IN4(4),
    IN5(5),
    IN6(6),
    HM(7);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isHigherThan(Role role) {
        return this.level > role.level;
    }
}
