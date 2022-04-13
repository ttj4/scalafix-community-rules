package fix.scala213

import scalafix.v1._

import scala.meta.Import
import scala.meta.Mod.Annot

class HandleScalaTest extends SyntacticRule("HandleScalaTest") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Import =>
        if (t.importers.size == 1 && t.importers.head.importees.size == 1) {
          val syn = t.importers.head.syntax
          if (syn == "org.scalatest.junit.JUnitRunner" || syn == "org.junit.runner.RunWith") {
            Patch.replaceTree(t, "")
          } else if (syn == "org.scalatest.mock.MockitoSugar") {
            Patch.replaceTree(t, "import org.scalatestplus.mockito.MockitoSugar")
          } else Patch.empty
        } else Patch.empty
      case t: Annot =>
        if (t.syntax == "@RunWith(classOf[JUnitRunner])") {
          Patch.replaceTree(t, "")
        } else Patch.empty
    }.asPatch
  }

  override def description: String =
    "Handles scalatest JUnitRunner & MockitSugar"

  override def isRewrite: Boolean = true
}
