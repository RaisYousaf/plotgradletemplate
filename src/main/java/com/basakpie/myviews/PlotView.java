/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.myviews;

import com.basakpie.models.Plot;
import com.basakpie.models.PlotRepository;
import com.basakpie.sidebar.Sections;
import com.basakpie.view.AbstractView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

/**
 *
 * @author Moieen
 */
@SpringView(name = PlotView.VIEW_NAME)
@SideBarItem(sectionId = Sections.MYVIEWS, caption = "Plot", order = 1)
@VaadinFontIcon(VaadinIcons.ADJUST)
@UIScope
@SpringComponent
@SpringUI

public class PlotView extends AbstractView {
    

    public static final String VIEW_NAME = "Plot";
    
        private FormLayout form;
        private TextField name;
        private TextField dimension;
        private TextField location;
        private TextField address;
        private TextField type;
        private TextField marlas;
        private TextField purchasePricePerMarla;
        private TextField totalprice;
        private TextField saleprice;
        private TextArea description;
        private Button save,add_plot,update;
        private Button clear;
        private Plot plot;
        private Grid<Plot> grid;
        private Window window;
        private HorizontalLayout buttonlayout;
        
        private Binder<Plot> binder;
        @Autowired
        PlotRepository plotRepository;

    public PlotView() {
        setViewHeader("Plot View", VaadinIcons.CUBE);
    }

