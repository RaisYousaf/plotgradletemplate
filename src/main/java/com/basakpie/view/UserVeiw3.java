package com.basakpie.view;

import com.basakpie.sidebar.Sections;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import javax.annotation.PostConstruct;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

/**
 *
 * @author Muhammad Rizwan
 */
@SpringView(name = UserVeiw3.VIEW_NAME)
@SideBarItem(sectionId = Sections.USER, caption = "UserView3", order = 3)
@VaadinFontIcon(VaadinIcons.USER)
public class UserVeiw3 extends AbstractView{
    public static final String VIEW_NAME = "user_view_3";

    public UserVeiw3() {
        setViewHeader("UserView33333");
    }

    @PostConstruct
    public void init() {
        addComponent(new Label("hahahahahah."));
    }
}
