/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.view;

import com.basakpie.sidebar.Sections;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import javax.annotation.PostConstruct;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

/**
 *
 * @author Moieen
 */
@SpringView(name = CustomerView.VIEW_NAME)
@SideBarItem(sectionId = Sections.CUSTOMER, caption = "CusstomerView", order =2)
@VaadinFontIcon(VaadinIcons.ADJUST)
public class CustomerView extends AbstractView {

    public static final String VIEW_NAME = "customerView";

    public CustomerView() {
        setViewHeader("Customer View", VaadinIcons.ADJUST);
    }

    @PostConstruct
    public void init() {
        addComponents(new Label("Hello, Welcome to Customer View."),new TextArea("Demo Text Area", "Write something here"));
    }

}