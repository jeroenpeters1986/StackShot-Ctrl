package mtb.devices.rails;

/**
 * Pure Java StackShot speed calculator extracted for the arm64 speed preview.
 * No Swing, JNA, FTDI, frame/status-log, or Canon EDSDK imports.
 */
public final class StackShotSpeedCalculator {

    public static final double MAX_STEP_RATE = 20000.0;

    private StackShotSpeedCalculator() {
        // utility class
    }

    public static SpeedResult calculate(double speedMmPerSecond, double stepsPerRevolution, double mmPerRevolution) {
        double requestedStepRate = (double) Math.round(
                Math.abs(speedMmPerSecond * stepsPerRevolution / mmPerRevolution));
        double appliedStepRate = Math.min(requestedStepRate, MAX_STEP_RATE);
        double effectiveSpeedMmPerSecond = appliedStepRate * mmPerRevolution / stepsPerRevolution;
        boolean limited = requestedStepRate > MAX_STEP_RATE;
        return new SpeedResult(requestedStepRate, appliedStepRate, effectiveSpeedMmPerSecond, limited);
    }

    public static final class SpeedResult {
        private final double requestedStepRate;
        private final double appliedStepRate;
        private final double effectiveSpeedMmPerSecond;
        private final boolean limited;

        private SpeedResult(double requestedStepRate, double appliedStepRate, double effectiveSpeedMmPerSecond, boolean limited) {
            this.requestedStepRate = requestedStepRate;
            this.appliedStepRate = appliedStepRate;
            this.effectiveSpeedMmPerSecond = effectiveSpeedMmPerSecond;
            this.limited = limited;
        }

        public double getRequestedStepRate() {
            return requestedStepRate;
        }

        public double getAppliedStepRate() {
            return appliedStepRate;
        }

        public double getEffectiveSpeedMmPerSecond() {
            return effectiveSpeedMmPerSecond;
        }

        public boolean isLimited() {
            return limited;
        }
    }
}
