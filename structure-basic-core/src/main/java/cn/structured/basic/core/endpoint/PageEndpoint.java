package cn.structured.basic.core.endpoint;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structured.basic.api.interfaces.IRepository;
import cn.structured.basic.api.model.BasicEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 页面控制器
 *
 * @author chuck
 * @since JDK1.8
 */
@RestController
@RequestMapping("/api/page")
public class PageEndpoint {

    @Resource
    private IRepository<BasicEntity> repository;

    @GetMapping("/{id}")
    public ResResultVO<BasicEntity> getJsonSchema(@PathVariable("id") String id) {
        BasicEntity basicEntity = repository.findById(id);
        if (null == basicEntity) {
            return ResultUtilSimpleImpl.fail("404", "not fund page!", null);
        }
        return ResultUtilSimpleImpl.success(basicEntity);
    }

}
