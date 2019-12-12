package org.syde.fundmanagerrack.impl

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class FundmanagerrackEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll {

  private val system = ActorSystem("FundmanagerrackEntitySpec",
    JsonSerializerRegistry.actorSystemSetupFor(FundmanagerrackSerializerRegistry))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  private def withTestDriver(block: PersistentEntityTestDriver[FundmanagerrackCommand[_], FundmanagerrackEvent, FundmanagerrackState] => Unit): Unit = {
    val driver = new PersistentEntityTestDriver(system, new FundmanagerrackEntity, "fundmanagerrack-1")
    block(driver)
    driver.getAllIssues should have size 0
  }

  "FundManagerRack entity" should {

    "say hello by default" in withTestDriver { driver =>
      val outcome = driver.run(Hello("Alice"))
      outcome.replies should contain only "Hello, Alice!"
    }

    "allow updating the greeting message" in withTestDriver { driver =>
      val outcome1 = driver.run(UseGreetingMessage("Hi"))
      outcome1.events should contain only GreetingMessageChanged("Hi")
      val outcome2 = driver.run(Hello("Alice"))
      outcome2.replies should contain only "Hi, Alice!"
    }

  }
}
