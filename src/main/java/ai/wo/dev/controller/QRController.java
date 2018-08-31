package ai.wo.dev.controller;


import ai.wo.dev.util.QrUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
public class QRController {

    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;
    private static final String QR_FILE_TYPE = "png";
    private static final String QR_FILE_NAME = "qr.png";


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

    @GetMapping("qr2")
    public void getQr2(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       String data, String label, String logo) throws IOException, WriterException {
        //downloadQr(httpServletRequest, httpServletResponse, data);
        String fileUrl = QrUtil.generate(data, label, logo, QR_WIDTH, QR_HEIGHT);
        if (fileUrl != null) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
           /* String realPath = request.getServletContext().getRealPath(
                    "//WEB-INF//");*/
            /*File file = new File(realPath, fileName);*/
            File file = new File(fileUrl);
            if (file.exists()) {
                //httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
                //httpServletResponse.addHeader("Content-Disposition",
                //        "attachment;fileName=" + QR_FILE_NAME);// 设置文件名
                httpServletResponse.setContentType("image/png");

                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = httpServletResponse.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }
}
