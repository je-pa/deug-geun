package com.zb.deuggeun.common.util;

import static com.zb.deuggeun.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.zb.deuggeun.common.exception.CustomException;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

@UtilityClass
public class EntityServiceUtil {

  public static <T, ID> T findById(JpaRepository<T, ID> repository, ID id) {
    return repository.findById(id)
        .orElseThrow(() -> new CustomException(
            ENTITY_NOT_FOUND.getStatus(),
            ENTITY_NOT_FOUND.getMessage()
        ));
  }

}
