package com.shopsphere.desktop.view;

import com.shopsphere.model.Category;
import com.shopsphere.service.CategoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Swing View Panel: Category Management.
 */
public class CategoryPanel extends JPanel {

    private final CategoryService categoryService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnDelete;
    private final JButton btnRefresh;

    public CategoryPanel() {
        this.categoryService = new CategoryService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnAdd = new JButton("Add Category");
        btnEdit = new JButton("Edit Category");
        btnDelete = new JButton("Delete Category");
        btnRefresh = new JButton("Refresh");

        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Category Name", "Description", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Listeners
        btnRefresh.addActionListener(e -> loadCategories());
        btnAdd.addActionListener(e -> showCategoryDialog(null));
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        loadCategories();
    }

    public void loadCategories() {
        new SwingWorker<List<Category>, Void>() {
            @Override
            protected List<Category> doInBackground() throws Exception {
                return categoryService.getAllCategories();
            }

            @Override
            protected void done() {
                try {
                    List<Category> list = get();
                    tableModel.setRowCount(0);
                    for (Category c : list) {
                        tableModel.addRow(new Object[]{
                                c.getCategoryId(),
                                c.getName(),
                                c.getDescription(),
                                c.getStatus()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CategoryPanel.this, "Error loading categories: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a category to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String desc = (String) tableModel.getValueAt(row, 2);
        Category.Status st = (Category.Status) tableModel.getValueAt(row, 3);

        showCategoryDialog(new Category(id, name, desc, st));
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a category to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete category '" + name + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    categoryService.deleteCategory(id);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        loadCategories();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(CategoryPanel.this, "Cannot delete category: In use by products.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }

    private void showCategoryDialog(Category existing) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                existing != null ? "Edit Category" : "Add Category", true);
        dialog.setSize(380, 260);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtName = new JTextField(existing != null ? existing.getName() : "");
        JTextField txtDesc = new JTextField(existing != null ? existing.getDescription() : "");
        JComboBox<Category.Status> cmbStatus = new JComboBox<>(Category.Status.values());
        if (existing != null) cmbStatus.setSelectedItem(existing.getStatus());

        panel.add(new JLabel("Category Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Description:"));
        panel.add(txtDesc);
        panel.add(new JLabel("Status:"));
        panel.add(cmbStatus);

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        panel.add(btnSave);
        panel.add(btnCancel);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) return;

            Category c = new Category(existing != null ? existing.getCategoryId() : 0,
                    name, txtDesc.getText().trim(), (Category.Status) cmbStatus.getSelectedItem());

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    categoryService.saveCategory(c);
                    return null;
                }

                @Override
                protected void done() {
                    dialog.dispose();
                    loadCategories();
                }
            }.execute();
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
