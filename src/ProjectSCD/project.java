package ProjectSCD;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;

public class project {

    private JFrame frmProject;
    private JTextField amountField, sourceField;
    private DefaultListModel<Income> incomeListModel;
    private JList<Income> incomeList;
    private ArrayList<Income> incomeData = new ArrayList<>();

    private JTextField expenseAmountField, expenseDateField;
    private JComboBox<String> categoryBox;
    private DefaultListModel<Expense> expenseListModel;
    private JList<Expense> expenseList;
    private ArrayList<Expense> expenseData = new ArrayList<>();
    private HashMap<String, Double> categoryLimits = new HashMap<>();

    private double totalIncome = 0;
    private double totalExpense = 0;

    private final Color bgColor = new Color(245, 245, 255);
    private final Color panelColor = new Color(255, 255, 255);
    private final Color accent = new Color(100, 149, 237);
    private final Color textColor = new Color(44, 62, 80);

    class Income {
        double amount;
        String source;

        Income(double amount, String source) {
            this.amount = amount;
            this.source = source;
        }

        public String toString() {
            return "Amount: $" + amount + ", Source: " + source;
        }
    }

    class Expense {
        double amount;
        String date, category;

        Expense(double amount, String date, String category) {
            this.amount = amount;
            this.date = date;
            this.category = category;
        }

        public String toString() {
            return "Amount: $" + amount + ", Date: " + date + ", Category: " + category;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new project().frmProject.setVisible(true));
    }

    public project() {
        initialize();
    }

