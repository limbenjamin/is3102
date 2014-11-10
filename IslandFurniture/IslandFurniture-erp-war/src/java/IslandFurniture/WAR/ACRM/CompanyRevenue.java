/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.ACRM;

import IslandFurniture.EJB.ACRM.ACRMChartGeneratorBeanLocal;
import IslandFurniture.EJB.ACRM.basketAnalysisBeanLocal;
import IslandFurniture.Entities.CompanyPeriodAnalysisReport;
import IslandFurniture.Entities.CountryOffice;
import IslandFurniture.Entities.CountryPeriodAnalysisReport;
import IslandFurniture.Entities.Customer;
import IslandFurniture.Entities.CustomerPeriodAnalysisReport;
import IslandFurniture.Entities.FurnitureModel;
import IslandFurniture.Entities.ProductAnalysisReport;
import IslandFurniture.Entities.Stock;
import IslandFurniture.Entities.StorePeriodAnalysisReport;
import IslandFurniture.WAR.CommonInfrastructure.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author James
 */
@ManagedBean(name = "companyRevenue")
@SessionScoped
public class CompanyRevenue implements Serializable {

    private BarChartModel companymodel;
    private Map<String, Calendar> companymodel_points_mapping = new HashMap<>();

    private DonutChartModel storeBreakdowninstance;
    private PieChartModel storeBreakdownAggregated;
    private BubbleChartModel cnproductAnalysisChart;
    private BubbleChartModel cnproductAnalysisChart_instance;
    private DonutChartModel aggregatedCountryBreakdown;
    private PieChartModel countryAggregatedMembershiptier;
    private PieChartModel countryAggregatedMembershiptier_instance;
    private BarChartModel product_time_Series;
    private List<String> Related_Products = new ArrayList<>();
    private String country;
    private String dateweek;
    private String product;
    private List<String> countryOptions;
    private List<String> productoptions;
    private Boolean nonHQmode = false;
    private List<Customer> customers;

    @EJB
    private ACRMChartGeneratorBeanLocal acrm;
    @EJB
    private basketAnalysisBeanLocal basket;

    private Calendar from;
    private Calendar to;
    private List<CompanyPeriodAnalysisReport> cpar;

    public void countrychangelisten() {
        country_analysis();
    }

    public void productchangelisten() {

        product_analysis();
        this.Related_Products.clear();

        for (FurnitureModel zzz : basket.findListOfrelated(product, country)) {
            Related_Products.add(zzz.getName());
        }

    }

    public void customerchangelisten() {
        customer_analysis();

    }

    public void customer_analysis() {

        customers = acrm.filterCustomer(country);
        System.out.println("Customers Count:" + customers.size());

    }

    public DonutChartModel getStoreBreakdown() {
        return storeBreakdowninstance;
    }

    public void setStoreBreakdown(DonutChartModel storeBreakdown) {
        this.storeBreakdowninstance = storeBreakdown;
    }

    public PieChartModel getStoreBreakdownAggregated() {
        return storeBreakdownAggregated;
    }

    public void setStoreBreakdownAggregated(PieChartModel storeBreakdownAggregated) {
        this.storeBreakdownAggregated = storeBreakdownAggregated;
    }

    @PostConstruct
    public void init() {
        System.out.println("ACRM Charts(): Init !");
        from = Calendar.getInstance();
        from.setFirstDayOfWeek(Calendar.MONDAY);
        from.add(Calendar.WEEK_OF_YEAR, -12);
        from.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        to = Calendar.getInstance();
        to.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        refreshAreaModel();
        if (companymodel_points_mapping.size() > 0) {
            dateweek = companymodel_points_mapping.keySet().toArray()[0].toString();
        }

        HttpSession session = Util.getSession();
        String username = (String) session.getAttribute("username");
        CountryOffice co = acrm.getCountryOffice(username);
        System.out.println("Defaulted to " + co.getName() + " for " + username);
        country = co.getName();
        country_analysis();
        customer_analysis();

        countryOptions = new ArrayList<>();
        if (acrm.hasHQPermission(username)) {
            for (ChartSeries s : this.getAreaModel().getSeries()) {
                countryOptions.add(s.getLabel());
            }
        } else {
            nonHQmode = true;
            System.out.println("NON-HQ PERSONNEL");
            countryOptions.add(co.getName());
        }

        productoptions = new ArrayList<>();
        for (BubbleChartSeries bc : cnproductAnalysisChart.getData()) {
            productoptions.add(bc.getLabel());
        }
        product = cnproductAnalysisChart.getData().get(0).getLabel();
        System.out.println("Defaulted Product to " + product + " for " + username);
        product_analysis();

    }

