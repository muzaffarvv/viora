package uz.zero.taskservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {

    @PostMapping
    fun createTask(@RequestBody request: CreateTaskRequest): ResponseEntity<TaskResponse> {
        val task = taskService.create(request)
        return ResponseEntity(task, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskResponse> {
        val task = taskService.getById(id)
        return ResponseEntity.ok(task)
    }

    @GetMapping
    fun getAllTasks(): ResponseEntity<List<TaskResponse>> {
        val tasks = taskService.getAll()
        return ResponseEntity.ok(tasks)
    }

    @PutMapping("/{id}")
    fun updateTaskDetails(@PathVariable id: Long, @RequestBody request: UpdateTaskRequest): ResponseEntity<TaskResponse> {
        val updatedTask = taskService.updateDetails(id, request)
        return ResponseEntity.ok(updatedTask)
    }

    @PostMapping("/{id}/move")
    fun moveTask(@PathVariable id: Long, @RequestBody request: MoveTaskRequest): ResponseEntity<TaskResponse> {
        val movedTask = taskService.move(id, request)
        return ResponseEntity.ok(movedTask)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Void> {
        taskService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {

    @PostMapping
    fun createProject(@RequestBody request: CreateProjectRequest): ResponseEntity<ProjectResponse> {
        val project = projectService.create(request)
        return ResponseEntity(project, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: Long): ResponseEntity<ProjectResponse> {
        val project = projectService.getById(id)
        return ResponseEntity.ok(project)
    }

    @GetMapping
    fun getAllProjects(): ResponseEntity<List<ProjectResponse>> {
        val projects = projectService.getAll()
        return ResponseEntity.ok(projects)
    }

    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: Long, @RequestBody request: UpdateProjectRequest): ResponseEntity<ProjectResponse> {
        val updatedProject = projectService.update(id, request)
        return ResponseEntity.ok(updatedProject)
    }

    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: Long): ResponseEntity<Void> {
        projectService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(@RequestBody request: CreateBoardRequest): ResponseEntity<BoardResponse> {
        val board = boardService.create(request)
        return ResponseEntity(board, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getBoardById(@PathVariable id: Long): ResponseEntity<BoardResponse> {
        val board = boardService.getById(id)
        return ResponseEntity.ok(board)
    }

    @GetMapping
    fun getAllBoards(): ResponseEntity<List<BoardResponse>> {
        val boards = boardService.getAll()
        return ResponseEntity.ok(boards)
    }

    @PutMapping("/{id}")
    fun updateBoard(@PathVariable id: Long, @RequestBody request: UpdateBoardRequest): ResponseEntity<BoardResponse> {
        val updatedBoard = boardService.update(id, request)
        return ResponseEntity.ok(updatedBoard)
    }

    @DeleteMapping("/{id}")
    fun deleteBoard(@PathVariable id: Long): ResponseEntity<Void> {
        boardService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/task-states")
class TaskStateController(private val taskStateService: TaskStateService) {

    @PostMapping
    fun createTaskState(@RequestBody request: CreateTaskStateRequest): ResponseEntity<TaskStateResponse> {
        val taskState = taskStateService.create(request)
        return ResponseEntity(taskState, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getTaskStateById(@PathVariable id: Long): ResponseEntity<TaskStateResponse> {
        val taskState = taskStateService.getById(id)
        return ResponseEntity.ok(taskState)
    }

    @GetMapping
    fun getTaskStatesByBoard(@RequestParam boardId: Long): ResponseEntity<List<TaskStateResponse>> {
        val taskStates = taskStateService.getAllByBoard(boardId)
        return ResponseEntity.ok(taskStates)
    }

    @PutMapping("/{id}")
    fun updateTaskState(@PathVariable id: Long, @RequestBody request: UpdateTaskStateRequest): ResponseEntity<TaskStateResponse> {
        val updatedTaskState = taskStateService.update(id, request)
        return ResponseEntity.ok(updatedTaskState)
    }

    @DeleteMapping("/{id}")
    fun deleteTaskState(@PathVariable id: Long): ResponseEntity<Void> {
        taskStateService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/task-files")
class TaskFileController(private val taskFileService: TaskFileService) {

    @PostMapping
    fun createTaskFile(@RequestBody request: CreateTaskFileRequest): ResponseEntity<TaskFileResponse> {
        val taskFile = taskFileService.create(request)
        return ResponseEntity(taskFile, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getTaskFileById(@PathVariable id: Long): ResponseEntity<TaskFileResponse> {
        val taskFile = taskFileService.getById(id)
        return ResponseEntity.ok(taskFile)
    }

    @GetMapping
    fun getAllTaskFiles(): ResponseEntity<List<TaskFileResponse>> {
        val taskFiles = taskFileService.getAll()
        return ResponseEntity.ok(taskFiles)
    }

    @DeleteMapping("/{id}")
    fun deleteTaskFile(@PathVariable id: Long): ResponseEntity<Void> {
        taskFileService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/account-tasks")
class AccountTaskController(private val accountTaskService: AccountTaskService) {

    @PostMapping
    fun assignAccountToTask(@RequestBody request: AssignAccountToTaskRequest): ResponseEntity<AccountTaskResponse> {
        val accountTask = accountTaskService.assignAccountToTask(request)
        return ResponseEntity(accountTask, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getAccountTaskById(@PathVariable id: Long): ResponseEntity<AccountTaskResponse> {
        val accountTask = accountTaskService.getById(id)
        return ResponseEntity.ok(accountTask)
    }

    @GetMapping
    fun getAllAccountTasksByTaskId(@RequestParam taskId: Long): ResponseEntity<List<AccountTaskResponse>> {
        val accountTasks = accountTaskService.getAllByTaskId(taskId)
        return ResponseEntity.ok(accountTasks)
    }

    @DeleteMapping("/{id}")
    fun deleteAccountTask(@PathVariable id: Long): ResponseEntity<Void> {
        accountTaskService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
