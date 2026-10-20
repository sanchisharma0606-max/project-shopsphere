package com.shopsphere.desktop.view;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Swing View Panel: Warehouse Stock and Inventory Control.
 */
public class InventoryPanel extends JPanel {

    private final ProductService productService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JCheckBox chkLowStockOnly;
    private final JSpinner spnNewStock;
    private final JButton btnApplyStock;
    private final JButton btnRefresh;

    public InventoryPanel() {
        this.productService = new ProductService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        chkLowStockOnly = new JCheckBox("Show Low Stock Only (<= 10 units)");
        toolbar.add(chkLowStockOnly);

        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(new JLabel("Set New Stock:"));
        spnNewStock = new JSpinner(new SpinnerNumberModel(25, 0, 9999, 5));
        toolbar.add(spnNewStock);

        btnApplyStock = new JButton("Update Selected Stock");
        btnRefresh = new JButton("Refresh Inventory");
        toolbar.add(btnApplyStock);
        toolbar.add(btnRefresh);

        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Product ID", "Product Name", "Brand", "Category", "Current Stock Level", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Listeners
        btnRefresh.addActionListener(e -> loadInventory());
        chkLowStockOnly.addActionListener(e -> loadInventory());
        btnApplyStock.addActionListener(e -> handleUpdateStock());

        loadInventory();
    }

    public void loadInventory() {
        boolean lowOnly = chkLowStockOnly.isSelected();
        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                if (lowOnly) {
                    return productService.getLowStockProducts(10);
                }
                return productService.getAllProducts(true);
            }

            @Override
            protected void done() {
                try {
                    List<Product> list = get();
                    tableModel.setRowCount(0);
                    for (Product p : list) {
                        tableModel.addRow(new Object[]{
                                p.getProductId(),
                                p.getName(),
                                p.getBrand(),
                                p.getCategoryName(),
                                p.getStock(),
                                p.getStatus()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(InventoryPanel.this, "Error loading inventory: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void handleUpdateStock() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to update stock level.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(row, 0);
        int newStock = (int) spnNewStock.getValue();

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return productService.updateStock(productId, newStock);
            }

            @Override
            protected void done() {
                loadInventory();
                JOptionPane.showMessageDialog(InventoryPanel.this, "Stock updated to " + newStock + " units.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }.execute();
    }
}
