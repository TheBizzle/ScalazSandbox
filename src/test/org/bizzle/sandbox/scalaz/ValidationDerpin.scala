package org.bizzle.sandbox.scalaz

import
  org.scalatest.{ BeforeAndAfterEach, FunSuite, matchers },
    matchers.ShouldMatchers

import
  shapeless.{ :: => hcons, _ },
    Tuples._

import
  scalaz._,
    Scalaz._

/**
 * Created by IntelliJ IDEA.
 * User: Jason
 * Date: 5/15/12
 * Time: 11:27 PM
 */

class ValidationDerpin extends FunSuite with BeforeAndAfterEach with ShouldMatchers {

  type VII = ValidationNel[Int, Int]

  def refineNumOpt(guard: Int => Boolean) : (Int) => VII = (x: Int) => if (guard(x)) x.successNel[Int] else x.failNel
  def failNel(xs: Int*) : VII = NonEmptyList[Int](xs.head, xs.tail: _*).fail

  test("something simple and successful") {

    // Tinkering with mapping the validation function onto the tuple.
    // I'm finding it very difficult to generalize this into something that
    // isn't simply a mess of boilerplate (i.e. this)
    // Ideally, I'd love to be able to give the function and the tuple and
    // get back the applicative.  That'll be boilerplate-y no matter what,
    // though, so I'd settle for just taking the function and tuple and
    // getting back `refined`
    // Ideally, I'd love to be able to give the function and the tuple and
    // get back the applicative.  That'll be boilerplate-y no matter what,
    // though, so I'd settle for just taking the function and tuple and
    // getting back `refined`.  Even that is difficult, though; Shapeless
    // is secretly using too much implicit voodoo....
    val f = refineNumOpt((y: Int) => (y % 2) == 0)
    object refine extends (Int -> VII) ((x: Int) => f(x))
    val refined = (4, 2, 8, 14).hlisted.map(refine).tupled
    val applicative = refined fold (_ |@| _ |@| _ |@| _)
    val validated = applicative {
      (four, two, eight, fourteen) => four + two + eight + fourteen
    }

    validated should be (28.successNel[String])
    
  }

  test("something simple and derpy and semi-faily") {

    val f = (refineNumOpt((y: Int) => (y % 2) == 0))

    val validated = (f(1) |@| f(2) |@| f(5) |@| f(14)) {
      (one, two, five, fourteen) => one + two + five + fourteen
    }

    validated should be (failNel(1, 5))
    
  }

  test("something simple, derpy, and fully-faily") {

    val f = (refineNumOpt((_: Int) => false))

    val validated = (f(1) |@| f(2) |@| f(5) |@| f(14)) {
      (one, two, five, fourteen) => one + two + five + fourteen
    }

    validated should be (failNel(1, 2, 5, 14))

  }

  def validateList(strings: List[String]) : ValidationNel[String, String] =
    strings match {
      case Nil      => "An entry was entirely empty!".failNel
      case x :: Nil => s"No data for ${x.init}".failNel
      case x :: xs  => strings.mkString(" ").successNel[String]
    }

  def personValidationToString(v: ValidationNel[String, String]) : String = v fold (
    (f => s"Failed to create, due to the following errors:\n${f.toList.mkString}"),
    (s => s"Success!  Here's our dude:\n$s")
  )

  test("some strings (good)") {

    val name       = List("Name:", "Johnny Appleseed", "The Third")
    val age        = List("Age:", "34")
    val address    = List("Address:", "123", "Fake", "Street")
    val occupation = List("Occupation:", "Horticulturist")

    val f = validateList _

    val validated = (f(name) |@| f(age) |@| f(address) |@| f(occupation)) {
      (nm, age, addr, occ) => Seq(nm, age, addr, occ).mkString("\n")
    }

    println(personValidationToString(validated))
    println()

  }

  test("some strings (with empty entry)") {

    val name       = List("Name:", "Johnny Appleseed", "The Third")
    val age        = List("Age:", "34")
    val address    = List()
    val occupation = List("Occupation:", "Horticulturist")

    val f = validateList _

    val validated = (f(name) |@| f(age) |@| f(address) |@| f(occupation)) {
      (nm, age, addr, occ) => Seq(nm, age, addr, occ).mkString("\n")
    }

    println(personValidationToString(validated))
    println()

  }

  test("""some strings (with a too-short "Address" entry""") {

    val name       = List("Name:", "Johnny Appleseed", "The Third")
    val age        = List("Age:", "34")
    val address    = List("Address:")
    val occupation = List("Occupation:", "Horticulturist")

    val f = validateList _

    val validated = (f(name) |@| f(age) |@| f(address) |@| f(occupation)) {
      (nm, age, addr, occ) => Seq(nm, age, addr, occ).mkString("\n")
    }

    println(personValidationToString(validated))
    println()

  }

}
