package com.striveonger.common.leaf.service;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.leaf.entity.AllocEntity;
import com.striveonger.common.leaf.mapper.AllocMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.striveonger.common.leaf.entity.table.AllocEntityTableDef.ALLOC_ENTITY;

/**
 * @author Mr.Lee
 * @since 2024-08-16 00:32
 */
@Service
public class AllocService extends ServiceImpl<AllocMapper, AllocEntity> implements IService<AllocEntity> {
    private final Logger log = LoggerFactory.getLogger(AllocService.class);


    public List<String> listTags() {
        QueryWrapper condition = QueryChain.create().select(AllocEntity::getTag);
        return this.listAs(condition, String.class);
    }

    @Transactional
    public AllocEntity getAlloc(String tag) {
        // 更新数据
        UpdateChain.of(AllocEntity.class)
                .set(ALLOC_ENTITY.MAX_ID, ALLOC_ENTITY.MAX_ID.add(ALLOC_ENTITY.STEP))
                .where(ALLOC_ENTITY.TAG.eq(tag))
                .update();
        return getOne(QueryWrapper.create().where(AllocEntity::getTag).eq(tag));
    }

    @Transactional
    public AllocEntity updateMaxIdByCustomAlloc(AllocEntity temp) {
        UpdateChain.of(AllocEntity.class)
                .set(ALLOC_ENTITY.MAX_ID, ALLOC_ENTITY.MAX_ID.add(temp.getStep()))
                .where(ALLOC_ENTITY.TAG.eq(temp.getTag()))
                .update();
        return getOne(QueryWrapper.create().where(AllocEntity::getTag).eq(temp.getTag()));
    }
}
