package fix.scala213

import scalafix.v1._

import scala.meta.{Importee, Importer, Term}

class FixMockitoMatchers extends SyntacticRule("FixMockitoMatchers") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t: Importee.Name =>
        if(t.name.value == "Matchers") {
          t.parent match {
            case Some(importer: Importer) if importer.ref.syntax == "org.mockito" =>
              if(importer.toString().contains("{")) {
                Patch.addLeft(t.name.tokens.head, "ArgumentMatchers => ")
              } else {
                Patch.addLeft(t.name.tokens.head, "{ ArgumentMatchers => ") + Patch.addRight(t.name.tokens.last, " }")
              }
            case _ => Patch.empty
          }
        } else Patch.empty
      case t: Importee.Rename =>
        if(t.name.value == "Matchers") {
          t.parent match {
            case Some(importer: Importer) if importer.ref.syntax == "org.mockito" =>
              Patch.replaceTree(t.name, "ArgumentMatchers")
            case _ => Patch.empty
          }
        } else Patch.empty
      case t: Term.Select if t.name.value == "Matchers" && t.qual.syntax == "org.mockito" => Patch.replaceTree(t.name, "ArgumentMatchers")
    }.asPatch
  }

  override def description: String = "Replace org.mockito.Matchers with org.mockito.ArgumentMatchers"

  override def isRewrite: Boolean = true
}