    public Calendar getFrom() {
        return from;
    }

    public void setFrom(Calendar from) {
        this.from = from;
    }

    public Calendar getTo() {
        return to;
    }

    public void setTo(Calendar to) {
        this.to = to;
    }

    public void refreshAreaModel() {
        cpar = acrm.getCompanyReport(from, to);
        System.out.println("CompanyRevenue(): Getting Company Revenue from " + from.getTime() + " to: " + to.getTime());
        companymodel = new BarChartModel();
        companymodel.setTitle("Company Revenue Analysis");
        companymodel.setLegendPosition("ne");
        companymodel.setShowPointLabels(true);
        companymodel.setStacked(true);
        companymodel.setDatatipFormat("%2$d");

        aggregatedCountryBreakdown = new DonutChartModel();
        aggregatedCountryBreakdown.setTitle("Company Average Weekly Revenue By Country");
        aggregatedCountryBreakdown.setLegendPosition("ne");
        aggregatedCountryBreakdown.setShowDataLabels(true);

        Axis xAxis = new CategoryAxis("WeekNo/Year");
        companymodel.getAxes().put(AxisType.X, xAxis);
        Axis yAxis = companymodel.getAxis(AxisType.Y);
        yAxis.setLabel("Revenue (Thousands)");
        yAxis.setMin(0);
        xAxis.setTickAngle(60);
//        yAxis.setMin(0);
//        yAxis.setMax(300);
        Map<CountryOffice, BarChartSeries> data = new HashMap<>();
        Map<String, Number> data_r = new HashMap<>();

        int average_divisor = 0;
        for (CompanyPeriodAnalysisReport cpar_i : cpar) {
            average_divisor += 1;
            for (CountryPeriodAnalysisReport co_par : cpar_i.getCountryPeriodAnalysisReports()) {

                if (data.get(co_par.getCountryOffice()) == null) {
                    data.put(co_par.getCountryOffice(), new BarChartSeries());
                    data.get(co_par.getCountryOffice()).setLabel(co_par.getCountryOffice().getName());
                    data_r.put(co_par.getCountryOffice().getName(), 0.0);

                }

                data.get(co_par.getCountryOffice()).set(cpar_i.getAnalysis_date().get(Calendar.YEAR) + "/" + cpar_i.getAnalysis_date().get(Calendar.WEEK_OF_YEAR), co_par.getTotalRevenue() / 1000.00);
                companymodel_points_mapping.put(cpar_i.getAnalysis_date().get(Calendar.YEAR) + "/" + cpar_i.getAnalysis_date().get(Calendar.WEEK_OF_YEAR), cpar_i.getAnalysis_date());
                data_r.put(co_par.getCountryOffice().getName(), data_r.get(co_par.getCountryOffice().getName()).doubleValue() + co_par.getTotalRevenue() / 1000.00);
            }
        }

        for (CountryOffice co_par : data.keySet()) {
            companymodel.addSeries(data.get(co_par));
            if (average_divisor > 0) {
                data_r.put(co_par.getName() + " (" + Math.floor(data_r.get(co_par.getName()).doubleValue() / average_divisor) + ")", data_r.get(co_par.getName()).doubleValue() / average_divisor);
                data_r.remove(co_par.getName());
            }

        }
        if (average_divisor > 0) {
            aggregatedCountryBreakdown.addCircle(data_r);
        }

    }

    public BarChartModel getAreaModel() {

        return companymodel;
    }

    public void setAreaModel(BarChartModel areaModel) {
        this.companymodel = areaModel;
    }

    public void toggleCountry(ItemSelectEvent event) {
        this.country = companymodel.getSeries().get(event.getSeriesIndex()).getLabel();
        this.dateweek = (String) companymodel.getSeries().get(event.getSeriesIndex()).getData().keySet().toArray()[event.getItemIndex()];
        System.out.println("Date: " + dateweek + ", Series Index: " + country);
        country_analysis();

    }

    public void toogleProduct(ItemSelectEvent event) {
        this.product = this.cnproductAnalysisChart.getData().get(event.getItemIndex()).getLabel();
        System.out.println("Seleceted Products: " + this.product);
        product_analysis();
        this.Related_Products.clear();

        for (FurnitureModel zzz : basket.findListOfrelated(product, country)) {
            Related_Products.add(zzz.getName());
        }

    }

