package cn.structured.basic.core.repository.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.structured.basic.api.interfaces.IRepository;
import cn.structured.basic.api.model.BasicEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存仓库实现类
 *
 * @author chuck
 * @since JDK1.8
 */
@Slf4j
@RequiredArgsConstructor
public class MemoryRepositoryImpl implements IRepository<BasicEntity> {

    private static final Map<String, BasicEntity> ENTITY_CACHE = new ConcurrentHashMap<>();

    @Override
    public Serializable save(BasicEntity basicEntity) {
        MD5 md5 = SecureUtil.md5();
        String id = md5.digestHex(basicEntity.getPath());
        basicEntity.setId(id);
        ENTITY_CACHE.put(id, basicEntity);
        log.info("加载Entity INFO : id -> {},name -> {} ,entityType  ->{}", id, basicEntity.getName(), basicEntity.getEntityType());
        return id;
    }

    @Override
    public boolean updateById(Serializable id, BasicEntity basicEntity) {
        ENTITY_CACHE.put(id.toString(), basicEntity);
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        ENTITY_CACHE.remove(id.toString());
        return true;
    }

    @Override
    public BasicEntity findById(Serializable id) {
        return ENTITY_CACHE.get(id.toString());
    }

}
