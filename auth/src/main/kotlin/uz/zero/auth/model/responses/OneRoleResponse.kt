package uz.zero.auth.model.responses

import uz.davrbank.auth.models.responses.PermissionGroupSimpleResponse

data class OneRoleResponse(
    var id: Long,
    var key: String,
    var name: String,
    var description: String,
    var level: Int,
    val permissionGroups: List<PermissionGroupSimpleResponse>
)