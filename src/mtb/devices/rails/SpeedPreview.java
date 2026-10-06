package mtb.devices.rails;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Standalone preview for the StackShot speed conversion and software limit.
 */
public final class SpeedPreview {

    private static final double DEFAULT_SPEED_MM_PER_SECOND = 2.0d;
    private static final double DEFAULT_STEPS_PER_REVOLUTION = 3200.0d;
    private static final double DEFAULT_MM_PER_REVOLUTION = 1.5875d;
    private static final String INVALID_MESSAGE = "Enter positive, finite numbers in all three fields.";

    private final NumberFormat parseFormat;
    private final NumberFormat stepFormat;
    private final NumberFormat speedFormat;

    private final JTextField requestedSpeedField;
    private final JTextField stepsPerRevolutionField;
    private final JTextField mmPerRevolutionField;

    private final JLabel requestedStepsValueLabel;
    private final JLabel appliedStepsValueLabel;
    private final JLabel effectiveSpeedValueLabel;
    private final JLabel limitValueLabel;
    private final JLabel validationLabel;

    private final JFrame frame;

    public SpeedPreview() {
        Locale locale = Locale.getDefault();
        this.parseFormat = NumberFormat.getNumberInstance(locale);
        this.stepFormat = NumberFormat.getNumberInstance(locale);
        this.speedFormat = NumberFormat.getNumberInstance(locale);
        this.stepFormat.setGroupingUsed(false);
        this.stepFormat.setMinimumFractionDigits(0);
        this.stepFormat.setMaximumFractionDigits(0);
        this.speedFormat.setGroupingUsed(false);
        this.speedFormat.setMinimumFractionDigits(0);
        this.speedFormat.setMaximumFractionDigits(4);

        this.requestedSpeedField = new JTextField(12);
        this.stepsPerRevolutionField = new JTextField(12);
        this.mmPerRevolutionField = new JTextField(12);

        this.requestedStepsValueLabel = new JLabel("—");
        this.appliedStepsValueLabel = new JLabel("—");
        this.effectiveSpeedValueLabel = new JLabel("—");
        this.limitValueLabel = new JLabel("—");
        this.validationLabel = new JLabel(" ");
        this.validationLabel.setForeground(new Color(153, 0, 0));

        this.frame = new JFrame("StackShot Speed Preview");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setContentPane(buildContent());

        this.requestedSpeedField.setText(Double.toString(DEFAULT_SPEED_MM_PER_SECOND));
        this.stepsPerRevolutionField.setText("3200");
        this.mmPerRevolutionField.setText(Double.toString(DEFAULT_MM_PER_REVOLUTION));

        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePreview();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePreview();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePreview();
            }
        };
        this.requestedSpeedField.getDocument().addDocumentListener(listener);
        this.stepsPerRevolutionField.getDocument().addDocumentListener(listener);
        this.mmPerRevolutionField.getDocument().addDocumentListener(listener);

        updatePreview();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpeedPreview().showWindow());
    }

    public void showWindow() {
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Inputs"));
        addRow(inputPanel, 0, "Requested speed (mm/s):", requestedSpeedField);
        addRow(inputPanel, 1, "Steps per revolution:", stepsPerRevolutionField);
        addRow(inputPanel, 2, "mm per revolution:", mmPerRevolutionField);

        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        addResultRow(resultPanel, 0, "Requested steps/s:", requestedStepsValueLabel);
        addResultRow(resultPanel, 1, "Applied steps/s:", appliedStepsValueLabel);
        addResultRow(resultPanel, 2, "Effective speed (mm/s):", effectiveSpeedValueLabel);
        addResultRow(resultPanel, 3,
                "Software limit active (20,000 steps/s conservative software limit; hardware confirmation still required):",
                limitValueLabel);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints centerConstraints = new GridBagConstraints();
        centerConstraints.gridx = 0;
        centerConstraints.gridy = 0;
        centerConstraints.weightx = 1.0d;
        centerConstraints.fill = GridBagConstraints.HORIZONTAL;
        centerConstraints.insets = new Insets(0, 0, 10, 0);
        center.add(inputPanel, centerConstraints);

        centerConstraints.gridy = 1;
        center.add(resultPanel, centerConstraints);

        panel.add(center, BorderLayout.CENTER);

        validationLabel.setHorizontalAlignment(SwingConstants.LEFT);
        validationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(validationLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void addRow(JPanel panel, int row, String labelText, JTextField field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.LINE_END;
        labelConstraints.insets = new Insets(4, 4, 4, 8);
        panel.add(new JLabel(labelText), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1.0d;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(4, 0, 4, 4);
        panel.add(field, fieldConstraints);
    }

    private void addResultRow(JPanel panel, int row, String labelText, JLabel valueLabel) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.LINE_END;
        labelConstraints.insets = new Insets(4, 4, 4, 8);
        panel.add(new JLabel(labelText), labelConstraints);

        GridBagConstraints valueConstraints = new GridBagConstraints();
        valueConstraints.gridx = 1;
        valueConstraints.gridy = row;
        valueConstraints.weightx = 1.0d;
        valueConstraints.fill = GridBagConstraints.HORIZONTAL;
        valueConstraints.anchor = GridBagConstraints.LINE_START;
        valueConstraints.insets = new Insets(4, 0, 4, 4);
        panel.add(valueLabel, valueConstraints);
    }

    private void updatePreview() {
        Double requestedSpeed = parsePositiveDouble(requestedSpeedField);
        Double stepsPerRevolution = parsePositiveDouble(stepsPerRevolutionField);
        Double mmPerRevolution = parsePositiveDouble(mmPerRevolutionField);

        if (requestedSpeed == null || stepsPerRevolution == null || mmPerRevolution == null) {
            clearResults();
            validationLabel.setText(INVALID_MESSAGE);
            return;
        }

        StackShotSpeedCalculator.SpeedResult result = StackShotSpeedCalculator.calculate(
                requestedSpeed.doubleValue(),
                stepsPerRevolution.doubleValue(),
                mmPerRevolution.doubleValue());

        requestedStepsValueLabel.setText(stepFormat.format(result.getRequestedStepRate()));
        appliedStepsValueLabel.setText(stepFormat.format(result.getAppliedStepRate()));
        effectiveSpeedValueLabel.setText(speedFormat.format(result.getEffectiveSpeedMmPerSecond()));
        limitValueLabel.setText(result.isLimited() ? "Yes" : "No");
        validationLabel.setText(" ");
    }

    private Double parsePositiveDouble(JTextField field) {
        String text = field.getText();
        if (text == null) {
            return null;
        }

        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        ParsePosition position = new ParsePosition(0);
        Number parsed = parseFormat.parse(trimmed, position);
        if (parsed == null || position.getIndex() != trimmed.length()) {
            return null;
        }

        double value = parsed.doubleValue();
        if (!Double.isFinite(value) || value <= 0.0d) {
            return null;
        }
        return Double.valueOf(value);
    }

    private void clearResults() {
        requestedStepsValueLabel.setText("—");
        appliedStepsValueLabel.setText("—");
        effectiveSpeedValueLabel.setText("—");
        limitValueLabel.setText("—");
    }
}
