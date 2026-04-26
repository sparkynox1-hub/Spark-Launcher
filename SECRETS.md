# 🔐 Spark Launcher — Required GitHub Secrets

Set these in: **Settings → Secrets and variables → Actions**

---

## Android Signing (Release APK)

| Secret | Description |
|--------|-------------|
| `KEYSTORE_BASE64` | Base64-encoded `.jks` keystore file |
| `KEYSTORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias inside keystore |
| `KEY_PASSWORD` | Key password |

### Generate keystore:
```bash
keytool -genkey -v -keystore spark_release.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias spark
# Then encode:
base64 -w 0 spark_release.jks
```

---

## API Keys

| Secret | Description |
|--------|-------------|
| `CURSEFORGE_API_KEY` | CurseForge API key from https://console.curseforge.com |

---

## iOS Signing (optional, only for IPA builds)

| Secret | Description |
|--------|-------------|
| `IOS_CERT_BASE64` | Base64 .p12 Apple distribution certificate |
| `IOS_CERT_PASSWORD` | .p12 password |
| `IOS_PROVISION_BASE64` | Base64 provisioning profile |
| `IOS_KEYCHAIN_PASSWORD` | Temp keychain password (any random string) |

---

## Notifications (optional)

| Secret | Description |
|--------|-------------|
| `DISCORD_WEBHOOK` | Discord webhook URL for release notifications |

---

## Tags → Releases

Push a tag to trigger a full release build + GitHub Release:
```bash
git tag v1.0.0
git push origin v1.0.0
```
