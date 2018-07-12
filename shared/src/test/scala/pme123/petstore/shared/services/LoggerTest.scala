package pme123.petstore.shared.services

import pme123.petstore.shared.services.LogLevel._

import scala.util.Try

/**
  * Created by pascal.mengelt on 05.03.2015.
  *
  */
class LoggerTest extends UnitTest {
  val logMsgPrefix = "this is the official test log message"
  val logParam1 = "testArg1"
  val logParam2 = "testArg2"
  val logMsg: String = s"$logMsgPrefix $logParam1 $logParam2"

  // Logger.debug
  {
    val msg = debug(logMsg)
    matchLogMsg(msg)
  }
  // Logger.info
  {
    val msg = info(logMsg)
    matchLogMsg(msg)
  }
  // Logger.warn
  {
    val msg = warn(logMsg)
    matchLogMsg(msg)
  }
  // Logger.error
  {
    val msg = error(logMsg)
    matchLogMsg(msg)
  }
  // Logger.error(Throwable)
  {
    val msg = error(new Exception(logMsgPrefix))
    it should "match the log message" in {
      msg should be(logMsgPrefix)
    }
  }
  // Logger.error(Throwable, msg)
  {
    val simpleMsg = "simple log"
    val msg = error(simpleMsg, new Exception(logMsgPrefix))

    it should "match the log message" in {
      msg should be(simpleMsg)
    }
  }
  // Logger.error(Throwable, msg, params)
  {
    val msg = error(logMsg,new Exception(logMsgPrefix))

    matchLogMsg(msg)
  }

  "AllErrorMsgs for a AdaptersException" should "be formatted in an expected format" in {
    exceptionToString(new Exception("configException")) should startWith(
      "configException ["
    )
  }


  it should "be formatted also correct for Exceptions with causes" in {
    val msg = exceptionToString(new Exception("configException", new Exception("firstCause", new Exception("secondCause"))))
      .split("\\n")
    msg.head should startWith("configException [")
    msg.tail.head should startWith(" - Cause: firstCause [")
    msg.last should startWith(" - Cause: secondCause [")
  }
  // LogEntry.asString
  {
    val msg = Logging.info(logMsg)
    "A LogEntry as string" should "be a nice readable text preceeded by the LogLevel" in {
      msg should be(s"INFO: $logMsgPrefix $logParam1 $logParam2")
    }
    it should "have no problems with UTF-8 encodings" in {
      Logging.info("12%\u00e4Tafelgetr\u00e4nke\nw") should be("INFO: 12%\u00e4Tafelgetr\u00e4nke\nw")
    }
  }
  // LogLevel.fromLevel
  {
    "A LogLevel DEBUG" should "be created from the String Debug" in {
      LogLevel.withNameInsensitive("Debug") should be(DEBUG)
    }
    "A LogLevel INFO" should "be created from the String Info" in {
      LogLevel.withNameInsensitive("Info") should be(INFO)
    }
    "A LogLevel WARN" should "be created from the String Warn" in {
      LogLevel.withNameInsensitive("Warn") should be(WARN)
    }
    "A LogLevel ERROR" should "be created from the String Error" in {
      LogLevel.withNameInsensitive("Error") should be(ERROR)
    }

    "An unsupported Level" should "return a Failure with an IllegalArgumentException." in {
      val badLevel = "autsch"
      val fromLevel = Try(LogLevel.withNameInsensitive(badLevel))
      assert(fromLevel.isFailure)
      fromLevel.failed.get.getMessage should be("autsch is not a member of Enum (DEBUG, INFO, WARN, ERROR)")
    }
  }
  // LogLevel >= logLevel
  {
    val correct = true
    val incorrect = false
    "A LogLevel DEBUG" should "be >= than DEBUG" in {
      DEBUG >= DEBUG should be(correct)
    }
    it should "be < than INFO" in {
      DEBUG >= INFO should be(incorrect)
    }
    it should "be < than WARN" in {
      DEBUG >= WARN should be(incorrect)
    }
    it should "be < than ERROR" in {
      DEBUG >= ERROR should be(incorrect)
    }
    "A LogLevel INFO" should "be >= than DEBUG" in {
      INFO >= DEBUG should be(correct)
    }
    "A LogLevel INFO" should "be >= than INFO" in {
      INFO >= INFO should be(correct)
    }
    it should "be < than WARN" in {
      INFO >= WARN should be(incorrect)
    }
    it should "be < than ERROR" in {
      INFO >= ERROR should be(incorrect)
    }
    "A LogLevel WARN" should "be >= than INFO" in {
      WARN >= INFO should be(correct)
    }
    it should "be >= than WARN" in {
      WARN >= WARN should be(correct)
    }
    it should "be < than ERROR" in {
      WARN >= ERROR should be(incorrect)
    }
    "A LogLevel ERROR" should "be >= than INFO" in {
      ERROR >= INFO should be(correct)
    }
    it should "be >= than WARN" in {
      ERROR >= WARN should be(correct)
    }
    it should "be >= than ERROR" in {
      ERROR >= ERROR should be(correct)
    }
  }

  private def matchLogMsg(msg:String): Unit =
    it should "match the log message" in {
      msg should be(logMsg)
    }

}
