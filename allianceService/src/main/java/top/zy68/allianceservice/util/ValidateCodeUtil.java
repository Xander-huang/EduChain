package top.zy68.allianceservice.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * @ClassName ValidateCodeUtil
 * @Description 生成动态验证码图片
 * @Author Sustart
 * @Date2021/12/22 14:24
 * @Version 1.0
 **/
@Slf4j
public class ValidateCodeUtil {
    private static final Random RANDOM = new Random();
    private static final int IMAGE_WIDTH = 160;
    private static final int IMAGE_HEIGHT = 40;
    /**
     * 干扰线数量
     */
    private static final int LINE_SIZE = 30;
    /**
     * 验证码字符个数
     */
    private static final int CHAR_NUM = 4;
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 获取随机生成的验证码
     *
     * @return 验证码字符串
     */
    public static String getCaptcha() {
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < CHAR_NUM; i++) {
            captcha.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return captcha.toString();
    }

    /**
     * 获取验证码的Bases64编码图片
     *
     * @param captcha 验证码字符串
     * @return 编码
     */
    public static String getCaptchaImg(String captcha) {
        ValidateCodeUtil validateCodeUtil = new ValidateCodeUtil();
        return validateCodeUtil.randomCodeBase64(captcha);
    }

    /**
     * 生成验证码图片后进行Base64编码
     *
     * @param validateCode 已生成的验证码，用于生成图片
     * @return 图片编码序列
     */
    private String randomCodeBase64(String validateCode) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.getGraphics();
        //设置图片基本属性
        graphics.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        graphics.setColor(getRandomColor(105, 189));
        graphics.setFont(getFont());

        // 绘制干扰线
        drawLine(graphics);
        // 绘制验证码
        drawString(graphics, validateCode);
        // 图片处理
        graphics.dispose();

        // 进行Base64编码
        String base64String = "";
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", bos);
            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64String = encoder.encodeToString(bytes);
        } catch (Exception e) {
            log.error("对验证码图片进行Base64编码失败" + e);
        }

        return "data:image/png;base64," + base64String;
    }

    /**
     * 获取/设置字体
     *
     * @return 字体对象
     */
    private Font getFont() {
        return new Font("Times New Roman", Font.PLAIN, 40);
    }

    /**
     * 随机获取颜色RBG码
     *
     * @param fc 颜色数极值
     * @param bc 颜色数极值
     * @return 颜色对象
     */
    private Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + RANDOM.nextInt(bc - fc - 16);
        int g = fc + RANDOM.nextInt(bc - fc - 14);
        int b = fc + RANDOM.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    /**
     * 绘制干扰线
     *
     * @param graphics 图对象
     */
    private void drawLine(Graphics graphics) {
        int x, y, xl, yl;
        graphics.setFont(getFont());
        // 每根线一个随机坐标
        for (int i = 0; i < LINE_SIZE; i++) {
            graphics.setColor(getRandomColor(108, 190));
            x = RANDOM.nextInt(IMAGE_WIDTH);
            y = RANDOM.nextInt(IMAGE_HEIGHT);
            xl = RANDOM.nextInt(20);
            yl = RANDOM.nextInt(10);
            graphics.drawLine(x, y, x + xl, y + yl);
        }
    }

    /**
     * 绘制字符串
     *
     * @param graphics  图对象
     * @param randomStr 随机字符的字符串
     */
    private void drawString(Graphics graphics, String randomStr) {
        graphics.setFont(getFont());
        char[] chars = randomStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // 每个字符一种颜色
            graphics.setColor(getRandomColor(108, 190));
            graphics.translate(RANDOM.nextInt(3), RANDOM.nextInt(6));
            graphics.drawString(String.valueOf(chars[i]), 40 * i + 10, 25);
        }
    }

    /*
     *  保留直接生成随机图片方法
     */
    // public void getRandomCodeImage(HttpServletRequest request, HttpServletResponse response) {
    //     HttpSession session = request.getSession();
    //     // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
    //     BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    //     Graphics g = image.getGraphics();
    //     g.fillRect(0, 0, width, height);
    //     g.setColor(getRandomColor(105, 189));
    //     g.setFont(getFont());
    //
    //     // 绘制干扰线
    //     for (int i = 0; i < lineSize; i++) {
    //         drawLine(g);
    //     }
    //
    //     // 绘制随机字符
    //     String random_string = "";
    //     for (int i = 0; i < stringNum; i++) {
    //         random_string = drawString(g, random_string, i);
    //     }
    //
    //     System.out.println(random_string);
    //
    //     g.dispose();
    //
    //     session.removeAttribute(sessionKey);
    //     session.setAttribute(sessionKey, random_string);
    //
    //     String base64String = "";
    //     try {
    //         //  直接返回图片
    //         ImageIO.write(image, "PNG", response.getOutputStream());
    //
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}