    public void toogleProduct2(ItemSelectEvent event) {
        this.product = this.cnproductAnalysisChart_instance.getData().get(event.getItemIndex()).getLabel();
        System.out.println("Seleceted Products: " + this.product);
        product_analysis();
        this.Related_Products.clear();

        for (FurnitureModel zzz : basket.findListOfrelated(product, country)) {
            Related_Products.add(zzz.getName());
        }

    }

    public void product_analysis() {

        product_time_Series = new BarChartModel();
        product_time_Series.setTitle("Time Series of Product " + product + " in " + country);
        product_time_Series.getAxis(AxisType.Y).setMin(0);
        product_time_Series.getAxis(AxisType.X).setTickAngle(60);

        ChartSeries product_s = new ChartSeries();
        product_s.setLabel(product);

        Map<String, Double> data = new HashMap<>();

        for (CompanyPeriodAnalysisReport cpar_i : cpar) {
            for (CountryPeriodAnalysisReport co : cpar_i.getCountryPeriodAnalysisReports()) {
                if (co.getCountryOffice().getName().equals(country)) {
                    for (StorePeriodAnalysisReport ss : co.getStorePeriodAnalysisReports()) {
                        for (ProductAnalysisReport pa : ss.getProductAnalysisReports()) {

                            if (pa.getStock().getName().equals(product)) {

                                String date = cpar_i.getAnalysis_date().get(Calendar.YEAR) + "/" + cpar_i.getAnalysis_date().get(Calendar.WEEK_OF_YEAR);

                                if (data.get(date) == null) {
                                    data.put(date, 0.0);
                                }

                                data.put(date, data.get(date) + pa.getTotalRevenue());
                            }

                        }

                    }
                }

            }
        }

        List<String> v = new ArrayList<String>(data.keySet());
        Collections.sort(v);

        for (String s : v) {
            product_s.set(s, data.get(s) / 1000.00);
        }

        product_time_Series.addSeries(product_s);

    }

