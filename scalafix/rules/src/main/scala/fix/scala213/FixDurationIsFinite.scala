package fix.scala213

import scalafix.v1._

import scala.meta._

class FixDurationIsFinite extends SyntacticRule("FixDurationIsFinite") {

  override def description: String =
    "Remove brackets from accessor-like methods : _.isFinite() => _.isFinite"

  override def isRewrite: Boolean = true

  override def fix(
                    implicit doc: SyntacticDocument
                  ): _root_.scalafix.v1.Patch = {
    doc.tree.collect {
      case t: Term.Apply =>
        val name = t.fun match {
          case f: Term.Name   => Option(f.value)
          case f: Term.Select => Option(f.name).map(_.value)
          case _              => None
        }
        if (t.args.isEmpty && name.contains("isFinite")) {
          Patch.replaceTree(t, t.fun.syntax)
        } else Patch.empty
    }.asPatch
  }
}