    @PostConstruct
    public void init() {
        form= new FormLayout();
        form.setCaption("<h3>Enter Plot Detail...</h3>");
        form.setCaptionAsHtml(true);
        form.setSizeUndefined();
        
        name=new TextField("Name :");
        name.setRequiredIndicatorVisible(true);
        name.setWidth("50%");
        name.setPlaceholder("Name...");
        
        dimension=new TextField();
        dimension.setRequiredIndicatorVisible(true);
        dimension.setCaption("Dismension :");
        dimension.setDescription("<texarea rows=\"4\" cols=\"35\">Enter plot dimenstion in feet."
                + "For example 12 ft x 15 ft</textarea>",ContentMode.HTML);
        dimension.setWidth("50%");
        dimension.setPlaceholder("Enter Dimension");
        
        location=new TextField();
        location.setRequiredIndicatorVisible(true);
        location.setCaption("Location :");
        location.setWidth("50%");
        location.setPlaceholder("Location");
        
        address=new TextField();
        address.setRequiredIndicatorVisible(true);
        address.setWidth("50%");
        address.setCaption("Address :");
        address.setPlaceholder("Address...");
        
        
        type=new TextField();
        type.setRequiredIndicatorVisible(true);
        type.setWidth("50%");
        type.setCaption("Type :");
        type.setDescription("<texarea rows=\"4\" cols=\"35\">Enter plot type."
                + "For example residential or commercial</textarea>",ContentMode.HTML);
        type.setPlaceholder("Address...");
        
        marlas = new TextField();
        marlas.setRequiredIndicatorVisible(true);
        marlas.setCaption("Marla :");
        marlas.setWidth("50%");
        marlas.setPlaceholder("Marla...");
        
        purchasePricePerMarla = new TextField();
        purchasePricePerMarla.setRequiredIndicatorVisible(true);
        purchasePricePerMarla.setCaption("Purchase price :");
        purchasePricePerMarla.setWidth("50%");
        purchasePricePerMarla.setPlaceholder("Purchase price/Marla...");
        
        
        
        totalprice = new TextField();
        totalprice.setRequiredIndicatorVisible(true);
        totalprice.setCaption("Total price :");
        totalprice.setWidth("50%");
        totalprice.setPlaceholder("Total price...");
        
        
        
        saleprice = new TextField();
        saleprice.setRequiredIndicatorVisible(true);
        saleprice.setCaption("Sale price :");
        saleprice.setWidth("50%");
        saleprice.setPlaceholder("Sale price...");
        
        
        description = new TextArea();
        description.setStyleName(ValoTheme.TEXTAREA_LARGE);
        description.setCaption("Description :");
        description.setWidth("50%");
        description.setPlaceholder("Description...");
        
        
//        binder.bindInstanceFields(this);
        
        save=new Button("Save");
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setDescription("This Button saves new Plot Detail...");
        save.setClickShortcut(KeyCode.ENTER);
        save.addClickListener((event) -> {
            try {
                
                binder.writeBean(plot);

            } catch (ValidationException ex) {
                Notification.show("Enter Valid Data in Form",Notification.Type.ERROR_MESSAGE);
            }

            plotRepository.save(plot);
            clear();
            window.close();
            getGridData();
        });
        
        
        
        
        clear=new Button("Clear");
        clear.setDescription("Clear all Field to their Default Values(Empty)");
        clear.addClickListener(e ->{
            clear();
        });
    
        form.addComponents(name, dimension, location, address,type,marlas,purchasePricePerMarla
                ,totalprice,saleprice, description,new HorizontalLayout(clear,save));
        form.setMargin(true);
//        form.setSizeFull();
        
        
        grid=new Grid(Plot.class);
        grid.setCaption("<h3>Plots Detail...</h3>");
        grid.setCaptionAsHtml(true);
        getGridData();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//        grid.setStyleName(ValoTheme.LAYOUT_WELL);
        grid.setWidth(this.getWidth(),Unit.PERCENTAGE);
        grid.setColumns("id","name","dimension","location","address","type","marlas","description");
        HeaderRow filterRow=grid.appendHeaderRow();
        HeaderCell filternamecell=filterRow.getCell("name");
        TextField filter=new TextField();
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setPlaceholder("Filter name...");
        filternamecell.setComponent(filter);
        filter.addValueChangeListener(e ->{
                      
            filterName(e.getValue());
        
        });
        
        ///Binder for data get/
        plot=new Plot();
        binder=new Binder(Plot.class);
        binder.forField(name)
//                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Plot::getName, Plot::setName);
        binder.forField(dimension)
//                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Plot::getDimension, Plot::setDimension);
        binder.forField(location)
//                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Plot::getLocation, Plot::setLocation);
        binder.forField(address)
//                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Plot::getAddress, Plot::setAddress);
        binder.forField(type)
//                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Plot::getType, Plot::setType);
        binder.forField(marlas)
//                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Plot::getMarlas, Plot::setMarlas);
        
        
        binder.forField(purchasePricePerMarla)
                .withConverter(new StringToDoubleConverter("Enter Value in Double"))
//                .withValidator(Double -> Double > 0, "Input value should be a Greater than Zero.")
                .bind(Plot::getPurchasePricePerMarla
                        , Plot::setPurchasePricePerMarla);
        binder.forField(totalprice)
                .withConverter(new StringToDoubleConverter("Enter Value in Double"))
//                .withValidator(Double -> Double > 0, "Input value should be a Greater than Zero.")
                .bind(Plot::getTotalprice, Plot::setTotalprice);
        binder.forField(saleprice)
                .withConverter(new StringToDoubleConverter("Enter Value in Double"))
//                .withValidator(Double -> Double > 0, "Input value should be a Greater than Zero.")
                .bind(Plot::getSaleprice, Plot::setSaleprice);
        binder.forField(description)
                .bind(Plot::getDescription, Plot::setDescription);
        binder.readBean(plot);
        
        window=new Window();
        window.setWidth(400.0f,Unit.PIXELS);
        window.setContent(form);
        window.center();
        window.setResizeLazy(true);
        
        add_plot=new Button("Add Plot");
        add_plot.setStyleName(ValoTheme.BUTTON_PRIMARY);
        add_plot.setIcon(VaadinIcons.PLUS);
        add_plot.addClickListener(e ->{
            try {
                plot.setId(null);
            this.getUI().addWindow(window);
                
            } catch (IllegalArgumentException ex) {
            }
        });
        
        update=new Button("Update");
        update.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        update.setIcon(VaadinIcons.EDIT);
        update.addClickListener(e ->{
        if(!grid.getSelectedItems().isEmpty()){
            try {
                plot=grid.asSingleSelect().getValue();
                name.setValue(plot.getName());
                address.setValue(plot.getAddress());
                location.setValue(plot.getLocation());
                description.setValue(plot.getDescription());
                dimension.setValue(plot.getDimension());
                type.setValue(plot.getType());
                marlas.setValue(plot.getMarlas());
                purchasePricePerMarla.setValue(String.valueOf(plot.getPurchasePricePerMarla()));
                saleprice.setValue(String.valueOf(plot.getSaleprice()));
                totalprice.setValue(String.valueOf(plot                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 .getTotalprice()));
                
               
            this.getUI().addWindow(window);
                
            } catch (IllegalArgumentException ex) {
            Notification.show(ex.getMessage(),Notification.Type.ERROR_MESSAGE);
            }
        }else{
            Notification.show("Please First Select a Row to Update",Notification.Type.WARNING_MESSAGE);
        }
});

        
        ///This horizental layout is for adding button on the top of grid..
        buttonlayout=new HorizontalLayout();
        buttonlayout.addComponents(add_plot,update);
       
               
        //adding components to view...
        addComponents(buttonlayout,grid);
    }
    
        ///Clear all Field to their Default values mean Empty....
       void clear(){
        name.setValue("");
        address.setValue("");
        location.setValue("");
        description.setValue("");
        dimension.setValue("");
        type.setValue("");
        marlas.setValue("");
        purchasePricePerMarla.setValue("");
        saleprice.setValue("");
        totalprice.setValue("");
            
    }
       
       void getGridData() {
        grid.setDataProvider(
                (sortOrders, offset, limit)
                -> plotRepository.findAll(new PageRequest(offset/limit, limit)).getContent().stream(),
                () -> Integer.parseInt(plotRepository.count() + "")
        );
    }
    
       ////Method to filter Data on filtering textfield change value..
    public void filterName(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setDataProvider(
                (sortOrders, offset, limit)
                -> plotRepository.findAll(new PageRequest(offset/limit, limit)).getContent().stream(),
                () -> Integer.parseInt(plotRepository.count() + "")
        );
        } else {
            grid.setItems(plotRepository.findByNameIgnoreCaseContaining(filterText));
        }
    }
}
