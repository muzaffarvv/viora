package uz.zero.taskservice

import org.springframework.stereotype.Service

@Service
class TaskService(val repository: TaskRepository) {
    fun createTask(): TaskResponse {

    }
}