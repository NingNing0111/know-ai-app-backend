package com.knowhubai.repository;

import com.knowhubai.model.entity.KnowFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Project: com.knowhubai.repository
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/18 15:48
 * @Description:
 */
@Repository
public interface KnowFileRepository extends JpaRepository<KnowFile, String> {
    Page<KnowFile> findByUserIdAndFileNameContaining(Long userId, String keyword, Pageable pageable);
}
