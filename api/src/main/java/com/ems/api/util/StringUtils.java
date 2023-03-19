package com.ems.api.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Map;

public class StringUtils {

    public static String nvl(String str, String nvl) {
        return str != null ? str : nvl;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    @SneakyThrows
    public static String updateToCloudinary(String path, String barcodeText) {

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);


        File currDir = new File(path + "/.");
        String currDirAbsolutePath = currDir.getAbsolutePath();
        long currentMillis = System.currentTimeMillis();
        String fileName = currentMillis+".png";
        String fileLocation = path.substring(0, path.length() - 1) + fileName;
        File newFile = File.createTempFile(fileLocation,"png");  //new File(currDirAbsolutePath + fileName);

        ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", newFile);
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dvguzfw8w",
                "api_key", "642877532248482",
                "api_secret", "azo5372yoeMZC2TbEhZakzHn-i4",
                "secure",true));
        Map uploadResult = cloudinary.uploader().upload(newFile,ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }
}
