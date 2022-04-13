package fix.scala213

import scalafix.v1._

import scala.meta.Importer

final class FixCollectionConverters
    extends SyntacticRule("FixCollectionConverters") {
  override def fix(
    implicit doc: SyntacticDocument
  ): _root_.scalafix.v1.Patch = {
    doc.tree.collect {
      case t: Importer =>
        if (t.ref.syntax == "scala.collection.JavaConverters" && t.importees.size == 1 && t.importees.head.syntax == "_") {
          Patch.replaceTree(t, "scala.jdk.CollectionConverters._")
        } else Patch.empty
    }.asPatch
  }

  override def description: String =
    "Converts import scala.collection.JavaConverters._ to import scala.jdk.CollectionConverters._"

  override def isRewrite: Boolean = true
}
