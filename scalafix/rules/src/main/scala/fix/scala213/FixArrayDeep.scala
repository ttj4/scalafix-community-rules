package fix.scala213

import scalafix.v1._

import scala.meta.Term

class FixArrayDeep extends SyntacticRule("FixArrayDeep") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Term.Select if t.name.value == "deep" =>
        Patch.replaceTree(t.name, "toIndexedSeq")
    }.asPatch
  }

  override def description: String = "Replace arr.deep to arr.toSeq"

  override def isRewrite: Boolean = true
}
