package com.motyldrogi.bot.entity.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.motyldrogi.bot.entity.CounterEntity;
import com.motyldrogi.bot.util.Buildable;

@Entity
@Table(name = "counter")
public class CounterEntityImpl implements CounterEntity {

  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long identifier;
  
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer value;

  private CounterEntityImpl() {
  }

  private CounterEntityImpl(Builder builder) {
    this.identifier = builder.identifier;
    this.name = builder.name;
    this.value = builder.value;
  }

  public Long getIdentifier() {
    return identifier;
  }

  public void setIdentifier(Long identifier) {
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public static class Builder implements Buildable<CounterEntityImpl> {

    private Long identifier;
    private String name;
    private Integer value;
    
    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withIdentifier(Long identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder setValue(Integer value) {
      this.value = value;
      return this;
    }

    @Override
      public CounterEntityImpl build() {
      return new CounterEntityImpl(this);
    }

  }

}
