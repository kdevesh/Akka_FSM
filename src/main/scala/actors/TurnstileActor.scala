package actors

import akka.actor.LoggingFSM
import messages.{InsertCoin, PushBar}

/**
  * Created by gianlo.fagiolo on 10/06/2016.
  */

class TurnstileActor extends LoggingFSM[TurnstileStatus, Coins] {
  startWith(Locked, Coins(0))
  when(Locked) {
    case Event(InsertCoin,_) =>
      goto(Unlocked).using(stateData.addOne)
    case Event(PushBar, _) => {
      println("Currently number of coins in turnstile"+stateData.getCoins)
      println(s"Seriously Buddy? Before you can pass you need to insert a coin! I'm currently $stateName.")
      stay()
    }
  }

  when(Unlocked) {
    case Event(InsertCoin, _) => println("No need of wasting money the turnstile is unlocked")
      println("Currently number of coins in turnstile"+stateData.getCoins)
      stay()
    case Event(PushBar, _) => {
      println("Currently number of coins in turnstile"+stateData.getCoins)
      goto(Locked)
    }
  }

  initialize()
}

sealed trait TurnstileStatus

case object Locked extends TurnstileStatus

case object Unlocked extends TurnstileStatus

case class Coins(total: Int) {
  def addOne = Coins(total + 1)
  def getCoins= total
}

