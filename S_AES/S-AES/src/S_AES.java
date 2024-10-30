import javax.swing.*;

public class S_AES {
    public static String key0, key1, key2;
    public static String[] Rcon = new String[]{"10000000", "00110000"};
    public static String[][] S_Box = new String[][]{
            {"1001", "0100", "1010", "1011"},
            {"1101", "0001", "1000", "0101"},
            {"0110", "0010", "0000", "0011"},
            {"1100", "1110", "1111", "0111"}
    };
    public static String[][] IS_Box = new String[][]{
            {"1010", "0101", "1001", "1011"},
            {"0001", "0111", "1000", "1111"},
            {"0110", "0000", "0010", "0011"},
            {"1100", "0100", "1101", "1110"}
    };

    //异或操作
    public static String xor(String str1, String str2){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str1.length(); i++){
            sb.append((str1.charAt(i) == str2.charAt(i)) ? '0' : '1');
        }
        return sb.toString();
    }

    //Box替换
    public static String Boxch(String str, String[][] box){
        int R = Integer.parseInt(str.substring(0, 1)) * 2 + Integer.parseInt(str.substring(1, 2));
        int L = Integer.parseInt(str.substring(2, 3)) * 2 + Integer.parseInt(str.substring(3, 4));
        return box[R][L];
    }

    //g函数
    public static String g(String inW, String rcon){
        StringBuilder sb = new StringBuilder();
        sb.append(Boxch(inW.substring(4), S_Box));
        sb.append(Boxch(inW.substring(0, 4), S_Box));
        return xor(sb.toString(), rcon);
    }

    //密钥生成
    public static void KeyGeneration(String k){
        key0 = k;
        String[] W = new String[6];
        W[0] = k.substring(0, 8);
        W[1] = k.substring(8);
        W[2] = xor(W[0], g(W[1], Rcon[0]));
        W[3] = xor(W[2], W[1]);
        W[4] = xor(W[2], g(W[3], Rcon[1]));
        W[5] = xor(W[4], W[3]);
        key1 = W[2] + W[3];
        key2 = W[4] + W[5];
    }

    //轮密钥加
    public static String KeyAdd(String str1, String str2){
        return xor(str1, str2);
    }

    //半字节代替
    public static String NS(String str){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 12; i+=4) {
            sb.append(Boxch(str.substring(i, i+4), S_Box));
        }
        return sb.toString();
    }

    //逆半字节代替
    public static String INS(String str){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 12; i+=4) {
            sb.append(Boxch(str.substring(i, i+4), IS_Box));
        }
        return sb.toString();
    }

    //行移位
    public static String SR(String str){
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 4));
        sb.append(str.substring(12, 16));
        sb.append(str.substring(8, 12));
        sb.append(str.substring(4, 8));
        return sb.toString();
    }

    //逆行移位
    public static String ISR(String str){
        return SR(str);
    }

    //列混淆
    public static String MC(String str){
        String[][] S = new String[2][2];
        S[0][0] = str.substring(0, 4);
        S[1][0] = str.substring(4, 8);
        S[0][1] = str.substring(8, 12);
        S[1][1] = str.substring(12, 16);
        String[][] anS = new String[2][2];
        anS[0][0] = xor(S[0][0], GF4.Mutilpy("0100", S[1][0]));
        anS[1][0] = xor(GF4.Mutilpy("0100", S[0][0]), S[1][0]);
        anS[0][1] = xor(S[0][1], GF4.Mutilpy("0100", S[1][1]));
        anS[1][1] = xor(GF4.Mutilpy("0100", S[0][1]), S[1][1]);
        return anS[0][0] + anS[1][0] + anS[0][1] + anS[1][1];
    }

    //逆列混淆
    public static String IMC(String str){
        String[][] S = new String[2][2];
        S[0][0] = str.substring(0, 4);
        S[1][0] = str.substring(4, 8);
        S[0][1] = str.substring(8, 12);
        S[1][1] = str.substring(12, 16);
        String[][] anS = new String[2][2];
        anS[0][0] = xor(GF4.Mutilpy("1001", S[0][0]), GF4.Mutilpy("0010", S[1][0]));
        anS[1][0] = xor(GF4.Mutilpy("0010", S[0][0]), GF4.Mutilpy("1001", S[1][0]));
        anS[0][1] = xor(GF4.Mutilpy("1001", S[0][1]), GF4.Mutilpy("0010", S[1][1]));
        anS[1][1] = xor(GF4.Mutilpy("0010", S[0][1]), GF4.Mutilpy("1001", S[1][1]));
        return anS[0][0] + anS[1][0] + anS[0][1] + anS[1][1];
    }

    //加密
    public static String encrypt(String str, String key){
        KeyGeneration(key);

        String tmp = str;
        tmp = KeyAdd(tmp, key0);

        tmp = NS(tmp);
        tmp = SR(tmp);
        tmp = MC(tmp);
        tmp = KeyAdd(tmp, key1);

        tmp = NS(tmp);
        tmp = SR(tmp);
        tmp = KeyAdd(tmp, key2);

        return tmp;
    }

    //解密
    public static String decrypt(String str, String key){
        KeyGeneration(key);

        String tmp = str;
        tmp = KeyAdd(tmp, key2);

        tmp = ISR(tmp);
        tmp = INS(tmp);
        tmp = KeyAdd(tmp, key1);
        tmp = IMC(tmp);

        tmp = ISR(tmp);
        tmp = INS(tmp);
        tmp = KeyAdd(tmp, key0);

        return tmp;
    }

    //将单个ascll字符转为八位二进制
    public static String AtoB(char a){
        String binaryString = Integer.toBinaryString((int) a);
        while (binaryString.length() < 8) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

    //将八位二进制转为ascll字符
    public static String BtoA(String str){
        int decimal = Integer.parseInt(str, 2);
        return String.valueOf((char) decimal);
    }

    //长二进制字符串加密
    public static String Long_encrypt(String str, String key){
        int i = str.length() % 16;
        if(i == 0) {
            str = str + "";
        }else {
            str = str + "00000000";
        }


        StringBuilder sb = new StringBuilder();
        for(i=0; i<str.length(); i+=16){
            String tmp = str.substring(i, i+16);
            sb.append(encrypt(tmp, key));
        }
        return sb.toString();
    }

    //长二进制字符串解密
    public static String Long_decrypt(String str, String key){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<str.length(); i+=16){
            String tmp = str.substring(i, i+16);
            sb.append(decrypt(tmp, key));
        }

        if(!sb.substring(sb.length()-8).equals("00000000")){
            return sb.toString();
        }else {
            return sb.substring(0, sb.length()-8);
        }
    }

    //长ascll字符串加密
    public static String ascEncrypt(String str, String key){
        String Bstr = "";
        for (int i = 0; i < str.length(); i++) {
            Bstr = Bstr + AtoB(str.charAt(i));
        }

        String tmp = Long_encrypt(Bstr, key);

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<tmp.length(); i+=8){
            sb.append(BtoA(tmp.substring(i, i+8)));
        }
        return sb.toString();
    }

    //长ascll字符串解密
    public static String ascDecrypt(String str, String key){
        String tmp = "";
        for(int i=0; i<str.length(); i++){
            tmp = tmp + AtoB(str.charAt(i));
        }

        String Bstr = Long_decrypt(tmp, key);

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<Bstr.length(); i+=8){
            sb.append(BtoA(Bstr.substring(i, i+8)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String key = "1100101100111010";

//        String enc = ascEncrypt("AbcdejkK", key);
//        System.out.println(enc);
//        String dec = ascDecrypt(enc, key);
//        System.out.println(dec);
        System.out.println(encrypt("1010010110101100", key));
    }
}