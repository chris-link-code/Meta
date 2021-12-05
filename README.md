# 说明
- 这是一个简单的Android应用  
- 功能是将小红书缓存的图片(/storage/emulated/0/Android/data/com.xingin.xhs/cache/image_manager_disk_cache/)复制到"/storage/emulated/0/ADM/"路径下并重命名
- 安装本APP的apk后，一定要开启“读写手机存储”权限
- 会递归遍历缓存目录下的所有子文件
- AndroidManifest.xml增加了读写外部存储的权限配置
- android:requestLegacyExternalStorage="true"
- uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
- uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"

// 确认线程池的所有任务都执行完毕，再执行后面的代码
// 切记：这个时间一定要设置长一点，如果在此时间内，线程池的任务没有执行完成，就会返回false，但线程池中的任务会继续执行
ExecutorService awaitTermination(30, TimeUnit.MINUTES);


# 问题
- 阻塞队列LinkedBlockingQueue在多线程并发时效率低下

