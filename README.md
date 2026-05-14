# Paryavaran-Kavalu

A geo-tagging environmental reporting app that empowers citizens to report illegal waste dumping sites (Waste Blackspots) to local cleanup volunteers.

## Features

- **Quick Report**: Capture location, select waste type, attach photo, and submit reports.
- **Automatic Geo-Tagging**: Uses FusedLocationProviderClient for precise GPS coordinates.
- **Cleanliness Map View**: Google Maps integration with color-coded markers (Red for Pending, Green for Cleaned).
- **Points System (Eco-Karma)**: Earn points for verified reports, view in profile.
- **Volunteer Simulation**: Test button to mark reports as cleaned.

## Setup Instructions

1. **Clone or Download the Project**:
   - Open in Android Studio.

2. **Google Maps API Key**:
   - Get an API key from [Google Cloud Console](https://console.cloud.google.com/).
   - Enable Maps SDK for Android.
   - Replace `YOUR_API_KEY_HERE` in `app/src/main/AndroidManifest.xml` with your key.

3. **Build and Run**:
   - Ensure Android SDK 24+ is installed.
   - Sync Gradle files.
   - Run on device or emulator with location permissions.

## Permissions Required

- Location (Fine and Coarse)
- Camera
- Storage (Read/Write External)

## Architecture

- **MVVM**: ViewModels handle business logic, Fragments for UI.
- **Room Database**: Local storage for reports and user data.
- **Clean Architecture**: Separation between UI, business logic, and data layers.

## Key Components

- `MainActivity`: Navigation between fragments.
- `ReportFragment`: Create new reports.
- `MapFragment`: View reports on map, simulate cleaning.
- `ProfileFragment`: View Eco-Karma points.
- `ReportRepository`: Data access layer.
- Utils: Location, Image compression, Permissions.

## Testing

- Submit reports with photos and locations.
- View markers on map.
- Click marker and use FAB to mark as cleaned.
- Check points in profile.

## Notes

- Photos are compressed to under 500KB.
- Location accuracy depends on device GPS.
- For production, integrate real backend instead of local Room DB.