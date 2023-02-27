package com.epam.task1;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
class StudentTest {

    @Test
    public void testSerializeDeserialize() {
        Person person = new Person("max", "man", 40);

        try (FileOutputStream fos = new FileOutputStream("st.txt"); ObjectOutputStream oos = new ObjectOutputStream(fos);
             FileInputStream fis = new FileInputStream("st.txt");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            oos.writeObject(person);
            Person student1 = (Person) ois.readObject();

            assertEquals("max", student1.getName());
            assertEquals(0, student1.getAge());

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSerializeDeserializeWomen() {
        Women women = new Women("margarita", "woman", 25);
        women.setEducation("school");
        women.setBrestSize(3);

        try (FileOutputStream fos = new FileOutputStream("st.txt"); ObjectOutputStream oos = new ObjectOutputStream(fos);
             FileInputStream fis = new FileInputStream("st.txt");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            oos.writeObject(women);
            Women women1 = (Women) ois.readObject();

            assertEquals("margarita", women1.getName());
            assertEquals(0, women1.getAge());
            assertEquals(0, women1.getBrestSize());

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}