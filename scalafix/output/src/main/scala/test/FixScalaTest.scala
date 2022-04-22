package test

import org.scalatest.DontDelete
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

object org {
  object scalatest {
    case object FreeSpec
    case object Matchers
    case object DontDelete
    object matchers {
      object should {
        case object Matchers
      }
    }
    object freespec {
      case object AnyFreeSpec
    }
  }
}

trait Matchers
trait FreeSpec
trait AnyFreeSpec

class SomeTest extends AnyFreeSpec with Matchers