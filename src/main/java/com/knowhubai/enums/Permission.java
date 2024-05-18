package com.knowhubai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Project: com.knowhubai.enums
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/18 15:21
 * @Description:
 */
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete");


    @Getter
    private final String permission;
}
