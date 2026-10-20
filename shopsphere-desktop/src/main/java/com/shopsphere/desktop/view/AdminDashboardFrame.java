package com.shopsphere.desktop.view;

import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Main Swing Admin Dashboard Frame containing tabs for Products, Categories, Orders, Customers, and Inventory.
 */
public class AdminDashboardFrame extends JFrame {

    private final User currentUser;
    private final OrderService orderService;
    private final JLabel lblStats;

    public AdminDashboardFrame(User user) {
        this.currentUser = user;
        this.orderService = new OrderService();

        setTitle("ShopSphere Desktop Administration Suite - Logged in as " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem miRefresh = new JMenuItem("Refresh All Metrics");
        JMenuItem miExit = new JMenuItem("Exit Application");
        menuFile.add(miRefresh);
        menuFile.addSeparator();
        menuFile.add(miExit);

        JMenu menuHelp = new JMenu("Help");
        JMenuItem miAbout = new JMenuItem("About ShopSphere");
        menuHelp.add(miAbout);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);

        // Main Layout
        JPanel contentPane = new JPanel(new BorderLayout(5, 5));

        // Header KPI Banner
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 23, 42));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titleLabel = new JLabel("ShopSphere Enterprise Admin [Swing Desktop]");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        lblStats = new JLabel("Loading operational telemetry...");
        lblStats.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStats.setForeground(new Color(199, 210, 254));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(lblStats, BorderLayout.EAST);
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane hosting panels
        JTabbedPane tabbedPane = new JTabbedPane();
        ProductPanel productPanel = new ProductPanel();
        CategoryPanel categoryPanel = new CategoryPanel();
        OrderPanel orderPanel = new OrderPanel();
        CustomerPanel customerPanel = new CustomerPanel();
        InventoryPanel inventoryPanel = new InventoryPanel();

        tabbedPane.addTab("Products", new ImageIcon(), productPanel, "Manage Products & Catalog");
        tabbedPane.addTab("Categories", new ImageIcon(), categoryPanel, "Manage Categories");
        tabbedPane.addTab("Orders", new ImageIcon(), orderPanel, "Customer Orders & Statuses");
        tabbedPane.addTab("Customers", new ImageIcon(), customerPanel, "Registered Customers");
        tabbedPane.addTab("Inventory", new ImageIcon(), inventoryPanel, "Stock Levels & Warehouse Alerts");

        contentPane.add(tabbedPane, BorderLayout.CENTER);
        add(contentPane);

        // Menu Actions
        miExit.addActionListener(e -> System.exit(0));
        miRefresh.addActionListener(e -> {
            loadKPIs();
            productPanel.loadProducts(null);
            categoryPanel.loadCategories();
            orderPanel.loadOrders();
            customerPanel.loadUsers(null);
            inventoryPanel.loadInventory();
        });
        miAbout.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "ShopSphere Desktop Admin Suite v1.0.0\n" +
                "Built with Java Swing (Swing MVC, Background SwingWorker, JTable)\n" +
                "RTU Advanced Java Capstone Project",
                "About ShopSphere", JOptionPane.INFORMATION_MESSAGE));

        loadKPIs();
    }

    private void loadKPIs() {
        new SwingWorker<Map<String, Object>, Void>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return orderService.getAdminDashboardStats();
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> stats = get();
                    lblStats.setText(String.format("Sales: ₹%s | Total Orders: %s | Products: %s | Low Stock Alerts: %s",
                            stats.get("totalSales"), stats.get("totalOrders"), stats.get("totalProducts"), stats.get("lowStockCount")));
                } catch (Exception ex) {
                    lblStats.setText("Database metrics offline.");
                }
            }
        }.execute();
    }
}
