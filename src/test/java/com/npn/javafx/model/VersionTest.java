package com.npn.javafx.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    void compare() {
        try {
            Version version1 = new Version("10.00.01");
            Version version2 = new Version("09.10.01");
            Version version3 = new Version("10.01.00");
            Version version4 = new Version("11.00.00");
            Version version5 = new Version("10.00.01");
            assertEquals(version1.compareTo(version2),1);
            assertEquals(version1.compareTo(version3),-1);
            assertEquals(version1.compareTo(version4),-1);
            assertEquals(version1.compareTo(version5),0);
            assertEquals(version1.compareTo(version1),0);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            Version version1 = new Version("as.00.01");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
        try
        {
            Version version1 = new Version("00.00.02ss");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }

        try
        {
            Version version1 = new Version("10.00.01");
            assertEquals(version1.compareTo(null),0);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }


    }
}