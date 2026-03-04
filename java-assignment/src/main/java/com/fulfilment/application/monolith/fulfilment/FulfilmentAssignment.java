package com.fulfilment.application.monolith.fulfilment;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fulfilment_assignment")
@Cacheable
public class FulfilmentAssignment {

  @Id
  @GeneratedValue
  public Long id;

  @ManyToOne(optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  public Store store;

  @ManyToOne(optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  public Product product;

  @ManyToOne(optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  public DbWarehouse warehouse;

  public FulfilmentAssignment() {}
}
