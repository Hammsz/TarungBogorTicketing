package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import model.Admin;
import model.Customer;
import model.SesiEvent;
import model.Tiket;
import model.Transaksi;
import service.BookingService;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;

public class MainFrame extends JFrame {
    private static final String PAGE_LOGIN = "LOGIN";
    private static final String PAGE_BOOKING = "BOOKING";
    private static final String PAGE_REVIEW = "REVIEW";
    private static final String PAGE_PAYMENT = "PAYMENT";
    private static final String PAGE_RECEIPT = "RECEIPT";
    private static final String PAGE_ADMIN = "ADMIN";

    private static final Color BACKGROUND = new Color(244, 242, 238);
    private static final Color SURFACE = Color.WHITE;
    private static final Color DARK = new Color(24, 24, 24);
    private static final Color CHARCOAL = new Color(38, 38, 38);
    private static final Color MUTED = new Color(70, 70, 70);
    private static final Color PLACEHOLDER = new Color(82, 82, 82);
    private static final Color LINE = new Color(224, 220, 213);
    private static final Color GOLD = new Color(190, 139, 67);
    private static final Color GOLD_DARK = new Color(117, 78, 28);
    private static final Color SOFT_GOLD = new Color(248, 239, 223);
    private static final Color ERROR = new Color(188, 55, 55);
    private static final Color SUCCESS = new Color(55, 133, 93);

    private BookingService bookingService;
    private NumberFormat formatRupiah;
    private DateTimeFormatter dateFormatter;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField adminUsernameField;
    private JPasswordField adminPasswordField;

    private JTextField namaField;
    private JTextField noHpField;
    private JTextField emailField;
    private JComboBox<String> kategoriComboBox;
    private JComboBox<String> sesiComboBox;
    private JSpinner jumlahSpinner;
    private JButton nextButton;
    private JLabel bookingMessageLabel;
    private JPanel userStockPanel;
    private JLabel selectedCategoryLabel;
    private JLabel selectedSessionLabel;
    private JLabel selectedPriceLabel;
    private JLabel selectedStockLabel;
    private Border spinnerNormalBorder;
    private Border spinnerErrorBorder;

    private JLabel reviewNamaLabel;
    private JLabel reviewNoHpLabel;
    private JLabel reviewEmailLabel;
    private JLabel reviewKategoriLabel;
    private JLabel reviewSesiLabel;
    private JLabel reviewHargaLabel;
    private JLabel reviewJumlahLabel;
    private JLabel reviewTotalLabel;
    private JLabel reviewStockLabel;

    private JComboBox<String> paymentMethodComboBox;
    private JLabel paymentTotalLabel;
    private JLabel paymentCodeLabel;
    private JLabel paymentStatusLabel;

    private JLabel receiptIdLabel;
    private JLabel receiptNameLabel;
    private JLabel receiptPhoneLabel;
    private JLabel receiptEmailLabel;
    private JLabel receiptCategoryLabel;
    private JLabel receiptSessionLabel;
    private JLabel receiptQuantityLabel;
    private JLabel receiptPriceLabel;
    private JLabel receiptTotalLabel;
    private JLabel receiptMethodLabel;
    private JLabel receiptDateLabel;

    private JPanel adminDashboardCardsPanel;
    private DefaultTableModel adminTableModel;
    private JTable adminTable;
    private JComboBox<String> adminKategoriComboBox;
    private JComboBox<String> adminSesiComboBox;
    private JSpinner adminJumlahSpinner;

    private Customer pendingCustomer;
    private Tiket pendingTiket;
    private SesiEvent pendingSesi;
    private int pendingJumlah;
    private Transaksi currentTransaction;

