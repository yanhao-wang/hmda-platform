package hmda.validation.rules.lar.validity

import hmda.model.filing.lar.LoanApplicationRegister
import hmda.model.filing.lar.enums._
import hmda.validation.dsl.PredicateCommon._
import hmda.validation.dsl.PredicateSyntax._
import hmda.validation.dsl.ValidationResult
import hmda.validation.rules.EditCheck

object V670_1 extends EditCheck[LoanApplicationRegister] {
  override def name: String = "V670-1"

  override def parent: String = "V670"

  override def apply(lar: LoanApplicationRegister): ValidationResult =
    when(lar.action.actionTakenType is oneOf(ApplicationDenied, PreapprovalRequestDenied)) {
      lar.denial.denialReason1 not oneOf(EmptyDenialValue, DenialReasonNotApplicable, new InvalidDenialReasonCode)
    }
}
