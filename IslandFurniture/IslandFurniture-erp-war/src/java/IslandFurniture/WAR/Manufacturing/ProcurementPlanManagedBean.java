/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IslandFurniture.WAR.Manufacturing;

import IslandFurniture.EJB.CommonInfrastructure.ManageUserAccountBeanLocal;
import IslandFurniture.EJB.Entities.ManufacturingFacility;
import IslandFurniture.EJB.Entities.Month;
import IslandFurniture.EJB.Entities.MonthlyProcurementPlan;
import IslandFurniture.EJB.Entities.RetailItem;
import IslandFurniture.EJB.Entities.Staff;
import IslandFurniture.EJB.Manufacturing.ManageProcurementPlanLocal;
import IslandFurniture.StaticClasses.Helper.Helper;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import IslandFurnitures.DataStructures.JDataTable;
import IslandFurnitures.DataStructures.JDataTable.Row;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.component.commandbutton.CommandButton;



/**
 *
 * @author Benjamin
 */
@ManagedBean
@ViewScoped
public class ProcurementPlanManagedBean {

    private String username;
    private List<MonthlyProcurementPlan> mppList;
    private Staff staff;
    private JDataTable<String> dt;
    private MonthlyProcurementPlan mpp;
    private List<RetailItem> retailList;
    private RetailItem retailItem;
    private int size;
    
    @EJB
    private ManageProcurementPlanLocal mppl;
    @EJB
    private ManageUserAccountBeanLocal  muabl;
    
    
    @PostConstruct
    public void init(){
        HttpSession session = Util.getSession();
        username = (String) session.getAttribute("username");
        mppList = mppl.viewMonthlyProcurementPlan();
        retailList = mppl.viewRetailItems();
        dt = new JDataTable<String>();
        dt.Title = "Procurement Plan";
        dt.columns.add("");
        int c_year = Helper.getCurrentYear();
        int c_month = Helper.getCurrentMonth().value;
        
        for (int i = 0; i <= 6; i++) {

            try {
                int i_month = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, true);
                int i_year = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, false);

                dt.columns.add(Helper.translateMonth(i_month) + "/" + i_year);
            } catch (Exception ex) {
            }
        }
        Iterator<RetailItem> iterator = retailList.iterator();
        while (iterator.hasNext()) {
            retailItem = iterator.next();
            JDataTable.Row r = dt.newBindedRow(retailItem);
            r.newCell(retailItem.getName());
            size = r.getRowNo();
        }
        c_year = Helper.getCurrentYear();
        c_month = Helper.getCurrentMonth().value;
        int set=0;
        for (int j = 0; j <= size; j++) {
                for (int i = 0; i <= 6; i++) {
                    for (MonthlyProcurementPlan mpp : mppl.viewMonthlyProcurementPlan() ) {
                        try {
                            int i_month = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, true);
                            int i_year = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, false);
                            if (mpp.getRetailItem().getName().equals(dt.getRow(j).getCell(0).getValue()) &&
                                    mpp.getYear() == i_year && mpp.getMonth().equals(Helper.translateMonth(i_month))){
                                Row r = dt.getRow(j);
                                r.newCell(mpp.getQty().toString());
                                set = 1;
                            }
                            else{
                            }
                        }catch (Exception ex) {
                        }
                    }
                    if (set == 0){
                        Row r = dt.getRow(j);
                        r.newCell("");
                    }
                    set=0;
                }
        }
        JDataTable.Row r = dt.newRow();
        r.newCell("Create Purchase Order");
        for (int i = 0; i <= 6; i++) {

            try {
                int i_month = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, true);
                int i_year = Helper.addMonth(Helper.translateMonth(c_month), c_year, i, false);
                ManufacturingFacility mf = (ManufacturingFacility) muabl.getStaff(username).getPlant();
                if(mppl.checkMppLocked(mf , Helper.translateMonth(i_month), i_year)){
                    r.newCell("Locked");
                }else{
                    r.newCell("Create").setCommand("CREATE").setIdentifier(i_month+"-"+i_year);
                }
            } catch (Exception ex) {
            }
        }
        
    }
    
    public void listenToCell(ActionEvent actionEvent) {
        try {
            CommandButton button = (CommandButton) actionEvent.getComponent();
            String ID = button.getAlt();
            String command = button.getLabel();

            switch (command) {
                case "CREATE":
                    StringTokenizer stringTokenizer = new StringTokenizer(ID,"-");
                    Month month = Helper.translateMonth(Integer.parseInt(stringTokenizer.nextToken()));
                    Integer year = Integer.parseInt(stringTokenizer.nextToken());
                    ManufacturingFacility mf = (ManufacturingFacility) muabl.getStaff(username).getPlant();
                    mppl.lockMpp(mf,month,year);
                    mppl.createPurchaseOrder(mf,month,year);
                    break;
            }
            
            System.out.println("listenToCell(): " + command + " ON " + ID);
        } catch (Exception ex) {

        }
    }
    
    public String generateProcurementPlan(){
        staff = muabl.getStaff(username);
        mppl.createMonthlyProcumentPlan((ManufacturingFacility) staff.getPlant());
        return "procurementplan";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ManageProcurementPlanLocal getMppl() {
        return mppl;
    }

    public void setMppl(ManageProcurementPlanLocal mppl) {
        this.mppl = mppl;
    }

    public List<MonthlyProcurementPlan> getMppList() {
        return mppList;
    }

    public void setMppList(List<MonthlyProcurementPlan> mppList) {
        this.mppList = mppList;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ManageUserAccountBeanLocal getMuabl() {
        return muabl;
    }

    public void setMuabl(ManageUserAccountBeanLocal muabl) {
        this.muabl = muabl;
    }

    public JDataTable<String> getDt() {
        return dt;
    }

    public void setDt(JDataTable<String> dt) {
        this.dt = dt;
    }

    public MonthlyProcurementPlan getMpp() {
        return mpp;
    }

    public void setMpp(MonthlyProcurementPlan mpp) {
        this.mpp = mpp;
    }

    public List<RetailItem> getRetailList() {
        return retailList;
    }

    public void setRetailList(List<RetailItem> retailList) {
        this.retailList = retailList;
    }

    public RetailItem getRetailItem() {
        return retailItem;
    }

    public void setRetailItem(RetailItem retailItem) {
        this.retailItem = retailItem;
    }
    
    
    
}
