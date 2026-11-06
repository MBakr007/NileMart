package org.route.shipping;

/**
 * The 27 Egyptian governorates, each mapped to its shipping zone.
 */
public enum Governorate {

    CAIRO("Cairo", Zone.GREATER_CAIRO),
    GIZA("Giza", Zone.GREATER_CAIRO),
    QALYUBIA("Qalyubia", Zone.GREATER_CAIRO),

    ALEXANDRIA("Alexandria", Zone.ALEX_DELTA),
    BEHEIRA("Beheira", Zone.ALEX_DELTA),
    DAKAHLIA("Dakahlia", Zone.ALEX_DELTA),
    DAMIETTA("Damietta", Zone.ALEX_DELTA),
    GHARBIA("Gharbia", Zone.ALEX_DELTA),
    KAFR_EL_SHEIKH("Kafr El Sheikh", Zone.ALEX_DELTA),
    MENOUFIA("Menoufia", Zone.ALEX_DELTA),
    SHARQIA("Sharqia", Zone.ALEX_DELTA),

    ISMAILIA("Ismailia", Zone.CANAL),
    PORT_SAID("Port Said", Zone.CANAL),
    SUEZ("Suez", Zone.CANAL),

    BENI_SUEF("Beni Suef", Zone.UPPER_EGYPT),
    FAYOUM("Fayoum", Zone.UPPER_EGYPT),
    MINYA("Minya", Zone.UPPER_EGYPT),
    ASYUT("Asyut", Zone.UPPER_EGYPT),
    SOHAG("Sohag", Zone.UPPER_EGYPT),
    QENA("Qena", Zone.UPPER_EGYPT),
    LUXOR("Luxor", Zone.UPPER_EGYPT),
    ASWAN("Aswan", Zone.UPPER_EGYPT),

    RED_SEA("Red Sea", Zone.REMOTE),
    NEW_VALLEY("New Valley", Zone.REMOTE),
    MATROUH("Matrouh", Zone.REMOTE),
    NORTH_SINAI("North Sinai", Zone.REMOTE),
    SOUTH_SINAI("South Sinai", Zone.REMOTE);

    private final String displayName;
    private final Zone zone;

    Governorate(String displayName, Zone zone) {
        this.displayName = displayName;
        this.zone = zone;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Zone getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
