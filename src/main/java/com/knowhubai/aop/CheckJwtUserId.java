package com.knowhubai.aop;

import java.lang.annotation.*;

/**
 * @Project: com.knowhubai.aop
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 15:37
 * @Description: 一般使用在controller中的method上 判断jwt中的userId与targetUserId是否一致
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckJwtUserId {
}
