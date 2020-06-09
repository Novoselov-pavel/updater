package com.npn.javafx.model;

public enum MainFormStage {
    SELECT_BASE_PATH("SELECT_BASE_PATH"),
    SELECT_FILE("SELECT_FILES_TO_CREATE_DISTR"),
    CHECK_INPUT("CHECKING"),
    DISTR_VIEW("DISTR_VIEW"),
    SELECT_DESTINATION_DIR("SELECT_DESTINATION_DIR"),
    PACK_DISTR("PACK_DISTR");

    MainFormStage(String headerText) {
        this.headerText = headerText;
    }

    private final String headerText;

    public String getHeaderText() {
        return headerText;
    }
}
