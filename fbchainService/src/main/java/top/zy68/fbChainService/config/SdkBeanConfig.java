package top.zy68.fbChainService.config;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SdkBeanConfig
 * @Description JAVA SDK的配置：配置核心类 Client。
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/

@Configuration
@Slf4j
public class SdkBeanConfig {

    @Autowired
    private FbSystemConfig config;

    /**
     * 注入Bean Client
     *
     * @return Client
     * @throws Exception 异常
     */
    @Bean
    public Client client() throws Exception {
        String certPaths = this.config.getCertPath();
        String[] possibilities = certPaths.split(",|;");
        for (String certPath : possibilities) {
            try {
                ConfigProperty property = new ConfigProperty();
                configNetwork(property);
                configCryptoMaterial(property, certPath);

                ConfigOption configOption = new ConfigOption(property);
                Client client = new BcosSDK(configOption).getClient(config.getGroupId());

                BigInteger blockNumber = client.getBlockNumber().getBlockNumber();
                log.info("Chain connect successful. Current block number {}", blockNumber);

                configCryptoKeyPair(client);
                log.info("is Gm:{}, address:{}", client.getCryptoSuite().cryptoTypeConfig == 1, client.getCryptoSuite().getCryptoKeyPair().getAddress());
                return client;
            } catch (Exception ex) {
                log.error(ex.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                }
            }
        }
        throw new ConfigException("Failed to connect to peers:" + config.getPeers());
    }

    /**
     * 配置网络
     *
     * @param configProperty 配置信息属性application.properties
     */
    public void configNetwork(ConfigProperty configProperty) {
        String peerStr = config.getPeers();
        List<String> peers = Arrays.stream(peerStr.split(",")).collect(Collectors.toList());
        Map<String, Object> networkConfig = new HashMap<>();
        networkConfig.put("peers", peers);

        configProperty.setNetwork(networkConfig);
    }

    /**
     * @param configProperty 配置信息属性application.properties
     * @param certPath       加密证书保存路径/conf
     */
    public void configCryptoMaterial(ConfigProperty configProperty, String certPath) {
        Map<String, Object> cryptoMaterials = new HashMap<>();
        cryptoMaterials.put("certPath", certPath);
        configProperty.setCryptoMaterial(cryptoMaterials);
    }

    /**
     * 配置密钥对
     *
     * @param client client
     */
    public void configCryptoKeyPair(Client client) {
        if (config.getHexPrivateKey() == null || config.getHexPrivateKey().isEmpty()) {
            client.getCryptoSuite().setCryptoKeyPair(client.getCryptoSuite().createKeyPair());
            return;
        }
        String privateKey;
        if (!config.getHexPrivateKey().contains(",")) {
            privateKey = config.getHexPrivateKey();
        } else {
            String[] list = config.getHexPrivateKey().split(",");
            privateKey = list[0];
        }
        if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
            privateKey = privateKey.substring(2);
            config.setHexPrivateKey(privateKey);
        }
        client.getCryptoSuite().setCryptoKeyPair(client.getCryptoSuite().createKeyPair(privateKey));
    }
}
