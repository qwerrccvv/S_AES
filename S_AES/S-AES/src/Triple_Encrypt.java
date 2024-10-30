public class Triple_Encrypt {
    //使用48bits的模式进行三重加密

    //三重加密
    public static String Triple_encrypt(String str, String key){
        String Key1 = key.substring(0, 16);
        String key2 = key.substring(16, 32);
        String key3 = key.substring(32);

        String tmp = S_AES.encrypt(str, Key1);
        tmp = S_AES.encrypt(tmp, key2);
        return S_AES.encrypt(tmp, key3);
    }

    //三重解密
    public static String Triple_decrypt(String enc, String key){
        String Key1 = key.substring(0, 16);
        String key2 = key.substring(16, 32);
        String key3 = key.substring(32);

        String tmp = S_AES.decrypt(enc, key3);
        tmp = S_AES.decrypt(tmp, key2);
        return S_AES.decrypt(tmp, Key1);
    }

    public static void main(String[] args) {
        String key = "1010010110010110" + "1110000110000111" + "1111000011000011";
        String ori = "1001100000111100";
        String enc = Triple_encrypt(ori, key);
        String dec = Triple_decrypt(enc, key);
        System.out.println("enc: " + enc);
        System.out.println("dec: " + dec);
    }
}
