# Hva
Hva is a tiny app that helps you to keep track of your work hours. It is written in Java and uses JavaFX for the GUI. 
The app is designed to be as simple as possible. It is not intended to be a full-fledged time tracking system, 
but rather a simple tool that helps you remember what you were doing from hour to hour.
             
## Language
It is currently only available in norwegian

## Create a windows executable
On a Windows machine

- Install JDK
- Install WiX Toolset, version 3. https://wixtoolset.org/docs/wix3/ 
- Copy hva.jar and the icon file to a directory named i.e. hva-files
- Run the following command in the parent directory
```
 & "c:\Program Files\Java\jdk-22\bin\jpackage.exe" -n Hva --main-class hva.Hva --main-jar hva.jar --input hva-files --type exe --icon hva-files\eye_256.ico --win-menu --win-shortcut --vendor "Sigurd Stendal" --app-version 4.0
```

## Author

* **Sigurd Stendal**

## License

This project is licensed under the GPLv3 license - see the [LICENSE](LICENSE) file for details.
