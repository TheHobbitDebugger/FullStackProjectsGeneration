package com.generation.sbb.model;

public enum BookingStatus {
    PENDING,
    CHECKEDIN,
    CHECKEDOUT,
    CLEANED,
    CANCELLED;

    BookingStatus next() {
        switch (this) {
            case PENDING:
                return CHECKEDIN;
            case CHECKEDIN:
                return CHECKEDOUT;
            case CHECKEDOUT:
                return CLEANED;
            default:
                return null;
        }
    }
}
