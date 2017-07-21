package com.basakpie.myviews;

import com.basakpie.models.Customer;
import com.basakpie.models.CustomerRepository;
import com.basakpie.models.Plot;
import com.basakpie.models.PlotRepository;
import com.basakpie.models.Sale;
import com.basakpie.models.SaleRepository;
import com.basakpie.sidebar.Sections;
import com.basakpie.view.AbstractView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import javax.annotation.PostConstruct;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

/**
 *
 * @author Ali
 */
@SpringView(name = SaleView.VIEW_NAME)
@SideBarItem(sectionId = Sections.MYVIEWS, caption = SaleView.VIEW_NAME, order = 3)
@VaadinFontIcon(VaadinIcons.ABACUS)
@UIScope
@SpringComponent
@SpringUI
public class SaleView extends AbstractView {

    public static final String VIEW_NAME = "Sale";

    private FormLayout form;
    private TextField salePricePerMarla, noOfInstallment, advance, tokenMoney, amountPerInstallment;
    private TextArea description;
    private Button save, add_sale, update, clear_btn;
    private Sale sale;
    private Customer customer;
    private Plot plot;
    private Grid<Sale> grid;
    private Window window;
    private HorizontalLayout buttonlayout;
    private ComboBox<Customer> combo_Customer;
    private ComboBox<Plot> combo_Plot;
    
    private Binder<Sale> binder;
    
    @Autowired
    SaleRepository saleRepository;

    @Autowired
    PlotRepository plotRepository;
    
    @Autowired
    CustomerRepository customerRepository;

    public SaleView() {
        setViewHeader("Sale View", VaadinIcons.BOMB);
    }

