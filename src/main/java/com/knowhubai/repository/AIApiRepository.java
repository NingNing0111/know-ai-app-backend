package com.knowhubai.repository;

import com.knowhubai.model.entity.AIApi;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Project: com.knowhubai.repository
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/18 15:49
 * @Description:
 */
public interface AIApiRepository extends JpaRepository<AIApi,Long> {
    // 查询所有未禁用的Key
    List<AIApi> findAllByDisableIsFalse();
    List<AIApi> findAllByDisableIsFalse(Pageable pageable);

    //批量删除指定id
    void deleteByIdIn(List<Long> id);
    //查询ids
    List<AIApi> findAllByIdIn(List<Long> ids);
}
