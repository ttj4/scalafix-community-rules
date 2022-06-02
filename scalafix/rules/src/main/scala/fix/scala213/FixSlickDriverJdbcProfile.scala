package fix.scala213

import scalafix.v1._
import scala.meta.{Importee, Importer, Source, _}

class FixSlickDriverJdbcProfile extends SyntacticRule("FixSlickDriverJdbcProfile") {

  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case i: Importee.Name =>
        if (i.name.value == "JdbcProfile") {
          i.parent match {
            case Some(importer: Importer) if importer.ref.syntax == "slick.driver" =>
              Patch.replaceTree(importer, "slick.jdbc.JdbcProfile")
            case _ => Patch.empty
          }
        } else Patch.empty
    }.asPatch
  }

  override def description: String = "Replace slick.driver.JdbcProfile with slick.jdbc.JdbcProfile"

  override def isRewrite: Boolean = true
}