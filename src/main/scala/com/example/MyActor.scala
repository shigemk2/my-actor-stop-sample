package com.example

import akka.actor.{Actor, ActorRef, Props, Terminated}

class MyActor extends Actor {

  // 子スレッドを生成して監視。障害時の停止は検知できません。正常終了を検知します。
  val child = context.actorOf(Props[MyActor2], name = "myChild")
  context.watch(child)

  // 依頼元
  var lastSender: ActorRef = context.system.deadLetters // 既定は `/dev/null` 相当の deadLetters

  def receive = {
    case "kill" => {
      context.stop(child) // 停止処理
      lastSender = sender // 返信先
    }
    case Terminated(`child`) => {
      // https://www.qoosky.io/techs/c7921ddb98 に記載したとおり
      // 変数へのバインドを回避する目的でバッククォートで囲んでいます。
      lastSender ! "finished"
    }
    case _ => {
      context.stop(self) // 自分を停止することもできます。
    }
  }
}
