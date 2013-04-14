package org.bizzle.sandbox.scalaz

import
  org.scalatest.{ BeforeAndAfterEach, FunSuite, matchers },
    matchers.ShouldMatchers

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

  def validateNumOpt(x: Option[Int]) : ValidationNel[String, Int] = if (x.isEmpty) "This is a `None`!\n".failNel else x.get.successNel[String]
  def numOptValidationToString(v: ValidationNel[String, Int]) : String = v fold (
    (f => s"Way to go!\n${f.toList.mkString}You dun good.\n"),
    (s => s"Success!  The answer was: $s\n")
  )

  test("something simple and successful") {

    def refineNumOpt(x: Option[Int]) : Option[Int] = x flatMap (y => if ((y % 2) == 0) Option(y) else None)
    val f = (refineNumOpt _) andThen (validateNumOpt _)

    val validated = (f(Option(4)) |@| f(Option(2)) |@| f(Option(8)) |@| f(Option(14))) {
      (four, two, eight, fourteen) => four + two + eight + fourteen
    }

    println(numOptValidationToString(validated))
    
  }

  test("something simple and derpy and semi-faily") {

    def refineNumOpt(x: Option[Int]) : Option[Int] = x flatMap (y => if ((y % 2) == 0) Option(y) else None)
    val f = (refineNumOpt _) andThen (validateNumOpt _)

    val validated = (f(Option(1)) |@| f(Option(2)) |@| f(Option(5)) |@| f(Option(14))) {
      (one, two, five, fourteen) => one + two + five + fourteen
    }

    println(numOptValidationToString(validated))
    
  }

  test("something simple, derpy, and fully-faily") {

    def refineNumOpt(x: Option[Int]) : Option[Int] = x flatMap (y => if (false) Option(y) else None)
    val f = (refineNumOpt _) andThen (validateNumOpt _)

    val validated = (f(Option(1)) |@| f(Option(2)) |@| f(Option(5)) |@| f(Option(14))) {
      (one, two, five, fourteen) => one + two + five + fourteen
    }

    println(numOptValidationToString(validated))
    println()

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
