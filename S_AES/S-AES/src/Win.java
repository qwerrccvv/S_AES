import javax.swing.*;
import java.awt.*;

//登录界面
public class Win extends JFrame {
    public JLabel input_lbl, key_lbl, output_lbl;
    public JTextField key_tbx, input_tbx, output_tbx;
    public JButton enc_btn, dec_btn;
    public JRadioButton asc_rbtn;
    public Font btn_font = new Font("宋体",Font.BOLD, 25);    //按钮字体
    public Font lbl_font = new Font("宋体", Font.BOLD, 30);   //标签字体

    Win(){
        //输入标签
        input_lbl = new JLabel("输入");
        input_lbl.setFont(btn_font);
        input_lbl.setBounds(30, 10, 100, 75);
        add(input_lbl);

        //密钥标签
        key_lbl = new JLabel("密钥");
        key_lbl.setFont(btn_font);
        key_lbl.setBounds(30, 85, 100, 75);
        add(key_lbl);

        //输出标签
        output_lbl = new JLabel("输出");
        output_lbl.setFont(btn_font);
        output_lbl.setBounds(30, 160, 100, 75);
        add(output_lbl);

        //输入框
        input_tbx = new JTextField();
        input_tbx.setFont(btn_font);
        input_tbx.setBounds(130, 25, 350, 50);
        add(input_tbx);

        //密钥框
        key_tbx = new JTextField();
        key_tbx.setFont(btn_font);
        key_tbx.setBounds(130, 100, 400, 50);
        add(key_tbx);

        //输出框
        output_tbx = new JTextField();
        output_tbx.setFont(btn_font);
        output_tbx.setBounds(130, 175, 400, 50);
        add(output_tbx);

        //ascll选项
        asc_rbtn = new JRadioButton("ascll");
        asc_rbtn.setFont(btn_font);
        asc_rbtn.setBounds(490, 30, 100, 40);
        add(asc_rbtn);

        //加密按钮
        enc_btn = new JButton("加密");
        enc_btn.setFont(btn_font);
        enc_btn.setBounds(100, 270, 150, 50);
        add(enc_btn);
        enc_btn.addActionListener(e -> {
            String inp = input_tbx.getText();
            String key = key_tbx.getText();
            String otp;
            if(asc_rbtn.isSelected()){
                otp = S_AES.ascEncrypt(inp, key);
            }else {
                otp = S_AES.Long_encrypt(inp, key);
            }
            output_tbx.setText(otp);
        });

        //解密按钮
        dec_btn = new JButton("解密");
        dec_btn.setFont(btn_font);
        dec_btn.setBounds(350, 270, 150, 50);
        add(dec_btn);
        dec_btn.addActionListener(e -> {
            String inp = input_tbx.getText();
            String key = key_tbx.getText();
            String otp;
            if(asc_rbtn.isSelected()){
                otp = S_AES.ascDecrypt(inp, key);
            }else {
                otp = S_AES.Long_decrypt(inp, key);
            }
            output_tbx.setText(otp);
        });

        //界面设置
        setSize(600,400);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Win win = new Win();
    }
}