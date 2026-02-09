package uz.zero.taskservice

import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllByDeletedFalse(): List<T>
    fun findAllByDeletedFalse(pageable: Pageable): Page<T>
}


class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>, entityManager: EntityManager
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

    val isNotDeletedSpecification = Specification<T> { root, _, cb -> cb.equal(root.get<Boolean>("deleted"), false) }

    override fun findByIdAndDeletedFalse(id: Long) = findByIdOrNull(id)?.run { if (deleted) null else this }

    @Transactional
    override fun trash(id: Long): T? = findByIdOrNull(id)?.run {
        deleted = true
        save(this)
    }

    override fun findAllByDeletedFalse(): List<T> = findAll(isNotDeletedSpecification)
    override fun findAllByDeletedFalse(pageable: Pageable): Page<T> = findAll(isNotDeletedSpecification, pageable)
    override fun trashList(ids: List<Long>): List<T?> = ids.map { trash(it) }
}

@Repository
interface TaskRepository: BaseRepository<Task>

@Repository
interface TaskStateRepository: BaseRepository<TaskState> {
    fun findByNameAndBoardIdAndDeletedFalse(name: String, boardId: Long): TaskState?
    fun findAllByBoardIdAndDeletedFalse(boardId: Long): List<TaskState>
}

@Repository
interface ProjectRepository: BaseRepository<Project>

@Repository
interface BoardRepository: BaseRepository<Board>

@Repository
interface TaskFileRepository: BaseRepository<TaskFile>

@Repository
interface AccountTaskRepository: BaseRepository<AccountTask> {
    fun findAllByTaskIdAndDeletedFalse(taskId: Long): List<AccountTask>
}