package com.zb.deuggeun.common.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.zb.deuggeun.common.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityService {

  static <T, ID> T findById(JpaRepository<T, ID> repository, ID id) {
    return repository.findById(id)
        .orElseThrow(() -> new CustomException(
            ENTITY_NOT_FOUND.getStatus(),
            ENTITY_NOT_FOUND.getMessage()
        ));
  }
}
