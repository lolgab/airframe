package wvlet.airspec

import wvlet.airframe.{Design, LazyF0}
import wvlet.airframe.surface.Surface

/**
  */
private[airspec] trait AirSpecSpiCompat { self: AirSpecSpi =>

  /**
    * This will add Scala.js support to the AirSpec.
    *
    * Scala.js does not support runtime reflection, so the user needs to
    * explicitly create Seq[MethodSurface] at compile-time.
    * This method is a helper method to populate methodSurfaces automatically.
    */
  protected def scalaJsSupport: Unit = ???
}

private[airspec] object AirSpecSpiCompat {
  import scala.quoted._

//  def scalaJsSupportImpl(self: Expr[AirSpecSpi])(using Quotes): Expr[Unit] = {
//    '{ if(${self}.isScalaJs) {
//        ${self}._methodSurfaces = Surface.methodsOf[A]
//      }
//    }
//    import c.universe._
//    val t = c.prefix.actualType.typeSymbol
//    q"if(wvlet.airspec.compat.isScalaJs) { ${c.prefix}._methodSurfaces = wvlet.airframe.surface.Surface.methodsOf[${t}] }"
//  }
//
}


class AirSpecTestBuilder(val spec: AirSpecSpi, val name: String, val design: Design) extends wvlet.log.LogSupport {
  inline def apply[R](body: => R): Unit = ${ AirSpecMacros.test0Impl[R]('this, 'body) }
  inline def apply[D1, R](body: D1 => R): Unit = ${ AirSpecMacros.test1Impl[R, D1]('this, 'body) }
  inline def apply[D1, D2, R](body: (D1, D2) => R): Unit = ${ AirSpecMacros.test2Impl[R, D1, D2]('this, 'body) }
  inline def apply[D1, D2, D3, R](body: (D1, D2, D3) => R): Unit = ${ AirSpecMacros.test3Impl[R, D1, D2, D3]('this, 'body) }
  inline def apply[D1, D2, D3, D4, R](body: (D1, D2, D3, D4) => R): Unit = ${ AirSpecMacros.test4Impl[R, D1, D2, D3, D4]('this, 'body) }
  inline def apply[D1, D2, D3, D4, D5, R](body: (D1, D2, D3, D4, D5) => R): Unit = ${ AirSpecMacros.test5Impl[R, D1, D2, D3, D4, D5]('this, 'body) }
}

object AirSpecTestBuilder extends wvlet.log.LogSupport {
  implicit class Helper(val v: AirSpecTestBuilder) extends AnyVal {

    def addF0[R](r: Surface, body: wvlet.airframe.LazyF0[R]): Unit = {
      v.spec.addLocalTestDef(AirSpecDefF0(v.name, v.design, r, body))
    }
    def addF1[D1, R](d1: Surface, r: Surface, body: D1 => R): Unit = {
      v.spec.addLocalTestDef(AirSpecDefF1(v.name, v.design, d1, r, body))
    }
    def addF2[D1, D2, R](d1: Surface, d2: Surface, r: Surface, body: (D1, D2) => R): Unit = {
      v.spec.addLocalTestDef(AirSpecDefF2(v.name, v.design, d1, d2, r, body))
    }
    def addF3[D1, D2, D3, R](d1: Surface, d2: Surface, d3: Surface, r: Surface, body: (D1, D2, D3) => R): Unit = {
      v.spec.addLocalTestDef(AirSpecDefF3(v.name, v.design, d1, d2, d3, r, body))
    }
    def addF4[D1, D2, D3, D4, R](
            d1: Surface,
            d2: Surface,
            d3: Surface,
            d4: Surface,
            r: Surface,
            body: (D1, D2, D3, D4) => R
    ): Unit = {
      v.spec.addLocalTestDef(AirSpecDefF4(v.name, v.design, d1, d2, d3, d4, r, body))
    }
    def addF5[D1, D2, D3, D4, D5, R](
            d1: Surface,
            d2: Surface,
            d3: Surface,
            d4: Surface,
            d5: Surface,
            r: Surface,
            body: (D1, D2, D3, D4, D5) => R
    ): Unit = {
      v.spec.addLocalTestDef(AirSpecDefF5(v.name, v.design, d1, d2, d3, d4, d5, r, body))
    }
  }
}


trait AirSpecContextCompat {

  /**
    * Build an instance of type A using Airframe DI, and run the test method within A.
    * @return the generated instance of A
    */
  def test[A <: AirSpecBase]: A = ???

  /**
    * Run the test methods in a given AirSpec instance
    */
  def run[A <: AirSpecBase](spec: A): A = ???

}

private[airspec] object AirSpecMacros {
  import scala.quoted._

  def test0Impl[R](self: Expr[AirSpecTestBuilder], body: Expr[ => R])(using Type[R], Quotes): Expr[Unit] = {
    '{
      import AirSpecTestBuilder._
      ${self}.addF0(Surface.of[R], LazyF0(${body}))
    }
  }

  def test1Impl[R, D1](self: Expr[AirSpecTestBuilder], body: Expr[D1 => R])(using Type[R], Type[D1], Quotes): Expr[Unit] = {
    '{
      import AirSpecTestBuilder._
      ${self}.addF1(Surface.of[D1], Surface.of[R], LazyF0(${body}))
    }
  }

  def test2Impl[R, D1, D2](self: Expr[AirSpecTestBuilder], body: Expr[(D1, D2) => R])(using Type[R], Type[D1], Type[D2], Quotes): Expr[Unit] = {
    '{
      import AirSpecTestBuilder._
      ${self}.addF2(Surface.of[D1], Surface.of[D2], Surface.of[R], LazyF0(${body}))
    }
  }

  def test3Impl[R, D1, D2, D3](self: Expr[AirSpecTestBuilder], body: Expr[(D1, D2, D3) => R])(using Type[R], Type[D1], Type[D2], Quotes): Expr[Unit] = {
    '{
      import AirSpecTestBuilder._
      ${self}.addF3(Surface.of[D1], Surface.of[D2], Surface.of[D3], Surface.of[R], LazyF0(${body}))
    }
  }

  def test4Impl[R, D1, D2, D3, D4](self: Expr[AirSpecTestBuilder], body: Expr[(D1, D2, D3, D4) => R])(using Type[R], Type[D1], Type[D2], Quotes): Expr[Unit] = {
    '{
      import AirSpecTestBuilder._
      ${self}.addF4(Surface.of[D1], Surface.of[D2], Surface.of[D3], Surface.of[D4], Surface.of[R], LazyF0(${body}))
    }
  }

  def test5Impl[R, D1, D2, D3, D4, D5](self: Expr[AirSpecTestBuilder], body: Expr[(D1, D2, D3, D4, D5) => R])(using Type[R], Type[D1], Type[D2], Quotes): Expr[Unit] = {
    '{
      import AirSpecTestBuilder._
      ${self}.addF4(Surface.of[D1], Surface.of[D2], Surface.of[D3], Surface.of[D4], Surface.of[D5], Surface.of[R], LazyF0(${body}))
    }
  }
}
