package uz.zero.auth.model.requests

data class ChangeCurrentOrganizationRequest(
    val userId:Long,
    val newOrgId:Long
)