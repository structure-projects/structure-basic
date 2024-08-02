package cn.structured.basic.core.configuration;

import cn.structure.common.utils.HttpClientUtil;
import cn.structured.basic.api.enums.*;
import cn.structured.basic.api.interfaces.IRepository;
import cn.structured.basic.api.model.BasicEntity;
import cn.structured.basic.api.model.ModelEntity;
import cn.structured.basic.api.model.ViewEntity;
import cn.structured.basic.api.model.api.ApiDefinition;
import cn.structured.basic.api.model.logic.NodeDefinition;
import cn.structured.basic.api.model.logic.ProcessDefinition;
import cn.structured.basic.api.model.trigger.MessageDefinition;
import cn.structured.basic.api.model.trigger.ScheduleDefintion;
import cn.structured.basic.api.model.trigger.TriggerDefinition;
import cn.structured.basic.core.execute.CallAsync;
import cn.structured.basic.core.execute.HandleRequest;
import cn.structured.basic.core.execute.NodeExecute;
import cn.structured.basic.core.execute.ProcessExecute;
import cn.structured.basic.core.execute.transaction.AtExecute;
import cn.structured.basic.core.execute.transaction.LocalExecute;
import cn.structured.basic.core.execute.transaction.TccExecute;
import cn.structured.function.api.dataspecs.*;
import cn.structured.function.api.entity.EnumEntity;
import cn.structured.function.api.enums.DataType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 基座上下文
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@Slf4j
public class BasicContext {

    private Map<String, HandleRequest> handleRequestCache = new ConcurrentHashMap<>();

    private IRepository<BasicEntity> repository;

    /**
     * 资源的版本
     */
    private String resourceVersion;

    /**
     * 获取执行求请求
     *
     * @param triggerId 触发定义
     * @return
     */
    public HandleRequest getHandleRequest(String triggerId) {
        return handleRequestCache.get(triggerId);
    }

    /**
     * 构建执行请求处理器
     *
     * @param triggerDefinition 触发定义
     */
    public void buildHandleRequest(TriggerDefinition triggerDefinition) {
        //节点定义
        NodeDefinition boundNode = triggerDefinition.getBoundNode();

        //请求处理器
        HandleRequest handleRequest;

        //判断节点或者是流程
        if (null != boundNode) {
            //节点构建
            handleRequest = new NodeExecute(boundNode);
        } else {
            //构建出流程的执行过程
            handleRequest = buildProcess(triggerDefinition);
        }

        //存储到缓存中
        handleRequestCache.put(triggerDefinition.getId(), handleRequest);

    }

