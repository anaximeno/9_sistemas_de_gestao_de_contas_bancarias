package com.groupnine.banku.controllers;

import com.groupnine.banku.businesslogic.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;

public class ClientsController {
    @FXML
    private Button dashboardBtn;

    @FXML
    protected void dashboardBtnOnClick() throws IOException {
        WindowsContextController.showDashboardOnPrincipalStage();
    }

    public static abstract class AccountOwnerBean {
        final private SimpleStringProperty id;
        final private SimpleStringProperty name;
        final private SimpleStringProperty NIF;
        final private SimpleStringProperty address;

        public AccountOwnerBean(final AccountOwner accountowner) {
            this.id = new SimpleStringProperty(accountowner.getOwnerID());
            this.name = new SimpleStringProperty(accountowner.getName());
            this.NIF = new SimpleStringProperty(accountowner.getNIF());
            this.address = new SimpleStringProperty(accountowner.getAddress());
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getNIF() {
            return NIF.get();
        }

        public void setNIF(String NIF) {
            this.NIF.set(NIF);
        }

        public String getAddress() {
            return address.get();
        }

        public void setAddress(String address) {
            this.address.set(address);
        }
    }

    public static class ParticularAccountOwnerBean extends ClientsController.AccountOwnerBean {

        final private SimpleStringProperty nationality;
        final private SimpleIntegerProperty accountNum;

        public ParticularAccountOwnerBean(final ParticularAccountOwner particularAccountOwner) {
            super((AccountOwner) particularAccountOwner);
            this.nationality = new SimpleStringProperty(particularAccountOwner.getNationality());
            this.accountNum = new SimpleIntegerProperty(particularAccountOwner.getAccounts() != null ? particularAccountOwner.getAccounts().size() : 0);
        }

        public String getNationality() {
            return nationality.get();
        }

        public void setNationality(String nationality) {
            this.nationality.set(nationality);
        }

        public int getAccountNum() {
            return accountNum.get();
        }

        public void setAccountNum(int accountNum) {
            this.accountNum.set(accountNum);
        }
    }

    public static class EnterpriseAccountOwnerBean extends ClientsController.AccountOwnerBean {
        final private SimpleIntegerProperty partnersNum;
        final private SimpleIntegerProperty accountsNum;

        EnterpriseAccountOwnerBean(final EnterpriseAccountOwner enterpriseAccountOwner) {
            super((AccountOwner) enterpriseAccountOwner);
            this.partnersNum = new SimpleIntegerProperty(enterpriseAccountOwner.getPartners() != null ? enterpriseAccountOwner.getPartners().size() : 0);
            this.accountsNum = new SimpleIntegerProperty(enterpriseAccountOwner.getAccounts() != null ? enterpriseAccountOwner.getAccounts().size() : 0);
        }

        public int getPartnersNum() {
            return partnersNum.get();
        }

        public void setPartnersNum(int partnersNum) {
            this.partnersNum.set(partnersNum);
        }

        public int getAccountsNum() {
            return accountsNum.get();
        }

        public void setAccountsNum(int accountsNum) {
            this.accountsNum.set(accountsNum);
        }
    }
}
