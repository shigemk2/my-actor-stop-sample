package com.example

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.pattern.ask

object Hello {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("mySystem")

    // アクターを生成
    val props = Props[MyActor]
    val actor = system.actorOf(props, name = "myActor")
    Thread.sleep(1000)

    // (子スレッドの) 停止を要請
    implicit val timeout = Timeout(5 seconds)
    // '?' の暗黙の引数として必要な情報です。
    val future: Future[Any] = actor ? "kill"
    // val future: Future[String] = ask(actor, "kill").mapTo[String] // としても同じです。

    // 同期して待つ (非同期ではない。ここで処理が停止)
    val result = Await.result(future, timeout.duration).asInstanceOf[String]
    // val result = Await.result(future, timeout.duration) // ↑の後者の場合は String に変換済み。

    println(result)

    // アクターの停止
    system.stop(actor)
    Thread.sleep(1000)

    // システムの停止 (`system.shutdown` は非推奨になりました。)
    system.terminate()
  }
}
