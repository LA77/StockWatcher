package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {
  private VerticalPanel mainPanel = new VerticalPanel();
  private FlexTable stocksFlexTable = new FlexTable();
  private HorizontalPanel addPanel = new HorizontalPanel();
  private TextBox newSymbolTextBox = new TextBox();
  private Button addStockButton = new Button("Add");
  private Label lastUpdatedLabel = new Label();
  private ArrayList<String> stocks = new ArrayList<>();

  public void onModuleLoad() {
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");

    // Assemble Add Stock panel.
    addPanel.add(newSymbolTextBox);
    addPanel.add(addStockButton);

    // Assemble Main panel.
    mainPanel.add(stocksFlexTable);
    mainPanel.add(addPanel);
    mainPanel.add(lastUpdatedLabel);

    // Associate the Main panel with the HTML host page.
    RootPanel.get("stockList").add(mainPanel);

    // Move cursor focus to the input box.
    newSymbolTextBox.setFocus(true);

    //Setup a timer to refresh the list automatically
    Timer refreshTimer = new Timer() {
      @Override
      public void run() {
        refreshWatchList();
      }
    };

    // Listen for mouse events on the Add button.
    // converted to lambda
    addStockButton.addClickHandler(event -> addStock());

    // Listen for keyboard events in the input box.
    // replaced with lambda to try out
    newSymbolTextBox.addKeyDownHandler(event -> {
      if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
        addStock();
      }
    });
  }

  private void refreshWatchList() {
  }

  /**
   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
   * presses enter in the newSymbolTextBox.
   */
  private void addStock() {
    final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
    newSymbolTextBox.setFocus(true);

    // Stock code must be between 1 and 10 characters that are numbers, letters or dots
    if(!symbol.matches("^[0-9A-Z.]{1,10}$")) {
      Window.alert("'" + symbol + "' is not a valid symbol.");
      newSymbolTextBox.selectAll();
      return;
    }

    newSymbolTextBox.setText("");

    // Don't add the stock if it's already in the table.
    if(stocks.contains(symbol))
      return;

    // Add the stock to the table
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);
    stocksFlexTable.setText(row, 0, symbol);

    // Add a button to remove this stock from the table.
    Button removeStockButton = new Button("x");
    removeStockButton.addClickHandler(event -> {
      int removedIndex = stocks.indexOf(symbol);
      stocks.remove(removedIndex);
      stocksFlexTable.removeRow(removedIndex+1);
    });
    stocksFlexTable.setWidget(row, 3, removeStockButton);

    // TODO Get the stock price.
  }
}