    public MainFrame(BookingService bookingService) {
        this.bookingService = bookingService;
        this.formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        setupLookAndFeel();
        setupFrame();
        initComponents();
        refreshAllViews();
        cardLayout.show(mainPanel, PAGE_LOGIN);
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Jika look and feel sistem gagal, Swing default tetap bisa digunakan.
        }
    }

    private void setupFrame() {
        setTitle("Tarung Bogor Ticketing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 780);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND);

        mainPanel.add(createLoginPage(), PAGE_LOGIN);
        mainPanel.add(createBookingPage(), PAGE_BOOKING);
        mainPanel.add(createReviewPage(), PAGE_REVIEW);
        mainPanel.add(createPaymentPage(), PAGE_PAYMENT);
        mainPanel.add(createReceiptPage(), PAGE_RECEIPT);
        mainPanel.add(createAdminPage(), PAGE_ADMIN);

        setContentPane(mainPanel);
    }

    private JPanel createLoginPage() {
        JPanel page = createPagePanel();
        page.setLayout(new GridBagLayout());

        JPanel card = createPanelCard();
        card.setPreferredSize(new Dimension(440, 520));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

       JLabel title = createLogoLabel("src/assets/logo.png", 90, 90);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = createMutedLabel("Tarung Bogor Ticketing");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton guestButton = createPrimaryButton("Continue as Guest");
        guestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guestButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        guestButton.addActionListener(e -> {
            resetBookingForm();
            refreshAllViews();
            cardLayout.show(mainPanel, PAGE_BOOKING);
        });

        JLabel adminTitle = createCenteredSectionText("Admin Login");
        adminUsernameField = createTextField();
        adminPasswordField = new JPasswordField();
        styleTextField(adminPasswordField);

        JButton adminLoginButton = createSecondaryDarkButton("Login as Admin");
        adminLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminLoginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        adminLoginButton.addActionListener(e -> handleAdminLogin());

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(34));
        card.add(guestButton);
        card.add(Box.createVerticalStrut(26));
        card.add(createDivider());
        card.add(Box.createVerticalStrut(22));
        card.add(adminTitle);
        card.add(Box.createVerticalStrut(12));
        card.add(createInputBlock("Username", adminUsernameField));
        card.add(Box.createVerticalStrut(12));
        card.add(createInputBlock("Password", adminPasswordField));
        card.add(Box.createVerticalStrut(18));
        card.add(adminLoginButton);

        page.add(card);
        return page;
    }

    private JPanel createBookingPage() {
        JPanel page = createPagePanel();
        page.setLayout(new BorderLayout(20, 20));
        page.add(createTopBar("Booking Ticket", "Pilih tiket, cek stok, lalu lanjut ke review pesanan.", true), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setOpaque(false);

        JPanel formCard = createPanelCard();
        formCard.setPreferredSize(new Dimension(410, 0));
        formCard.setLayout(new BorderLayout(0, 18));
        formCard.add(createTitleLabel("Form Booking", 22), BorderLayout.NORTH);

        namaField = createTextField();
        noHpField = createTextField();
        emailField = createTextField();
        kategoriComboBox = createComboBox(new String[] { "VIP", "All Day", "Per-Sesi" });
        sesiComboBox = createComboBox(new String[] { "Pilih Sesi", "Sesi 1", "Sesi 2", "Sesi 3", "Sesi 4" });
        jumlahSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        styleSpinner(jumlahSpinner);
        spinnerNormalBorder = ((JSpinner.DefaultEditor) jumlahSpinner.getEditor()).getTextField().getBorder();
        spinnerErrorBorder = BorderFactory.createLineBorder(ERROR, 1);

        ((AbstractDocument) namaField.getDocument()).setDocumentFilter(new NameFilter());
        ((AbstractDocument) noHpField.getDocument()).setDocumentFilter(new NumberFilter());

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = createGbc();
        addFormRow(form, gbc, 0, "Nama Pembeli", namaField);
        addFormRow(form, gbc, 1, "Nomor HP", noHpField);
        addFormRow(form, gbc, 2, "Email", emailField);
        addFormRow(form, gbc, 3, "Kategori Tiket", kategoriComboBox);
        addFormRow(form, gbc, 4, "Sesi", sesiComboBox);
        addFormRow(form, gbc, 5, "Jumlah Tiket", jumlahSpinner);

        bookingMessageLabel = new JLabel("Lengkapi form untuk melanjutkan.");
        bookingMessageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bookingMessageLabel.setForeground(MUTED);

        nextButton = createPrimaryButton("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> handleNextToReview());

        JButton resetButton = createLightButton("Reset Form");
        resetButton.addActionListener(e -> resetBookingForm());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(nextButton);
        buttonPanel.add(resetButton);

        JPanel bottom = new JPanel(new BorderLayout(0, 12));
        bottom.setOpaque(false);
        bottom.add(bookingMessageLabel, BorderLayout.NORTH);
        bottom.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(form, BorderLayout.CENTER);
        formCard.add(bottom, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 16));
        rightPanel.setOpaque(false);
        rightPanel.add(createSelectedTicketCard(), BorderLayout.NORTH);
        rightPanel.add(createUserStockSection(), BorderLayout.CENTER);

        content.add(formCard, BorderLayout.WEST);
        content.add(rightPanel, BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);

        attachBookingListeners();
        updateSesiState();
        updateSelectedTicketInfo();
        updateBookingState();
        return page;
    }

    private JPanel createSelectedTicketCard() {
        JPanel card = createPanelCard();
        card.setLayout(new BorderLayout(0, 14));
        card.add(createTitleLabel("Selected Ticket Info", 20), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 4, 12, 0));
        grid.setOpaque(false);
        selectedCategoryLabel = createInfoBadge("Category", "-");
        selectedSessionLabel = createInfoBadge("Session", "-");
        selectedPriceLabel = createInfoBadge("Price", "-");
        selectedStockLabel = createInfoBadge("Available Stock", "-");
        grid.add(selectedCategoryLabel);
        grid.add(selectedSessionLabel);
        grid.add(selectedPriceLabel);
        grid.add(selectedStockLabel);

        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JPanel createUserStockSection() {
        JPanel wrapper = createPanelCard();
        wrapper.setLayout(new BorderLayout(0, 14));
        wrapper.add(createTitleLabel("Available Stock", 20), BorderLayout.NORTH);

        userStockPanel = new JPanel(new GridLayout(2, 3, 12, 12));
        userStockPanel.setOpaque(false);
        wrapper.add(userStockPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createReviewPage() {
        JPanel page = createPagePanel();
        page.setLayout(new BorderLayout(20, 20));
        page.add(createTopBar("Order Review", "Cek ulang pesanan. Stok belum dikurangi di tahap ini.", false), BorderLayout.NORTH);

        JPanel card = createPanelCard();
        card.setLayout(new BorderLayout(0, 20));
        card.add(createTitleLabel("Review Pesanan", 24), BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(9, 2, 16, 12));
        details.setOpaque(false);
        reviewNamaLabel = addReviewRow(details, "Nama Pembeli");
        reviewNoHpLabel = addReviewRow(details, "Nomor HP");
        reviewEmailLabel = addReviewRow(details, "Email");
        reviewKategoriLabel = addReviewRow(details, "Kategori Tiket");
        reviewSesiLabel = addReviewRow(details, "Sesi");
        reviewHargaLabel = addReviewRow(details, "Harga Tiket");
        reviewJumlahLabel = addReviewRow(details, "Jumlah Tiket");
        reviewTotalLabel = addReviewRow(details, "Total Harga");
        reviewStockLabel = addReviewRow(details, "Available Stock");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        JButton backButton = createLightButton("Back");
        JButton continueButton = createPrimaryButton("Continue to Payment");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, PAGE_BOOKING));
        continueButton.addActionListener(e -> handleContinueToPayment());
        buttons.add(backButton);
        buttons.add(continueButton);

        card.add(details, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        page.add(card, BorderLayout.CENTER);
        return page;
    }

    private JPanel createPaymentPage() {
        JPanel page = createPagePanel();
        page.setLayout(new BorderLayout(20, 20));
        page.add(createTopBar("Payment", "Simulasi pembayaran saja, tanpa payment gateway asli.", false), BorderLayout.NORTH);

        JPanel card = createPanelCard();
        card.setLayout(new BorderLayout(0, 22));
        card.add(createTitleLabel("Payment Page", 24), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = createGbc();

        paymentMethodComboBox = createComboBox(new String[] { "QRIS", "Bank Transfer", "E-Wallet", "Cash on Venue" });
        paymentTotalLabel = createValueLabel("-");
        paymentCodeLabel = createValueLabel("-");
        paymentStatusLabel = createStatusLabel("Waiting Payment", GOLD_DARK);
        paymentMethodComboBox.addActionListener(e -> updatePaymentNote());

        addFormRow(center, gbc, 0, "Payment Method", paymentMethodComboBox);
        addDisplayRow(center, gbc, 1, "Total Payment", paymentTotalLabel);
        addDisplayRow(center, gbc, 2, "Virtual Payment Code / Note", paymentCodeLabel);
        addDisplayRow(center, gbc, 3, "Status", paymentStatusLabel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        JButton backButton = createLightButton("Back");
        JButton confirmButton = createPrimaryButton("Confirm Payment");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, PAGE_REVIEW));
        confirmButton.addActionListener(e -> handleConfirmPayment());
        buttons.add(backButton);
        buttons.add(confirmButton);

        card.add(center, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        page.add(card, BorderLayout.CENTER);
        return page;
    }

    private JPanel createReceiptPage() {
        JPanel page = createPagePanel();
        page.setLayout(new GridBagLayout());

        JPanel card = createPanelCard();
        card.setPreferredSize(new Dimension(680, 640));
        card.setLayout(new BorderLayout(0, 18));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = createTitleLabel("TARUNG BOGOR TICKETING", 24);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel success = createStatusLabel("Payment Successful", SUCCESS);
        success.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel message = createMutedLabel("See you at the ring!!");
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(success);
        header.add(Box.createVerticalStrut(8));
        header.add(message);

        JPanel details = new JPanel(new GridLayout(11, 2, 14, 10));
        details.setOpaque(false);
        receiptIdLabel = addReviewRow(details, "Transaction ID");
        receiptNameLabel = addReviewRow(details, "Customer Name");
        receiptPhoneLabel = addReviewRow(details, "Phone Number");
        receiptEmailLabel = addReviewRow(details, "Email");
        receiptCategoryLabel = addReviewRow(details, "Ticket Category");
        receiptSessionLabel = addReviewRow(details, "Session");
        receiptQuantityLabel = addReviewRow(details, "Quantity");
        receiptPriceLabel = addReviewRow(details, "Ticket Price");
        receiptTotalLabel = addReviewRow(details, "Total Payment");
        receiptMethodLabel = addReviewRow(details, "Payment Method");
        receiptDateLabel = addReviewRow(details, "Transaction Date");

        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        buttons.setOpaque(false);
        JButton newBookingButton = createPrimaryButton("New Booking");
        JButton loginButton = createLightButton("Back to Login");
        newBookingButton.addActionListener(e -> {
            resetBookingForm();
            cardLayout.show(mainPanel, PAGE_BOOKING);
        });
        loginButton.addActionListener(e -> {
            resetBookingForm();
            cardLayout.show(mainPanel, PAGE_LOGIN);
        });
        buttons.add(newBookingButton);
        buttons.add(loginButton);

        card.add(header, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        page.add(card);
        return page;
    }

    private JPanel createAdminPage() {
        JPanel page = createPagePanel();
        page.setLayout(new BorderLayout(20, 20));
        page.add(createTopBar("Admin Page", "Dashboard, riwayat transaksi, dan kelola stok.", true), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("Dashboard", createAdminDashboardTab());
        tabs.addTab("Riwayat Transaksi", createAdminTransactionTab());
        tabs.addTab("Kelola Stok", createAdminStockTab());

        page.add(tabs, BorderLayout.CENTER);
        return page;
    }

    private JPanel createAdminDashboardTab() {
        JPanel tab = createPanelCard();
        tab.setLayout(new BorderLayout(0, 16));
        tab.add(createTitleLabel("Dashboard Admin", 22), BorderLayout.NORTH);

        adminDashboardCardsPanel = new JPanel(new GridLayout(2, 4, 12, 12));
        adminDashboardCardsPanel.setOpaque(false);
        tab.add(adminDashboardCardsPanel, BorderLayout.CENTER);
        return tab;
    }

    private JPanel createAdminTransactionTab() {
        JPanel tab = createPanelCard();
        tab.setLayout(new BorderLayout(0, 16));
        tab.add(createTitleLabel("Riwayat Transaksi", 22), BorderLayout.NORTH);

        String[] columns = {
            "ID Transaksi", "Nama Pembeli", "Nomor HP", "Email", "Kategori Tiket",
            "Sesi", "Jumlah", "Total Harga", "Payment Method", "Tanggal"
        };
        adminTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        adminTable = createStyledTable(adminTableModel);
        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(LINE));
        tab.add(scrollPane, BorderLayout.CENTER);
        return tab;
    }

    private JPanel createAdminStockTab() {
        JPanel tab = createPanelCard();
        tab.setLayout(new BorderLayout(0, 20));
        tab.add(createTitleLabel("Kelola Stok Tiket", 22), BorderLayout.NORTH);

        adminKategoriComboBox = createComboBox(new String[] { "VIP", "All Day", "Per-Sesi" });
        adminSesiComboBox = createComboBox(new String[] { "Pilih Sesi", "Sesi 1", "Sesi 2", "Sesi 3", "Sesi 4" });
        adminJumlahSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        styleSpinner(adminJumlahSpinner);
        adminKategoriComboBox.addActionListener(e -> updateAdminSesiState());

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setPreferredSize(new Dimension(430, 180));
        GridBagConstraints gbc = createGbc();
        addFormRow(form, gbc, 0, "Kategori", adminKategoriComboBox);
        addFormRow(form, gbc, 1, "Sesi", adminSesiComboBox);

        JButton addButton = createSmallPrimaryButton("OK");
        addButton.addActionListener(e -> handleTambahStok());
        addFormRowWithButton(form, gbc, 2, "Tambah Stok", adminJumlahSpinner, addButton);

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);
        body.add(form, BorderLayout.NORTH);

        tab.add(body, BorderLayout.WEST);
        return tab;
    }

    private JPanel createTopBar(String title, String subtitle, boolean showLoginBack) {
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setOpaque(false);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(createTitleLabel(title, 28));
        text.add(Box.createVerticalStrut(4));
        text.add(createMutedLabel(subtitle));

        JButton backButton = createLightButton("Back to Login");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, PAGE_LOGIN));
        backButton.setVisible(showLoginBack);

        topBar.add(text, BorderLayout.WEST);
        topBar.add(backButton, BorderLayout.EAST);
        return topBar;
    }

    private JPanel createPagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(24, 26, 24, 26));
        return panel;
    }

    private JPanel createPanelCard() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(22, 22, 22, 22)));
        return panel;
    }

    private JLabel createTitleLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        label.setForeground(DARK);
        return label;
    }

    private JLabel createMutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(MUTED);
        return label;
    }

    private JLabel createSectionText(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(CHARCOAL);
        return label;
    }

    private JLabel createCenteredSectionText(String text) {
        JLabel label = createSectionText(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        return label;
    }

    private Component createDivider() {
        JPanel divider = new JPanel();
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(1, 1));
        divider.setBackground(LINE);
        return divider;
    }

    private JPanel createInputBlock(String labelText, Component component) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(DARK);
        block.add(label, BorderLayout.NORTH);
        block.add(component, BorderLayout.CENTER);
        block.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        return block;
    }

    private JLabel createInfoBadge(String title, String value) {
        JLabel label = new JLabel("<html><span style='font-size:10px;color:#444444;'>" + title
                + "</span><br><b style='font-size:14px;color:#181818;'>" + value + "</b></html>");
        label.setOpaque(true);
        label.setBackground(SOFT_GOLD);
        label.setBorder(new EmptyBorder(12, 12, 12, 12));
        return label;
    }

    private JPanel createStockCard(String title, String subtitle, int stok) {
        return createMetricCard(title, subtitle, String.valueOf(stok));
    }

    private JPanel createMetricCard(String title, String subtitle, String value) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(DARK);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(MUTED);

        JLabel stockLabel = new JLabel(value);
        stockLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        stockLabel.setForeground(GOLD_DARK);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitleLabel);

        card.add(textPanel, BorderLayout.NORTH);
        card.add(stockLabel, BorderLayout.SOUTH);
        return card;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        applySolidButtonStyle(button, GOLD, DARK);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBorder(new EmptyBorder(11, 16, 11, 16));
        return button;
    }

    private JButton createSecondaryDarkButton(String text) {
        JButton button = new JButton(text);
        applySolidButtonStyle(button, DARK, Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBorder(new EmptyBorder(11, 16, 11, 16));
        return button;
    }

    private JButton createLightButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(237, 235, 230));
        button.setForeground(DARK);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBorder(new EmptyBorder(11, 16, 11, 16));
        return button;
    }

    private void applySolidButtonStyle(JButton button, Color background, Color foreground) {
        button.setUI(new BasicButtonUI());
        button.setBackground(background);
        button.setForeground(foreground);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
    }

    private JButton createSmallPrimaryButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setPreferredSize(new Dimension(58, 36));
        button.setMinimumSize(new Dimension(58, 36));
        button.setMaximumSize(new Dimension(58, 36));
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(8, 12, 8, 12));
        return button;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(DARK);
        field.setCaretColor(DARK);
        field.setBackground(SURFACE);
        field.setPreferredSize(new Dimension(0, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(8, 10, 8, 10)));
    }

    private JComboBox<String> createComboBox(String[] values) {
        JComboBox<String> comboBox = new JComboBox<>(values);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboBox.setForeground(DARK);
        comboBox.setPreferredSize(new Dimension(0, 36));
        comboBox.setBackground(SURFACE);
        return comboBox;
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("SansSerif", Font.PLAIN, 13));
        spinner.setPreferredSize(new Dimension(0, 36));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setForeground(DARK);
        editor.getTextField().setBackground(SURFACE);
        editor.getTextField().setHorizontalAlignment(JTextField.LEFT);
        editor.getTextField().setBorder(new EmptyBorder(5, 8, 5, 8));
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        return gbc;
    }

    private void addFormRow(JPanel form, GridBagConstraints gbc, int row, String labelText, Component component) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(DARK);

        gbc.gridx = 0;
        gbc.gridy = row * 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.weightx = 1;
        form.add(label, gbc);

        gbc.gridx = 0;
        gbc.gridy = row * 2 + 1;
        gbc.insets = new Insets(0, 0, 13, 0);
        form.add(component, gbc);
    }

    private void addFormRowWithButton(JPanel form, GridBagConstraints gbc, int row, String labelText,
            Component component, JButton button) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(DARK);

        gbc.gridx = 0;
        gbc.gridy = row * 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.weightx = 1;
        form.add(label, gbc);

        gbc.gridy = row * 2 + 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 13, 8);
        gbc.weightx = 1;
        form.add(component, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 13, 0);
        gbc.weightx = 0;
        form.add(button, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 1;
    }

    private void addDisplayRow(JPanel form, GridBagConstraints gbc, int row, String labelText, Component component) {
        addFormRow(form, gbc, row, labelText, component);
    }

    private JLabel addReviewRow(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(PLACEHOLDER);

        JLabel value = createValueLabel("-");
        panel.add(label);
        panel.add(value);
        return value;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(DARK);
        return label;
    }

    private JLabel createStatusLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setBorder(new EmptyBorder(8, 14, 8, 14));
        return label;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(DARK);
        table.setGridColor(new Color(232, 229, 224));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(SOFT_GOLD);
        table.setSelectionForeground(DARK);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(DARK);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(DARK);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("SansSerif", Font.BOLD, 12));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(new EmptyBorder(0, 8, 0, 8));
                return label;
            }
        });

        DefaultTableCellRenderer readableRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBorder(new EmptyBorder(0, 8, 0, 8));
                label.setForeground(DARK);
                label.setBackground(isSelected ? SOFT_GOLD : SURFACE);
                return label;
            }
        };

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        centerRenderer.setForeground(DARK);
        centerRenderer.setBackground(SURFACE);
        table.setDefaultRenderer(Object.class, readableRenderer);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        setColumnWidth(table, 0, 95);
        setColumnWidth(table, 1, 150);
        setColumnWidth(table, 2, 115);
        setColumnWidth(table, 3, 190);
        setColumnWidth(table, 4, 120);
        setColumnWidth(table, 5, 95);
        setColumnWidth(table, 6, 70);
        setColumnWidth(table, 7, 130);
        setColumnWidth(table, 8, 130);
        setColumnWidth(table, 9, 160);
        return table;
    }

    private void setColumnWidth(JTable table, int columnIndex, int width) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
        column.setMinWidth(Math.max(55, width - 45));
    }

    private void attachBookingListeners() {
        DocumentListener documentListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateBookingState();
            }

            public void removeUpdate(DocumentEvent e) {
                updateBookingState();
            }

            public void changedUpdate(DocumentEvent e) {
                updateBookingState();
            }
        };

        namaField.getDocument().addDocumentListener(documentListener);
        noHpField.getDocument().addDocumentListener(documentListener);
        emailField.getDocument().addDocumentListener(documentListener);
        kategoriComboBox.addActionListener(e -> {
            updateSesiState();
            updateSelectedTicketInfo();
            updateBookingState();
        });
        sesiComboBox.addActionListener(e -> {
            updateSelectedTicketInfo();
            updateBookingState();
        });
        jumlahSpinner.addChangeListener(e -> updateBookingState());
    }

    private void updateSesiState() {
        boolean perSesi = "Per-Sesi".equals(kategoriComboBox.getSelectedItem());
        sesiComboBox.setEnabled(perSesi);
        if (!perSesi) {
            sesiComboBox.setSelectedIndex(0);
        }
    }

    private void updateAdminSesiState() {
        boolean perSesi = "Per-Sesi".equals(adminKategoriComboBox.getSelectedItem());
        adminSesiComboBox.setEnabled(perSesi);
        if (!perSesi) {
            adminSesiComboBox.setSelectedIndex(0);
        }
    }

    private void updateSelectedTicketInfo() {
        Tiket tiket = getSelectedTiket();
        SesiEvent sesi = getSelectedSesi(tiket);
        int stok = bookingService.getStokTersedia(tiket, sesi);
        String sesiText = getDisplaySesi(tiket, sesi);

        selectedCategoryLabel.setText(formatInfoBadge("Category", tiket == null ? "-" : tiket.getKategori()));
        selectedSessionLabel.setText(formatInfoBadge("Session", sesiText));
        selectedPriceLabel.setText(formatInfoBadge("Price", tiket == null ? "-" : formatRupiah.format(tiket.getHarga())));
        selectedStockLabel.setText(formatInfoBadge("Available Stock", String.valueOf(stok)));
    }

    private String formatInfoBadge(String title, String value) {
        return "<html><span style='font-size:10px;color:#686868;'>" + title
                + "</span><br><b style='font-size:14px;color:#181818;'>" + value + "</b></html>";
    }

    private void updateBookingState() {
        if (nextButton == null) {
            return;
        }

        updateSelectedTicketInfo();
        String errorMessage = getBookingValidationMessage();
        boolean valid = errorMessage == null;
        nextButton.setEnabled(valid);

        JTextField quantityField = ((JSpinner.DefaultEditor) jumlahSpinner.getEditor()).getTextField();
        if ("Jumlah tiket melebihi stok tersedia.".equals(errorMessage)) {
            quantityField.setForeground(ERROR);
            quantityField.setBorder(spinnerErrorBorder);
        } else {
            quantityField.setForeground(DARK);
            quantityField.setBorder(spinnerNormalBorder);
        }

        if (valid) {
            bookingMessageLabel.setText("Form valid. Lanjutkan ke review pesanan.");
            bookingMessageLabel.setForeground(SUCCESS);
        } else {
            bookingMessageLabel.setText(errorMessage);
            bookingMessageLabel.setForeground(errorMessage.contains("melebihi") ? ERROR : MUTED);
        }
    }

    private String getBookingValidationMessage() {
        String nama = namaField.getText().trim();
        String noHp = noHpField.getText().trim();
        String email = emailField.getText().trim();
        int jumlah = (Integer) jumlahSpinner.getValue();
        Tiket tiket = getSelectedTiket();
        SesiEvent sesi = getSelectedSesi(tiket);

        if (nama.isEmpty()) {
            return "Nama pembeli wajib diisi.";
        }
        if (!nama.matches("[A-Z ]+")) {
            return "Nama hanya boleh berisi huruf dan spasi.";
        }
        if (noHp.isEmpty()) {
            return "Nomor HP wajib diisi.";
        }
        if (!noHp.matches("[0-9]+")) {
            return "Nomor HP hanya boleh berisi angka.";
        }
        if (email.isEmpty()) {
            return "Email wajib diisi.";
        }
        if (!email.endsWith("@gmail.com")) {
            return "Email harus berakhiran @gmail.com.";
        }
        if (tiket != null && tiket.isMembutuhkanSesi() && sesi == null) {
            return "Pilih sesi terlebih dahulu.";
        }
        if (jumlah <= 0) {
            return "Jumlah tiket harus lebih dari 0.";
        }
        if (!bookingService.cekKetersediaanStok(tiket, sesi, jumlah)) {
            return "Jumlah tiket melebihi stok tersedia.";
        }
        return null;
    }

    private void handleNextToReview() {
        String errorMessage = getBookingValidationMessage();
        if (errorMessage != null) {
            showPopup("Validasi Gagal", errorMessage, true);
            if (errorMessage.contains("@gmail.com")) {
                emailField.requestFocusInWindow();
            }
            updateBookingState();
            return;
        }

        pendingTiket = getSelectedTiket();
        pendingSesi = getSelectedSesi(pendingTiket);
        pendingJumlah = (Integer) jumlahSpinner.getValue();
        pendingCustomer = new Customer("C" + System.currentTimeMillis(),
                namaField.getText().trim().toUpperCase(),
                emailField.getText().trim(),
                noHpField.getText().trim());

        showReviewData();
        cardLayout.show(mainPanel, PAGE_REVIEW);
    }

    private void showReviewData() {
        int stok = bookingService.getStokTersedia(pendingTiket, pendingSesi);
        reviewNamaLabel.setText(pendingCustomer.getNama());
        reviewNoHpLabel.setText(pendingCustomer.getNoHp());
        reviewEmailLabel.setText(pendingCustomer.getEmail());
        reviewKategoriLabel.setText(pendingTiket.getKategori());
        reviewSesiLabel.setText(getDisplaySesi(pendingTiket, pendingSesi));
        reviewHargaLabel.setText(formatRupiah.format(pendingTiket.getHarga()));
        reviewJumlahLabel.setText(String.valueOf(pendingJumlah));
        reviewTotalLabel.setText(formatRupiah.format(pendingTiket.hitungSubtotal(pendingJumlah)));
        reviewStockLabel.setText(String.valueOf(stok));
    }

    private void handleContinueToPayment() {
        if (!bookingService.cekKetersediaanStok(pendingTiket, pendingSesi, pendingJumlah)) {
            showPopup("Stok Tidak Cukup", "Stok sudah berubah. Silakan pilih jumlah tiket lagi.", true);
            refreshAllViews();
            cardLayout.show(mainPanel, PAGE_BOOKING);
            return;
        }

        updatePaymentPage();
        cardLayout.show(mainPanel, PAGE_PAYMENT);
    }

    private void updatePaymentPage() {
        paymentTotalLabel.setText(formatRupiah.format(pendingTiket.hitungSubtotal(pendingJumlah)));
        paymentStatusLabel.setText("Waiting Payment");
        updatePaymentNote();
    }

    private void updatePaymentNote() {
        if (paymentMethodComboBox == null || pendingTiket == null) {
            return;
        }

        String method = (String) paymentMethodComboBox.getSelectedItem();
        String code;
        if ("QRIS".equals(method)) {
            code = "QRIS-TB-" + bookingService.generateIdTransaksi();
        } else if ("Bank Transfer".equals(method)) {
            code = "VA-TB-" + bookingService.generateIdTransaksi() + "-8808";
        } else if ("E-Wallet".equals(method)) {
            code = "EWALLET-TB-" + bookingService.generateIdTransaksi();
        } else {
            code = "Bayar tunai di venue sebelum masuk area ring.";
        }
        paymentCodeLabel.setText(code);
    }

    private void handleConfirmPayment() {
        if (!bookingService.cekKetersediaanStok(pendingTiket, pendingSesi, pendingJumlah)) {
            showPopup("Payment Gagal", "Stok tiket tidak cukup. Transaksi tidak dibuat.", true);
            refreshAllViews();
            cardLayout.show(mainPanel, PAGE_BOOKING);
            return;
        }

        try {
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
            currentTransaction = bookingService.prosesBooking(
                    pendingCustomer, pendingTiket, pendingSesi, pendingJumlah, paymentMethod);

            refreshAllViews();
            showReceiptData();
            showPopup("Payment Successful", "THANK YOU. SEE YOU AT THE RING!!", false);
            cardLayout.show(mainPanel, PAGE_RECEIPT);
        } catch (IllegalArgumentException ex) {
            showPopup("Payment Gagal", ex.getMessage(), true);
        }
    }

    private void showReceiptData() {
        if (currentTransaction == null) {
            return;
        }

        receiptIdLabel.setText(currentTransaction.getIdTransaksi());
        receiptNameLabel.setText(currentTransaction.getCustomer().getNama());
        receiptPhoneLabel.setText(currentTransaction.getCustomer().getNoHp());
        receiptEmailLabel.setText(currentTransaction.getCustomer().getEmail());
        receiptCategoryLabel.setText(currentTransaction.getTiket().getKategori());
        receiptSessionLabel.setText(currentTransaction.getNamaSesi());
        receiptQuantityLabel.setText(String.valueOf(currentTransaction.getJumlah()));
        receiptPriceLabel.setText(formatRupiah.format(currentTransaction.getTiket().getHarga()));
        receiptTotalLabel.setText(formatRupiah.format(currentTransaction.getTotalHarga()));
        receiptMethodLabel.setText(currentTransaction.getPaymentMethod());
        receiptDateLabel.setText(currentTransaction.getTanggalTransaksi().format(dateFormatter));
    }

    private void handleAdminLogin() {
        String username = adminUsernameField.getText().trim();
        String password = new String(adminPasswordField.getPassword());

        if ("admin".equals(username) && "admin".equals(password)) {
            Admin admin = new Admin("A001", "Admin Ticketing", "admin@tarungbogor.com", username);
            showPopup("Login Admin Berhasil", admin.tampilkanInfo(), false);
            refreshAllViews();
            cardLayout.show(mainPanel, PAGE_ADMIN);
        } else {
            showPopup("Login Admin Gagal", "Username atau password admin salah.", true);
        }
    }

    private void handleTambahStok() {
        try {
            String kategori = (String) adminKategoriComboBox.getSelectedItem();
            String namaSesi = (String) adminSesiComboBox.getSelectedItem();
            int jumlah = (Integer) adminJumlahSpinner.getValue();

            if (jumlah <= 0) {
                throw new IllegalArgumentException("Tambah stok harus angka positif.");
            }

            Tiket tiket = bookingService.getTiketByKategori(kategori);
            if (tiket != null && tiket.isMembutuhkanSesi()) {
                if (namaSesi == null || "Pilih Sesi".equals(namaSesi)) {
                    throw new IllegalArgumentException("Admin wajib memilih sesi untuk tiket Per-Sesi.");
                }
                SesiEvent sesi = bookingService.getSesiByNama(tiket, namaSesi);
                bookingService.tambahStokSesi(tiket, sesi, jumlah);
            } else {
                bookingService.tambahStokTiket(tiket, jumlah);
            }

            adminJumlahSpinner.setValue(1);
            refreshAllViews();
            showPopup("Stok Berhasil Ditambah", "Stok tiket sudah diperbarui.", false);
        } catch (IllegalArgumentException ex) {
            showPopup("Tambah Stok Gagal", ex.getMessage(), true);
        }
    }

    private Tiket getSelectedTiket() {
        return bookingService.getTiketByKategori((String) kategoriComboBox.getSelectedItem());
    }

    private SesiEvent getSelectedSesi(Tiket tiket) {
        if (tiket == null || !tiket.isMembutuhkanSesi()) {
            return null;
        }
        String namaSesi = (String) sesiComboBox.getSelectedItem();
        if (namaSesi == null || "Pilih Sesi".equals(namaSesi)) {
            return null;
        }
        return bookingService.getSesiByNama(tiket, namaSesi);
    }

    private String getDisplaySesi(Tiket tiket, SesiEvent sesi) {
        if (tiket == null) {
            return "-";
        }
        if (tiket.isMembutuhkanSesi()) {
            return sesi == null ? "Pilih Sesi" : sesi.getNamaSesi();
        }
        return tiket.getKategori().equalsIgnoreCase("All Day") ? "Semua Sesi" : "-";
    }

    private void resetBookingForm() {
        if (namaField == null) {
            return;
        }
        namaField.setText("");
        noHpField.setText("");
        emailField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        sesiComboBox.setSelectedIndex(0);
        jumlahSpinner.setValue(1);
        pendingCustomer = null;
        pendingTiket = null;
        pendingSesi = null;
        pendingJumlah = 0;
        updateSesiState();
        updateSelectedTicketInfo();
        updateBookingState();
    }

    private void refreshAllViews() {
        refreshUserStockCards();
        refreshAdminDashboard();
        refreshAdminTransactionTable();
        if (kategoriComboBox != null) {
            updateSelectedTicketInfo();
            updateBookingState();
        }
        if (adminKategoriComboBox != null) {
            updateAdminSesiState();
        }
    }

    private void refreshUserStockCards() {
        if (userStockPanel == null) {
            return;
        }
        userStockPanel.removeAll();
        addStockCardsToPanel(userStockPanel);
        userStockPanel.revalidate();
        userStockPanel.repaint();
    }

    private void refreshAdminDashboard() {
        if (adminDashboardCardsPanel == null) {
            return;
        }
        adminDashboardCardsPanel.removeAll();
        adminDashboardCardsPanel.add(createStockCard("Total Transaksi", "Transaksi berhasil",
                bookingService.getDaftarTransaksi().size()));
        adminDashboardCardsPanel.add(createMetricCard("Total Pendapatan", "Revenue",
                formatRupiah.format(bookingService.hitungTotalPendapatan())));

        Tiket vip = bookingService.getTiketByKategori("VIP");
        Tiket allDay = bookingService.getTiketByKategori("All Day");
        Tiket perSesi = bookingService.getTiketByKategori("Per-Sesi");

        adminDashboardCardsPanel.add(createStockCard("VIP Stock", formatRupiah.format(vip.getHarga()), vip.getStok()));
        adminDashboardCardsPanel.add(createStockCard("All Day Stock", formatRupiah.format(allDay.getHarga()), allDay.getStok()));
        for (SesiEvent sesi : perSesi.getDaftarSesi()) {
            adminDashboardCardsPanel.add(createStockCard("Per-Sesi " + sesi.getNamaSesi(),
                    formatRupiah.format(perSesi.getHarga()), sesi.getStok()));
        }

        adminDashboardCardsPanel.revalidate();
        adminDashboardCardsPanel.repaint();
    }

    private void addStockCardsToPanel(JPanel panel) {
        Tiket vip = bookingService.getTiketByKategori("VIP");
        Tiket allDay = bookingService.getTiketByKategori("All Day");
        Tiket perSesi = bookingService.getTiketByKategori("Per-Sesi");

        panel.add(createStockCard("VIP", formatRupiah.format(vip.getHarga()), vip.getStok()));
        panel.add(createStockCard("All Day", formatRupiah.format(allDay.getHarga()), allDay.getStok()));
        for (SesiEvent sesi : perSesi.getDaftarSesi()) {
            panel.add(createStockCard("Per-Sesi " + sesi.getNamaSesi(), formatRupiah.format(perSesi.getHarga()), sesi.getStok()));
        }
    }

    private void refreshAdminTransactionTable() {
        if (adminTableModel == null) {
            return;
        }
        adminTableModel.setRowCount(0);
        for (Transaksi transaksi : bookingService.getDaftarTransaksi()) {
            adminTableModel.addRow(new Object[] {
                transaksi.getIdTransaksi(),
                transaksi.getCustomer().getNama(),
                transaksi.getCustomer().getNoHp(),
                transaksi.getCustomer().getEmail(),
                transaksi.getTiket().getKategori(),
                transaksi.getNamaSesi(),
                transaksi.getJumlah(),
                formatRupiah.format(transaksi.getTotalHarga()),
                transaksi.getPaymentMethod(),
                transaksi.getTanggalTransaksi().format(dateFormatter)
            });
        }
    }

    private void showPopup(String title, String message, boolean error) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(new EmptyBorder(10, 8, 6, 8));
        panel.setBackground(SURFACE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(error ? ERROR : SUCCESS);

        JLabel messageLabel = new JLabel("<html><body style='width:320px;'>" + message + "</body></html>");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageLabel.setForeground(DARK);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(this, panel, title, error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private class NameFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            String upper = text.toUpperCase();
            if (upper.matches("[A-Z ]*")) {
                super.replace(fb, offset, length, upper, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    private class NumberFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            if (text.matches("[0-9]*")) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    private JLabel createLogoLabel(String imagePath, int width, int height) {
    File logoFile = new File(imagePath);

    if (!logoFile.exists()) {
        System.out.println("Logo tidak ditemukan di path: " + logoFile.getAbsolutePath());
        return createTitleLabel("LOGO NOT FOUND", 24);
    }

    ImageIcon originalIcon = new ImageIcon(logoFile.getAbsolutePath());

    Image scaledImage = originalIcon.getImage().getScaledInstance(
            width,
            height,
            Image.SCALE_SMOOTH
    );

    JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
    logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    return logoLabel;
}
}
