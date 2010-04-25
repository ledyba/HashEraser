package hasheraser;

import java.security.*;
import java.io.*;

/**
 * <p>�^�C�g��: HashEraser</p>
 *
 * <p>�־: �n�b�V���쩂č폜���܂�</p>
 *
 * <p>���쌠: Copyright (c) 2007 PSi</p>
 *
 * <p>��Ж�: </p>
 *
 * @author �����
 * @version 1.0
 */
public class Hash {
    private MessageDigest Digest;
    private byte[] Mem = new byte[1024*1024];//1MB
    public Hash() {
        try {
            Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] getHash(File file) {
        InputStream is = null;
        Digest.reset();
        try {
            int read = 0;
            is = new FileInputStream(file);
            while(is.available() > 0){
                read = is.read(Mem, 0, Mem.length);
                Digest.update(Mem,0,read);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        	try {
				is.close();
			} catch (IOException e) {}
        }
        return Digest.digest();
    }
    public static boolean comp(byte[] check_1,byte[] check_2){
        return MessageDigest.isEqual(check_1,check_2);
    }
    public static String toString(byte[] hash){
        String ret = "";
        for(int i=0;i<hash.length;i++){
            int b = hash[i];
            if(b < 0) b+=256;
            if(b<0x10){
                ret+="0";
            }
            ret+=Integer.toHexString(b);
        }
        return ret;
    }
}
