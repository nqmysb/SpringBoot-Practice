package com.nqmysb.practice.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author liaocan
 * @since 2018-08-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MyPage<T> extends Page<T> {
    private static final long serialVersionUID = 5194933845448697148L;

    private String selectStr1;
    private String selectStr2;

    public MyPage(long current, long size) {
        super(current, size);
    }
}
