package com.hessian.vo;

/**
 * Lottery结果类别枚举类
 * 
 * 一等奖   动态计算                     6+1
 * 二等奖    动态计算                    6+0
 * 三等奖   3,000元                  5+1
 * 四等奖   200元                        5+0/4+1
 * 五等奖   10元                           4+0/3+1
 * 六等奖    5元                             2+1/1+1/0+1
 * 
 * @author Liubao
 * @version 3.0
 * @Date 2016年3月26日
 * 
 */
public enum LotteryResult {
    一等奖 (1, "6+1" ,-1),    
    二等奖 (2, "6+0" ,-1),    
    三等奖 (3, "5+1" ,3000),    
    四等奖 (4, "5+0/4+1" ,200),    
    五等奖 (5, "4+0/3+1" ,10),    
    六等奖 (6, "2+1/1+1/0+1",5 ),    
    未中奖(0, "预留" ,0); 

    private int index;
    private String descrition;
    private int money;

    private LotteryResult() {}
    
    private LotteryResult(int index, String descrition, int money) {
        this.index = index;
        this.descrition = descrition;
        this.money = money;
    }

    public static LotteryResult getByKey(int index) {
        LotteryResult[] os = LotteryResult.values();
        for (int i = 0; i < os.length; i++) {
            if (os[i].getIndex()==index) {
                return os[i];
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    
}