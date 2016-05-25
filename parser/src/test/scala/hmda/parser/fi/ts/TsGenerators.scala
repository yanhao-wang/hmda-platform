package hmda.parser.fi.ts

import hmda.model.fi.ts.{ Contact, Parent, Respondent, TransmittalSheet }
import hmda.parser.fi.FIGenerators
import hmda.parser.util.FITestData._
import org.scalacheck.Gen

trait TsGenerators extends FIGenerators {

  implicit def tsGen: Gen[TransmittalSheet] = {
    for {
      code <- agencyCodeGen
      timeStamp <- timeGen
      activityYear <- activityYearGen
      taxId <- taxIdGen
      totalLines = 10000
      respondent <- respondentGen
      parent <- parentGen
      contact <- contactGen
    } yield TransmittalSheet(
      1,
      code,
      timeStamp,
      activityYear,
      taxId,
      totalLines,
      respondent,
      parent,
      contact
    )
  }

  implicit def timeGen: Gen[Long] = {
    Gen.oneOf(201602021453L, 201602051234L)
  }

  implicit def activityYearGen: Gen[Int] = {
    Gen.oneOf(2017, 2018, 2019, 2020)
  }

  implicit def taxIdGen: Gen[String] = {
    for {
      prefix <- Gen.numStr
      sep = "-"
      suffix <- Gen.numStr
    } yield List(prefix.take(2), sep, suffix.take(7)).mkString
  }

  implicit def respondentGen: Gen[Respondent] = {
    for {
      id <- respIdGen
      name <- Gen.alphaStr
      address <- Gen.alphaStr
      city <- Gen.alphaStr
      state <- stateGen
      zip <- zipGen
    } yield Respondent(id, name, address, city, state, zip)
  }

  implicit def parentGen: Gen[Parent] = {
    for {
      name <- Gen.alphaStr
      address <- Gen.alphaStr
      city <- Gen.alphaStr
      state <- stateGen
      zip <- zipGen
    } yield Parent(name, address, city, state, zip)
  }

  implicit def stateGen: Gen[String] = {
    Gen.oneOf(states)
  }

  implicit def zipGen: Gen[String] = {
    Gen.oneOf(zip5Gen, zipPlus4Gen)
  }

  implicit def zip5Gen: Gen[String] = {
    for {
      zip <- Gen.numStr
    } yield zip.take(5)
  }

  implicit def zipPlus4Gen: Gen[String] = {
    for {
      zip <- Gen.numStr
      plus <- Gen.numStr
      sep = "-"
    } yield List(zip.take(5), sep, plus.take(4)).mkString
  }

  implicit def contactGen: Gen[Contact] = {
    for {
      name <- Gen.alphaStr
      phone <- phoneGen
      fax <- phoneGen
      email <- emailGen
    } yield Contact(name, phone, fax, email)
  }

  implicit def phoneGen: Gen[String] = {
    for {
      p1 <- Gen.choose(0, 999)
      p2 <- Gen.choose(0, 999)
      p3 <- Gen.choose(0, 9999)
      sep = "-"
    } yield List(
      p1.toString.reverse.padTo(3, "0").reverse.mkString,
      sep,
      p2.toString.reverse.padTo(3, "0").reverse.mkString,
      sep,
      p3.toString.reverse.padTo(4, "0").reverse.mkString
    ).mkString
  }

  implicit def emailGen: Gen[String] = {
    for {
      name <- Gen.alphaStr.filter(s => s.nonEmpty)
      at = "@"
      domain <- Gen.alphaStr.filter(s => s.nonEmpty)
      dotCom = ".com"
    } yield List(name, at, domain, dotCom).mkString
  }

}