    /**
     * 加载资源
     *
     * @param uri uri
     */
    public void loadResource(String uri, String accessToken) {
        log.info("load resource this url -> {}", uri);
        HttpClient httpClient = HttpClientUtil.getHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("accessToken", accessToken);
        try {
            HttpResponse execute = httpClient.execute(httpGet);
            HttpEntity entity = execute.getEntity();
            //下载的是一个压缩包
            InputStream content = entity.getContent();
            //读取这个文件然后判断文件的类型将文件加载到内存中
            Map<String, byte[]> files = new HashMap<>();
            try (ZipInputStream zipInputStream = new ZipInputStream(content, StandardCharsets.UTF_8)) {
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    String entryName = zipEntry.getName();
                    if (entryName.lastIndexOf(".") > 0) {
                        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = zipInputStream.read(buf, 0, buf.length)) > 0) {
                                byteArrayOutputStream.write(buf, 0, len);
                            }
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            files.put(entryName, bytes);
                        }
                    }
                    zipInputStream.closeEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //更新版本
            updateVersion(files);
            content.close();
        } catch (Exception e) {
            log.error("加载资源失败！ -> {} ", e.getMessage());
            //打印加载失败导致的错误堆栈信息
            //e.printStackTrace();
        }
    }

    /**
     * 更新资源版本
     *
     * @param files 资源文件
     */
    private void updateVersion(Map<String, byte[]> files) {
        //资源清单
        JSONObject versionContext = JSON.parseObject(new String(files.get("version.json"), StandardCharsets.UTF_8));
        this.resourceVersion = versionContext.getString("version");
        JSONArray resources = versionContext.getJSONArray("resources");
        for (int i = 0; i < resources.size(); i++) {
            String fileName = resources.getString(i);
            byte[] bytes = files.get(fileName);
            String jsonStr = new String(bytes, StandardCharsets.UTF_8);
            try {
                loadJsonSchema(jsonStr);
            } catch (Exception e) {
                log.error("加载JsonSchema错误 -> {}", e.getMessage());
            }
        }
    }

    /**
     * 加载JSON
     *
     * @param jsonSchema jsonSchema
     */
    private void loadJsonSchema(String jsonSchema) {
        JSONObject jsonObject = JSON.parseObject(jsonSchema);
        String entityType = jsonObject.getString("entityType");
        //对所有的 dataSpecs 单独进行反序列化
        ParserConfig.getGlobalInstance().putDeserializer(IDataSpecs.class, new ObjectDeserializer() {
            @Override
            public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
                JSONObject contextJson = defaultJSONParser.parseObject(JSONObject.class, o);
                String dataType = contextJson.getString("dataType");
                Integer length = contextJson.getInteger("length");
                String format = contextJson.getString("format");
                Integer digits = contextJson.getInteger("digits");
                String falseKey = contextJson.getString("falseKey");
                String trueKey = contextJson.getString("trueKey");
                IDataSpecs dataSpecs = null;
                switch (DataType.valueOf(dataType)) {
                    case INT:
                        dataSpecs = new IntegerDataSpecs(length);
                        break;
                    case DATE:
                        dataSpecs = new DateDataSpecs(format);
                        break;
                    case DOUBLE:
                        dataSpecs = new DoubleDataSpecs(length, digits);
                        break;
                    case BOOL:
                        dataSpecs = new BooleanDataSpecs(trueKey, falseKey);
                        break;
                    case ENUM:
                        List<EnumEntity> enumEntities = jsonObject.entrySet()
                                .stream()
                                .filter(e -> e.getKey().equals("dataType"))
                                .map(e ->
                                        new EnumEntity(e.getKey(), e.getValue())

                                ).collect(Collectors.toList());
                        dataSpecs = new EnumDataSpecs(enumEntities);
                        break;
                    case TEXT:
                        dataSpecs = new TextDataSpecs(length);
                        break;
                    case ARRAY:
                        IDataSpecs subDataSpecs = jsonObject.getObject("subDataSpecs", IDataSpecs.class);
                        dataSpecs = new ArrayDataSpecs(subDataSpecs);
                        break;
                    case FLOAT:
                        break;
                    case STRUCT:
                        Map<String, IDataSpecs> dataSpecsMap = new HashMap<>();
                        jsonObject.entrySet()
                                .stream()
                                .filter(e -> e.getKey().equals("dataType"))
                                .forEach(e ->
                                        dataSpecsMap.put(e.getKey(), jsonObject.getObject(e.getKey(), IDataSpecs.class))
                                );
                        dataSpecs = new StructDataSpecs(dataSpecsMap);
                        break;
                    case LONG:
                        dataSpecs = new LongDataSpecs(length);
                        break;
                }
                return (T) dataSpecs;
            }

            @Override
            public int getFastMatchToken() {
                return 0;
            }
        });
        BasicEntity basicEntity = null;
        switch (EntityType.valueOf(entityType)) {
            case NODE:
                basicEntity = JSON.parseObject(jsonSchema, NodeDefinition.class);
                break;
            case VIEW:
                basicEntity = JSON.parseObject(jsonSchema, ViewEntity.class);
                break;
            case EVENT:
                String triggerType = jsonObject.getString("triggerType");
                switch (TriggerType.valueOf(triggerType)) {
                    case MQ:
                        basicEntity = JSON.parseObject(jsonSchema, MessageDefinition.class);
                        break;
                    case HTTP:
                        basicEntity = JSON.parseObject(jsonSchema, ApiDefinition.class);
                        String inputEntity = jsonObject.getString("inputEntity");
                        String outputEntity = jsonObject.getString("outputEntity");
                        String boundProcess = jsonObject.getString("boundProcess");
                        loadJsonSchema(boundProcess);
                        loadJsonSchema(inputEntity);
                        loadJsonSchema(outputEntity);
                        break;
                    case CALLBACK:
                        basicEntity = JSON.parseObject(jsonSchema, TriggerDefinition.class);
                        break;
                    case SCHEDULE:
                        basicEntity = JSON.parseObject(jsonSchema, ScheduleDefintion.class);
                        break;
                }
                break;
            case LOGIC:
                basicEntity = JSON.parseObject(jsonSchema, ProcessDefinition.class);
                JSONArray nodes = jsonObject.getJSONArray("nodes");
                for (int i = 0; i < nodes.size(); i++) {
                    String nodeStr = nodes.getString(i);
                    loadJsonSchema(nodeStr);
                }

                break;
            case MODEL:
                basicEntity = JSON.parseObject(jsonSchema, ModelEntity.class);
        }
        basicEntity.setContext(jsonSchema);
        repository.save(basicEntity);
        if (basicEntity instanceof TriggerDefinition) {
            //加载流程实体
            TriggerDefinition triggerDefinition = (TriggerDefinition) basicEntity;
            buildHandleRequest(triggerDefinition);
        }
    }

    /**
     * 构建流程
     *
     * @param triggerDefinition 触发器
     * @return
     */
    private HandleRequest buildProcess(TriggerDefinition triggerDefinition) {
        //绑定的流程
        ProcessDefinition boundProcess = triggerDefinition.getBoundProcess();
        //查询出这个流程的所有流程
        List<NodeDefinition> nodes = boundProcess.getNodes();
        TransactionType transactionType = boundProcess.getTransactionType();
        //流程执行器
        HandleRequest processExecute = new ProcessExecute(boundProcess);
        //当前处理器
        HandleRequest currentExecute = processExecute;
        //调用方式
        CallType sync = triggerDefinition.getSync();
        //添加异步执行
        if (sync == CallType.ASYNC) {
            CallAsync callAsync = new CallAsync();
            currentExecute.setNextHandle(callAsync);
            currentExecute = callAsync;
        }
        //构建事务类型
        HandleRequest transactionExecute = null;
        switch (transactionType) {
            case AT:
                transactionExecute = new AtExecute();
                break;
            case TCC:
                transactionExecute = new TccExecute();
                break;
            case LOCAL:
                transactionExecute = new LocalExecute();
                break;
        }

        //判断不为null则交换执行器
        if (null != transactionExecute) {
            currentExecute.setNextHandle(transactionExecute);
            currentExecute = transactionExecute;
        }

        //构建执行责任链条
        for (NodeDefinition node : nodes) {
            //开始节点和结束节点不处理
            if (node.getNodeType() != NodeType.START || node.getNodeType() != NodeType.END) {
                currentExecute = setNextNode(currentExecute, node);
            }
        }
        return processExecute;
    }

    /**
     * 设置下一个节点
     *
     * @param currentExecute 当前执行器
     * @param nodeDefinition 节点定义
     * @return
     */
    private HandleRequest setNextNode(HandleRequest currentExecute, NodeDefinition nodeDefinition) {
        HandleRequest nodeExecute = new NodeExecute(nodeDefinition);
        currentExecute.setNextHandle(nodeExecute);
        return nodeExecute;
    }
}
