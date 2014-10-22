/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.Enums;

/**
 *
 * @author Chen Tong <chentong@nus.edu.sg>
 */
public enum CampaignGoal {

    PROSPECTING(0), SALES_BOOST(1), SEASONAL_PROMO(2), COMPETITION(3), NEW_PRODUCT_TESTING(4);
    public int value;

    private CampaignGoal(int value) {
        this.value = value;
    }
}
