package hmda.api.http.institutions.submissions

import akka.http.javadsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import hmda.api.http.InstitutionHttpApiSpec
import hmda.model.fi.lar.LarGenerators
import hmda.query.DbConfiguration
import hmda.query.model.filing.Irs
import hmda.query.repository.filing.{ FilingComponent, LarConverter }

import scala.concurrent.duration._
import scala.concurrent.Await

class SubmissionIrsPathsSpec
    extends InstitutionHttpApiSpec
    with DbConfiguration
    with FilingComponent
    with LarGenerators {

  import LarConverter._
  import config.profile.api._

  val duration = 5.seconds

  val repository = new LarRepository(config)
  val larTotalMsaRepository = new LarTotalMsaRepository(config)
  val modifiedLarRepository = new ModifiedLarRepository(config)

  override def beforeAll(): Unit = {
    super.beforeAll()
    dropAllObjects()
    Await.result(repository.createSchema(), duration)
    Await.result(larTotalMsaRepository.createSchema(""), duration)
    Await.result(modifiedLarRepository.createSchema(), duration)

    val msa1 = geographyGen.sample.get.copy(msa = "12345")
    val msaNa = geographyGen.sample.get.copy(msa = "NA")
    val loan = loanGen.sample.get.copy(amount = 12)
    val lar1 = larGen.sample.get.copy(geography = msa1, loan = loan)
    val lar2 = larGen.sample.get.copy(geography = msa1, loan = loan)
    val lar3 = larGen.sample.get.copy(geography = msa1, loan = loan)
    val lar4 = larGen.sample.get.copy(geography = msaNa, loan = loan)
    repository.insertOrUpdate(lar1).map(x => x mustBe 1)
    repository.insertOrUpdate(lar2).map(x => x mustBe 1)
    repository.insertOrUpdate(lar3).map(x => x mustBe 1)
    repository.insertOrUpdate(lar4).map(x => x mustBe 1)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    dropAllObjects()
    repository.config.db.close()
    system.terminate()
  }

  private def dropAllObjects() = {
    val db = repository.config.db
    val dropAll = sqlu"""DROP ALL OBJECTS"""
    Await.result(db.run(dropAll), duration)
  }

  "Submission Irs Paths" must {
    "return a 200" in {
      getWithCfpbHeaders("/institutions/0/filings/2017/submissions/1/irs") ~> institutionsRoutes ~> check {
        status mustBe StatusCodes.OK
        val irs = responseAs[Irs]
        irs.totals.amount mustBe 48
        irs.totals.lars mustBe 4
        irs.msas.length mustBe 2
      }
    }
  }
}
