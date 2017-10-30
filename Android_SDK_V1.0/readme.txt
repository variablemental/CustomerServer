1. 解压下载EmotibotSDK.zip

2. File > New > Import Module选中xychatlib的路径，finish

3. app下的build.gradle的dependencies中加入compile project(':xychatlib')

4. 应用的AndroidManifest.xml中加入权限配置
	<uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />

5. 应用的AndroidManifest.xml中加入聊天activity配置
	<activity android:name="com.emotibot.xychatlib.XYlibChatActivity"
        android:windowSoftInputMode="stateHidden|stateAlwaysHidden">
    </activity>

6. 在应用的需要加入小影lib的布局中加入
	<com.emotibot.xychatlib.XYlibFloatingTriggerButton
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:src="@drawable/icon_shadow"
        android:background="@android:color/transparent"/>

7. 打开xychatlib下的 XYlibConfig.java文件，配置APPID为开发者申请到 的AppId