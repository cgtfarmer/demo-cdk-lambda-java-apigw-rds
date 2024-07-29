package com.cgtfarmer.demo.controller;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.cgtfarmer.demo.mapper.UserMapper;
import com.cgtfarmer.demo.model.User;
import com.cgtfarmer.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {

  private final UserService userService;

  private final UserMapper userMapper;

  private final Map<String, String> headers;

  public UserController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;

    this.headers = new HashMap<>();
    this.headers.put("Content-Type", "application/json");
  }

  public APIGatewayV2HTTPResponse index(APIGatewayV2HTTPEvent event)
      throws JsonProcessingException {

    System.out.println("[UserController#index]");

    List<User> users = this.userService.findAll();

    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withHeaders(this.headers)
        .withBody(this.userMapper.toString(users))
        .build();
  }

  public APIGatewayV2HTTPResponse create(APIGatewayV2HTTPEvent event)
      throws JsonProcessingException {

    System.out.println("[UserController#create]");

    String eventBody = event.getBody();

    System.out.println("[UserController#create] " + eventBody);

    User user = this.userMapper.fromString(eventBody);

    User newUser = this.userService.create(user);

    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(201)
        .withHeaders(this.headers)
        .withBody(this.userMapper.toString(newUser))
        .build();
  }

  public APIGatewayV2HTTPResponse show(APIGatewayV2HTTPEvent event)
      throws JsonProcessingException {

    System.out.println("[UserController#show]");

    Map<String, String> eventPathParameters = event.getPathParameters();

    String rawId = eventPathParameters.get("id");

    int id = Integer.parseInt(rawId);

    System.out.println("[UserController#show] " + id);

    User user = this.userService.findById(id);

    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withHeaders(this.headers)
        .withBody(this.userMapper.toString(user))
        .build();
  }

  public APIGatewayV2HTTPResponse update(APIGatewayV2HTTPEvent event)
      throws JsonProcessingException {

    System.out.println("[UserController#update]");

    Map<String, String> eventPathParameters = event.getPathParameters();
    String rawId = eventPathParameters.get("id");
    int id = Integer.parseInt(rawId);

    String eventBody = event.getBody();

    System.out.println("[UserController#update] id=" + id + " body=" + eventBody);

    User user = this.userMapper.fromString(eventBody);
    user.setId(id);

    User newUser = this.userService.update(user);

    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withHeaders(this.headers)
        .withBody(this.userMapper.toString(newUser))
        .build();
  }

  public APIGatewayV2HTTPResponse destroy(APIGatewayV2HTTPEvent event)
      throws JsonProcessingException {

    System.out.println("[UserController#destroy]");

    Map<String, String> eventPathParameters = event.getPathParameters();

    String rawId = eventPathParameters.get("id");

    int id = Integer.parseInt(rawId);

    System.out.println("[UserController#destroy] " + id);

    this.userService.destroy(id);

    return APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withHeaders(this.headers)
        .withBody(String.format("{ \"message:\" \"ID: %d deleted successfully\" }", id))
        .build();
  }
}
