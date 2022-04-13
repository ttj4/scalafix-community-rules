package fix.scala213

import scalafix.v1._

import scala.meta.Lit

class FixLowerCaseEl extends SyntacticRule("FixLowerCaseEl") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Lit.Long if t.toString().endsWith("l") =>
        Patch.replaceTree(t, t.syntax.replace('l', 'L'))
    }.asPatch
  }

  override def description: String = "Convert 2l to 2L"

  override def isRewrite: Boolean = true
}
