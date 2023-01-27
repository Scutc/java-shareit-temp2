package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.RequestDto;
import ru.practicum.shareit.request.RequestDtoForResponse;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.utility.PaginationConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements IRequestService {
    private final RequestRepository requestRepository;
    private final IUserService userService;
    private final UserRepository userRepository;
    private final PaginationConverter paginationConverter;

    @Transactional
    @Override
    public RequestDtoForResponse addRequest(RequestDto requestDto, Long requestorId) {
        log.info("Создание нового Запроса на бронирование {}", requestDto);
        userService.getUserById(requestorId);
        requestDto.setRequestorId(requestorId);
        Request request = RequestMapper.toRequest(requestDto);
        request.setRequestor(userRepository.getUserById(requestorId));
        request = requestRepository.save(request);
        return RequestMapper.toRequestDtoForResponse(request);
    }

    @Override
    public List<RequestDtoForResponse> findRequestByRequestorId(Long requestorId) {
        log.info("Получение запросов на бронирование от пользователя ID = {}", requestorId);
        userService.getUserById(requestorId);
        List<Request> requests = requestRepository.findRequestByRequestor_Id(requestorId);
        return requests.stream().map(RequestMapper::toRequestDtoForResponse).collect(Collectors.toList());
    }

    @Override
    public List<RequestDtoForResponse> findAllRequests(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        Pageable pageable = paginationConverter.convert(from, size, "created");
        List<Request> requests = requestRepository.findAllRequests(userId, pageable);
        return requests.stream().map(RequestMapper::toRequestDtoForResponse).collect(Collectors.toList());
    }

    @Override
    public RequestDtoForResponse getRequestById(Long requestId, Long userId) {
        userService.getUserById(userId);
        Request request = requestRepository.findRequestById(requestId);
        if (request == null) {
            throw new RequestNotFoundException(requestId);
        }
        return RequestMapper.toRequestDtoForResponse(request);
    }
}
