// Runtime: Java 17
package com.cgtfarmer.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.cgtfarmer.demo.config.DependencyGraph;
import com.cgtfarmer.demo.controller.UserController;
import com.cgtfarmer.demo.exception.ExceptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Lambda Handler.
 *
 * Note: RequestHandler<X, Y>
 *   X = event type
 *   Y = return type
 */
public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  @Override
  public APIGatewayV2HTTPResponse handleRequest(
      APIGatewayV2HTTPEvent event,
      Context context
  ) {
    LambdaLogger logger = context.getLogger();

    DependencyGraph graph;
    try {
      graph = DependencyGraph.getInstance();
    } catch (Exception e) {
      logger.log(
          "Failed to initialize dependency graph: " +
          ExceptionUtils.mapStackTraceToString(e)
      );

      APIGatewayV2HTTPResponse response = APIGatewayV2HTTPResponse.builder()
          .withStatusCode(500)
          .withBody(e.getMessage())
          .build();

      logger.log(response.toString());

      return response;
    }

    UserController userController = graph.getUserController();

    APIGatewayV2HTTPResponse response;
    try {
      response = this.processRequest(event, userController, logger);
    } catch (Exception e) {
      logger.log(
          "Failed to process request: " +
          ExceptionUtils.mapStackTraceToString(e)
      );

      response = APIGatewayV2HTTPResponse.builder()
          .withStatusCode(500)
          .withBody(e.getMessage())
          .build();
    }

    logger.log(response.toString());
    return response;
  }

  private APIGatewayV2HTTPResponse processRequest(
      APIGatewayV2HTTPEvent event,
      UserController userController,
      LambdaLogger logger
  ) throws JsonProcessingException {

    String eventRouteKey = event.getRouteKey();

    logger.log("ROUTE: " + eventRouteKey);

    APIGatewayV2HTTPResponse response = null;
    switch (eventRouteKey) {
      case "GET /users":
        response = userController.index(event);
        break;

      case "POST /users":
        response = userController.create(event);
        break;

      case "GET /users/{id}":
        response = userController.show(event);
        break;

      case "PUT /users/{id}":
        response = userController.update(event);
        break;

      case "DELETE /users/{id}":
        response = userController.destroy(event);
        break;

      default:
        String msg = ("Unsupported route: " + eventRouteKey);
        logger.log(msg);

        response = APIGatewayV2HTTPResponse.builder()
            .withStatusCode(400)
            .withBody(msg)
            .build();
    }

    return response;
  }
}
