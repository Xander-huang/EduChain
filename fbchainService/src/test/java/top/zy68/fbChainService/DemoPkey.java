package top.zy68.fbChainService;

import cn.hutool.crypto.SecureUtil;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.SM2KeyPair;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;

public class DemoPkey {

    @Test
    public void keyGeneration() throws Exception {
        //ECDSA key generation
        CryptoKeyPair ecdsaKeyPair = new ECDSAKeyPair().generateKeyPair();
        System.out.println("ecdsa private key :" + ecdsaKeyPair.getHexPrivateKey());
        System.out.println("ecdsa public key :" + ecdsaKeyPair.getHexPublicKey());
        System.out.println("ecdsa address :" + ecdsaKeyPair.getAddress());
        //SM2 key generation
        CryptoKeyPair sm2KeyPair = new SM2KeyPair().generateKeyPair();
        System.out.println("sm2 private key :" + sm2KeyPair.getHexPrivateKey());
        System.out.println("sm2 public key :" + sm2KeyPair.getHexPublicKey());
        System.out.println("sm2 address :" + sm2KeyPair.getAddress());
    }


//    ecdsa加密测试
    /**
     * 公钥加密
     *
     * @param data   待加密数据
     * @param publicKey   公钥
     * @return byte[]  加密数据
     * @throws Exception
     */
    @Test
    public void encryptByPublicKey() {
        try {
            KeyPair keyPair = SecureUtil.generateKeyPair("ECIES");
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            byte[] data = "computer".getBytes(StandardCharsets.UTF_8);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("ECIES", "BC");//写不写 BC都可以，都是会选择BC实现来做
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] data1 = cipher.doFinal(data);
            System.out.println(Arrays.toString(data1));
            System.out.println("````````````````````````````````````````````");


            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher2 = Cipher.getInstance("ECIES","BC");
            cipher2.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] data2 = cipher2.doFinal(data1);
            System.out.println(new String(data2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 私钥解密
     *
     * @param data    待解密数据
     * @param privateKey    私钥
     * @return byte[]   解密数据
     * @throws Exception
     */
//    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey)
//            throws Exception {
////        keyGen.generateKeyPair();
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        Cipher cipher = Cipher.getInstance("ECIES","BC");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        return cipher.doFinal(data);
//    }









}
