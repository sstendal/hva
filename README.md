# Hva
Hva is a tiny app that helps you to keep track of your work hours. It is written in Java and uses JavaFX for the GUI.
The app is designed to be as simple as possible. It is not intended to be a full-fledged time tracking system,
but rather a simple tool that helps you remember what you were doing from hour to hour.

## Language
It is currently only available in norwegian

## Development

Use maven to build the jar file.

```
mvn clean install
```

### Build native installers

#### Create a windows executable
On a Windows machine

- Install JDK
- Install WiX Toolset, version 3. https://wixtoolset.org/docs/wix3/
- Run the powershell build script
- The installer will be created in the target directory
```
PowerShell.exe -ExecutionPolicy Bypass -File .\build-windows-installer.ps1
```
     
#### Create a Mac installer
On a Mac

- Install JDK
- Run the bash build script
- The installer will be created in the target directory
```
bash build-mac-installer.sh
``` 

## Author

* **Sigurd Stendal**

## License

This project is licensed under the GPLv3 license - see the [LICENSE](LICENSE) file for details.
