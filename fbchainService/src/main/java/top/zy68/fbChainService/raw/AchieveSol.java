package top.zy68.fbChainService.raw;

import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
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

@SuppressWarnings("unchecked")
public class AchieveSol extends Contract {

    public static final String BINARY = IOUtil.readResourceAsString("bin/AchieveSol.bin");
    public static final String ABI = IOUtil.readResourceAsString("abi/AchieveSol.abi");

    public static final String FUNC_INSERT = "insert";

    public static final String FUNC_SELECT = "select";

    public AchieveSol(String contractAddress, Client client, CryptoKeyPair credential) {
        super(BINARY, contractAddress, client, credential);
    }

    public TransactionReceipt insert(String eduId, List<String> titleList, List<String> acquireTimeList, List<String> certifyFileList) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(eduId),
                        titleList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(titleList, Utf8String.class)),
                        acquireTimeList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(acquireTimeList, Utf8String.class)),
                        certifyFileList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(certifyFileList, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void insert(String eduId, List<String> titleList, List<String> acquireTimeList, List<String> certifyFileList, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(eduId),
                        titleList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(titleList, Utf8String.class)),
                        acquireTimeList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(acquireTimeList, Utf8String.class)),
                        certifyFileList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(certifyFileList, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForInsert(String eduId, List<String> titleList, List<String> acquireTimeList, List<String> certifyFileList) {
        final Function function = new Function(
                FUNC_INSERT,
                Arrays.<Type>asList(new Utf8String(eduId),
                        titleList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(titleList, Utf8String.class)),
                        acquireTimeList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(acquireTimeList, Utf8String.class)),
                        certifyFileList.isEmpty() ? DynamicArray.empty("string[]") : new DynamicArray<Utf8String>(
                                org.fisco.bcos.sdk.abi.Utils.typeMap(certifyFileList, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<String, List<String>, List<String>, List<String>> getInsertInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INSERT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<DynamicArray<Utf8String>>() {
                }, new TypeReference<DynamicArray<Utf8String>>() {
                }, new TypeReference<DynamicArray<Utf8String>>() {
                }));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<String, List<String>, List<String>, List<String>>(

                (String) results.get(0).getValue(),
                convertToNative((List<Utf8String>) results.get(1).getValue()),
                convertToNative((List<Utf8String>) results.get(2).getValue()),
                convertToNative((List<Utf8String>) results.get(3).getValue())
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

    public Tuple3<List<String>, List<String>, List<String>> select(String eduId) throws ContractException {
        final Function function = new Function(FUNC_SELECT,
                Arrays.<Type>asList(new Utf8String(eduId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {
                }, new TypeReference<DynamicArray<Utf8String>>() {
                }, new TypeReference<DynamicArray<Utf8String>>() {
                }));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<List<String>, List<String>, List<String>>(
                convertToNative((List<Utf8String>) results.get(0).getValue()),
                convertToNative((List<Utf8String>) results.get(1).getValue()),
                convertToNative((List<Utf8String>) results.get(2).getValue()));
    }

    public static AchieveSol load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AchieveSol(contractAddress, client, credential);
    }

    public static AchieveSol deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(AchieveSol.class, client, credential, BINARY, "");
    }
}
