package fix.scala213

import scalafix.v1._
import scala.meta.{Importee, Importer, Init, Template, Term, Name}

class FixScalaTest extends SyntacticRule("FixScalaTest") {

  val scalaTest = Map(
    "FunSuiteLike" -> "org.scalatest.funsuite.AnyFunSuiteLike",
    "FunSuite" -> "org.scalatest.funsuite.AnyFunSuite",
    "AsyncFunSuiteLike" -> "org.scalatest.funsuite.AsyncFunSuiteLike",
    "AsyncFunSuite" -> "org.scalatest.funsuite.AsyncFunSuite",
    "FeatureSpecLike" -> "org.scalatest.featurespec.AnyFeatureSpecLike",
    "FeatureSpec" -> "org.scalatest.featurespec.AnyFeatureSpec",
    "AsyncFeatureSpecLike" -> "org.scalatest.featurespec.AsyncFeatureSpecLike",
    "AsyncFeatureSpec" -> "org.scalatest.featurespec.AsyncFeatureSpec",
    "FunSpecLike" -> "org.scalatest.funspec.AnyFunSpecLike",
    "FunSpec" -> "org.scalatest.funspec.AnyFunSpec",
    "AsyncFunSpecLike" -> "org.scalatest.funspec.AsyncFunSpecLike",
    "AsyncFunSpec" -> "org.scalatest.funspec.AsyncFunSpec",
    "FreeSpecLike" -> "org.scalatest.freespec.AnyFreeSpecLike",
    "FreeSpec" -> "org.scalatest.freespec.AnyFreeSpec",
    "AsyncFreeSpecLike" -> "org.scalatest.freespec.AsyncFreeSpecLike",
    "AsyncFreeSpec" -> "org.scalatest.freespec.AsyncFreeSpec",
    "FlatSpecLike" -> "org.scalatest.flatspec.AnyFlatSpecLike",
    "FlatSpec" -> "org.scalatest.flatspec.AnyFlatSpec",
    "AsyncFlatSpecLike" -> "org.scalatest.flatspec.AsyncFlatSpecLike",
    "AsyncFlatSpec" -> "org.scalatest.flatspec.AsyncFlatSpec",
    "PropSpecLike" -> "org.scalatest.propspec.AnyPropSpecLike",
    "PropSpec" -> "org.scalatest.propspec.AnyPropSpec",
    "WordSpecLike" -> "org.scalatest.wordspec.AnyWordSpecLike",
    "WordSpec" -> "org.scalatest.wordspec.AnyWordSpec",
    "AsyncWordSpecLike" -> "org.scalatest.wordspec.AsyncWordSpecLike",
    "AsyncWordSpec" -> "org.scalatest.wordspec.AsyncWordSpec",
    "Matchers" -> "org.scalatest.matchers.should.Matchers",
    "MustMatchers" -> "org.scalatest.matchers.must.Matchers",
    "DiagrammedAssertions" -> "org.scalatest.diagrams.Diagrams",
  )

  private def getInitName(name: String): String = {
    scalaTest
      .get(name)
      .map(_.split("\\.").last)
      .getOrElse(name)
  }

  private def buildImport(orgiStr: String): Patch = {

    val parts = orgiStr.split("\\.")
    if (parts.size == 3) {
      Patch.addGlobalImport(Importer(
        Term.Select(
          Term.Name(parts(0)), Term.Name(parts(1))
        ), List(Importee.Name(Name(parts(3))))
      ))
    } else if (parts.size == 4) {
      Patch.addGlobalImport(Importer(
        Term.Select(
          Term.Select(
            Term.Name(parts(0)), Term.Name(parts(1))
          ), Term.Name(parts(2))
        ), List(Importee.Name(Name(parts(3))))
      ))
    } else if (parts.size == 5) {
      Patch.addGlobalImport(Importer(
        Term.Select(
          Term.Select(
            Term.Select(
              Term.Name(parts(0)), Term.Name(parts(1))
            ), Term.Name(parts(2))
          ), Term.Name(parts(3))
        ), List(Importee.Name(Name(parts(4))))
      ))
    } else {
      Patch.empty
    }
  }

  override def fix(implicit doc: SyntacticDocument): Patch = {

    doc.tree.collect {
      case ip: Importer if ip.ref.syntax == "org.scalatest" =>
        ip.importees.collect {
          case ipt: Importee if scalaTest.contains(ipt.syntax) =>
            Iterable(
              Patch.removeImportee(ipt),
              buildImport(scalaTest(ipt.syntax))
            ).asPatch
        }.asPatch
      case temp: Template =>
        temp.inits.collect {
          case ini if scalaTest.contains(ini.syntax) =>
            Patch.replaceTree(ini, getInitName(ini.syntax))
        }.asPatch
    }.asPatch
  }

  override def description: String = "Replace grouped scalatest and fix deprecated inheretence"

  override def isRewrite: Boolean = true
}