package uz.zero.taskservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

    @PostMapping
    fun createTask(@RequestBody request: CreateTaskRequest): TaskResponse {
        return taskService.create(request)
    }

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): TaskResponse {
        return taskService.getById(id)
    }

    @GetMapping
    fun getAllTasks(): List<TaskResponse> {
        return taskService.getAll()

    }

    @PutMapping("/{id}")
    fun updateTaskDetails(@PathVariable id: Long, @RequestBody request: UpdateTaskRequest): TaskResponse {
        return taskService.updateDetails(id, request)
    }

    @PostMapping("/{id}/move")
    fun moveTask(@PathVariable id: Long, @RequestBody request: MoveTaskRequest): TaskResponse {
        return taskService.move(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long){
        taskService.delete(id)
    }
}

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {

    @PostMapping
    fun createProject(@RequestBody request: CreateProjectRequest): ProjectResponse {
        return projectService.create(request)
    }

    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: Long): ProjectResponse {
        return projectService.getById(id)
    }

    @GetMapping
    fun getAllProjects(): List<ProjectResponse> {
        return projectService.getAll()
    }

    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: Long, @RequestBody request: UpdateProjectRequest): ProjectResponse {
        return projectService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: Long) {
        projectService.delete(id)
    }
}

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(@RequestBody request: CreateBoardRequest): BoardResponse {
        return boardService.create(request)
    }

    @GetMapping("/{id}")
    fun getBoardById(@PathVariable id: Long): ResponseEntity<BoardResponse> {
        val board = boardService.getById(id)
        return ResponseEntity.ok(board)
    }

//    @GetMapping
//    fun getBoardsByProjectId(@RequestParam projectId: Long): ResponseEntity<List<BoardResponse>> {
//        val boards = boardService.getByProjectId(projectId)
//        return ResponseEntity.ok(boards)
//    }

    @GetMapping
    fun getAllBoards(): List<BoardResponse> {
        return boardService.getAll()
    }

    @PutMapping("/{id}")
    fun updateBoard(@PathVariable id: Long, @RequestBody request: UpdateBoardRequest): BoardResponse {
        return boardService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteBoard(@PathVariable id: Long) {
        boardService.delete(id)
    }
}

@RestController
@RequestMapping("/task-states")
class TaskStateController(private val taskStateService: TaskStateService) {

    @PostMapping
    fun createTaskState(@RequestBody request: CreateTaskStateRequest): TaskStateResponse{
        return taskStateService.create(request)
    }

    @GetMapping("/{id}")
    fun getTaskStateById(@PathVariable id: Long): TaskStateResponse {
        return taskStateService.getById(id)
    }

    @GetMapping
    fun getTaskStatesByBoard(@RequestParam boardId: Long): List<TaskStateResponse> {
        return taskStateService.getAllByBoard(boardId)
    }

    @PutMapping("/{id}")
    fun updateTaskState(@PathVariable id: Long, @RequestBody request: UpdateTaskStateRequest): TaskStateResponse {
        return taskStateService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteTaskState(@PathVariable id: Long) {
        taskStateService.delete(id)
    }
}

@RestController
@RequestMapping("/task-files")
class TaskFileController(private val taskFileService: TaskFileService) {

    @PostMapping
    fun createTaskFile(@RequestBody request: CreateTaskFileRequest): TaskFileResponse {
        return taskFileService.create(request)
    }

    @GetMapping("/{id}")
    fun getTaskFileById(@PathVariable id: Long): TaskFileResponse {
        return taskFileService.getById(id)
    }

    @GetMapping
    fun getAllTaskFiles(): List<TaskFileResponse> {
        return taskFileService.getAll()
    }

    @DeleteMapping("/{id}")
    fun deleteTaskFile(@PathVariable id: Long){
        taskFileService.delete(id)
    }
}

@RestController
@RequestMapping("/account-tasks")
class AccountTaskController(private val accountTaskService: AccountTaskService) {

    @PostMapping
    fun assignAccountToTask(@RequestBody request: AssignAccountToTaskRequest): AccountTaskResponse {
        return accountTaskService.assignAccountToTask(request)
    }

    @GetMapping("/{id}")
    fun getAccountTaskById(@PathVariable id: Long): AccountTaskResponse {
        return accountTaskService.getById(id)
    }

    @GetMapping
    fun getAllAccountTasksByTaskId(@RequestParam taskId: Long): List<AccountTaskResponse> {
        return accountTaskService.getAllByTaskId(taskId)
    }

    @DeleteMapping("/{id}")
    fun deleteAccountTask(@PathVariable id: Long) {
        accountTaskService.delete(id)
    }
}
