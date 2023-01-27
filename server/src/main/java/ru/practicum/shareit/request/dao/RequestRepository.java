package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {
    Request findRequestById(Long requestId);

    @Query("SELECT r FROM Request r WHERE r.requestor.id = ?1 ORDER BY r.created DESC")
    List<Request> findRequestByRequestor_Id(Long requestorId);

    @Query("SELECT r FROM Request r WHERE r.requestor.id <> ?1 ORDER BY r.created DESC")
    List<Request> findAllRequests(Long userId, Pageable pageable);
}
