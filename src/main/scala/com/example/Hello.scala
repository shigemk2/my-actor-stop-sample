package com.example

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.pattern.ask

object Hello {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("mySystem")

    val props = Props[MyActor]
    val actor = system.actorOf(props, name = "myActor")
    Thread.sleep(1000)

    implicit val timeout = Timeout(5 seconds)
    val future: Future[Any] = actor ? "kill"

    val result = Await.result(future, timeout.duration).asInstanceOf[String]

    println(result)

    system.stop(actor)
    Thread.sleep(1000)

    system.terminate()
  }
}
