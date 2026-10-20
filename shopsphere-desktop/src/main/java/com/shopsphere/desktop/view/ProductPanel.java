package com.shopsphere.desktop.view;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Swing View Panel: Product Management and Catalogue CRUD.
 */
public class ProductPanel extends JPanel {

    private final ProductService productService;
    private final CategoryService categoryService;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;
    private final JButton btnSearch;
    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnDelete;
    private final JButton btnRefresh;

    public ProductPanel() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Control Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        searchField = new JTextField(18);
        btnSearch = new JButton("Search");
        btnAdd = new JButton("Add Product");
        btnEdit = new JButton("Edit Product");
        btnDelete = new JButton("Delete Product");
        btnRefresh = new JButton("Refresh");

        toolbar.add(new JLabel("Find:"));
        toolbar.add(searchField);
        toolbar.add(btnSearch);
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);

        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Brand", "Category", "Price (₹)", "Discount (%)", "Stock", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Action Listeners
        btnSearch.addActionListener(e -> loadProducts(searchField.getText().trim()));
        btnRefresh.addActionListener(e -> {
            searchField.setText("");
            loadProducts(null);
        });
        btnAdd.addActionListener(e -> showProductDialog(null));
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        // Initial Load
        loadProducts(null);
    }

    public void loadProducts(String query) {
        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                if (query != null && !query.isEmpty()) {
                    return productService.searchAndFilter(query, null, null, null, null, 1, 100);
                }
                return productService.getAllProducts(true);
            }

            @Override
            protected void done() {
                try {
                    List<Product> products = get();
                    tableModel.setRowCount(0);
                    for (Product p : products) {
                        tableModel.addRow(new Object[]{
                                p.getProductId(),
                                p.getName(),
                                p.getBrand(),
                                p.getCategoryName(),
                                p.getPrice(),
                                p.getDiscount(),
                                p.getStock(),
                                p.getStatus()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProductPanel.this, "Error loading products: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        new SwingWorker<Product, Void>() {
            @Override
            protected Product doInBackground() throws Exception {
                return productService.getProductById(productId).orElse(null);
            }

            @Override
            protected void done() {
                try {
                    Product p = get();
                    if (p != null) {
                        showProductDialog(p);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProductPanel.this, "Error retrieving product details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete product '" + name + "' (#" + productId + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return productService.deleteProduct(productId);
                }

                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(ProductPanel.this, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadProducts(null);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ProductPanel.this, "Cannot delete product: It is linked to existing customer orders.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }

    private void showProductDialog(Product existing) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                existing != null ? "Edit Product #" + existing.getProductId() : "Add New Product", true);
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JTextField txtName = new JTextField(existing != null ? existing.getName() : "");
        JTextField txtBrand = new JTextField(existing != null ? existing.getBrand() : "");
        JComboBox<Category> cmbCategory = new JComboBox<>();
        JTextField txtPrice = new JTextField(existing != null ? existing.getPrice().toString() : "0.00");
        JTextField txtDiscount = new JTextField(existing != null ? existing.getDiscount().toString() : "0.00");
        JTextField txtStock = new JTextField(existing != null ? String.valueOf(existing.getStock()) : "10");
        JComboBox<Product.Status> cmbStatus = new JComboBox<>(Product.Status.values());
        if (existing != null) cmbStatus.setSelectedItem(existing.getStatus());

        // Load categories into combobox
        try {
            List<Category> cats = categoryService.getAllCategories();
            for (Category c : cats) {
                cmbCategory.addItem(c);
                if (existing != null && c.getCategoryId() == existing.getCategoryId()) {
                    cmbCategory.setSelectedItem(c);
                }
            }
        } catch (Exception ignored) {}

        panel.add(new JLabel("Product Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Brand:"));
        panel.add(txtBrand);
        panel.add(new JLabel("Category:"));
        panel.add(cmbCategory);
        panel.add(new JLabel("Price (₹):"));
        panel.add(txtPrice);
        panel.add(new JLabel("Discount (%):"));
        panel.add(txtDiscount);
        panel.add(new JLabel("Stock Quantity:"));
        panel.add(txtStock);
        panel.add(new JLabel("Status:"));
        panel.add(cmbStatus);

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        panel.add(btnSave);
        panel.add(btnCancel);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                String brand = txtBrand.getText().trim();
                Category cat = (Category) cmbCategory.getSelectedItem();
                BigDecimal price = new BigDecimal(txtPrice.getText().trim());
                BigDecimal discount = new BigDecimal(txtDiscount.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());
                Product.Status status = (Product.Status) cmbStatus.getSelectedItem();

                if (name.isEmpty() || brand.isEmpty() || cat == null) {
                    JOptionPane.showMessageDialog(dialog, "Please complete all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Product p = new Product();
                if (existing != null) p.setProductId(existing.getProductId());
                p.setName(name);
                p.setBrand(brand);
                p.setCategoryId(cat.getCategoryId());
                p.setPrice(price);
                p.setDiscount(discount);
                p.setStock(stock);
                p.setStatus(status);
                p.setDescription(existing != null ? existing.getDescription() : "Product added via Swing Desktop Suite.");
                p.setImageUrl(existing != null ? existing.getImageUrl() : "assets/images/products/default.jpg");

                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        productService.saveProduct(p);
                        return null;
                    }

                    @Override
                    protected void done() {
                        dialog.dispose();
                        loadProducts(null);
                        JOptionPane.showMessageDialog(ProductPanel.this, "Product saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }.execute();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid numeric input: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
