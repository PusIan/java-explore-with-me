package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c from Compilation c where c.pinned = :pinned or :pinned is null")
    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
