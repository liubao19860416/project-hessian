package com.hessian.vo;

import com.hessian.vo.base.BaseViewObject;

public class LotteryRecordViewObject extends
        BaseViewObject<LotteryRecordViewObject> {

    private static final long serialVersionUID = 2008584433057353890L;
    private String issueNumber;
    private int red1;
    private int red2;
    private int red3;
    private int red4;
    private int red5;
    private int red6;
    private int blue;
    // 中奖结果
    private LotteryResult lotteryResult;

    public LotteryRecordViewObject() {
        super();
    }

    public LotteryRecordViewObject(String issueNumber, int red1, int red2,
            int red3, int red4, int red5, int red6, int blue) {
        super();
        this.issueNumber = issueNumber;
        this.red1 = red1;
        this.red2 = red2;
        this.red3 = red3;
        this.red4 = red4;
        this.red5 = red5;
        this.red6 = red6;
        this.blue = blue;
    }

    public LotteryResult getLotteryResult() {
        return lotteryResult;
    }

    public void setLotteryResult(LotteryResult lotteryResult) {
        this.lotteryResult = lotteryResult;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public int getRed1() {
        return red1;
    }

    public void setRed1(int red1) {
        this.red1 = red1;
    }

    public int getRed2() {
        return red2;
    }

    public void setRed2(int red2) {
        this.red2 = red2;
    }

    public int getRed3() {
        return red3;
    }

    public void setRed3(int red3) {
        this.red3 = red3;
    }

    public int getRed4() {
        return red4;
    }

    public void setRed4(int red4) {
        this.red4 = red4;
    }

    public int getRed5() {
        return red5;
    }

    public void setRed5(int red5) {
        this.red5 = red5;
    }

    public int getRed6() {
        return red6;
    }

    public void setRed6(int red6) {
        this.red6 = red6;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + blue;
        result = prime * result + red1;
        result = prime * result + red2;
        result = prime * result + red3;
        result = prime * result + red4;
        result = prime * result + red5;
        result = prime * result + red6;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        LotteryRecordViewObject other = (LotteryRecordViewObject) obj;
        if (blue != other.blue)
            return false;
        if (red1 != other.red1)
            return false;
        if (red2 != other.red2)
            return false;
        if (red3 != other.red3)
            return false;
        if (red4 != other.red4)
            return false;
        if (red5 != other.red5)
            return false;
        if (red6 != other.red6)
            return false;
        return true;
    }

}
