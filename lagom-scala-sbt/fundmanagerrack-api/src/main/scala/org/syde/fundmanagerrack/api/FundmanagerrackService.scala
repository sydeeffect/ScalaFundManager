package org.syde.fundmanagerrack.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

/**
  * The FundManagerRack service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the FundmanagerrackService.
  */

trait FundmanagerrackService extends Service {

  /**
    * Example: curl http://localhost:9000/api/hello/Alice
    */
  def hello(id: String): ServiceCall[NotUsed, Player]
  def deposit(): ServiceCall[DepositMoneyMessage, Response]
  def getBalance(): ServiceCall[GetBalanceRequest, GetBalanceResponse]
  def buy(): ServiceCall[BuyRequest, BuyResponse]
  def sell(): ServiceCall[SellRequest, SellResponse]


  override final def descriptor: Descriptor = {
    import Service._
    // @formatter:off
    named("fundmanagerrack")
      .withCalls(
        restCall(Method.GET,"/api/hello/:id", hello _),
        restCall(Method.POST,"/api/deposit/money/:id", deposit _),
        restCall(Method.POST,"/api/balance", getBalance _),
        restCall(Method.POST,"/api/buy", buy _),
        restCall(Method.POST,"/api/sell", sell _),
       // TODO: Completet
       // restCall(Method.GET,"/api/transactions", hello _),
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}
  case class Player(name: String)
  object Player {
      implicit val format: Format[Player] = Json.format
  }

  case class DepositMoneyMessage(amount: Float, customerID: Int)
  object DepositMoneyMessage {
      implicit val format: Format[DepositMoneyMessage] = Json.format
  }

  case class Response(message: String)
  object Response{
      implicit val format: Format[Response] = Json.format
  }

/**
  * The message class.
  */
case class GreetingMessage(message: String)
case class GetBalanceRequest(customerID: Int)
object GetBalanceRequest{
    implicit val format: Format[GetBalanceRequest] = Json.format
}

case class GetBalanceResponse(sum: Float, assetWorth: Float)
object GetBalanceResponse{
    implicit val format: Format[GetBalanceResponse] = Json.format
}

case class SellRequest(customerID: Int, isin: String, stocks:Float)
object SellRequest{
    implicit val format: Format[SellRequest] = Json.format
}

case class SellResponse(message: String)
object SellResponse{
    implicit val format: Format[SellResponse] = Json.format
}

case class BuyRequest(customerID: Int, isin: String, amount:Float)
object BuyRequest{
    implicit val format: Format[BuyRequest] = Json.format
}

case class BuyResponse(message: String)
object BuyResponse{
    implicit val format: Format[BuyResponse] = Json.format
}
