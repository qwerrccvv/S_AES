public class GF4 {
    // 计算x^n * a在模x^4 + x + 1下的乘积
    private static int[] xFx(int[] a) {
        int[] xfx = new int[4];
        if (a[0] == 0) {
            System.arraycopy(a, 1, xfx, 0, 3);
        } else {
            xfx[1] = a[2];
            xfx[2] = a[3] == 1 ? 0 : 1;
            xfx[3] = 1;
        }
        return xfx;
    }

    // 在GF(2^4)上进行乘法运算
    public static String Mutilpy(String Sa, String Sb) {

        int[] a = new int[4];
        int[] b = new int[4];
        for (int i = 0; i < 4; i++) {
            a[i] = Integer.parseInt(Sa.substring(i, i+1));
            b[i] = Integer.parseInt(Sb.substring(i, i+1));
        }

        int[] IntResult = new int[4];
        for (int i = 0; i < 4; i++) {
            IntResult[i] = 0;
        }

        int[] xfx = xFx(a);
        int[] x2fx = xFx(xfx);
        int[] x3fx = xFx(x2fx);

        // 根据b的系数进行异或操作
        if (b[0] == 1) {
            for (int i = 0; i < 4; i++) {
                IntResult[i] ^= x3fx[i];
            }
        }
        if (b[1] == 1) {
            for (int i = 0; i < 4; i++) {
                IntResult[i] ^= x2fx[i];
            }
        }
        if (b[2] == 1) {
            for (int i = 0; i < 4; i++) {
                IntResult[i] ^= xfx[i];
            }
        }
        if (b[3] == 1) {
            for (int i = 0; i < 4; i++) {
                IntResult[i] ^= a[i];
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(IntResult[i]);
        }

        return sb.toString();
    }

}
