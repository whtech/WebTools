package ai.wo.dev.util;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