    @PostConstruct
    public void init() {

// Form Layout        
        form = new FormLayout();
        form.setCaption("<h3>Enter Sale Detail...</h3>");
        form.setCaptionAsHtml(true);
        form.setSizeUndefined();

        salePricePerMarla = new TextField();
        salePricePerMarla.setRequiredIndicatorVisible(true);
        salePricePerMarla.setCaption("Sale per Marla Price :");
        salePricePerMarla.setWidth("50%");
        salePricePerMarla.setPlaceholder("Sale per Marla Price :");

        noOfInstallment = new TextField();
        noOfInstallment.setRequiredIndicatorVisible(true);
        noOfInstallment.setCaption("No Of Installment :");
        noOfInstallment.setWidth("50%");
        noOfInstallment.setPlaceholder("No Of Installment :");

        advance = new TextField();
        advance.setRequiredIndicatorVisible(true);
        advance.setCaption("Advance :");
        advance.setWidth("50%");
        advance.setPlaceholder("Advance :");

        tokenMoney = new TextField();
        tokenMoney.setRequiredIndicatorVisible(true);
        tokenMoney.setCaption("Token Money :");
        tokenMoney.setWidth("50%");
        tokenMoney.setPlaceholder("Token Money :");

        amountPerInstallment = new TextField();
        amountPerInstallment.setRequiredIndicatorVisible(true);
        amountPerInstallment.setCaption("Amount Per Intallment :");
        amountPerInstallment.setWidth("50%");
        amountPerInstallment.setPlaceholder("Amount Per Intallment : ");

        combo_Customer = new ComboBox<Customer>("Select your customer Name : ");
        combo_Customer.setPlaceholder("customer Name ...");
        combo_Customer.addStyleName(ValoTheme.COMBOBOX_LARGE);
        
        combo_Plot = new ComboBox<Plot>("Select your plot : ");
        combo_Plot.setPlaceholder("Plot name ...");
        combo_Plot.addStyleName(ValoTheme.COMBOBOX_LARGE);
        

// Save record into database.
        save = new Button("Save");
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setDescription("This Button saves new Sale Detail...");
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener((event) -> {
            try {
                binder.writeBean(sale);
                sale.setPlot(combo_Plot.getValue());
                sale.setCustomer(combo_Customer.getValue());
                
                

            } catch (ValidationException ex) {
                Notification.show("Enter Valid Data in Form", Notification.Type.ERROR_MESSAGE);
            }
            saleRepository.save(sale);
            clear();
            window.close();
            getGridData();
        });

// Clear all fields        
        clear_btn = new Button("Clear");
        clear_btn.setDescription("Clear all Field to their Default Values(Empty)");
        clear_btn.addClickListener(e -> {
            clear();
        });

// Add components to Form Layout        
        form.addComponents(combo_Customer, combo_Plot, salePricePerMarla, noOfInstallment, advance, tokenMoney, amountPerInstallment,
                new HorizontalLayout(clear_btn, save));
        form.setMargin(true);

        grid = new Grid(Sale.class);
        grid.setCaption("<h2>Sales Detail...</h2>");
        grid.setCaptionAsHtml(true);
        getGridData();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setStyleName(ValoTheme.LAYOUT_WELL);
        grid.setWidth(this.getWidth(), Unit.PERCENTAGE);
        grid.setColumns("id", "salePricePerMarla", "numberOfInstallment", "advance", "tokenMoney", "amountPerInstallment");
//        HeaderRow filterRow=grid.appendHeaderRow();
//        HeaderCell filternamecell=filterRow.getCell("name");
//        TextField filter=new TextField();
//        filter.setValueChangeMode(ValueChangeMode.LAZY);
//        filter.setPlaceholder("Filter name...");
//        filternamecell.setComponent(filter);
//        filter.addValueChangeListener(e ->{
//                      
//            filterName(e.getValue());
//        
//        });
//        

///Binder for data get/
        sale = new Sale();
        plot = new Plot();
        customer = new Customer();
        binder = new Binder(Sale.class);


        binder.forField(salePricePerMarla)
                .withConverter(new StringToLongConverter("Enter Value in Long"))
                //                .withValidator(Double -> Double > 0, "Input value should be a Greater than Zero.")
                .bind(Sale::getSalePricePerMarla, Sale::setSalePricePerMarla);
        binder.forField(noOfInstallment)
                .withConverter(new StringToIntegerConverter("Enter Value in Integer"))
                //                .withValidator(Double -> Double > 0, "Input value should be a Greater than Zero.")
                .bind(Sale::getNumberOfInstallment, Sale::setNumberOfInstallment);
        binder.forField(advance)
                .withConverter(new StringToLongConverter("Enter Value in Long"))
                //                .withValidator(Double -> Double > 0, "Input value should be a Greater than Zero.")
                .bind(Sale::getAdvance, Sale::setAdvance);
        binder.forField(tokenMoney)
                .withConverter(new StringToLongConverter("Enter Value in Long"))
                .bind(Sale::getTokenMoney, Sale::setTokenMoney);
        binder.forField(amountPerInstallment)
                .withConverter(new StringToLongConverter("Enter Value in Long"))
                .bind(Sale::getAmountPerInstallment, Sale::setAmountPerInstallment);
        binder.readBean(sale);

        window = new Window();
        window.setWidth(400.0f, Unit.PIXELS);
        window.setContent(form);
        window.center();
        window.setResizeLazy(true);

        add_sale = new Button("Add Sale");
        add_sale.setStyleName(ValoTheme.BUTTON_PRIMARY);
        add_sale.setIcon(VaadinIcons.PLUS);
        add_sale.addClickListener(e -> {
            try {
                sale.setId(null);
                combo_Customer.setItems(customerRepository.findAll());
                combo_Customer.setItemCaptionGenerator(Customer::getFullName);
                
                combo_Plot.setItems(plotRepository.findAll());
                combo_Plot.setItemCaptionGenerator(Plot::getName);

                this.getUI().addWindow(window);

            } catch (IllegalArgumentException ex) {
            }
        });

        update = new Button("Update");
        update.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        update.setIcon(VaadinIcons.EDIT);
        update.addClickListener(e -> {
            if (!grid.getSelectedItems().isEmpty()) {
                try {
                    sale = grid.asSingleSelect().getValue();
                    salePricePerMarla.setValue(String.valueOf(sale.getSalePricePerMarla()));
                    noOfInstallment.setValue(String.valueOf(sale.getNumberOfInstallment()));
                    advance.setValue(String.valueOf(sale.getAdvance()));
                    tokenMoney.setValue(String.valueOf(sale.getTokenMoney()));
                    amountPerInstallment.setValue(String.valueOf(sale.getAmountPerInstallment()));

                    this.getUI().addWindow(window);

                } catch (IllegalArgumentException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            } else {
                Notification.show("Please First Select a Row to Update", Notification.Type.WARNING_MESSAGE);
            }
        });

        ///This horizental layout is for adding button on the top of grid..
        buttonlayout = new HorizontalLayout();
        buttonlayout.addComponents(add_sale, update);

        //adding components to view...
        addComponents(buttonlayout, grid);

    }


    void clear() {
        amountPerInstallment.setValue("");
        tokenMoney.setValue("");
        noOfInstallment.setValue("");
        advance.setValue("");
        salePricePerMarla.setValue("");

    }

    void getGridData() {
        grid.setDataProvider(
                (sortOrders, offset, limit)
                -> saleRepository.findAll(new PageRequest(offset / limit, limit)).getContent().stream(),
                () -> Integer.parseInt(saleRepository.count() + "")
        );
    }

    ////Method to filter Data on filtering textfield change value..
    public void filterName(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setDataProvider(
                    (sortOrders, offset, limit)
                    -> saleRepository.findAll(new PageRequest(offset / limit, limit)).getContent().stream(),
                    () -> Integer.parseInt(saleRepository.count() + "")
            );
        } else {
//            grid.setItems(saleRepository.findByNameIgnoreCaseContaining(filterText));
        }
    }

}