    public void country_analysis() {

        storeBreakdowninstance = new DonutChartModel();
        storeBreakdowninstance.setShowDataLabels(true);
        storeBreakdowninstance.setLegendPosition("ne");
        storeBreakdowninstance.setShowDataLabels(true);

        storeBreakdownAggregated = new PieChartModel();
        storeBreakdownAggregated.setShowDataLabels(true);
        storeBreakdownAggregated.setLegendPosition("ne");
        storeBreakdownAggregated.setShowDataLabels(true);
        storeBreakdownAggregated.setTitle("Average Weekly Store Revenue for " + country);

        Calendar selected_date = companymodel_points_mapping.get(dateweek);

        HashMap<String, Number> instance = new HashMap<>();
        HashMap<String, Number> averaged = new HashMap<>();

        double period = 0;
        for (CompanyPeriodAnalysisReport c : cpar) {
            Boolean writeinstance = false;
            if (c.getAnalysis_date().equals(selected_date)) {
                writeinstance = true;

            }

            period += 1;
            for (CountryPeriodAnalysisReport cparx : c.getCountryPeriodAnalysisReports()) {

                if (writeinstance) {
                    System.out.println("Populating Donut: " + "Breakdown of Stores Profit " + country + "[Week Revenue=" + cparx.getTotalRevenue() + "] on " + dateweek);
                    storeBreakdowninstance.setTitle("Breakdown of Stores Profit " + country + " on " + dateweek);

                }

                if (cparx.getCountryOffice().getName().equals(country)) {
                    //right place

                    for (StorePeriodAnalysisReport storep : cparx.getStorePeriodAnalysisReports()) {
                        if (writeinstance) {
                            instance.put(storep.getStore().getName().toString() + "(" + storep.getTotalRevenue() / 1000.0 + ")", storep.getTotalRevenue());
                        }

                        //calc average
                        if (averaged.get(storep.getStore().getName()) == null) {
                            averaged.put(storep.getStore().getName(), 0.0);
                        }

                        averaged.put(storep.getStore().getName(), averaged.get(storep.getStore().getName()).doubleValue() + storep.getTotalRevenue());

                    }

                }

            }

        }

        for (Object o : (Object[]) averaged.keySet().toArray()) {
            String s = o.toString();
            if (period > 0) {
                storeBreakdownAggregated.set(s + " (" + Math.floor(averaged.get(s).doubleValue() / (period * 1000.0)) + ")", averaged.get(s).doubleValue() / period);
            }

        }

        storeBreakdowninstance.addCircle(instance);
        storeBreakdowninstance.addCircle(averaged);

        cnproductAnalysisChart_instance = new BubbleChartModel();
        cnproductAnalysisChart_instance.setTitle("Week Revenue Period: " + dateweek + " for Country" + country);
        cnproductAnalysisChart_instance.getAxis(AxisType.Y).setLabel("Week Revenue(Thousands)");
        cnproductAnalysisChart_instance.getAxis(AxisType.Y).setLabel("Week Quantity");
        cnproductAnalysisChart_instance.setBubbleGradients(true);
        cnproductAnalysisChart_instance.setBubbleAlpha(0.5);
        cnproductAnalysisChart_instance.setShowDatatip(true);
        cnproductAnalysisChart_instance.setShowPointLabels(true);

        //Prep Country
        Double inst = 0.0;
        HashMap<Stock, Double> data_p = new HashMap<>();
        HashMap<Stock, Long> data_p_qty = new HashMap<>();
        for (CompanyPeriodAnalysisReport c : cpar) {
            inst++;
            for (CountryPeriodAnalysisReport co : c.getCountryPeriodAnalysisReports()) {
                if (co.getCountryOffice().getName().equals(country)) {
                    for (StorePeriodAnalysisReport sp : co.getStorePeriodAnalysisReports()) {
                        for (ProductAnalysisReport p : sp.getProductAnalysisReports()) {

                            if (data_p.get(p.getStock()) == null) {
                                data_p.put(p.getStock(), 0.0);
                                data_p_qty.put(p.getStock(), 0L);
                            }

                            if (c.getAnalysis_date().equals(companymodel_points_mapping.get(dateweek))) {
                                Double rev = p.getTotalRevenue() / 1000.0;
                                cnproductAnalysisChart_instance.add(new BubbleChartSeries(p.getStock().getName(), p.getTotalQty().intValue(), rev.intValue(), 1));
                            }

                            data_p.put(p.getStock(), data_p.get(p.getStock()) + p.getTotalRevenue());
                            data_p_qty.put(p.getStock(), data_p_qty.get(p.getStock()) + p.getTotalQty().longValue());
                        }
                    }
                    break;

                }
            }
        }

        if (data_p.keySet().size() > 0) {

            cnproductAnalysisChart = new BubbleChartModel();
            cnproductAnalysisChart.setTitle("Average Weekly Revenue Of Products for " + country);
            cnproductAnalysisChart.getAxis(AxisType.Y).setLabel("Avg Weekly Revenue(Thousands)");
            cnproductAnalysisChart.getAxis(AxisType.X).setLabel("Weekly Quantity");
            cnproductAnalysisChart.setBubbleGradients(true);
            cnproductAnalysisChart.setBubbleAlpha(0.5);
            cnproductAnalysisChart.setShowDatatip(true);
            cnproductAnalysisChart.setShowPointLabels(true);

            for (Stock s : data_p.keySet()) {
                Double tosands = (data_p.get(s) / inst) / 1000.0;
                Double avgqty = data_p_qty.get(s).intValue() / inst;
                cnproductAnalysisChart.add(new BubbleChartSeries(s.getName(), avgqty.intValue(), tosands.intValue(), 1));
            }
            //cnproductAnalysisChart.addSeries(qty);
            System.out.println("ACRM():PopulatedProduct Analysis for Country ");

        }

        //Membership Tier Analysis
        inst = 0.0;
        HashMap<String, Double> data_p_2 = new HashMap<>();
        HashMap<String, Long> data_p_2_cust = new HashMap<>();
        countryAggregatedMembershiptier_instance = new PieChartModel();
        countryAggregatedMembershiptier_instance.setTitle("Customer Per Weekly Spending at " + dateweek);
        countryAggregatedMembershiptier_instance.setShowDataLabels(true);
        countryAggregatedMembershiptier_instance.setLegendPosition("ne");

        countryAggregatedMembershiptier = new PieChartModel();
        countryAggregatedMembershiptier.setTitle("Average Customer Per Weekly Spending at " + dateweek);
        countryAggregatedMembershiptier.setShowDataLabels(true);
        countryAggregatedMembershiptier.setLegendPosition("ne");

        for (CompanyPeriodAnalysisReport c : cpar) {
            inst++;
            for (CountryPeriodAnalysisReport co : c.getCountryPeriodAnalysisReports()) {
                if (co.getCountryOffice().getName().equals(country)) {
                    for (CustomerPeriodAnalysisReport cust : co.getCustomerPeriodAnalysisReport()) {

                        if (c.getAnalysis_date().equals(companymodel_points_mapping.get(dateweek))) {
                            countryAggregatedMembershiptier_instance.set(cust.getCustomerSegment().getTitle(), cust.getTotalRevenue() / cust.getNumofcustomers());
                        }

                        if (data_p_2.get(cust.getCustomerSegment().getTitle()) == null) {
                            data_p_2.put(cust.getCustomerSegment().getTitle(), 0.0);
                            data_p_2_cust.put(cust.getCustomerSegment().getTitle(), 0L);

                        }

                        data_p_2.put(cust.getCustomerSegment().getTitle(), data_p_2.get(cust.getCustomerSegment().getTitle()) + cust.getTotalRevenue());
                        data_p_2_cust.put(cust.getCustomerSegment().getTitle(), data_p_2_cust.get(cust.getCustomerSegment().getTitle()) + cust.getNumofcustomers());
                    }
                    break;
                }

            }
        }

        if (data_p_2.keySet().size() > 0) {

            for (String s : data_p_2.keySet()) {
                countryAggregatedMembershiptier.set(s, (data_p_2.get(s) / data_p_2_cust.get(s)) / inst);
            }

        }

    }

