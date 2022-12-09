package top.zy68.fbChainService.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FbSystemConfig
 * @Description FISCO BCOS 区块链的配置类：用于从application.properties中加载区块链的相关信息
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/

@Data
@Configuration
@ConfigurationProperties(
        prefix = "system"
)
public class FbSystemConfig {
    private String peers;

    private int groupId = 1;

    private String certPath = "conf";

    private String hexPrivateKey;

    @NestedConfigurationProperty
    private ContractConfig contract;

    public String getPeers() {
        return peers;
    }

    public void setPeers(String peers) {
        this.peers = peers;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getHexPrivateKey() {
        return hexPrivateKey;
    }

    public void setHexPrivateKey(String hexPrivateKey) {
        this.hexPrivateKey = hexPrivateKey;
    }

    public ContractConfig getContract() {
        return contract;
    }

    public void setContract(ContractConfig contract) {
        this.contract = contract;
    }
}
