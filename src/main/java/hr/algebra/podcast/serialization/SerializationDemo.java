package hr.algebra.podcast.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.InvalidObjectException;
import hr.algebra.podcast.enums.Role;
import java.time.LocalDateTime;

import hr.algebra.podcast.entity.User;



public class SerializationDemo {

    static void serializeToFile(Object obj, String fileName)
            throws IOException {

        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(obj);
            oos.flush();
        }
    }

    static boolean verifyBytes(String fileName)
            throws IOException {

        try (FileInputStream fis = new FileInputStream(fileName)) {

            byte[] header = new byte[4];

            if (fis.read(header) != 4) {
                return false;
            }

            return (header[0] & 0xFF) == 0xAC
                    && (header[1] & 0xFF) == 0xED
                    && (header[2] & 0xFF) == 0x00
                    && (header[3] & 0xFF) == 0x05;
        }
    }

    static Object secureDeserialize(String fileName)
            throws IOException, ClassNotFoundException {

        if (!verifyBytes(fileName)) {
            throw new InvalidObjectException("thhis is not a valid file for serialization");
        }

        try (FileInputStream fis = new FileInputStream(fileName);
             WhitelistObjectInputStream ois =
                     new WhitelistObjectInputStream(fis)) {

            return ois.readObject();
        }
    }
}