package org.syde.fundmanagerrackstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import org.syde.fundmanagerrackstream.api.FundmanagerrackStreamService
import org.syde.fundmanagerrack.api.FundmanagerrackService
import com.softwaremill.macwire._

class FundmanagerrackStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new FundmanagerrackStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new FundmanagerrackStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[FundmanagerrackStreamService])
}

abstract class FundmanagerrackStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[FundmanagerrackStreamService](wire[FundmanagerrackStreamServiceImpl])

  // Bind the FundmanagerrackService client
  lazy val fundmanagerrackService: FundmanagerrackService = serviceClient.implement[FundmanagerrackService]
}
