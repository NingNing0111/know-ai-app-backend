package com.knowhubai.model.vo;

import com.knowhubai.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: com.knowhubai.model.vo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 16:32
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVo {
    private Long userId;
    private String email;
    private Role role;

    private Boolean enabled;
}
