package ServerExchange;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dryush on 18.02.2018.
 */

public class Password {

    static MessageDigest md = null;
    public static String encode(String pass){
            String res = pass;
            try {
                if (md == null)
                    md = MessageDigest.getInstance("MD5");
                md.update(pass.getBytes());

                byte byteData[] = md.digest();

                //конвертируем байт в шестнадцатеричный формат
                StringBuffer sb = new StringBuffer();
                for (byte aByteData : byteData) {
                    sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
                }

                res = sb.toString();

            } catch (NoSuchAlgorithmException except){
                //Не может быть такого
            }
            return res;
    }
}
