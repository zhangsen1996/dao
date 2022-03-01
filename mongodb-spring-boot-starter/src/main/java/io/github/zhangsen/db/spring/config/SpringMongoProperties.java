package io.github.zhangsen.db.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mongodb")
public class SpringMongoProperties {

    //处理持久化线程的数量,默认1
    private int threadNum = 1;

    //序列化时是否包括class类型
    private boolean closeClassKey;

    //序列化时是否去掉基本数据类型的默认值字段
    private boolean simplifyDoc;

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public boolean isCloseClassKey() {
        return closeClassKey;
    }

    public void setCloseClassKey(boolean closeClassKey) {
        this.closeClassKey = closeClassKey;
    }

    public boolean isSimplifyDoc() {
        return simplifyDoc;
    }

    public void setSimplifyDoc(boolean simplifyDoc) {
        this.simplifyDoc = simplifyDoc;
    }
}
