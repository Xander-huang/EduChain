package top.zy68.fbChainService.raw;

import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
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
 * @ClassName PersonSol
 * @Description Solidity合约转换得到的Java类。该类为个人信息相关的合约类。
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/

@SuppressWarnings("unchecked")
public class PersonSol extends Contract {
    public static final String ABI = IOUtil.readResourceAsString("abi/PersonSol.abi");
    public static final String BINARY = IOUtil.readResourceAsString("bin/PersonSol.bin");

    public static final String FUNC_INSERT = "insert";
    public static final String FUNC_SELECT = "select";

    public PersonSol(String contractAddress, Client client, CryptoKeyPair credential) {
        super(BINARY, contractAddress, client, credential);
    }

    public TransactionReceipt insert(String id, String name, String gender, String nation, String birthday) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(id),
                        new Utf8String(name),
                        new Utf8String(gender),
                        new Utf8String(nation),
                        new Utf8String(birthday)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void insert(String id, String name, String gender, String nation, String birthday, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(id),
                        new Utf8String(name),
                        new Utf8String(gender),
                        new Utf8String(nation),
                        new Utf8String(birthday)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForInsert(String id, String name, String gender, String nation, String birthday) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(id),
                        new Utf8String(name),
                        new Utf8String(gender),
                        new Utf8String(nation),
                        new Utf8String(birthday)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple5<String, String, String, String, String> getInsertInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INSERT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<String, String, String, String, String>(

                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (String) results.get(4).getValue()
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

    public Tuple4<String, String, String, String> select(String id) throws ContractException {
        final Function function = new Function(FUNC_SELECT,
                Arrays.<Type>asList(new Utf8String(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<String, String, String, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue());
    }

    public static PersonSol load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new PersonSol(contractAddress, client, credential);
    }

    public static PersonSol deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(PersonSol.class, client, credential, BINARY, "");
    }
}
