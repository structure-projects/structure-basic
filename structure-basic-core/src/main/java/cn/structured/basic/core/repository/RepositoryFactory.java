package cn.structured.basic.core.repository;

import cn.structured.basic.api.interfaces.IRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元仓库工厂
 *
 * @author chuck
 * @since JDK1.8
 */
public class RepositoryFactory {


    /**
     * 仓库注册表
     */
    private static final Map<String, Class<? extends IRepository>> REPOSITORY_REGISTER_TABLE = new HashMap<>();


    /**
     * 对所有的仓库实现进行缓存
     */
    private static final Map<Class<? extends IRepository>, IRepository> REPOSITORY_CACHE = new HashMap<>();

    /**
     * 获取一个仓库实例
     *
     * @param repository 仓库实例
     * @return
     */
    public static IRepository getRepository(IRepository repository) {
        return REPOSITORY_CACHE.put(repository.getClass(), repository);
    }

    /**
     * 获取仓库实例
     *
     * @param repositoryClass 仓库class对象
     * @return
     */
    public static IRepository getRepository(Class<? extends IRepository> repositoryClass) {
        return REPOSITORY_CACHE.get(repositoryClass);
    }

    /**
     * 获取仓库实例
     *
     * @param name 仓库名
     * @return
     */
    public static IRepository getRepository(String name) {
        Class<? extends IRepository> repositoryClass = REPOSITORY_REGISTER_TABLE.get(name);
        if (null == repositoryClass) {
            return null;
        }
        return REPOSITORY_CACHE.get(repositoryClass);
    }

    /**
     * 注册这个仓库
     *
     * @param name            仓库名称
     * @param repositoryClass 仓库class对象
     * @return
     */
    public static IRepository registerRepository(String name, Class<? extends IRepository> repositoryClass) {
        REPOSITORY_REGISTER_TABLE.put(name, repositoryClass);
        IRepository repository = REPOSITORY_CACHE.get(repositoryClass);
        //如果缓存中没有则实例化这个对象
        if (null == repository) {
            try {
                repository = repositoryClass.newInstance();
                REPOSITORY_CACHE.put(repositoryClass, repository);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return repository;
    }

}
