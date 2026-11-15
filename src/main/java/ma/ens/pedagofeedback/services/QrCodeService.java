package ma.ens.pedagofeedback.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class QrCodeService {

    /** Transforme un texte (ici un lien) en image PNG de QR code. */
    public byte[] generer(String texte) {
        try {
            BitMatrix matrice = new QRCodeWriter().encode(texte, BarcodeFormat.QR_CODE, 320, 320);
            ByteArrayOutputStream sortie = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrice, "PNG", sortie);
            return sortie.toByteArray();
        } catch (Exception e) {
            throw new RegleGestionException("Impossible de générer le QR code.");
        }
    }
}