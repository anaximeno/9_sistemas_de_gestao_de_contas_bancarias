package com.groupnine.banku.controllers;

import com.groupnine.banku.BankuApp;
import com.groupnine.banku.businesslogic.*;
import com.groupnine.banku.miscellaneous.InputValidationResult;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AddNewParticularAccountController {
    public static WindowContextController activeInstance;

    @FXML
    private TextField accountNameInput;
    @FXML
    private TextField ownerNumberInput;
    @FXML
    private TextField initialBalanceInput;
    @FXML
    private TextField associateNumberInput;

    @FXML
    private Text resultText;
    @FXML
    private Text explainText;

    // todo: extract EMPTY_STR to miscellaneous
    final String EMPTY_STR = "";

    private void clearView() {
        resultText.setText(EMPTY_STR);
        explainText.setText(EMPTY_STR);
        clearInputForms();
    }

    private void clearInputForms() {
        accountNameInput.setText(EMPTY_STR);
        ownerNumberInput.setText(EMPTY_STR);
        initialBalanceInput.setText(EMPTY_STR);
        associateNumberInput.setText(EMPTY_STR);
    }

    @FXML
    protected void initialize()
    {
        clearView();
    }

    @FXML
    protected void clearButtonOnClick() {
        clearView();
    }

    @FXML
    protected void createButtonOnClick()
    {
        final BankAgency agency = BankAgency.getInstance();

        InputValidationResult result = validateInputs();

        if (result.isValid) {
            final String ownerNumber = ownerNumberInput.getText();
            final String associateNumber = associateNumberInput.getText();
            final String name = accountNameInput.getText();
            final String balance = initialBalanceInput.getText();

            final AccountOwner owner = agency.findAccountOwnerByID(ownerNumber);
            final ParticularAccountOwner associate = (ParticularAccountOwner) agency.findAccountOwnerByID(associateNumber);

            if (owner instanceof ParticularAccountOwner pOwner) {
                // for parAcc
                OrdinaryParticularAccount account = BankuApp.globalAccFactory.createOrdinaryParticularAccount(
                        name, pOwner, Double.parseDouble(balance), associate);

                BankuApp.currentOperator.addNewAccountToTheBank(account);

                clearInputForms();

                if (AccountsController.getActiveInstance() != null) {
                    AccountsController.getActiveInstance().refreshTables();
                }

                result.explainStatus = "A conta '" + name + "' foi adicionada com sucesso.";
            } else {
                result = new InputValidationResult(false, "O tipo de dono de conta fornecido não é particular!\n");
            }
        }

        displayResults(result);
    }

    @FXML
    protected void searchOwnerButtonOnClick() {
        openAccountIdSelectionWindow(selectedId -> ownerNumberInput.setText(selectedId));
    }

    @FXML
    protected void searchAssociateButtonOnClick() {
        openAccountIdSelectionWindow(selectedId -> associateNumberInput.setText(selectedId));
    }

    protected void openAccountIdSelectionWindow(OnValidSelectedAction action) {
        SelectOwnerIdController.setAccountTypeFilter(AccountType.PARTICULAR);
        SelectOwnerIdController.setOnValidSelectedAction(action);
        if (SelectOwnerIdController.activeWindowInstance == null) {
            SelectOwnerIdController.activeWindowInstance = new WindowContextController(390, 535, "views/select-owner-id-view.fxml", "Select Owner");
            SelectOwnerIdController.activeWindowInstance.showDefaultView();
        } else {
            SelectOwnerIdController.activeWindowInstance.getStage().show();
        }
    }

    @FXML
    protected void cancelButtonOnClick() {
        if (activeInstance != null)
            activeInstance.getStage().close();
    }

    private void displayResults(final InputValidationResult result)
    {
        resultText.setText(result.isValid ? "Sucesso!" : "Insucesso!");
        resultText.setFill(Paint.valueOf(result.isValid ? "green" : "red"));
        explainText.setText(result.explainStatus);
    }

    private InputValidationResult validateInputs()
    /* Valida os inputs e retorna o resultado da validação global. */
    {
        List<InputValidationResult> list = new ArrayList<>();
        list.add(validateAccountName(accountNameInput.getText()));
        list.add(validateOwnerNumber(ownerNumberInput.getText()));
        list.add(validateInitialBalance(initialBalanceInput.getText()));
        list.add(validateAssociateNumber(associateNumberInput.getText()));

        InputValidationResult finalResult = new InputValidationResult(true, "");

        for (InputValidationResult res : list) {
            if (!res.isValid) {
                finalResult.isValid = false;

                finalResult.explainStatus = finalResult.explainStatus.concat(res.explainStatus + "\n");
            }
        }

        if (finalResult.isValid) {
            finalResult.explainStatus = "Conta criada com sucesso!";
        }

        return finalResult;
    }

    private InputValidationResult validateAssociateNumber(String value)
    {
        if (value == null || value.isEmpty()) /* Associado é opcional. */
            return new InputValidationResult(true);
        else {
            try {
                int ret = Integer.parseInt(value);
                return new InputValidationResult(true);
            } catch (NumberFormatException exception) { /* Quando fornecido deve poder ser convertido para int. */
                return new InputValidationResult(false, "Número de associado inválido.");
            }
        }
    }

    private InputValidationResult validateAccountName(String value)
    {
        final BankAgency agency = BankAgency.getInstance();

        if (value.isEmpty()) {
            return new InputValidationResult(false, "Nome da conta não foi inserida.");
        }
        else if (agency.findAccountByName(value) != null) {
            return new InputValidationResult(false, "Nome da conta já existe na agência.");
        }
        else if (value.length() < 3) {
            return new InputValidationResult(false, "Nome da conta é muito pequeno, requer-se ao menos 3 caracteres.");
        }
        else {
            return new InputValidationResult(true);
        }
    }

    private InputValidationResult validateOwnerNumber(String value)
    {
        final BankAgency agency = BankAgency.getInstance();

        if (value.isEmpty()) {
            return new InputValidationResult(false, "Número do dono não foi inserido.");
        }
        else if (agency.findAccountOwnerByID(value) == null) {
            return new InputValidationResult(false, "Número do dono não encontrado na agência.");
        }
        else {
            return new InputValidationResult(true);
        }
    }

    private InputValidationResult validateInitialBalance(String value)
    {
        final double minimumInitialBalance = 5000;
        double initialBalance;

        try {
            initialBalance = Double.parseDouble(value);
        } catch (NullPointerException exception) {
            return new InputValidationResult(false, "Balanço inicial não foi inserido.");
        } catch (NumberFormatException exception) {
            if (value.isEmpty())
                return new InputValidationResult(false, "Balanço inicial não foi inserido.");
            return new InputValidationResult(false, "Valor inválido para balanço inicial. Deve ser um número.");
        }

        if (initialBalance < minimumInitialBalance) {
            return new InputValidationResult(false, "Balanço deve maior ou igual a " + minimumInitialBalance + " escudos.");
        }
        else {
            return new InputValidationResult(true);
        }
    }
}