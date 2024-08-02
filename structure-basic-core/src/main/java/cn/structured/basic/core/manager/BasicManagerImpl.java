package cn.structured.basic.core.manager;

import cn.structured.basic.api.interfaces.IManager;
import cn.structured.basic.api.interfaces.IRepository;
import cn.structured.basic.api.model.BasicEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 基座管理接口
 *
 * @author chuck
 * @since JDK1.8
 */
@Service
@RequiredArgsConstructor
public class BasicManagerImpl implements IManager<BasicEntity> {

    private final IRepository<BasicEntity> repository;

    @Override
    public String save(BasicEntity basicEntity) {
        repository.save(basicEntity);
        return null;
    }

    @Override
    public boolean updateById(String id, BasicEntity basicEntity) {
        return repository.updateById(id, basicEntity);
    }

    @Override
    public boolean removeById(String id) {
        return repository.removeById(id);
    }

    @Override
    public BasicEntity findById(String id) {
        return repository.findById(id);
    }

}
