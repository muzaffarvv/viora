package uz.zero.taskservice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.util.Date

data class CreateTaskRequest(
    val ownerAccountId: Long,
    val name: String,
    val description: String,
    val dueDate: Date,
    val maxEmployee: Int,
    @field:Max(100)
    @field:Min(0)
    val priority: Int,
    val boardId: Long
)

data class UpdateTaskRequest(
    val name: String?,
    val description: String?,
    val dueDate: Date?,
    val maxEmployee: Int?,
    val priority: Int?
)

data class MoveTaskRequest(
    val taskStateId: Long,
    val assigneeId: Long
)

data class TaskResponse(
    val id: Long,
    val ownerAccountId: Long,
    val name: String,
    val description: String,
    val dueDate: Date,
    val maxEmployee: Int,
    val priority: Int,
    val board: BoardResponse,
    val taskState: TaskStateResponse
)

data class CreateTaskStateRequest(
    val name: String,
    val code: String,
    val boardId: Long
)

data class UpdateTaskStateRequest(
    val name: String?,
    val code: String?
)

data class TaskStateResponse(
    val id: Long?,
    val name: String,
    val code: String
)

fun TaskState.toResponse() = TaskStateResponse(
    id = id,
    name = name,
    code = code
)

data class CreateProjectRequest(
    val name: String,
    val description: String
)

data class UpdateProjectRequest(
    val name: String?,
    val description: String?
)

data class ProjectResponse(
    val id: Long,
    val name: String,
    val description: String,
    val organizationId: Long
)

data class ProjectSummaryResponse(
    val id: Long,
    val name: String
)

fun Project.toResponse() = ProjectResponse(
    id = id!!,
    name = name,
    description = description,
    organizationId = organizationId
)

data class CreateBoardRequest(
    val name: String,
    val code: String,
    val title: String,
    val projectId: Long,
    val active: Boolean
)

data class UpdateBoardRequest(
    val name: String?,
    val title: String?,
    val active: Boolean?
)

data class BoardResponse(
    val id: Long,
    val name: String,
    val code: String,
    val title: String,
    val active: Boolean,
    val project: ProjectSummaryResponse,
    val taskStates: List<TaskStateResponse>
)

fun Board.toResponse() = BoardResponse(
    id = id!!,
    name = name,
    code = code,
    title = title,
    active = active,
    project = ProjectSummaryResponse(
        id = projectId.id!!,
        name = projectId.name
    ),
    taskStates = taskStates.map { it.toResponse() }
)

data class CreateTaskFileRequest(
    val taskId: Long,
    val keyName: String
)

data class TaskFileResponse(
    val id: Long,
    val taskId: Long,
    val keyName: String
)

fun TaskFile.toResponse() = TaskFileResponse(
    id = id!!,
    taskId = taskId.id!!,
    keyName = keyName
)

data class AssignAccountToTaskRequest(
    val accountId: Long,
    val taskId: Long
)

data class AccountTaskResponse(
    val id: Long,
    val accountId: Long
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val role: String,
)