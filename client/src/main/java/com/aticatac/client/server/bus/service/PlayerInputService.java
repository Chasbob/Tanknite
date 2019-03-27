//package com.aticatac.client.server.bus.service;
//
//import com.aticatac.common.model.CommandModel;
//import com.aticatac.client.server.bus.EventBusFactory;
//import com.aticatac.client.server.bus.event.PlayerInputEvent;
//import com.google.common.eventbus.EventBus;
//
//public class PlayerInputService {
//  private EventBus bus = EventBusFactory.getEventBus();
//
//  public void onClientInput(CommandModel model) {
//    PlayerInputEvent event = new PlayerInputEvent(model);
//    bus.post(event);
//  }
//}
