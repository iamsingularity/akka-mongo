package secondarymkt.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mongodb.casbah
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import org.joda.time._
import spray.json.DefaultJsonProtocol


case class Listing(
                    noteId: Long,
                    outstandingPrincipal: BigDecimal,
                    accruedInterest: BigDecimal,
                    loanStatus: String,
                    price: BigDecimal,
                    markupOrDiscount: BigDecimal,
                    yieldToMaturity: Option[BigDecimal],
                    daysSinceLastPayment: Int,
                    creditScoreTrend: String,
                    ficoEndRangeHigh: Int,
                    ficoEndRangeLow: Int,
                    listingStartDate: DateTime,
                    expirationDate: DateTime,
                    isNeverLate: Boolean,
                    subGrade: String,
                    term: Int,
                    originalNoteAmount: BigDecimal,
                    interestRate: BigDecimal,
                    remainingPayments: Int,
                    applicationType: String
                  )


// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  // TODO implicit val listingFormat = jsonFormat20(Listing)
}

object Listing {
  // implicit def read(): Listing = {
  // }
  // implicit def write(listing: Listing) = {
  // }

}

trait ListingsComponent {

  val listingsRepository: ListingsRepository


  trait ListingsRepository {
    def insert(listing: Listing)

    def find(): Traversable[Listing]
  }

  class ListingsMongoRepository()
    extends ListingsRepository {

    private val server = "localhost"
    private val port = 27017
    private val database = "secondaryMarket"
    private val mongoClient = casbah.MongoClient(server, port)
    private val db = mongoClient(database)
    private val collectionName = "listings"
    private val coll = db(collectionName)


    RegisterJodaTimeConversionHelpers()

    implicit def buildMongoObj(l: Listing): MongoDBObject = {
      val builder = MongoDBObject.newBuilder
      builder += "noteId" -> l.noteId
      builder += "outstandingPrincipal" -> l.outstandingPrincipal
      builder += "accruedInterest" -> l.accruedInterest
      builder += "loanStatus" -> l.loanStatus
      builder += "price" -> l.price
      builder += "markupOrDiscount" -> l.markupOrDiscount
      builder += "yieldToMaturity" -> l.yieldToMaturity
      builder += "daysSinceLastPayment" -> l.daysSinceLastPayment
      builder += "creditScoreTrend:" -> l.creditScoreTrend
      builder += "ficoEndRangeHigh" -> l.ficoEndRangeHigh
      builder += "ficoEndRangeLow" -> l.ficoEndRangeLow
      builder += "listingStartDate" -> l.listingStartDate
      builder += "expirationDate" -> l.expirationDate
      builder += "isNeverLate" -> l.isNeverLate
      builder += "subGrade" -> l.subGrade
      builder += "term" -> l.term
      builder += "originalNoteAmount" -> l.originalNoteAmount
      builder += "interestRate" -> l.interestRate
      builder += "remainingPayments" -> l.remainingPayments
      builder += "applicationType" -> l.applicationType
      builder.result
    }

    implicit def buildListing(l: MongoDBObject): Listing = {
      Listing(
        l.as[Long]("noteId"),
        l.as[BigDecimal]("outstandingPrincipal"),
        l.as[BigDecimal]("accruedInterest"),
        l.as[String]("loanStatus"),
        l.as[BigDecimal]("price"),
        l.as[BigDecimal]("markupOrDiscount"),
        l.as[Option[BigDecimal]]("yieldToMaturity"),
        l.as[Int]("daysSinceLastPayment"),
        l.as[String]("creditScoreTrend"),
        l.as[Int]("ficoEndRangeHigh"),
        l.as[Int]("ficoEndRangeLow"),
        l.as[DateTime]("listingStartDate"),
        l.as[DateTime]("expirationDate"),
        l.as[Boolean]("isNeverLate"),
        l.as[String]("subGrade"),
        l.as[Int]("term"),
        l.as[BigDecimal]("originalNoteAmount"),
        l.as[BigDecimal]("interestRate"),
        l.as[Int]("remainingPayments"),
        l.as[String]("applicationType")
      )
    }


    def insert(listing: Listing): Unit = {
      val mongoObj = MongoDBObject("hello" -> "world")
      coll.insert(mongoObj)
    }

    def findOne(): Option[Listing] = {
      val obj = MongoDBObject("hello" -> "world")
      val dbObj = coll.findOne(obj)
      dbObj
    }

    def find(): Traversable[Listing] = {
      coll.find()
    }

    // replace the existing one with the new document.
    def update(listing: Listing): Unit = {
      val obj = MongoDBObject("hello" -> "world")
      coll.findOne(obj)
    }

    // return number of documents removed
    def remove(listing: Listing): Int = {
      require(listing != null)

      val query = MongoDBObject("language" -> "clojure") //beware: blank object will drop all documents
      val result = coll.remove(query)
      result.getN
    }

    def removeAll(): Int = {
      val deletedCount = coll.count()
      coll.drop()
      deletedCount
    }


    def replaceCollection(listings: Iterator[Listing]) = {

      coll.drop()

      val builder = coll.initializeOrderedBulkOperation
      //      builder.insert(MongoDBObject("_id" -> 1))
      //      builder.insert(MongoDBObject("_id" -> 2))
      //      builder.insert(MongoDBObject("_id" -> 3))
      //
      //      builder.find(MongoDBObject("_id" -> 1)).updateOne($set("x" -> 2))
      //      builder.find(MongoDBObject("_id" -> 2)).removeOne()
      //      builder.find(MongoDBObject("_id" -> 3)).replaceOne(MongoDBObject("_id" -> 3, "x" -> 4))

      val result = builder.execute()
    }


  }

}
