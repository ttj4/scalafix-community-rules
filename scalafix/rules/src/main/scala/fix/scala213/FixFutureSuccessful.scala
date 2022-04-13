package fix.scala213

import scalafix.v1._

import scala.meta.Term

class FixFutureSuccessful extends SyntacticRule("FixFutureSuccessful") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Term.Apply
          if t.fun.syntax == "Future.successful" && t.args.isEmpty =>
        Patch.replaceTree(t, "Future.successful(())").atomic
    }.asPatch
  }

  override def description: String =
    "Replace 'Future.successful()' with 'Future.successful(())'"

  override def isRewrite: Boolean = true
}
