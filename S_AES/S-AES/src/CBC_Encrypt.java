import java.util.function.DoubleUnaryOperator;

public class CBC_Encrypt {
    //生成初始16位向量
    public static String oriVecGen(int i){
        int priNum1 = 82129;
        int priNum2 = 65983;

        double intAns = (i * priNum1) % priNum2 % Math.pow(2, 16);
        String ans = Integer.toBinaryString((int)intAns);
        while (ans.length()<16){
            ans = "0" + ans;
        }
        return ans;
    }

    //CBC加密
    public static String CBC_encrypt(String str, String key){
        String Vec = oriVecGen(str.length() / 16);
        String ans = "";

        for (int i = 0; i < str.length(); i+=16) {
            String sub = str.substring(i, i+16);
            sub = S_AES.xor(sub, Vec);
            Vec = S_AES.encrypt(sub, key);
            ans = ans + Vec;
        }

        return ans;
    }

    //CBC解密
    public static String CBC_decrypt(String str, String key){
        String Vec = oriVecGen(str.length() / 16);
        String ans = "";

        for (int i = 0; i < str.length(); i+=16) {
            String sub = str.substring(i, i+16);
            String tmp = S_AES.decrypt(sub, key);
            ans = ans + S_AES.xor(Vec, tmp);
            Vec = sub;
        }

        return ans;
    }

    //篡改密文分组下标为i的bit
    public static String encChange(String enc, int i){
        char tmp = enc.charAt(i) == '0' ? '1' : '0';
        return enc.substring(0, i) + tmp + enc.substring(i+1);
    }

    public static void main(String[] args) {
        String ori = "1100011010100101" + "1111000000001111" + "1010101010101010" + "0101010101010101";
        String key = "1001011010100101";
        String enc = CBC_encrypt(ori, key);
        String dec = CBC_decrypt(enc, key);
        System.out.println("ori:         " + ori);
        System.out.println("enc:         " + enc);
        System.out.println("dec:         " + dec);
        if (ori.equals(dec)){
            System.out.println("ori and dec is same");
        }
        String chdEnc = encChange(enc, 63);
        String chdDec = CBC_decrypt(chdEnc, key);
        System.out.println("changed enc: " + chdEnc);
        System.out.println("changed dec: " + chdDec);
    }
}
