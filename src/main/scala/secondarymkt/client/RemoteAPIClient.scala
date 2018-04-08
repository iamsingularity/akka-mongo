package secondarymkt.client

import secondarymkt.models._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.util.ByteString
import com.typesafe.scalalogging._



trait RemoteApiClientComponent {

  val remoteApiClient: RemoteApiClient

  trait RemoteApiClient {
    // def fetch(): Future[Iterator[Listing]]
  }

  class RemoteApiClientImpl extends RemoteApiClient with LazyLogging {

    val listingsUrl = "https://api.lendingclub.com/api/investor/v1/secondarymarket/listings"
    implicit val system = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()


    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher


    /**
      * Code Description
      * 200 Everything	worked	as	expected
      * 400 Failed	validation	or	business	validation
      * 401 Unauthorized
      * 404 Incorrect	url(with	no	response	body),	No	data	found(with	a	response
      * body)
      * 500 Unexpected	failure,	wrong	http	method
      * 503 Service	unavailable
      * *
      * Supported	formats: CSV
      * For	the	Listings	GET	call,	you	must	include	the	Accept:	text/csv	header	and	exclude
      * the	Content-Type	header
      * *
      * Bad	Request	– unauthorized	user
      * {
      * "errors":	[
      * {
      * "code":	"unauthorized",
      * "msg":	"Operation	not	allowed."
      * }
      * ]
      * }
      * No	Data	found:
      * {"errors":	[{
      * "code":	"not-found",
      * "message":	"Item	does	not	exist"
      * }]}
      *
      *
      *
      *
      */
    def fetch()= {  // : Future[Iterator[Listing]]

      val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = listingsUrl))

      responseFuture.map {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
            logger.info("Got response, body: " + body.utf8String)
          }
        case resp@HttpResponse(code, _, _, _) =>
          logger.info("Request failed, response code: " + code)
          resp.discardEntityBytes()
      }
    }
  }

}

