import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Double_Encrypt {
    public static List<String> plain = new ArrayList<>(); //明文数组
    public static List<String> cipher = new ArrayList<>(); //密文数组
    public static Map<String, List<String>> encryptionTable = new HashMap<>(); // 存储加密结果和对应的密钥列表
    public static List<String> checkedKey = new ArrayList<>(); //密钥数组

    //双重加密
    public static String Double_encrypt(String str, String KEY){
        String KEY1 = KEY.substring(0, 16);
        String KEY2 = KEY.substring(16);

        String tmp = S_AES.encrypt(str, KEY1);
        return S_AES.encrypt(tmp, KEY2);
    }

    //双重解密
    public static String Double_decrypt(String str, String KEY){
        String KEY1 = KEY.substring(0, 16);
        String KEY2 = KEY.substring(16);

        String tmp = S_AES.decrypt(str, KEY2);
        return S_AES.decrypt(tmp, KEY1);
    }

    // 使用所有可能的key进行加密
    public static void K1enc(String ori) {
        for (int i = 0; i < Math.pow(2, 16); i++) { // 枚举所有可能的16位二进制字符串密钥
            String key = Integer.toBinaryString(i);
            while (key.length() < 16) {
                key = "0" + key;
            }
            String encrypted = S_AES.encrypt(ori, key);
            encryptionTable.putIfAbsent(encrypted, new ArrayList<>());
            encryptionTable.get(encrypted).add(key); // 将加密结果和密钥存储在表中
        }
    }

    // 检查加密结果是否在表中，并返回对应的密钥
    public static List<String> getKeyIfEncrypted(String str) {
        return encryptionTable.getOrDefault(str, new ArrayList<>());
    }

    // 使用所有可能的key进行解密，并破解双重加密
    public static void K2dec(String enc) {
        for (int i=0; i<Math.pow(2, 16); i++){
            String key = Integer.toBinaryString(i);
            while (key.length() < 16){
                key = "0" + key;
            }
            String decrypted = S_AES.decrypt(enc, key);
            List<String> possibleKey = encryptionTable.getOrDefault(decrypted, new ArrayList<>());
            if(!possibleKey.isEmpty()){
                for(int j=0; j<possibleKey.size(); j++){
                    boolean checked = true;
                    for(int k=0; k< plain.size(); k++){
                        if(!S_AES.encrypt(plain.get(k), possibleKey.get(j)).equals(S_AES.decrypt(cipher.get(k), key))){
                            checked = false;
                        }
                    }
                    if(checked){
                        boolean addAble = true;
                        for (int k = 0; k < checkedKey.size(); k++) {
                            if((possibleKey.get(j) + key).equals(checkedKey.get(k))){
                                addAble = false;
                            }
                        }
                        if (addAble){
                            checkedKey.add(possibleKey.get(j) + key);
                        }
                    }
                }
            }
        }
    }

    //双重加密破解
    public static void Double_crack() {
        K1enc(plain.get(0));
        System.out.println("Precomputation done. Starting decryption...");
        K2dec(cipher.get(0));
        System.out.println("Possible keys:");
        for (int i = 0; i < checkedKey.size(); i++) {
            System.out.println(checkedKey.get(i));
        }
    }

    public static void main(String[] args) {
        String key = "0000000000000001" + "0000000000000011";//加密的key
        String ori = "1100110000110011";
        String enc = Double_encrypt(ori, key);
        System.out.println("enc:\n" + enc + "\n");
        plain.add(ori);//明文
        cipher.add(enc);//密文

        ori = "1100110110110011";
        enc = Double_encrypt(ori, key);
        System.out.println("enc:\n" + enc + "\n");
        plain.add(ori);
        cipher.add(enc);

        ori = "1010110110110011";
        enc = Double_encrypt(ori, key);
        System.out.println("enc:\n" + enc + "\n");
        plain.add(ori);
        cipher.add(enc);

        System.out.println("Starting cracking...");
        Double_crack();
        System.out.println("Cracking done.");
    }
}
