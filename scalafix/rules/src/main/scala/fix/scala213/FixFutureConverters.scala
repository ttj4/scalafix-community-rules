package fix.scala213

import scalafix.v1._

import scala.meta.Importer
import scala.meta.Term.Select

final class FixFutureConverters
    extends SyntacticRule("FixFutureConverters") {
  override def fix(
    implicit doc: SyntacticDocument
  ): _root_.scalafix.v1.Patch = {
    doc.tree.collect {
      case t: Importer =>
        if (t.ref.syntax == "scala.compat.java8.FutureConverters" && t.importees.size == 1 && t.importees.head.syntax == "_") {
          Patch.replaceTree(t, "scala.jdk.FutureConverters._")
        } else Patch.empty
      case t: Select if t.name.value == "toScala" => Patch.replaceTree(t.name, "asScala")
    }.asPatch
  }

  override def description: String =
    "Converts import import scala.compat.java8.FutureConverters._ to import scala.jdk.FutureConverters._"

  override def isRewrite: Boolean = true
}
