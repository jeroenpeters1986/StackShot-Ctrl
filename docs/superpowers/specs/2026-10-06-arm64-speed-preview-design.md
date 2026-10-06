# Apple Silicon speed preview design

## Status

Approved by the user on 2026-10-06.

## Goal

Let the user inspect the StackShot speed conversion and software step-rate
limit on an Apple Silicon Mac without a controller or rail. The preview must
make it easy to compare small distance-per-revolution values such as 0.2 and
0.1 mm/rev.

## Scope

- Add a small, standalone Java Swing speed-preview window.
- Share the speed calculation between the preview and `RailStackshot`, so the
  preview reflects the conversion used by the controller code.
- Provide a simple macOS launcher that compiles and starts only the preview
  classes using the installed JDK.
- Do not communicate with USB, FTDI, a controller, or a camera.

The preview is not the existing full StackShot application. Its packaged JAR
still has an Apple Silicon-incompatible JNA native library; repairing or
repackaging that application is a separate task.

## Inputs and results

The window has editable fields for:

- Requested motor speed in mm/s, initially 2.0.
- Steps per revolution, initially 3200.
- Distance per revolution in mm, initially 1.5875.

It updates the requested step rate, the step rate after the 20,000 steps/s
software limit, the resulting effective speed in mm/s, and whether the limit
is active. Changing distance/rev alone must recalculate the result, allowing
the user to inspect values such as 0.6, 0.2, and 0.1 mm/rev.

The 20,000 steps/s limit is the previously agreed conservative estimate, not
a manufacturer-verified maximum. The preview must label it as a software
limit and must not imply that it reproduces USB communication or proves
hardware behavior.

Inputs must be positive, finite numbers. Invalid input must produce a clear
validation message and must not leave stale calculated values presented as
current results. The interface should respect the machine's decimal locale.

## Calculation and boundaries

Use one hardware-independent calculator for both the preview and
`RailStackshot`. For valid positive inputs:

1. Calculate the requested step rate using the existing StackShot conversion
   and its integer rounding semantics.
2. Apply the 20,000 steps/s limit.
3. Convert the applied step rate back to effective mm/s.

The calculator must not depend on JNA, FTDI, Canon EDSDK, or Swing. The
preview window depends only on the calculator and the JDK. The existing rail
continues to own all hardware communication; the shared calculator only
returns calculated speed values.

The macOS launcher compiles the calculator and preview directly, then starts
the preview. It must not compile or start the full application, whose build
currently depends on unavailable proprietary Canon EDSDK bindings.

## Out of scope

- Fixing the existing packaged application's JNA incompatibility.
- Emulating rail motion, USB communication, or the reported stuttering.
- Testing on physical hardware or claiming the 20,000 steps/s estimate is
  confirmed.
- Changing the `C` keyboard shortcut or cancel behavior.
- Changing movement-distance conversion or adding a general test framework.

## Verification

- Compile the standalone calculator and preview with the installed JDK.
- Check representative calculations for the default 1.5875 mm/rev and low
  values 0.6, 0.2, and 0.1 mm/rev, including whether the cap applies and the
  effective mm/s result.
- Confirm the launcher opens the preview without loading JNA, FTDI, or EDSDK.
- Review the StackShot call path to confirm it uses the shared calculator.

Physical controller behavior remains unverified until the actual rail and
controller are available.
