/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.myviews;

import com.basakpie.models.Customer;
import com.basakpie.models.CustomerRepository;
import com.basakpie.models.Plot;
import com.basakpie.sidebar.Sections;
import com.basakpie.view.AbstractView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
@SpringView(name = CustomerView.VIEW_NAME)
@SideBarItem(sectionId = Sections.MYVIEWS, caption = "Customer", order = 2)
@VaadinFontIcon(VaadinIcons.ADJUST)
@UIScope
@SpringComponent
@SpringUI
public class CustomerView extends AbstractView {

    public static final String VIEW_NAME = "Customer";
    
    private byte [] fileByte;
    private File file;

    private TextField fullName;
    private TextField fatherName;
    private TextField cnic;
    private TextField currentAddress;
    private TextField permanentAddress;
    private TextField phoneNo;
    private TextField email;
    private TextField accountNo;
    private TextArea description;

    private Button save, add, update;
    private Button clear;
    
    private Panel upload;
    private Customer customer;
    private FormLayout form;
    private Grid<Customer> grid;
    private Window window;
    private HorizontalLayout buttonlayout;

    private Binder<Customer> binder;

    @Autowired
    CustomerRepository customerRepository;

    public CustomerView() {
        setViewHeader("Customer View", VaadinIcons.USERS);
    }
    
