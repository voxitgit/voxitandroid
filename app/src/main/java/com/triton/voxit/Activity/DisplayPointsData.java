package com.triton.voxit.Activity;

public class DisplayPointsData {
    private Integer AvailablePoints;
    private boolean RedeemButtonState;

    public boolean isRedeemButtonState() {
        return RedeemButtonState;
    }

    public void setRedeemButtonState(boolean redeemButtonState) {
        RedeemButtonState = redeemButtonState;
    }

    public Integer getAvailablePoints() {
        return AvailablePoints;
    }

    public void setAvailablePoints(Integer availablePoints) {
        AvailablePoints = availablePoints;
    }
}
