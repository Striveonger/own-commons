package com.striveonger.common.leaf.service;

import cn.hutool.core.util.StrUtil;
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

    /**
     * 注册新的 id tag
     * @param tag tag
     * @param description 描述
     * @return true 注册成功, 1分钟后生效
     */
    public boolean registerTag(String tag, String description) {
        return registerTag(tag, 1, 1, description);
    }

    /**
     * 注册新的 id tag
     * @param tag tag
     * @param step 步长
     * @param maxId 最大 id
     * @param description 描述
     * @return true 注册成功, 1分钟后生效
     */
    public boolean registerTag(String tag, int step, int maxId, String description) {
        if (StrUtil.isBlank(tag) || step <= 0 || maxId <= 0 || StrUtil.isBlank(description)) { return false; }
        synchronized (tag.intern()) {
            long cnt = count(QueryWrapper.create().where(ALLOC_ENTITY.TAG.eq(tag)));
            if (cnt > 0) { return false; }
            AllocEntity alloc = new AllocEntity();
            alloc.setTag(tag);
            alloc.setStep(step);
            alloc.setMaxId(maxId);
            alloc.setDescription(description);
            return save(alloc);
        }
    }

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