    @PostConstruct
    public void init(){
        form= new FormLayout();
        form.setCaption("<h3>Enter Customer Detail...</h3>");
        form.setCaptionAsHtml(true);
        form.setSizeUndefined();
        
        fullName=new TextField("Full Name :");
        fullName.setRequiredIndicatorVisible(true);
        fullName.setWidth("50%");
        fullName.setPlaceholder("FullName...");
        
        fatherName=new TextField("Father Name :");
        fatherName.setRequiredIndicatorVisible(true);
        fatherName.setWidth("50%");
        fatherName.setPlaceholder("FatherName...");
        
        cnic=new TextField("CNIC :");
        cnic.setRequiredIndicatorVisible(true);
        cnic.setWidth("50%");
        cnic.setPlaceholder("Enter CNIC...");
        
        email=new TextField("Email :");
        email.setRequiredIndicatorVisible(false);
        email.setWidth("50%");
        email.setPlaceholder("Enter Email...");
        
        phoneNo=new TextField("Phone No :");
        phoneNo.setRequiredIndicatorVisible(true);
        phoneNo.setWidth("50%");
        phoneNo.setPlaceholder("Phone#...");
        
        accountNo=new TextField("Account No :");
        accountNo.setRequiredIndicatorVisible(true);
        accountNo.setWidth("50%");
        accountNo.setPlaceholder("Account#...");
        
        currentAddress=new TextField("Current Address :");
        currentAddress.setRequiredIndicatorVisible(false);
        currentAddress.setWidth("50%");
        currentAddress.setHeight("30%");
        currentAddress.setPlaceholder("Current Address...");
        
        permanentAddress=new TextField("Permanent Address :");
        permanentAddress.setRequiredIndicatorVisible(true);
        permanentAddress.setWidth("50%");
        permanentAddress.setHeight("30%");
        permanentAddress.setPlaceholder("Permanent Address...");
        
        description = new TextArea();
        description.setStyleName(ValoTheme.TEXTAREA_SMALL);
        description.setCaption("Description :");
        description.setWidth("50%");
        description.setPlaceholder("Description...");
        
        upload=new Panel("Upload CNIC Image here:");
        
        ByteArrayOutputStream os =
                new ByteArrayOutputStream(10240);
        
        
        Upload.Receiver receiver=new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    
                    file=new File(filename);
                    
                    fos = new FileOutputStream(file);
                    
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                                     e.getMessage(),
                                     Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
                
               
            }
        };
        Upload cnicUpload=new Upload("Select File", receiver);
        
        
        
        upload.setContent(cnicUpload);
        save=new Button("Save");
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setDescription("This Button saves new Plot Detail...");
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(event -> {
           //to do save here
            try {
                binder.writeBean(customer);
            } catch (ValidationException ex) {
                Notification.show("Enter Valid Data in Form",Notification.Type.ERROR_MESSAGE);
            }

            customerRepository.save(customer);
            clear();
            getGridData();
            window.close();
        });
        
        
        clear=new Button("Clear");
        clear.setDescription("Clear all Field to their Default Values(Empty)");
        clear.addClickListener(e ->{
            ///to do clear here
            clear();
        });
    
        form.addComponents(fullName,fatherName,phoneNo,email,cnic,accountNo,currentAddress,permanentAddress,
                description,upload,new HorizontalLayout(clear,save));
        form.setMargin(true);
        
        
        
        grid=new Grid(Customer.class);
        grid.setCaption("<h3>Customer Details...</h3>");
        grid.setCaptionAsHtml(true);
        getGridData();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//        grid.setStyleName(ValoTheme.LAYOUT_WELL);
        grid.setWidth(this.getWidth(),Unit.PERCENTAGE);
        grid.setColumns("id","fullName","fatherName","cnic","phoneNo","email","accountNo","currentAddress","permanentAddress","description");
        HeaderRow filterRow=grid.appendHeaderRow();
        HeaderCell filternamecell=filterRow.getCell("fullName");
        TextField filter=new TextField();
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setPlaceholder("Filter name...");
        filternamecell.setComponent(filter);
        filter.addValueChangeListener(e ->{
           //todo filter here
           filterName(e.getValue());
        });
        
        //creat window for form display
        window=new Window();
        window.setWidth(400.0f,Unit.PIXELS);
        window.setContent(form);
        window.center();
        window.setResizeLazy(true);
        
        add=new Button("Add Customer");
        add.setStyleName(ValoTheme.BUTTON_PRIMARY);
        add.setIcon(VaadinIcons.PLUS);
        add.addClickListener(e ->{
            try {
                customer.setId(null);
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
               customer=grid.asSingleSelect().getValue();
                fullName.setValue(customer.getFullName());
                fatherName.setValue(customer.getFatherName());
                cnic.setValue(String.valueOf( customer.getCnic()));
                description.setValue(customer.getDescription());
                currentAddress.setValue(customer.getCurrentAddress());
                permanentAddress.setValue(customer.getPermanentAddress());
                email.setValue(customer.getEmail());
                accountNo.setValue(String.valueOf(customer.getAccountNo()));
                phoneNo.setValue(String.valueOf(customer.getPhoneNo()));
                
               
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
        buttonlayout.addComponents(add,update);
        
        
        ///binder intializing to fields..
        customer=new Customer();
        binder=new Binder(Customer.class);
        
        binder.forField(fullName)
                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Customer:: getFullName,Customer::setFullName);
        
        
        binder.forField(fatherName)
                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Customer:: getFatherName,Customer::setFatherName);
        
        
        binder.forField(cnic)
                .withConverter(new StringToLongConverter("Enter Value in Long Integer"))
                .withValidator(Long -> Long > 0, "Input value should be a Greater than Zero.")
                .bind(Customer:: getCnic,Customer::setCnic);
        
        
        binder.forField(currentAddress)
                .bind(Customer:: getCurrentAddress,Customer::setCurrentAddress);
        
        binder.forField(permanentAddress)
                .withValidator(string -> string != null && !string.isEmpty(), "Input values should not be empty")
                .bind(Customer:: getPermanentAddress,Customer::setPermanentAddress);
        
        binder.forField(phoneNo)
                .withConverter(new StringToLongConverter("Enter Value in Double"))
                .bind(Customer:: getPhoneNo,Customer::setPhoneNo);
        
        binder.forField(email)
                .bind(Customer:: getEmail,Customer::setEmail);
        
        binder.forField(accountNo)
                .withConverter(new StringToLongConverter("Enter Value in Double"))
                .bind(Customer:: getAccountNo,Customer::setAccountNo);
        
        binder.forField(description)
                .bind(Customer::getDescription, Customer::setDescription);
        
        binder.readBean(customer);
        
        
        
        //adding components to view...
        addComponents(buttonlayout,grid);
    }
    
    void getGridData() {
        grid.setDataProvider(
                (sortOrders, offset, limit)
                -> customerRepository.findAll(new PageRequest(offset/limit, limit)).getContent().stream(),
                () -> Integer.parseInt(customerRepository.count() + "")
        );
    }
    ////Method to filter Data on filtering textfield change value..
    public void filterName(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setDataProvider(
                (sortOrders, offset, limit)
                -> customerRepository.findAll(new PageRequest(offset/limit, limit)).getContent().stream(),
                () -> Integer.parseInt(customerRepository.count() + "")
        );
        } else {
            grid.setItems(customerRepository.findByFullNameIgnoreCaseContaining(filterText));
        }
    }
    
    
    ///Clear all Field to their Default values mean Empty....
       void clear(){
        fullName.setValue("");
        fatherName.setValue("");
        cnic.setValue("");
        description.setValue("");
        currentAddress.setValue("");
        permanentAddress.setValue("");
        email.setValue("");
        accountNo.setValue("");
        phoneNo.setValue("");
        
    }
}
