package com.motyldrogi.bot.entity;

public interface CounterEntity extends Entity<Long> {
    
    String getName();

    void setName(String name);

    Integer getValue();

    void setValue(Integer value);

}
