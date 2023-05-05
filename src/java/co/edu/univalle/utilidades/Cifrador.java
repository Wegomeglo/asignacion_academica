package co.edu.univalle.utilidades;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Cifrador {

    private static final String SECURE_KEY = "M6C$Y_TO@Mgox+zs";
    private static final String SECURE_IV = "|b-3xZqAvckr/ZtS";
    private static final String ALGORITHM = "AES";
    private static final String CIPHERTRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static String cifrar(String date) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHERTRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECURE_KEY.getBytes("UTF-8"), ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(SECURE_IV.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encVal = cipher.doFinal(date.getBytes());
            return Base64.encodeBase64String(encVal);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
