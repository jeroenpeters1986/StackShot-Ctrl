# StackShot Controller

Application Java de contrôle du rail macro StackShot (Cognisys) et des appareils photo Canon EOS.

## ⚠️ Fichiers absents du dépôt (dépendances propriétaires)

Certains fichiers sont exclus du dépôt car ils sont soumis à des licences tierces ou des NDAs.
Ils sont listés dans `.gitignore` et doivent être obtenus/récupérés séparément.

### Canon EDSDK (bindings + drivers)

| Fichier/Dossier manquant  | Source                  | Comment l'obtenir                                                                                                                                                                                                                                                                                                |
| ------------------------- | ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `src/edsdk/`              | Canon EDSDK (C headers) | Les bindings JNA ont été générés avec [JNAerator](https://github.com/nativelibs4java/JNAerator) à partir des headers C du Canon EOS Digital SDK. Le SDK Canon s'obtient sur demande auprès de Canon (nécessite un contrat de licence signé). Une fois les headers obtenus, regénérer les bindings via JNAerator. |
| `EDSDK.dll` (Windows)     | Canon                   | Inclus dans le Canon EOS Digital SDK. Placer à la racine du projet ou dans le PATH système.                                                                                                                                                                                                                      |
| `EDSPlugin.dll` (Windows) | Canon                   | Inclus dans le Canon EOS Digital SDK. Placer à la racine du projet ou dans le PATH système.                                                                                                                                                                                                                      |

### Canon Picture Styles (profils ICC/BIN)

| Fichier/Dossier manquant                                                                                                                   | Source                                 | Comment l'obtenir                                                                                               |
| ------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| `icc/*.BIN`                                                                                                                                | Canon EOS Utility / Firmware appareils | Extraits des appareils Canon. Pour les regénérer, connecter un appareil Canon et utiliser le Canon EOS Utility. |
| `icc/6000.ICC`                                                                                                                             | Canon                                  | Via Canon EOS Utility ou EDSDK.                                                                                 |
| `icc/FA.ICC`, `icc/FS.ICC`, `icc/LA.ICC`, `icc/LS.ICC`, `icc/NA.ICC`, `icc/NS.ICC`, `icc/PA.ICC`, `icc/PS.ICC`, `icc/SA.ICC`, `icc/SS.ICC` | Canon Picture Styles                   | Générés à partir de l'EDSDK.                                                                                    |

## Dépendances open source (incluses)

| Dépendance               | Licence  | Fichier(s)                       |
| ------------------------ | -------- | -------------------------------- |
| JNA (Java Native Access) | LGPL 2.1 | `jna-3.5.2.jar`, `jna-3.5.1.jar` |
| JNA Platform             | LGPL 2.1 | `platform-3.5.2.jar`             |
| JGoodies Forms           | BSD      | `forms-1.3.0.jar`                |
| JD2XX (FTDI wrapper)     | BSD      | `src/ftd2/*.java`                |

## Dépendances système / drivers

| Dépendance        | Licence                | Fichier(s)                   | Remarque                                                                                                                                                                                     |
| ----------------- | ---------------------- | ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| FTDI D2XX drivers | Propriétaire FTDI Chip | `ftd2xx.dll`, `ftd2xx64.dll` | Redistribuables avec l'application (FTDI autorise la redistribution avec les produits utilisant leurs puces). Téléchargeables sur [ftdichip.com](https://www.ftdichip.com/Drivers/D2XX.htm). |

## Build & Exécution

### Prérequis
- Java Runtime Environment (JRE) 6 ou 7, 32-bit ou 64-bit
- Drivers FTDI D2XX installés
- Canon EDSDK installé (si contrôle d'un appareil Canon)

### Compilation
Le projet est un projet Eclipse (.classpath, .project). Ouvrir dans Eclipse et builder.

### Lancement
Windows : lancer `StackShotCtrl.jar` ou l'exécutable `Simple Stackshot Controller.exe`
Linux : `./SimpleStackShotCtrl.bash`

### Aperçu de vitesse (macOS Apple Silicon)

Lancer `./run-speed-preview.command` depuis la racine du dépôt. Le script utilise le JDK 8 arm64 déjà installé et n'installe pas Rosetta. Cette fenêtre calcule la vitesse effective sans rail ni communication USB; elle ne lance pas l'application complète.

## Licence

Ce projet est sous licence propriétaire — Copyright (c) 2013 macro-toolbox.com.
Voir [LICENSE.txt](LICENSE.txt) pour les détails.

## Contact

support@macro-toolbox.com