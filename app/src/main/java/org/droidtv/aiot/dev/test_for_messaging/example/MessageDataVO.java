package org.droidtv.aiot.dev.test_for_messaging.example;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: bilahepan
 * @date: 2019/3/26 下午8:25
 */
public class MessageDataVO implements Serializable {

    private String dataId;

    private String devId;

    private String productKey;

    private List<Map<String, Object>> status;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public List<Map<String, Object>> getStatus() {
        return status;
    }

    public void setStatus(List<Map<String, Object>> status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MessageDataVO{" +
                "dataId='" + dataId + '\'' +
                ", devId='" + devId + '\'' +
                ", productKey='" + productKey + '\'' +
                ", status=" + status +
                '}';
    }
}