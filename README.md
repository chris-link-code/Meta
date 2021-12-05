# 说明
- 这是一个简单的Android应用  
- 功能是将小红书缓存的图片(/storage/emulated/0/Android/data/com.xingin.xhs/cache/image_manager_disk_cache/)复制到"/storage/emulated/0/ADM/"路径下并重命名
- 安装本APP的apk后，一定要开启“读写手机存储”权限
- 会递归遍历缓存目录下的所有子文件
- AndroidManifest.xml增加了读写外部存储的权限配置
- android:requestLegacyExternalStorage="true"
- uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
- uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"

# 问题
- 阻塞队列LinkedBlockingQueue在多线程并发时效率低下