package fix.scala213

import scalafix.v1._

import scala.meta.Term

class FixExplicitRight extends SyntacticRule("FixExplicitRight") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Term.Select if t.name.value == "right" =>
        val tokens = t.tokens
        if(tokens.size > 1) {
          val dot = tokens(tokens.size - 2)
          val right = tokens(tokens.size - 1)
          if(dot.text == "." && right.text == "right") {
            Patch.removeTokens(Iterable(dot, right))
          } else Patch.empty
        } else Patch.empty
    }.asPatch
  }

  override def description: String = "Remove explicit '.right' calls"

  override def isRewrite: Boolean = true
}
