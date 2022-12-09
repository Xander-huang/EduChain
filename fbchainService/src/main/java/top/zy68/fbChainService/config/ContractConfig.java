package top.zy68.fbChainService.config;

import lombok.Data;

/**
 * @ClassName ContractConfig
 * @Description 合约配置类：用于封装所有合约地址
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/
@Data
public class ContractConfig {
    private String courseSolAddress;

    private String eduRecordSolAddress;

    private String personSolAddress;

    private String achieveSolAddress;
}
