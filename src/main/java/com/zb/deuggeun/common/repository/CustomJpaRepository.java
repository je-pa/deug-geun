package com.zb.deuggeun.common.repository;

import static com.zb.deuggeun.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zb.deuggeun.common.exception.CustomException;

public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

  default T findByIdWithThrow(ID id) {
    return this.findById(id)
        .orElseThrow(() -> new CustomException(
            ENTITY_NOT_FOUND.getStatus(),
            ENTITY_NOT_FOUND.getMessage()
        ));
  }
}