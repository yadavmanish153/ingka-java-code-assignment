package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
import com.warehouse.api.WarehouseResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;

  @Inject private CreateWarehouseUseCase createWarehouseUseCase;

  @Inject private ReplaceWarehouseUseCase replaceWarehouseUseCase;

  @Inject private ArchiveWarehouseUseCase archiveWarehouseUseCase;

  @Override
  public List<com.warehouse.api.beans.Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  @Transactional
  public com.warehouse.api.beans.Warehouse createANewWarehouseUnit(
      @NotNull com.warehouse.api.beans.Warehouse data) {
    Warehouse warehouse = toDomain(data);

    try {
      createWarehouseUseCase.create(warehouse);
    } catch (IllegalArgumentException | IllegalStateException e) {
      throw new WebApplicationException(e.getMessage(), 400);
    }

    return toWarehouseResponse(warehouse);
  }

  @Override
  public com.warehouse.api.beans.Warehouse getAWarehouseUnitByID(String id) {
    Long warehouseId;
    try {
      warehouseId = Long.valueOf(id);
    } catch (NumberFormatException e) {
      throw new WebApplicationException("Invalid warehouse id", 400);
    }

    var dbWarehouse = warehouseRepository.findById(warehouseId);

    if (dbWarehouse == null || dbWarehouse.getArchivedAt() != null) {
      throw new WebApplicationException("Warehouse not found", 404);
    }

    return toWarehouseResponse(dbWarehouse.toWarehouse());
  }

  @Override
  @Transactional
  public void archiveAWarehouseUnitByID(String id) {
    Long warehouseId;
    try {
      warehouseId = Long.valueOf(id);
    } catch (NumberFormatException e) {
      throw new WebApplicationException("Invalid warehouse id", 400);
    }

    var dbWarehouse = warehouseRepository.findById(warehouseId);

    if (dbWarehouse == null || dbWarehouse.getArchivedAt() != null) {
      throw new WebApplicationException("Warehouse not found", 404);
    }

    Warehouse warehouse = dbWarehouse.toWarehouse();

    try {
      archiveWarehouseUseCase.archive(warehouse);
    } catch (IllegalArgumentException | IllegalStateException e) {
      throw new WebApplicationException(e.getMessage(), 400);
    }
  }

  @Override
  @Transactional
    public com.warehouse.api.beans.Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode, @NotNull com.warehouse.api.beans.Warehouse data) {
    Warehouse newWarehouse = toDomain(data);
    newWarehouse.businessUnitCode = businessUnitCode;

    try {
      replaceWarehouseUseCase.replace(newWarehouse);
    } catch (IllegalArgumentException e) {
      // Treat as not found or invalid input
      throw new WebApplicationException(e.getMessage(), 404);
    } catch (IllegalStateException e) {
      throw new WebApplicationException(e.getMessage(), 400);
    }

    return toWarehouseResponse(newWarehouse);
  }

  private com.warehouse.api.beans.Warehouse toWarehouseResponse(Warehouse warehouse) {
    var response = new com.warehouse.api.beans.Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }

  private Warehouse toDomain(com.warehouse.api.beans.Warehouse dto) {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = dto.getBusinessUnitCode();
    warehouse.location = dto.getLocation();
    warehouse.capacity = dto.getCapacity();
    warehouse.stock = dto.getStock();
    return warehouse;
  }
}
