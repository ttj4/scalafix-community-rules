package fix.scala213

import java.util.regex.Pattern

import scalafix.v1._

import scala.meta.Term

class FixPostfixSyntax extends SyntacticRule("FixPostfixSyntax") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Term.Select =>
        val pattern = Pattern.compile(
          s"${Pattern.quote(t.qual.syntax)}\\s+${Pattern.quote(t.name.syntax)}"
        )
        if (pattern.matcher(t.toString()).matches()) {
          Patch.replaceTree(t, s"${t.qual.syntax}.${t.name.syntax}")
        } else Patch.empty
    }.asPatch
  }

  override def description: String =
    "Fix postfix syntax. Ex: '2 second' to '2.second'"

  override def isRewrite: Boolean = true
}
