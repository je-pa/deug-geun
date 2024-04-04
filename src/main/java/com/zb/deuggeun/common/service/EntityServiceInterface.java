package com.zb.deuggeun.common.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zb.deuggeun.common.exception.CustomException;

public interface EntityServiceInterface<T, ID> {

  JpaRepository<T, ID> getRepository();


  default T findById(ID id) {
    return getRepository().findById(id)
        .orElseThrow(() -> new CustomException(
            ENTITY_NOT_FOUND.getStatus(),
            ENTITY_NOT_FOUND.getMessage()
        ));
  }
}