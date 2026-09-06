package hr.algebra.podcast.serialization;

import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.enums.Role;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SerializationDemoTest {

    @Test
    void successfulDeserialization()
            throws IOException, ClassNotFoundException {

        // arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("test123");
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setRegisteredAt(LocalDateTime.now());

        String fileName = "user.ser";

        // act
        SerializationDemo.serializeToFile(user, fileName);

        User deserializedUser =
                (User) SerializationDemo.secureDeserialize(fileName);

        // assert
        assertEquals(user.getUsername(), deserializedUser.getUsername());
        assertEquals(user.getEmail(), deserializedUser.getEmail());
        assertEquals(user.getRole(), deserializedUser.getRole());
    }

    @Test
    void rejectNonWhitelisted()
            throws IOException, ClassNotFoundException {

        // arrange
        InvalidFile invalidFile = new InvalidFile();
        String fileName = "invalidFile.ser";

        SerializationDemo.serializeToFile(invalidFile, fileName);

        // act
        // assert
        InvalidClassException exception =
            assertThrows(InvalidClassException.class, () ->
                    SerializationDemo.secureDeserialize(fileName)
            );
        System.out.println(exception.getMessage());

    }

    @Test
    void rejectInvalidBinaryFile()
            throws IOException {

        // arrange
        String fileName = "invalid.txt";

        Files.writeString(
                Path.of(fileName),
                "invalid binary file"
        );

        // act
        // assert
        InvalidObjectException exception =
            assertThrows(InvalidObjectException.class, () ->
                    SerializationDemo.secureDeserialize(fileName)
            );
        System.out.println(exception.getMessage());
    }
}