package fix.scala213

import scalafix.v1._

import scala.meta.Lit

class FixSymbolLiterals extends SyntacticRule("FixSymbolLiterals") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Lit.Symbol =>
        Patch.replaceTree(t, s"""Symbol("${t.value.name}")""")
    }.asPatch
  }

  override def description: String =
    "Replace symbol literals to Symbol.apply. Ex: 'right to Symbol(\"right\")"

  override def isRewrite: Boolean = true
}
