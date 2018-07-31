
# falafreud-tools

## Getting started

`$ npm install falafreud-tools --save`

### Mostly automatic installation

`$ react-native link falafreud-tools`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.falafreud-ToolsPackage;` to the imports at the top of the file
  - Add `new falafreud-ToolsPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':falafreud-tools'
  	project(':falafreud-tools').projectDir = new File(rootProject.projectDir, 	'../node_modules/falafreud-tools/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':falafreud-tools')
  	```


## Usage
```javascript
import falafreud-Tools from 'falafreud-tools';

// TODO: What to do with the module?
falafreud-Tools;
```
  