package hmda.census.api.http

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import hmda.api.http.HttpServer
import hmda.api.http.routes.BaseHttpApi

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object HmdaCensusQueryApi {
  def props(): Props = Props(new HmdaCensusQueryApi)
}

class HmdaCensusQueryApi
    extends HttpServer
    with BaseHttpApi
    with CensusQueryHttpApi {
  val config = ConfigFactory.load()
  override implicit val system: ActorSystem = context.system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val ec: ExecutionContext = context.dispatcher
  override val log = Logging(system, getClass)
  val duration = config.getInt("hmda.census.http.timeout").seconds
  println(duration)
  override implicit val timeout: Timeout = Timeout(duration)

  val createSchema = config.getBoolean("hmda.census.createSchema")
  override val name: String = "hmda-census-api"
  override val host: String = config.getString("hmda.census.http.host")
  override val port: Int = config.getInt("hmda.census.http.port")
  override val http: Future[Http.ServerBinding] = Http(system).bindAndHandle(
    paths,
    host,
    port
  )

  override val paths: Route = routes(s"$name") ~ censusRoutes
  http pipeTo self

}
