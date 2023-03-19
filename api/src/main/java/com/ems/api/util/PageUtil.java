package com.ems.api.util;

import com.ems.api.dto.request.PagingRequest;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {
    public static PageRequest createPageRequest(PagingRequest request) {
        if (request.getOrders().isEmpty()) {
            return PageRequest.of(request.getPage(), request.getSize());
        }
        return PageRequest.of(request.getPage(), request.getSize(),
                Sort.by(request.getOrders().stream().map(po -> {
                    Order o = new Order(po.getDirection(), po.getProperty(), po.getNullHandling());
                    return po.getIgnoreCase() ? o.ignoreCase() : o;

                }).collect(Collectors.toList())));
    }

    public static <T> Page<T> convertListToPage(List<T> responseList, Pageable pageable) {
        Page<T> responsePage;
        int start = (int) pageable.getOffset();
        if (start > responseList.size() - 1)
            start = -1;
        int end = (start + pageable.getPageSize()) > responseList.size() ? responseList.size()
                : (start + pageable.getPageSize());
        if (start != -1) {
            responsePage = new PageImpl<>(responseList.subList(start, end), pageable,
                    responseList.size());
        } else {
            responsePage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return responsePage;
    }
}
