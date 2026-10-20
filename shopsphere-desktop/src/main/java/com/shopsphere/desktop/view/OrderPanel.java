package com.shopsphere.desktop.view;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.service.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Swing View Panel: Order Management and Status Transitions.
 */
public class OrderPanel extends JPanel {

    private final OrderService orderService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JComboBox<Order.OrderStatus> cmbStatus;
    private final JButton btnUpdateStatus;
    private final JButton btnViewItems;
    private final JButton btnRefresh;

    public OrderPanel() {
        this.orderService = new OrderService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.add(new JLabel("Transition Status:"));
        cmbStatus = new JComboBox<>(Order.OrderStatus.values());
        btnUpdateStatus = new JButton("Update Selected Order");
        btnViewItems = new JButton("View Order Items");
        btnRefresh = new JButton("Refresh Orders");

        toolbar.add(cmbStatus);
        toolbar.add(btnUpdateStatus);
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(btnViewItems);
        toolbar.add(btnRefresh);

        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Order ID", "Customer", "Amount (₹)", "Payment Method", "Payment Status", "Order Status", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Listeners
        btnRefresh.addActionListener(e -> loadOrders());
        btnUpdateStatus.addActionListener(e -> handleUpdateStatus());
        btnViewItems.addActionListener(e -> handleViewItems());

        loadOrders();
    }

    public void loadOrders() {
        new SwingWorker<List<Order>, Void>() {
            @Override
            protected List<Order> doInBackground() throws Exception {
                return orderService.getAllOrders();
            }

            @Override
            protected void done() {
                try {
                    List<Order> orders = get();
                    tableModel.setRowCount(0);
                    for (Order o : orders) {
                        tableModel.addRow(new Object[]{
                                o.getOrderId(),
                                o.getUserName() + " (" + o.getUserEmail() + ")",
                                o.getNetPayable(),
                                o.getPaymentMethod(),
                                o.getPaymentStatus(),
                                o.getOrderStatus(),
                                o.getCreatedAt()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(OrderPanel.this, "Error loading orders: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void handleUpdateStatus() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(row, 0);
        Order.OrderStatus target = (Order.OrderStatus) cmbStatus.getSelectedItem();

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return orderService.updateOrderStatus(orderId, target);
            }

            @Override
            protected void done() {
                loadOrders();
                JOptionPane.showMessageDialog(OrderPanel.this, "Order #" + orderId + " updated to " + target, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }.execute();
    }

    private void handleViewItems() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an order to view line items.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(row, 0);
        new SwingWorker<Order, Void>() {
            @Override
            protected Order doInBackground() throws Exception {
                return orderService.getOrderDetails(orderId, 0, true).orElse(null);
            }

            @Override
            protected void done() {
                try {
                    Order order = get();
                    if (order != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Order #").append(order.getOrderId()).append(" Items:\n\n");
                        for (OrderItem item : order.getItems()) {
                            sb.append("• ").append(item.getProduct().getName())
                              .append(" | Qty: ").append(item.getQuantity())
                              .append(" | Price: ₹").append(item.getPrice())
                              .append(" | Subtotal: ₹").append(item.getSubtotal()).append("\n");
                        }
                        sb.append("\nNet Payable: ₹").append(order.getNetPayable());
                        JOptionPane.showMessageDialog(OrderPanel.this, sb.toString(), "Order Items", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }
}
