package com.fulfilment.application.monolith.fulfilment;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FulfilmentService {

  @Inject FulfilmentAssignmentRepository assignmentRepository;

  @Inject ProductRepository productRepository;

  @Inject WarehouseRepository warehouseRepository;

  public FulfilmentAssignment assignWarehouseToProductInStore(
      Long storeId, Long productId, Long warehouseId) {

    Store store = Store.findById(storeId);
    if (store == null) {
      throw new IllegalArgumentException("Store not found");
    }

    Product product = productRepository.findById(productId);
    if (product == null) {
      throw new IllegalArgumentException("Product not found");
    }

    DbWarehouse warehouse = warehouseRepository.findById(warehouseId);
    if (warehouse == null || warehouse.getArchivedAt() != null) {
      throw new IllegalArgumentException("Warehouse not found");
    }

    FulfilmentAssignment existing =
        assignmentRepository
            .find("store = ?1 and product = ?2 and warehouse = ?3", store, product, warehouse)
            .firstResult();

    if (existing != null) {
      // Idempotent: association already exists
      return existing;
    }

    // Constraint 1: Each Product can be fulfilled by a maximum of 2 different Warehouses per Store
    List<FulfilmentAssignment> byStoreAndProduct =
        assignmentRepository.list("store = ?1 and product = ?2", store, product);
    Set<Long> warehousesForProductInStore =
        byStoreAndProduct.stream()
            .map(a -> a.warehouse.id)
            .collect(Collectors.toSet());
    if (!warehousesForProductInStore.contains(warehouse.id)
        && warehousesForProductInStore.size() >= 2) {
      throw new IllegalStateException(
          "Each product can be fulfilled by at most 2 warehouses per store");
    }

    // Constraint 2: Each Store can be fulfilled by a maximum of 3 different Warehouses
    List<FulfilmentAssignment> byStore = assignmentRepository.list("store = ?1", store);
    Set<Long> warehousesForStore =
        byStore.stream().map(a -> a.warehouse.id).collect(Collectors.toSet());
    if (!warehousesForStore.contains(warehouse.id) && warehousesForStore.size() >= 3) {
      throw new IllegalStateException("Each store can be fulfilled by at most 3 warehouses");
    }

    // Constraint 3: Each Warehouse can store maximally 5 types of Products
    List<FulfilmentAssignment> byWarehouse =
        assignmentRepository.list("warehouse = ?1", warehouse);
    Set<Long> productsForWarehouse =
        byWarehouse.stream().map(a -> a.product.id).collect(Collectors.toSet());
    if (!productsForWarehouse.contains(product.id) && productsForWarehouse.size() >= 5) {
      throw new IllegalStateException("Each warehouse can store at most 5 products");
    }

    FulfilmentAssignment assignment = new FulfilmentAssignment();
    assignment.store = store;
    assignment.product = product;
    assignment.warehouse = warehouse;

    assignmentRepository.persist(assignment);

    return assignment;
  }
}
