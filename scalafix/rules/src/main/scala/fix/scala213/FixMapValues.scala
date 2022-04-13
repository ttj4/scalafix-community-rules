package fix.scala213

import scalafix.v1._

import scala.meta.Term

class FixMapValues extends SyntacticRule("FixMapValues") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Term.Apply =>
        t.fun match {
          case sel: Term.Select if sel.name.value == "mapValues" =>
            Patch.addLeft(sel.name.tokens.head, "view.") + Patch.addRight(t.tokens.last, ".toMap")
          case _ => Patch.empty
        }
    }.asPatch
  }

  override def description: String = "Rewrite _.mapValues(...) to _.view.mapValues(...).toMap"

  override def isRewrite: Boolean = true
}
