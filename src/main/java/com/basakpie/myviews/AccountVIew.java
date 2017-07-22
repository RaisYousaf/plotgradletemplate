/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.myviews;

import com.basakpie.models.Account;
import com.basakpie.models.AccountRepository;
import com.basakpie.models.Customer;
import com.basakpie.models.CustomerRepository;
import com.basakpie.models.Plot;
import com.basakpie.sidebar.Sections;
import com.basakpie.view.AbstractView;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

/**
 *
 * @author annonymous
 */
@SpringView(name = AccountVIew.VIEW_NAME)
@SideBarItem(sectionId = Sections.MYVIEWS, caption = "Accounts", order = 3)
@VaadinFontIcon(VaadinIcons.ADJUST)
@UIScope
@SpringComponent
@SpringUI

public class AccountVIew extends AbstractView {

    public static final String VIEW_NAME = "Accounts";
    private Button save;
    private Account account;
    private Customer customer;
    private TextField titlefield;
    private ComboBox<Account> titleBox;
    private ComboBox<Customer> customerBox;
    private Binder<Account> binder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    public AccountVIew() {
        setViewHeader("Accounts View", VaadinIcons.BOOK);

    }

    @PostConstruct
    public void init() {
        //Title Field
        account = new Account();
        titlefield = new TextField();
        titlefield.setCaption("Title");
        titlefield.setPlaceholder("Title here....");
        titlefield.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        //Binder
        binder = new Binder<>(Account.class);
        binder.forField(titlefield).bind(Account::getTitle, Account::setTitle);
        binder.readBean(account);

        //ComboBox For Title & Customer Name
        titleBox = new ComboBox<>("Select Account Category");
        customerBox = new ComboBox<>("Select Customer Name");
        customerBox.setPlaceholder("Select Customer");
        titleBox.setPlaceholder("Title...");

// Calling the ComboBox Function
        getComoboxData();
        getCustomerBox();
        save = new Button("Save");
        save.addClickListener(e -> {
            try {
                account.setId(null);
                binder.writeBean(account);

            } catch (Exception s) {
                s.printStackTrace();
            }

            account.setAccount(titleBox.getValue());
            account.setCustomer(customerBox.getValue());

            accountRepository.save(account);
            getComoboxData();
            getCustomerBox();
            clear();

        });

//    Horizontal Layout for combox
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponents(titleBox, customerBox);

        // AbstractLayout
        addComponents(titlefield, horizontalLayout, save);

    }

    //Get Title Data to "Account Category Combox"
    public void getComoboxData() {

        titleBox.setItems(accountRepository.findAll());
        titleBox.setItemCaptionGenerator(Account::getTitle);
    }

    //Get Customer Name To Customer ComboBox
    public void getCustomerBox() {

        customerBox.setItems(customerRepository.findAll());
        customerBox.setItemCaptionGenerator(Customer::getFullName);

    }

    //Cleear Title Field
    void clear() {
        titlefield.setValue("");

    }

}
