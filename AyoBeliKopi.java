import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AyoBeliKopi extends JFrame {

    static class Product {
        String name;
        int price;
        String desc;
        String imgUrl;
        int stock;

        Product(String name, int price, String desc, String imgUrl, int stock) {
            this.name = name;
            this.price = price;
            this.desc = desc;
            this.imgUrl = imgUrl;
            this.stock = stock;
        }

        public String toString() { return name; }
    }

    static class CartItem {
        Product product;
        int qty;

        CartItem(Product p, int q) { product = p; qty = q; }
        int subtotal() { return product.price * qty; }
    }

    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<CartItem> cart = new ArrayList<>();

    private final DefaultListModel<String> cartListModel = new DefaultListModel<>();
    private final JList<String> cartList = new JList<>(cartListModel);
    private final JLabel totalLabel = new JLabel();
    private final DecimalFormat rupiahFormat = new DecimalFormat("Rp#,###");

    // Variabel animasi gradasi
    private float hue = 0f;

    public AyoBeliKopi() {
        setTitle("Ayo Beli Kopi ☕ - Modern Coffee GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        products.add(new Product("Kopi Godday", 5000, "Kopi spesial pagi hari. Aroma bold, body pas.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTUOT57a29VD1SrBYb46KUWkYuR-dcR_cD2Ag&s", 10));
        products.add(new Product("Cappuccino", 7000, "Busa susu lembut di atas espresso singkat.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBlzdqzRUAjjeqh0LtfGPI-iAPZzDCNlMS0A", 8));
        products.add(new Product("Kopi Hitam", 4000, "Murni, panas, klasik.",
                "https://pyfahealth.com/wp-content/uploads/2024/11/manfaat-kopi-hitam-tanpa-gula-1200x900.jpg", 12));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        updateTotal();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
    }

    private JPanel createHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                Color c1 = Color.getHSBColor(hue, 0.6f, 1f);
                Color c2 = Color.getHSBColor((hue + 0.2f) % 1.0f, 0.6f, 1f);
                GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            }
        };
        header.setPreferredSize(new Dimension(10, 110));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("Ayo Beli Kopi ☕");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(16, 20, 10, 10));

        JLabel subtitle = new JLabel("Menu spesial: Harga murah, rasa nendang — buat semangat kuliah!");
        subtitle.setForeground(new Color(255, 245, 240));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setBorder(new EmptyBorder(0, 20, 16, 10));

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(title, BorderLayout.NORTH);
        left.add(subtitle, BorderLayout.SOUTH);

        header.add(left, BorderLayout.WEST);

        // Animasi warna
        Timer timer = new Timer(60, e -> {
            hue += 0.002f;
            if (hue > 1f) hue = 0f;
            header.repaint();
        });
        timer.start();

        return header;
    }

    private JPanel createMainPanel() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new GridLayout(0, 1, 12, 12));
        productsPanel.setOpaque(false);

        for (Product p : products) {
            productsPanel.add(createProductCard(p));
        }

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.7;
        c.weighty = 1.0;
        main.add(productsPanel, c);

        JPanel right = createCartPanel();
        c.gridx = 1;
        c.weightx = 0.3;
        main.add(right, c);

        return main;
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 140));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setBackground(Color.WHITE);

        JLabel imgLabel;
        try {
            ImageIcon imgIcon = new ImageIcon(new URL(p.imgUrl));
            Image scaled = imgIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            imgLabel = new JLabel("[No Image]");
        }
        imgLabel.setPreferredSize(new Dimension(110, 110));

        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);

        JLabel name = new JLabel(p.name);
        name.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel desc = new JLabel("<html><i>" + p.desc + "</i></html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(Color.DARK_GRAY);

        JLabel price = new JLabel(rupiahFormat.format(p.price));
        price.setFont(new Font("SansSerif", Font.BOLD, 14));
        price.setForeground(new Color(40, 140, 90));

        JLabel stockLabel = new JLabel("Stok: " + p.stock);
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        stockLabel.setForeground(Color.GRAY);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(name, BorderLayout.WEST);
        top.add(price, BorderLayout.EAST);

        info.add(top, BorderLayout.NORTH);
        info.add(desc, BorderLayout.CENTER);
        info.add(stockLabel, BorderLayout.SOUTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton addBtn = new JButton("+ Keranjang");
        addBtn.setFocusPainted(false);
        if (p.stock <= 0) addBtn.setEnabled(false);
        addBtn.addActionListener(e -> {
            if (p.stock > 0) {
                addToCart(p);
                p.stock--;
                stockLabel.setText("Stok: " + p.stock);
                if (p.stock <= 0) addBtn.setEnabled(false);
            }
        });
        actions.add(addBtn);

        card.add(imgLabel, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 8, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        return card;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel title = new JLabel("Keranjang");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(6, 6, 6, 6));

        panel.add(title, BorderLayout.NORTH);

        cartList.setVisibleRowCount(8);
        JScrollPane sp = new JScrollPane(cartList);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;

        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottom.add(totalLabel, c);

        JButton removeBtn = new JButton("Hapus Item");
        removeBtn.addActionListener(e -> removeSelectedItem());
        c.gridy = 1;
        bottom.add(removeBtn, c);

        JButton clearBtn = new JButton("Bersihkan");
        clearBtn.addActionListener(e -> { cart.clear(); cartListModel.clear(); updateTotal(); });
        c.gridy = 2;
        bottom.add(clearBtn, c);

        JButton checkoutBtn = new JButton("Checkout →");
        checkoutBtn.setBackground(new Color(60, 179, 113));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.addActionListener(e -> openCheckoutDialog());
        c.gridy = 3;
        bottom.add(checkoutBtn, c);

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setPreferredSize(new Dimension(10, 36));
        footer.setBackground(new Color(245, 247, 250));

        JLabel hint = new JLabel("Tip: Klik '+ Keranjang' untuk menambahkan, lalu Checkout. Bayar pakai Cash atau QRIS.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        footer.add(hint);

        return footer;
    }

    private void addToCart(Product p) {
        for (CartItem it : cart) {
            if (it.product.name.equals(p.name)) { it.qty++; refreshCartList(); updateTotal(); return; }
        }
        cart.add(new CartItem(p, 1));
        refreshCartList();
        updateTotal();
        JOptionPane.showMessageDialog(this, p.name + " ditambahkan ke keranjang!", "OK", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeSelectedItem() {
        int idx = cartList.getSelectedIndex();
        if (idx >= 0 && idx < cart.size()) {
            CartItem removed = cart.remove(idx);
            removed.product.stock += removed.qty;
            refreshCartList();
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih item dulu ya!", "Info", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshCartList() {
        cartListModel.clear();
        for (CartItem it : cart) {
            cartListModel.addElement(it.product.name + "  x" + it.qty + "    " + rupiahFormat.format(it.subtotal()));
        }
    }

    private void updateTotal() {
        int total = 0;
        for (CartItem it : cart) total += it.subtotal();
        totalLabel.setText("Total: " + rupiahFormat.format(total));
    }

    private void openCheckoutDialog() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong, tambahkan dulu ya 😄", "Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Checkout berhasil (simulasi)! Terima kasih sudah beli kopi ☕", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        cart.clear();
        cartListModel.clear();
        updateTotal();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AyoBeliKopi app = new AyoBeliKopi();
            app.setVisible(true);
        });
    }
}
