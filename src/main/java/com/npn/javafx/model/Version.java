package com.npn.javafx.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс описывающий версии программ в формате 01.02.03
 * где:
 * 01 - major Version
 * 02 - minor Version
 * 03 - fix Version
 */
public class Version implements Comparable<Version> {
    private static final Logger logger = LoggerFactory.getLogger(Version.class);

    private int majorVersion;
    private int minorVersion;
    private int fixVersion;
    private String stringVersion;


    /**
     * Возвращает список с распознанными версими из исходного списка строк
     * все
     *
     * @param list
     * @return
     */
    public static List<Version> getVersionsListFromStrings(List<String> list) {
        List<Version> retList = new ArrayList<>();

        list.forEach(x->{
            try{
                Version version = new Version(x);
                retList.add(version);
            } catch (IllegalArgumentException ignore) {}
        });
        return retList;
    }

    public Version(final String version) {
        logger.debug("Version created with\t {}",version);
        this.stringVersion = version;
        String[] strings = version.split("\\.");
        if (strings.length!=3) {
            logger.debug("Version recognize error with string\t{}",version);
            throw new IllegalArgumentException("Illegal version type, may be 01.02.03");
        }
        try {
            majorVersion = Integer.parseInt(strings[0]);
            minorVersion = Integer.parseInt(strings[1]);
            fixVersion = Integer.parseInt(strings[2]);
        } catch (Exception e) {
            logger.debug("Version recognize error with string\t{}",version);
            throw new IllegalArgumentException("Illegal version type, may be number.number.number", e);
        }

    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getFixVersion() {
        return fixVersion;
    }

    @Override
    public String toString() {
        return stringVersion;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *<p></p>
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(final Version o) {
        if (o == null) {
            logger.error("compareTo() does not permit null arguments\tthis={}\to={}",this,null);
            throw new NullPointerException("compareTo() does not permit null arguments");
        }
        if (this == o) return 0;

        if (this.getMajorVersion()>o.getMajorVersion()) {
            return 1;
        } else if (this.getMajorVersion()<o.getMajorVersion()) {
            return -1;
        }

        if (this.getMinorVersion()>o.getMinorVersion()) {
            return 1;
        } else if (this.getMinorVersion()<o.getMinorVersion()) {
            return -1;
        }

        if (this.getFixVersion()>o.getFixVersion()) {
            return 1;
        } else if (this.getFixVersion()<o.getFixVersion()) {
            return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return majorVersion == version.majorVersion &&
                minorVersion == version.minorVersion &&
                fixVersion == version.fixVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, minorVersion, fixVersion);
    }

}
