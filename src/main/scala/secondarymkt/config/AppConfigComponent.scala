package secondarymkt.config

import com.typesafe.config._
import com.typesafe.scalalogging.LazyLogging

trait AppConfigComponent {

  import AppConfigComponent._

  val appConfigService: AppConfigService

  /** Load config settings from application.conf
    * https://lightbend.github.io/config/latest/api/com/typesafe/config/Config.html	
    */
  //noinspection SpellCheckingInspectio  class AppConfigService extends LazyLogging {
  //-Dconfig.resource=production.conf for overriding
  class AppConfigService extends LazyLogging {
    //-Dconfig.resource=production.conf for overriding
    private val conf = ConfigFactory.load()

    private val appConf = conf.getConfig("akka-mongo") // will throw if not present  / wrong type
    private val db = appConf.getConfig("db")
    val host: Option[String] = db.getOptionalString("host")
    val port: Option[Int] = db.getOptionalInt("port")
    //val dbUsername: String = db.getString("username")
    //val dbPassword: String = db.getString("password")
  }
}


object AppConfigComponent {


  implicit class RichConfig(val underlying: Config) extends AnyVal {

    def getOptionalBoolean(path: String): Option[Boolean] = if (underlying.hasPath(path)) {
      Some(underlying.getBoolean(path))
    } else {
      None
    }

    def getOptionalString(path: String): Option[String] = if (underlying.hasPath(path)) {
      Some(underlying.getString(path))
    } else {
      None
    }

    def getOptionalInt(path: String): Option[Int] = if (underlying.hasPath(path)) {
      Some(underlying.getInt(path))
    } else {
      None
    }


  }
}
 
