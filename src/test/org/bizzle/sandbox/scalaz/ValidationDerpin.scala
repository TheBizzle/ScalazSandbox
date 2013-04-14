package org.bizzle.sandbox.scalaz

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import scalaz._

/**
 * Created by IntelliJ IDEA.
 * User: Jason
 * Date: 5/15/12
 * Time: 11:27 PM
 */

class ValidationDerpin extends FunSuite with BeforeAndAfterEach with ShouldMatchers {

  test("something simple and derpy") {

    def validateNumOpt(x: Option[Int]) : Validation[String, Int] = if (x.isEmpty) Failure("This is a `None`!\n") else Success(x.get)

    val opts = Seq(Option(1), Option(2), Option(5), Option(14))
    val mappedOpts = opts map (_ flatMap (x => if ((x % 2) == 0) Option(x) else None))
    val validations = mappedOpts map (validateNumOpt(_))

    println (validations reduce (_ >>*<< _) fold ({ "Way to go!\n" + _ + "You dun good." }, (_.toString))); println(); println()
    
  }

  test("something simple, derpy, and faily") {

    def validateNumOpt(x: Option[Int]) : Validation[String, Int] = if (x.isEmpty) Failure("This is a `None`!\n") else Success(x.get)

    val opts = Seq(Option(1), Option(2), Option(5), Option(14))
    val mappedOpts = opts map (_ flatMap (x => if (false) Option(x) else None))
    val validations = mappedOpts map (validateNumOpt(_))

    println (validations reduce (_ >>*<< _) fold ({ "Way to go!\n" + _ + "You dun good." }, (_.toString))); println(); println()

  }

  test("some strings") {

    def validateList(xs: List[String]) : Validation[String, String] = {
      if (xs.isEmpty)        Failure("Empty entry")
      else if (xs.size == 1) Failure("No data for " + xs(0))
      else                   Success(xs.mkString("\n"))
    }

    val name = List("Name:", "Johnny Appleseed", "The Third")
    val age = List("Age:", "34")
    val address = List()
    val occupation = List("Occupation:", "Horticulturist")

    val lists = Seq(name, age, address, occupation)
    val validations = lists map (validateList(_)) map (_ map (_ + "\n\n"))

    println (validations reduce (_ >>*<< _) fold ({ "Failed to create due to errors:\n" + _ }, (identity))); println(); println()

  }

  test("""some strings (with a too-short "Address" entry""") {

    def validateList(xs: List[String]) : Validation[String, String] = {
      if (xs.isEmpty)        Failure("Empty entry")
      else if (xs.size == 1) Failure("No data for " + xs(0))
      else                   Success(xs.mkString("\n"))
    }

    val name = List("Name:", "Johnny Appleseed", "The Third")
    val age = List("Age:", "34")
    val address = List("Address:")
    val occupation = List("Occupation:", "Horticulturist")

    val lists = Seq(name, age, address, occupation)
    val validations = lists map (validateList(_)) map (_ map (_ + "\n\n"))

    println (validations reduce (_ >>*<< _) fold ({ "Failed to create due to errors:\n" + _ }, (identity)))

  }

}
