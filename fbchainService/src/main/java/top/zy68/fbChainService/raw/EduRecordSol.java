package top.zy68.fbChainService.raw;

import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple10;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple11;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import top.zy68.fbChainService.utils.IOUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName EduRecordSol
 * @Description Solidity合约转换得到的Java类。该类为教育记录相关的合约类。
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/

@SuppressWarnings("unchecked")
public class EduRecordSol extends Contract {
    public static final String ABI = IOUtil.readResourceAsString("abi/EduRecordSol.abi");
    public static final String BINARY = IOUtil.readResourceAsString("bin/EduRecordSol.bin");

    public static final String FUNC_INSERT = "insert";
    public static final String FUNC_SELECT = "select";

    public EduRecordSol(String contractAddress, Client client, CryptoKeyPair credential) {
        super(BINARY, contractAddress, client, credential);
    }

    public TransactionReceipt insert(String eduId, String personId, String eduType, BigInteger nodeId, String nodeName, String beginTime, String endTime, String majorName, BigInteger acquireCredit, BigInteger graduateCredit, String certifyFile) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(eduId),
                        new Utf8String(personId),
                        new Utf8String(eduType),
                        new Int256(nodeId),
                        new Utf8String(nodeName),
                        new Utf8String(beginTime),
                        new Utf8String(endTime),
                        new Utf8String(majorName),
                        new Int256(acquireCredit),
                        new Int256(graduateCredit),
                        new Utf8String(certifyFile)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void insert(String eduId, String personId, String eduType, BigInteger nodeId, String nodeName, String beginTime, String endTime, String majorName, BigInteger acquireCredit, BigInteger graduateCredit, String certifyFile, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(eduId),
                        new Utf8String(personId),
                        new Utf8String(eduType),
                        new Int256(nodeId),
                        new Utf8String(nodeName),
                        new Utf8String(beginTime),
                        new Utf8String(endTime),
                        new Utf8String(majorName),
                        new Int256(acquireCredit),
                        new Int256(graduateCredit),
                        new Utf8String(certifyFile)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForInsert(String eduId, String personId, String eduType, BigInteger nodeId, String nodeName, String beginTime, String endTime, String majorName, BigInteger acquireCredit, BigInteger graduateCredit, String certifyFile) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(eduId),
                        new Utf8String(personId),
                        new Utf8String(eduType),
                        new Int256(nodeId),
                        new Utf8String(nodeName),
                        new Utf8String(beginTime),
                        new Utf8String(endTime),
                        new Utf8String(majorName),
                        new Int256(acquireCredit),
                        new Int256(graduateCredit),
                        new Utf8String(certifyFile)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple11<String, String, String, BigInteger, String, String, String, String, BigInteger, BigInteger, String> getInsertInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INSERT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Int256>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Int256>() {
                }, new TypeReference<Int256>() {
                }, new TypeReference<Utf8String>() {
                }));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple11<String, String, String, BigInteger, String, String, String, String, BigInteger, BigInteger, String>(

                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue(),
                (String) results.get(4).getValue(),
                (String) results.get(5).getValue(),
                (String) results.get(6).getValue(),
                (String) results.get(7).getValue(),
                (BigInteger) results.get(8).getValue(),
                (BigInteger) results.get(9).getValue(),
                (String) results.get(10).getValue()
        );
    }

    public Tuple1<BigInteger> getInsertOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_INSERT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {
                }));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
        );
    }

    public Tuple10<String, String, BigInteger, String, String, String, String, BigInteger, BigInteger, String> select(String eduId) throws ContractException {
        final Function function = new Function(FUNC_SELECT,
                Arrays.<Type>asList(new Utf8String(eduId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Int256>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Int256>() {
                }, new TypeReference<Int256>() {
                }, new TypeReference<Utf8String>() {
                }));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple10<String, String, BigInteger, String, String, String, String, BigInteger, BigInteger, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (String) results.get(4).getValue(),
                (String) results.get(5).getValue(),
                (String) results.get(6).getValue(),
                (BigInteger) results.get(7).getValue(),
                (BigInteger) results.get(8).getValue(),
                (String) results.get(9).getValue());
    }

    public static EduRecordSol load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new EduRecordSol(contractAddress, client, credential);
    }

    public static EduRecordSol deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(EduRecordSol.class, client, credential, BINARY, "");
    }
}
