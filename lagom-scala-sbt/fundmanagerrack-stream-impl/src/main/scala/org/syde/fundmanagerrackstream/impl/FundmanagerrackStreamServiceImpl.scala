package org.syde.fundmanagerrackstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.syde.fundmanagerrackstream.api.FundmanagerrackStreamService
import org.syde.fundmanagerrack.api.FundmanagerrackService

import scala.concurrent.Future

/**
  * Implementation of the FundmanagerrackStreamService.
  */
class FundmanagerrackStreamServiceImpl(fundmanagerrackService: FundmanagerrackService) extends FundmanagerrackStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(fundmanagerrackService.hello(_).invoke()))
  }
}
