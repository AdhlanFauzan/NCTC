package com.netcracker.rest;

import com.netcracker.classes.OrderJson;
import com.netcracker.classes.Point;
import com.netcracker.entity.OrderEntity;
import com.netcracker.entity.OrderStateEntity;
import com.netcracker.entity.PathEntity;
import com.netcracker.facade.local_int.Order;
import com.netcracker.facade.local_int.OrderState;
import com.netcracker.facade.local_int.User;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ubuntu on 19.05.2015.
 */
@Path("driver")
public class DriverRest {

    @EJB
    private Order order;

    @EJB
    private OrderState orderState;

    @EJB
    private User user;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    @POST
    @Path("getQueuedOrders")
    public Response getQueuedOrders() {
        List<OrderEntity> list = order.getOrdersByState(orderState.findByName("queued"));
        Collections.sort(list, (o1, o2) -> o2.getTimeCreated().toString().compareTo(o1.getTimeCreated().toString()));
        StringBuilder sb = new StringBuilder();
        sb.append("{\"orders\":[");
        for (OrderEntity orderEntity : list) {
            List<Point> points = order.getFirstAndLastPoints(orderEntity);
            sb.append("{\"startOrder\":\"")
                    .append(points.get(0).getAddress())
                    .append("\",\"endOrder\":\"")
                    .append(points.get(1).getAddress())
                    .append("\",\"dateOrderCreate\":\"")
                    .append(orderEntity.getTimeCreated().toString())
                    .append("\",\"id\":\"")
                    .append(orderEntity.getId())
                    .append("\",\"distance\":\"")
                    .append(orderEntity.getTotalLength())
                    .append("\",\"price\":\"")
                    .append(orderEntity.getFinalPrice())
                    .append("\" },");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]}");
        if (!list.isEmpty()) {
            return Response.status(200).entity(sb.toString()).build();
        } else {
            return Response.status(404).entity("Bad response.").build();
        }
    }

    @POST
    @Path("getAssignedOrders")
    @Consumes("text/plain")
    public Response getAssignedOrders(String uuid) {
        List<OrderEntity> list = order.getOrdersByStateAndDriverUuid(orderState.findByName("assigned"), uuid);
        Collections.sort(list, (o1, o2) -> o2.getTimeCreated().toString().compareTo(o1.getTimeCreated().toString()));
        StringBuilder sb = new StringBuilder();
        sb.append("{\"orders\":[");
        for (OrderEntity orderEntity : list) {
            List<Point> points = order.getFirstAndLastPoints(orderEntity);
            sb.append("{\"startOrder\":\"")
                    .append(points.get(0).getAddress())
                    .append("\",\"endOrder\":\"")
                    .append(points.get(1).getAddress())
                    .append("\",\"dateOrderCreate\":\"")
                    .append(orderEntity.getTimeCreated().toString())
                    .append("\",\"id\":\"")
                    .append(orderEntity.getId())
                    .append("\",\"distance\":\"")
                    .append(orderEntity.getTotalLength())
                    .append("\",\"price\":\"")
                    .append(orderEntity.getFinalPrice())
                    .append("\" },");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]}");
        if (!list.isEmpty()) {
            return Response.status(200).entity(sb.toString()).build();
        } else {
            return Response.status(204).entity("Bad response.").build();
        }
    }

    @POST
    @Path("historyByLength")
    @Consumes("text/plain")
    public Response getOrderHistoryByLength(String uuid) {
        List<OrderEntity> list = order.getOrdersByStateAndDriverUuid(orderState.findByName("completed"), uuid);
        list.addAll(order.getOrdersByStateAndDriverUuid(orderState.findByName("refused"), uuid));
        Collections.sort(list, (o1, o2) -> o2.getTotalLength().compareTo(o1.getTotalLength()));
        StringBuilder sb = new StringBuilder();
        sb.append("{\"orderHistory\":[");
        for (OrderEntity orderEntity : list) {
            List<Point> points = order.getFirstAndLastPoints(orderEntity);
            sb.append("{\"startOrder\":\"")
                    .append(points.get(0).getAddress())
                    .append("\",\"endOrder\":\"")
                    .append(points.get(1).getAddress())
                    .append("\",\"dateOrderCreate\":\"")
                    .append(orderEntity.getTimeCreated().toString())
                    .append("\",\"id\":\"")
                    .append(orderEntity.getId())
                    .append("\",\"distance\":\"")
                    .append(orderEntity.getTotalLength())
                    .append("\",\"price\":\"")
                    .append(orderEntity.getFinalPrice())
                    .append("\" },");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]}");

        if (!list.isEmpty()) {
            return Response.status(200).entity(sb.toString()).build();
        } else {
            return Response.status(404).entity("Bad response.").build();
        }
    }

    @POST
    @Path("historyByDate")
    @Consumes("text/plain")
    public Response getOrderHistoryByDate(String uuid) {
        List<OrderEntity> list = order.getOrdersByStateAndDriverUuid(orderState.findByName("completed"), uuid);
        list.addAll(order.getOrdersByStateAndDriverUuid(orderState.findByName("refused"), uuid));
        Collections.sort(list, (o1, o2) -> o2.getTimeCreated().toString().compareTo(o1.getTimeCreated().toString()));
        StringBuilder sb = new StringBuilder();
        sb.append("{\"orderHistory\":[");
        for (OrderEntity orderEntity : list) {
            List<Point> points = order.getFirstAndLastPoints(orderEntity);
            sb.append("{\"startOrder\":\"")
                    .append(points.get(0).getAddress())
                    .append("\",\"endOrder\":\"")
                    .append(points.get(1).getAddress())
                    .append("\",\"dateOrderCreate\":\"")
                    .append(orderEntity.getTimeCreated().toString())
                    .append("\",\"id\":\"")
                    .append(orderEntity.getId())
                    .append("\",\"distance\":\"")
                    .append(orderEntity.getTotalLength())
                    .append("\",\"price\":\"")
                    .append(orderEntity.getFinalPrice())
                    .append("\" },");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]}");

        if (!list.isEmpty()) {
            return Response.status(200).entity(sb.toString()).build();
        } else {
            return Response.status(404).entity("Bad response.").build();
        }
    }

    @POST
    @Path("historyByPrice")
    @Consumes("text/plain")
    public Response getOrderHistoryByPrice(String uuid) {
        List<OrderEntity> list = order.getOrdersByStateAndDriverUuid(orderState.findByName("completed"), uuid);
        list.addAll(order.getOrdersByStateAndDriverUuid(orderState.findByName("refused"), uuid));
        Collections.sort(list, (o1, o2) -> o2.getFinalPrice().compareTo(o1.getFinalPrice()));
        StringBuilder sb = new StringBuilder();
        sb.append("{\"orderHistory\":[");
        for (OrderEntity orderEntity : list) {
            List<Point> points = order.getFirstAndLastPoints(orderEntity);
            sb.append("{\"startOrder\":\"")
                    .append(points.get(0).getAddress())
                    .append("\",\"endOrder\":\"")
                    .append(points.get(1).getAddress())
                    .append("\",\"dateOrderCreate\":\"")
                    .append(orderEntity.getTimeCreated().toString())
                    .append("\",\"id\":\"")
                    .append(orderEntity.getId())
                    .append("\",\"distance\":\"")
                    .append(orderEntity.getTotalLength())
                    .append("\",\"price\":\"")
                    .append(orderEntity.getFinalPrice())
                    .append("\" },");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]}");

        if (!list.isEmpty()) {
            return Response.status(200).entity(sb.toString()).build();
        } else {
            return Response.status(404).entity("Bad response.").build();
        }
    }

    @POST
    @Path("assign")
    @Consumes("application/json")
    public Response getOrderAssign(OrderJson orderJson) {
        OrderEntity orderEntity = order.read(new BigInteger(orderJson.getId()));
        orderEntity.setDriverUserEntity(user.findByUuid(orderJson.getDriverUserUuid()));
        try {
            orderEntity.setTimeOfDriverArrival(new Timestamp(
                    simpleDateFormat.parse(orderJson.getTimeOfDriverArrival()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        orderEntity.setOrderStateEntity(orderState.findByName("assigned"));
        order.update(orderEntity);

        if (orderEntity != null) {
            return Response.status(200).entity("You assign on this Order.").build();
        } else {
            return Response.status(404).entity("Bad response order not assign.").build();
        }
    }

    @POST
    @Path("inProgress")
    @Consumes("application/json")
    public Response getOrderInProgress(OrderJson orderJson) {
        OrderEntity orderEntity = order.read(new BigInteger(orderJson.getId()));
        if(orderEntity.getOrderStateEntity().getName() == orderState.findByName("assigned").getName()){

            OrderStateEntity orderStateEntity = orderState.findByName("in progress");
            List<OrderEntity> orderEntities = order.getOrdersByStateAndDriverUuid(orderStateEntity, orderJson.getDriverUserUuid());
            if(orderEntities.isEmpty()) {
                orderEntity.setOrderStateEntity(orderState.findByName("in progress"));
                orderEntity.setTimeStarted(new Timestamp(new Date().getTime()));
                order.update(orderEntity);
                return Response.status(200).entity("You start this Order.").build();
            } else return Response.status(201).entity("Already you have order in progress.").build();
        } else {
            if (orderEntity != null) {
                return Response.status(201).entity("This order not assigned is now.").build();
            } else {
                return Response.status(404).entity("Bad response. Order not found in DB.").build();
            }
        }
    }
}