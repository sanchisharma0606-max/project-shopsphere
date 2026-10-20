package com.shopsphere.desktop.view;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Swing View Panel: Customer Accounts.
 */
public class CustomerPanel extends JPanel {

    private final UserService userService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;
    private final JButton btnSearch;
    private final JButton btnToggleStatus;
    private final JButton btnRefresh;

    public CustomerPanel() {
        this.userService = new UserService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchField = new JTextField(18);
        btnSearch = new JButton("Search Customer");
        btnToggleStatus = new JButton("Toggle Active/Suspended");
        btnRefresh = new JButton("Refresh");

        toolbar.add(searchField);
        toolbar.add(btnSearch);
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(btnToggleStatus);
        toolbar.add(btnRefresh);
        add(toolbar, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Email", "Mobile", "Role", "Status", "Joined Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        add(new JScrollPane(table), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadUsers(searchField.getText().trim()));
        btnRefresh.addActionListener(e -> {
            searchField.setText("");
            loadUsers(null);
        });
        btnToggleStatus.addActionListener(e -> handleToggleStatus());

        loadUsers(null);
    }

    public void loadUsers(String query) {
        new SwingWorker<List<User>, Void>() {
            @Override
            protected List<User> doInBackground() throws Exception {
                return userService.searchUsers(query);
            }

            @Override
            protected void done() {
                try {
                    List<User> list = get();
                    tableModel.setRowCount(0);
                    for (User u : list) {
                        tableModel.addRow(new Object[]{
                                u.getUserId(),
                                u.getName(),
                                u.getEmail(),
                                u.getMobile() != null ? u.getMobile() : "N/A",
                                u.getRole(),
                                u.getStatus(),
                                u.getCreatedAt()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CustomerPanel.this, "Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void handleToggleStatus() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer to toggle status.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        User.Role role = (User.Role) tableModel.getValueAt(row, 4);
        if (role == User.Role.ADMIN) {
            JOptionPane.showMessageDialog(this, "Cannot alter administrator accounts from customer panel.", "Protected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User.Status currentStatus = (User.Status) tableModel.getValueAt(row, 5);
        User.Status newStatus = (currentStatus == User.Status.ACTIVE) ? User.Status.SUSPENDED : User.Status.ACTIVE;

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                userService.toggleUserStatus(id, newStatus);
                return null;
            }

            @Override
            protected void done() {
                loadUsers(null);
            }
        }.execute();
    }
}
