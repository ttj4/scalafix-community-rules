package fix.scala213

import scalafix.v1._

import scala.meta.Term

class FixUnit extends SyntacticRule("FixUnit") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Term.Name if t.value == "Unit" => Patch.replaceTree(t, "()")
    }.asPatch
  }

  override def description: String = "Replace Unit with ()"

  override def isRewrite: Boolean = true
}
