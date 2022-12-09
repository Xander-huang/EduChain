package top.zy68.fbChainService.model.chainMainInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EduRecordMainInfo {
    private String eduId;

    private String personId;

    private String eduType;

    private BigInteger nodeId;

    private String nodeName;

    private String beginTime;

    private String endTime;

    private String majorName;

    private BigInteger acquireCredit;

    private BigInteger graduateCredit;

    private String certifyFile;

}
