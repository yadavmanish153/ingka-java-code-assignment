package com.fulfilment.application.monolith.fulfilment;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Path("fulfilment")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FulfilmentResource {

  @Inject FulfilmentService fulfilmentService;

  private static final Logger LOGGER = Logger.getLogger(FulfilmentResource.class.getName());

  public static class FulfilmentRequest {
    public Long storeId;
    public Long productId;
    public Long warehouseId;
  }

  public static class FulfilmentResponse {
    public Long id;
    public Long storeId;
    public Long productId;
    public Long warehouseId;
  }

  @POST
  @Transactional
  public Response create(FulfilmentRequest request) {
    if (request.storeId == null || request.productId == null || request.warehouseId == null) {
      throw new WebApplicationException("storeId, productId and warehouseId are required", 422);
    }

    try {
      FulfilmentAssignment assignment =
          fulfilmentService.assignWarehouseToProductInStore(
              request.storeId, request.productId, request.warehouseId);

      FulfilmentResponse response = new FulfilmentResponse();
      response.id = assignment.id;
      response.storeId = assignment.store.id;
      response.productId = assignment.product.id;
      response.warehouseId = assignment.warehouse.id;

      return Response.ok(response).status(201).build();
    } catch (IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 404);
    } catch (IllegalStateException e) {
      throw new WebApplicationException(e.getMessage(), 400);
    }
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Inject ObjectMapper objectMapper;

    @Override
    public Response toResponse(Exception exception) {
      LOGGER.error("Failed to handle request", exception);

      int code = 500;
      if (exception instanceof WebApplicationException) {
        code = ((WebApplicationException) exception).getResponse().getStatus();
      }

      ObjectNode exceptionJson = objectMapper.createObjectNode();
      exceptionJson.put("exceptionType", exception.getClass().getName());
      exceptionJson.put("code", code);

      if (exception.getMessage() != null) {
        exceptionJson.put("error", exception.getMessage());
      }

      return Response.status(code).entity(exceptionJson).build();
    }
  }
}
