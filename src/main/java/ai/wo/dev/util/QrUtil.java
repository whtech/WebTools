package ai.wo.dev.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QrUtil {

    public static String genQR(String value) {

        try {
            MessageDigest md5;
            md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(value));
            String hash = String.format("%032x", new BigInteger(1, md5.digest()));
            String filename = "./temp/" + hash + ".jpg";

            File file = new File(filename);
            if (!file.exists()) {
                file.mkdirs();
                Files.copy(QRCode.from(value).withSize(250, 250).to(ImageType.JPG).file().toPath(), file.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return filename;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private final static String DIR = "./images/";
    private final static String ext = ".png";
    //private final String CONTENT = "some content here";
    //private final static int WIDTH = 300;
    //private final static int HEIGHT = 300;

    private static boolean isNull(String v){
        if(v == null){
            return true;
        }
        if(v.trim().equalsIgnoreCase("")){
            return true;
        }
        if(v.trim().equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }
    public static String generate(String content, String label, String logo, int width, int height) {
        // Create new configuration that specifies the error correction
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            // init directory
            //cleanDirectory(DIR);
            initDirectory(DIR);
            // Create a qr code with the url as content and a size of WxH px
            bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // Load QR image
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

            // Initialize combined image
            BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();

            // Write QR code to new image at position 0/0
            g.drawImage(qrImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            try {
                if (!isNull(logo)) {
                    // Load logo image
                    BufferedImage overly = getOverly(logo);

                    if (overly == null) {
                        int overlyWidth = (int) (overly.getWidth() * caculateLogoScale(qrImage, overly));
                        int overlyHeight = (int) (overly.getHeight() * caculateLogoScale(qrImage, overly));

                        // Calculate the delta height and width between QR code and logo
                        int deltaHeight = qrImage.getHeight() - overlyHeight;
                        int deltaWidth = qrImage.getWidth() - overlyWidth;


//            int logoWidth = (int) (qrImage.getWidth() * 0.75 * 0.25);
//            int logoHeight = (int) (qrImage.getHeight() * 0.75 * 0.25);
//
//            g.setColor(Color.RED);
//            g.drawRect((qrImage.getWidth() - logoWidth) / 2, (qrImage.getHeight() - logoHeight) / 2, logoWidth, logoHeight);

                        // Write logo into combine image at position (deltaWidth / 2) and
                        // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
                        // the same space for the logo to be centered
                        //g.drawImage(overly, (int) Math.round((qrImage.getWidth() - overly.getWidth() * caculateLogoScale(qrImage, overly) / 2) / 2), (int) Math.round(deltaHeight / 2), overly.getWidth() / 2, overly.getHeight() / 2, null);
                        g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), overlyWidth, overlyHeight, null);
                    }
                }

            } catch (IOException ioe) {

            }
            // Write label into combine image at position
            if (label != null) {
                g.setColor(Color.black);
                g.drawString(label, Math.round((qrImage.getWidth() - label.getBytes("GBK").length * 6) / 2), (int) (qrImage.getHeight() * 0.95));
            }
            // Write combined image as PNG to OutputStream
            ImageIO.write(combined, "png", os);

            String fileName = DIR + generateRandomTitle(new Random(), 9) + ext;
            // Store Image
            Files.copy(new ByteArrayInputStream(os.toByteArray()), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (WriterException e) {
            e.printStackTrace();
            //LOG.error("WriterException occured", e);
        } catch (IOException e) {
            e.printStackTrace();
            //LOG.error("IOException occured", e);
        }
        return null;
    }

    private static double caculateLogoScale(BufferedImage background, BufferedImage logo) {

        double scaleHeight = logo.getHeight() > (background.getHeight() * 0.75 * 0.25) ? (background.getHeight() * 0.75 * 0.25) : logo.getHeight();

        double scaleWidth = logo.getWidth() > (background.getWidth() * 0.75 * 0.25) ? (background.getWidth() * 0.75 * 0.25) : logo.getWidth();

        return scaleWidth / logo.getWidth() > scaleHeight / logo.getHeight() ? scaleHeight / logo.getHeight() : scaleWidth / logo.getWidth();
        //return (int) (weightHeight > weightWidth ? weightWidth : weightHeight);
    }

    private static BufferedImage getOverly(String LOGO) throws IOException {
        URL url = new URL(LOGO);
        return ImageIO.read(url);
    }

    private static void initDirectory(String DIR) throws IOException {
        Files.createDirectories(Paths.get(DIR));
    }

    private static void cleanDirectory(String DIR) {
        try {
            Files.walk(Paths.get(DIR), FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            // Directory does not exist, Do nothing
        }
    }

    private static MatrixToImageConfig getMatrixConfig() {
        // ARGB Colors
        // Check Colors ENUM
        //return new MatrixToImageConfig(QrUtil.Colors.WHITE.getArgb(), QrUtil.Colors.ORANGE.getArgb());
        return new MatrixToImageConfig(Colors.BLACK.getArgb(), Colors.WHITE.getArgb());

    }

    private static String generateRandomTitle(Random random, int length) {
        return random.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public enum Colors {

        BLUE(0xFF40BAD0),
        RED(0xFFE91C43),
        PURPLE(0xFF8A4F9E),
        ORANGE(0xFFF4B13D),
        WHITE(0xFFFFFFFF),
        BLACK(0xFF000000);

        private final int argb;

        Colors(final int argb) {
            this.argb = argb;
        }

        public int getArgb() {
            return argb;
        }
    }

    public static void main(String[] args) {
        generate("hello", "123", "http://cms-bucket.nosdn.127.net/2018/05/31/c195074ad9f04c68bea4a46b58b11079.png?imageView&thumbnail=90y90&quality=85", 500, 500);
    }
}
