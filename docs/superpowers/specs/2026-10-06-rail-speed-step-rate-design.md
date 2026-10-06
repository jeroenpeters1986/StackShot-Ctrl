# Rail speed step-rate limit design

## Status

Design direction approved on 2026-10-06; verification scope revised at the
user's request and awaiting document review.

## Goal and scope

Prevent StackShot movement from requesting an excessive motor step rate when
the configured distance per revolution is very small. Preserve support for
rails with non-default distance-per-revolution values.

The reported `C` shortcut/cancel behavior is explicitly out of scope at the
user's request.

## Evidence and working hypothesis

`RailStackshot` stores motor speed in mm/s and converts it to controller steps
per second using `speed * STEPS_PER_REV / MM_PER_REV`. That conversion is
applied both when opening the controller and when changing motor speed, but
the resulting rate is not bounded.

With the reported default values of 2 mm/s and 3200 steps/rev, 0.2 mm/rev
requests 32,000 steps/s and 0.1 mm/rev requests 64,000 steps/s. This aligns
with the reported failure threshold. A short 1 mm position move may complete
before the problem is apparent, while a held back/forward move sustains the
rate for longer.

The controller's formal maximum is not documented in the repository and the
physical rail is unavailable for reproduction. Therefore, 20,000 steps/s is
an intentionally conservative application cap based on the reported
threshold, not a claimed manufacturer limit. It must be confirmed against the
real controller and rail.

## Behavior

- Compute the requested step rate from the requested mm/s, steps/rev, and
  mm/rev values.
- Send no more than 20,000 steps/s to the controller.
- Preserve the user's requested speed setting. When the cap reduces the
  effective mm/s speed, report the effective speed in the application's
  existing status log.
- Reapply the effective speed on controller open and whenever speed,
  distance/rev, or steps/rev changes, so updates cannot leave a stale speed
  configuration on the controller.
- Do not change motion distance conversion, limits, or keyboard shortcuts.

## Implementation boundary

Keep the cap and conversion in the StackShot rail implementation; do not
change `RailBase`, the virtual rail, or generic movement behavior. Use one
shared conversion path for initial controller setup and later updates.

## Verification

Do not add automated tests or a test harness at this stage, per the user's
request. Review the conversion and configuration-update paths in the code,
and run the repository's available Java compilation/build checks. Hardware
movement and USB communication still require confirmation on the actual
setup.
