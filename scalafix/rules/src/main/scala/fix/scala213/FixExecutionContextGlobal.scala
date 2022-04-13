package fix.scala213

import scalafix.v1._

import scala.meta.Term

class FixExecutionContextGlobal
    extends SyntacticRule("FixExecutionContextGlobal") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Term.Select if t.syntax == "ExecutionContext.Implicts.global" =>
        Patch.replaceTree(t, "ExecutionContext.global")
    }.asPatch
  }

  override def description: String =
    "Replace ExecutionContext.Implicts.global with ExecutionContext.global"

  override def isRewrite: Boolean = true
}
