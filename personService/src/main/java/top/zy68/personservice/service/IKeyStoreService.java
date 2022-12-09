package top.zy68.personservice.service;


import top.zy68.personservice.exception.BaseException;
import top.zy68.personservice.pojo.KeyStoreInfo;

public interface IKeyStoreService {

    /**
     * get KeyStoreInfo by privateKey.
     * @param privateKeyRaw hex format
     * @param encryptType 1: guomi, 0: standard
     */
    KeyStoreInfo getKeyStoreFromPrivateKey(String privateKeyRaw, int encryptType,String password) throws Exception;


    KeyStoreInfo newKeyByType(int encryptType,String password) throws Exception;
}