    public DonutChartModel getCountry_breakdown() {
        return storeBreakdowninstance;
    }

    public void setCountry_breakdown(DonutChartModel country_breakdown) {
        this.storeBreakdowninstance = country_breakdown;
    }

    public DonutChartModel getAggregatedCountryBreakdown() {
        return aggregatedCountryBreakdown;
    }

    public void setAggregatedCountryBreakdown(DonutChartModel aggregatedCountryBreakdown) {
        this.aggregatedCountryBreakdown = aggregatedCountryBreakdown;
    }

    public BubbleChartModel getCnproductAnalysisChart() {
        return cnproductAnalysisChart;
    }

    public void setCnproductAnalysisChart(BubbleChartModel cnproductAnalysisChart) {
        this.cnproductAnalysisChart = cnproductAnalysisChart;
    }

    public BubbleChartModel getCnproductAnalysisChart_instance() {
        return cnproductAnalysisChart_instance;
    }

    public void setCnproductAnalysisChart_instance(BubbleChartModel cnproductAnalysisChart_instance) {
        this.cnproductAnalysisChart_instance = cnproductAnalysisChart_instance;
    }

    public PieChartModel getCountryAggregatedMembershiptier() {
        return countryAggregatedMembershiptier;
    }

    public void setCountryAggregatedMembershiptier(PieChartModel countryAggregatedMembershiptier) {
        this.countryAggregatedMembershiptier = countryAggregatedMembershiptier;
    }

    public PieChartModel getCountryAggregatedMembershiptier_instance() {
        return countryAggregatedMembershiptier_instance;
    }

    public void setCountryAggregatedMembershiptier_instance(PieChartModel countryAggregatedMembershiptier_instance) {
        this.countryAggregatedMembershiptier_instance = countryAggregatedMembershiptier_instance;
    }

    public BarChartModel getProduct_time_Series() {
        return product_time_Series;
    }

    public void setProduct_time_Series(BarChartModel product_time_Series) {
        this.product_time_Series = product_time_Series;
    }

    public List<String> getRelated_Products() {
        return Related_Products;
    }

    public void setRelated_Products(List<String> Related_Products) {
        this.Related_Products = Related_Products;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country.length() > 0) { //some bug dunno from where
            this.country = country;
        }
    }

    public List<String> getCountryOptions() {
        return countryOptions;
    }

    public void setCountryOptions(List<String> countryOptions) {
        this.countryOptions = countryOptions;
    }

    public List<String> getProductoptions() {
        return productoptions;
    }

    public void setProductoptions(List<String> productoptions) {
        this.productoptions = productoptions;
    }

    public Boolean getNonHQmode() {
        return nonHQmode;
    }

    public void setNonHQmode(Boolean nonHQmode) {
        this.nonHQmode = nonHQmode;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

}