    private void initialize() {
        frmProject = new JFrame("\uD83D\uDCCA Budget Tracking App 2025");
        frmProject.getContentPane().setBackground(bgColor);
        frmProject.setBounds(100, 100, 950, 650);
        frmProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmProject.getContentPane().setLayout(null);

        JLabel heading = new JLabel("\uD83D\uDCCA Budget Tracker 2025");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(accent);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBounds(0, 10, 950, 40);
        frmProject.getContentPane().add(heading);

        JPanel incomePanel = new JPanel(null);
        incomePanel.setBackground(panelColor);
        incomePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accent, 2), "\uD83D\uDCB0 Income", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), accent));
        incomePanel.setBounds(20, 70, 400, 250);
        frmProject.getContentPane().add(incomePanel);

        amountField = new JTextField();
        amountField.setBounds(20, 30, 80, 25);
        incomePanel.add(amountField);

        sourceField = new JTextField();
        sourceField.setBounds(120, 30, 100, 25);
        incomePanel.add(sourceField);

        JButton btnAddIncome = new JButton("Add Income");
        styleButton(btnAddIncome);
        btnAddIncome.setBounds(240, 30, 130, 25);
        incomePanel.add(btnAddIncome);

        incomeListModel = new DefaultListModel<>();
        incomeList = new JList<>(incomeListModel);
        JScrollPane incomeScroll = new JScrollPane(incomeList);
        incomeScroll.setBounds(20, 70, 350, 150);
        incomePanel.add(incomeScroll);

        JPanel expensePanel = new JPanel(null);
        expensePanel.setBackground(panelColor);
        expensePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accent, 2), "\uD83D\uDCB8 Expenses", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), accent));
        expensePanel.setBounds(450, 70, 470, 250);
        frmProject.getContentPane().add(expensePanel);

        expenseAmountField = new JTextField();
        expenseAmountField.setBounds(20, 30, 80, 25);
        expensePanel.add(expenseAmountField);

        expenseDateField = new JTextField("YYYY-MM-DD");
        expenseDateField.setBounds(110, 30, 100, 25);
        expensePanel.add(expenseDateField);

        String[] categories = {"Food", "Travel", "Utilities", "Health", "Entertainment", "Other"};
        categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(220, 30, 100, 25);
        expensePanel.add(categoryBox);

        JButton btnAddExpense = new JButton("Add Expense");
        styleButton(btnAddExpense);
        btnAddExpense.setBounds(330, 30, 120, 25);
        expensePanel.add(btnAddExpense);

        expenseListModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseListModel);
        JScrollPane expenseScroll = new JScrollPane(expenseList);
        expenseScroll.setBounds(20, 70, 430, 150);
        expensePanel.add(expenseScroll);

        JPanel controlPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        controlPanel.setBackground(bgColor);
        controlPanel.setBounds(50, 350, 850, 150);
        frmProject.getContentPane().add(controlPanel);

        String[] btnNames = {"View Balance", "Set Category Limit", "Category Report", "Edit/Delete",
                "Sort by Amount", "Sort by Date", "Session Summary", "Reset Data"};

        JButton[] controlButtons = new JButton[btnNames.length];
        for (int i = 0; i < btnNames.length; i++) {
            controlButtons[i] = new JButton(btnNames[i]);
            styleButton(controlButtons[i]);
            controlPanel.add(controlButtons[i]);
        }

        btnAddIncome.addActionListener(e -> {
            try {
                if (amountField.getText().isEmpty() || sourceField.getText().isEmpty()) {
                    show("Please fill in both fields before adding income.");
                    return;
                }
                double amt = Double.parseDouble(amountField.getText());
                String src = sourceField.getText();
                Income income = new Income(amt, src);
                incomeData.add(income);
                incomeListModel.addElement(income);
                totalIncome += amt;
                amountField.setText("");
                sourceField.setText("");
                show("Income added successfully!");
            } catch (Exception ex) {
                show("Invalid input. Please enter valid income data.");
            }
        });

        btnAddExpense.addActionListener(e -> {
            try {
                if (expenseAmountField.getText().isEmpty() || expenseDateField.getText().isEmpty()) {
                    show("Please complete all expense fields.");
                    return;
                }
                double amt = Double.parseDouble(expenseAmountField.getText());
                String date = expenseDateField.getText();
                if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date)) {
                    show("Invalid date format. Use YYYY-MM-DD.");
                    return;
                }
                String cat = (String) categoryBox.getSelectedItem();
                double currentTotal = expenseData.stream().filter(e2 -> e2.category.equals(cat)).mapToDouble(e2 -> e2.amount).sum();
                if (categoryLimits.containsKey(cat) && currentTotal + amt > categoryLimits.get(cat)) {
                    show("Warning: This expense exceeds the set limit for category " + cat);
                    return;
                }
                Expense exp = new Expense(amt, date, cat);
                expenseData.add(exp);
                expenseListModel.addElement(exp);
                totalExpense += amt;
                expenseAmountField.setText("");
                expenseDateField.setText("YYYY-MM-DD");
                show("Expense added successfully!");
            } catch (Exception ex) {
                show("Invalid input. Please enter valid expense data.");
            }
        });

        controlButtons[3].addActionListener(e -> {
            if (!incomeList.isSelectionEmpty()) {
                Income selected = incomeList.getSelectedValue();
                String[] options = {"Edit", "Delete"};
                int choice = JOptionPane.showOptionDialog(frmProject, "Choose action for selected Income", "Edit/Delete",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    try {
                        String newAmount = JOptionPane.showInputDialog("New Amount:", selected.amount);
                        String newSource = JOptionPane.showInputDialog("New Source:", selected.source);
                        double amt = Double.parseDouble(newAmount);
                        totalIncome -= selected.amount;
                        selected.amount = amt;
                        selected.source = newSource;
                        totalIncome += amt;
                        incomeList.repaint();
                        incomeList.setSelectedValue(null, false);
                        show("Income edited successfully.");
                    } catch (Exception ex) {
                        show("Invalid input while editing income.");
                    }
                } else if (choice == 1) {
                    incomeData.remove(selected);
                    incomeListModel.removeElement(selected);
                    totalIncome -= selected.amount;
                    show("Income deleted successfully.");
                }
            } else if (!expenseList.isSelectionEmpty()) {
                Expense selected = expenseList.getSelectedValue();
                String[] options = {"Edit", "Delete"};
                int choice = JOptionPane.showOptionDialog(frmProject, "Choose action for selected Expense", "Edit/Delete",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    try {
                        String newAmount = JOptionPane.showInputDialog("New Amount:", selected.amount);
                        String newDate = JOptionPane.showInputDialog("New Date (YYYY-MM-DD):", selected.date);
                        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", newDate)) {
                            show("Invalid date format. Use YYYY-MM-DD.");
                            return;
                        }
                        String newCategory = (String) JOptionPane.showInputDialog(frmProject, "New Category:", "Select Category",
                                JOptionPane.PLAIN_MESSAGE, null, categories, selected.category);
                        double amt = Double.parseDouble(newAmount);
                        totalExpense -= selected.amount;
                        selected.amount = amt;
                        selected.date = newDate;
                        selected.category = newCategory;
                        totalExpense += amt;
                        expenseList.repaint();
                        expenseList.setSelectedValue(null, false);
                        show("Expense edited successfully.");
                    } catch (Exception ex) {
                        show("Invalid input while editing expense.");
                    }
                } else if (choice == 1) {
                    expenseData.remove(selected);
                    expenseListModel.removeElement(selected);
                    totalExpense -= selected.amount;
                    show("Expense deleted successfully.");
                }
            } else {
                show("Select an income or expense to edit or delete.");
            }
        });

        controlButtons[0].addActionListener(e -> show("Balance: $" + (totalIncome - totalExpense)));

        controlButtons[1].addActionListener(e -> {
            String cat = (String) JOptionPane.showInputDialog(frmProject, "Choose category", "Set Limit",
                    JOptionPane.PLAIN_MESSAGE, null, categories, categories[0]);
            if (cat != null) {
                String val = JOptionPane.showInputDialog(frmProject, "Set monthly limit for " + cat + ":");
                if (val != null) {
                    try {
                        double limit = Double.parseDouble(val);
                        categoryLimits.put(cat, limit);
                        show("Limit set for " + cat + ": $" + String.format("%.2f", limit));
                    } catch (Exception ex) {
                        show("Invalid limit value.");
                    }
                }
            }
        });

        controlButtons[2].addActionListener(e -> {
            Map<String, Double> catMap = new HashMap<>();
            for (Expense exp : expenseData) {
                catMap.put(exp.category, catMap.getOrDefault(exp.category, 0.0) + exp.amount);
            }
            StringBuilder sb = new StringBuilder("Category-wise Expense Report:\n");
            catMap.forEach((k, v) -> sb.append(k).append(": $").append(String.format("%.2f", v)).append("\n"));
            show(sb.toString());
        });

        controlButtons[4].addActionListener(e -> {
            String[] sortOptions = {"Ascending", "Descending"};
            int sortChoice = JOptionPane.showOptionDialog(frmProject, "Sort by Amount", "Sort Order",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, sortOptions, sortOptions[0]);
            if (sortChoice == 0)
                expenseData.sort(Comparator.comparingDouble(e1 -> e1.amount));
            else if (sortChoice == 1)
                expenseData.sort((e1, e2) -> Double.compare(e2.amount, e1.amount));
            expenseListModel.clear();
            for (Expense exp : expenseData)
                expenseListModel.addElement(exp);
            show("Expenses sorted by amount.");
        });

        controlButtons[5].addActionListener(e -> {
            expenseData.sort(Comparator.comparing(e1 -> e1.date));
            expenseListModel.clear();
            for (Expense exp : expenseData)
                expenseListModel.addElement(exp);
            show("Expenses sorted by date.");
        });

        controlButtons[6].addActionListener(e -> {
            StringBuilder sb = new StringBuilder("=== Session Summary ===\n");
            sb.append("Total Income: $").append(String.format("%.2f", totalIncome)).append("\n");
            sb.append("Total Expense: $").append(String.format("%.2f", totalExpense)).append("\n");
            sb.append("Balance: $").append(String.format("%.2f", totalIncome - totalExpense)).append("\n");
            show(sb.toString());
        });

        controlButtons[7].addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(frmProject, "Clear all data?") == JOptionPane.YES_OPTION) {
                incomeData.clear();
                expenseData.clear();
                incomeListModel.clear();
                expenseListModel.clear();
                totalIncome = 0;
                totalExpense = 0;
                categoryLimits.clear();
                show("All data reset.");
            }
        });
    }

    private void styleButton(JButton btn) {
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    }

    private void show(String msg) {
        JOptionPane.showMessageDialog(frmProject, msg);
    }
}
