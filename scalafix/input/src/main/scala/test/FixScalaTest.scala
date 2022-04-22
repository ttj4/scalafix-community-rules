/*
rule = FixScalaTest
 */
package test

import org.scalatest.{FreeSpec, Matchers, DontDelete}

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

class SomeTest extends FreeSpec with Matchers