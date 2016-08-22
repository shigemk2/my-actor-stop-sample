package com.example

import akka.actor.{Actor, ActorRef, Props, Terminated}

class MyActor extends Actor {
  val child = context.actorOf(Props[MyActor2], name = "myChild")
  context.watch(child)

  val lastSender: ActorRef = context.system.deadLetters

  def receive = {
    case "kill" => {
      context.stop(child)
      lastSender = sender
    }
    case Terminated(`child`) => {
      lastSender ! "finished"
    }
    case _ => {
      context.stop(self)
    }
  }
}
