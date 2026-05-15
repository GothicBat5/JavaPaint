import javax.crypto.Cipher; 
import javax.crypto.KeyGenerator; 
import javax.crypto.SecretKey; 
import java.security.MessageDigest; 
import java.util.Base64; 

public class encrypt 
{

    public static String caesarEncrypt(String text, int shift)
    {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray())
        {
            result.append((char)(c + shift));
        }

        return result.toString();
    }

    public static String toBinary(String text)
    {
        StringBuilder binary = new StringBuilder();

        for (char c : text.toCharArray())
        {
            binary.append(String.format("%8s",
                    Integer.toBinaryString(c))
                    .replace(' ', '0'));

            binary.append(" ");
        }

        return binary.toString();
    }

    public static void printMatrix(String text)
    {
        System.out.println("\nCharacter Matrix:");

        for (char c : text.toCharArray())
        {
            System.out.println(
                    c + " -> ASCII: " + (int)c
            );
        }
    }

    public static void main(String[] args) throws Exception 
  {

        String password = "BayHarbourButcher";

        System.out.println("Original:");
        System.out.println(password);

        String binary = toBinary(password);

        System.out.println("\nBinary:");
        System.out.println(binary);

        String caesar = caesarEncrypt(password, 3);

        System.out.println("\nCaesar Cipher (+3):");
        System.out.println(caesar);

        printMatrix(password);

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] hash = md.digest(password.getBytes());

        StringBuilder hashHex = new StringBuilder();

        for (byte b : hash)
        {
            hashHex.append(String.format("%02x", b));
        }

        System.out.println("\nSHA-256 Hash:");
        System.out.println(hashHex);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");

        keyGen.init(128);

        SecretKey secretKey = keyGen.generateKey();

        System.out.println("\nAES Secret Key:");

        String encodedKey = Base64.getEncoder().encodeToString( secretKey.getEncoded());

        System.out.println(encodedKey);

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encrypted = cipher.doFinal(password.getBytes());

        System.out.println("\nEncrypted Bytes:");

        for (byte b : encrypted)
        {
            System.out.print(b + " ");
        }

        String base64 = Base64.getEncoder().encodeToString(encrypted);

        System.out.println("\n\nBase64:");
        System.out.println(base64);
    }
}
