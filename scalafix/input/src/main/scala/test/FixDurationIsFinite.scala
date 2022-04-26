/*
rule = FixDurationIsFinite
 */
package test

trait Duration {
  def isFinite() : Boolean
}

object SomeRandomObject {
  def withTimeout(timeout: Duration): Boolean = {
    if (timeout.isFinite()) true else false
  }
}