# 🚀 Release Notes — EMF Pulse

All notable changes and releases for EMF Pulse are documented here.

---

## [v1.0.1] - 2026-11-12
> **Version Code:** `2` • **Target SDK:** `37` (Android 15+) • **Min SDK:** `24` (Android 7.0+)

### ✨ What's New
- **Brand New About Screen**:
  - Beautiful glowing app logo and typography badge.
  - Displays current version: **v1.0.1 (Build 2)**.
  - **Developer Profile Card**: Instant one-tap access to **Samyak Kamble**'s GitHub profile (`@samyak2403`).
  - **Source Code Card**: Direct link to the open-source repository on GitHub (`samyak2403/EMFPulse`).
  - Full app overview, MIT License badge, and 100% offline privacy notice.

- **Settings Screen Enhancements**:
  - Added **"ABOUT & PROJECT"** section.
  - Added quick-access tiles with custom trailing navigation icons for **About EMF Pulse**, **Developer**, and **Source Code**.

- **Navigation & UX Improvements**:
  - Integrated `ScreenState.ABOUT` with smooth crossfade screen transitions.
  - System and hardware `BackHandler` support for seamless navigation (`About` ➔ `Settings` ➔ `Detector`).

- **Project & Repository Cleanliness**:
  - Created a comprehensive `README.md` with visual architecture diagram, screenshots, and build commands.
  - Added `*.jar` to `.gitignore` and removed temporary build classes.

---

## [v1.0.0] - 2026-11-11
> **Version Code:** `1` • **Target SDK:** `37` (Android 15+) • **Min SDK:** `24` (Android 7.0+)

### 🎉 Initial Release
- **Precision SpeedView Gauge**:
  - Dynamic analog speedometer with distinct color safety zones (*Safe, Normal, Caution, Hazard*).
  - Scale ticks ranging from 0 to 3000 mG (or 0 to 300 µT).
  - Responsive needle pointer and real-time digital magnitude display.

- **K2-Style LED Level Indicator**:
  - 15-segment color-graded power meter with real-time percentage readout.

- **Real-Time Scrolling Waveform Graph**:
  - Live canvas graph tracking electromagnetic spikes, background noise, and micro-fluctuations.

- **Geiger Counter Acoustic Feedback**:
  - Real-time AudioTrack PCM sound synthesizer accelerating clicks as magnetic fields exceed 60 µT.

- **3-Axis Vector Breakdown & Memory**:
  - Individual live readouts for X, Y, and Z magnetic vectors.
  - Persistent Peak and Minimum retention.

- **Zero Baseline Calibration**:
  - One-tap tare function to zero-out ambient background magnetism.

- **Dual Measurement Units**:
  - Seamless switching between **Milligauss (mG)** and **Microtesla (µT)** ($1\ \mu\text{T} = 10\ \text{mG}$).

- **Test & Simulation Mode**:
  - Built-in simulation wave generator allowing full testing on devices without a hardware magnetometer.
  - Dedicated **No Sensor Screen** guiding users without a compass sensor.

- **Aesthetic & Theme**:
  - Modern VeilVPN dark aesthetic with ambient glow, custom emerald I/O switches, and Plus Jakarta Sans typography.
  - 100% offline, privacy-first, zero analytics, zero ads.
