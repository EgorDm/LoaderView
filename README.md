# LoaderView
A simple LoaderView to fill your screen when something is being processed.

This is a simple, lightweight and customizable loaderview which can be used in your app to show your users that something is being processed and the state of your proces.

![Loading state](http://i.imgur.com/XOBOZz5.png)

Loader view has two built in state layouts. One with a loader and one with an error message along with retry button.

## Gradle
```
dependencies {
  ...
  compile 'net.egordmitriev.loaderview:loaderview:1.0@aar'
}
```

## Usage
#### Adding into a layout
```
<net.egordmitriev.loaderview.LoaderView
        android:id="@+id/loaderview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:state="error"/>
```

##### Attributes
* state - Its used to set loaders initial state. There are only four states available to choose from: (loading, idle, error, extra)
* error_resourceID - Is used to use a custom layout to use when state is error. By default its [this layout](https://github.com/EgorDm/LoaderView/blob/master/loaderview/src/main/res/layout/component_loaderview_error.xml).
* extra_resourceID - Is used to use a custom layout to use when state is extra. By default its set to null, nothing will be displayed.
* loading_resourceID - Is used to use a custom layout to use when state is loading. By default its [this layout](https://github.com/EgorDm/LoaderView/blob/master/loaderview/src/main/res/layout/component_loaderview_loading.xml).
* idle_resourceID - Is used to use a custom layout to use when state is idle. By default its set to null, nothing will be displayed.

##### Programmatic usage
```
LoaderView loaderView = (LoaderView) findViewById(R.id.loaderview);
loaderView.setState(LoaderView.STATE_ERROR);
loaderView.setListener(new LoaderView.LoaderViewCallback() {
  @Override
  public void onRetryClick() {
    Log.d(TAG, "Retry clicked");
  }
});
```

You can change loaders state using **setState** and add a listener for retry button in default error layout with **setListener**.

## License
```
Copyright 2015 Egor Dmitriev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
