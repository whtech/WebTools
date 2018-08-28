package ai.wo.dev.controller;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@Controller
public class QRTools {

    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;
    private static final String QR_FILE_TYPE = "png";


    private void downloadQr(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String data) throws WriterException, IOException {
        String dataHandle = new String(data.getBytes("UTF8"), "UTF8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(dataHandle, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);

        httpServletResponse.reset();//清空输出流

        OutputStream os = httpServletResponse.getOutputStream();//取得输出流
        MatrixToImageWriter.writeToStream(bitMatrix, QR_FILE_TYPE, os);//写入文件刷新

        os.flush();
        os.close();//关闭输出流
    }


    @GetMapping("qr")
    public void getQr(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                      String data) throws IOException, WriterException {
        downloadQr(httpServletRequest, httpServletResponse, data);
    }
}
