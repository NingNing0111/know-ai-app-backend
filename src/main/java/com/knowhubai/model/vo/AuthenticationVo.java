package com.knowhubai.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: com.knowhubai.model.vo
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/21 20:07
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationVo {
    private String accessToken;
    private String refreshToken;
}
