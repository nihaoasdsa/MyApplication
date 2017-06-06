package com.example.text.myapplication.tool;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.example.text.myapplication.MyApplication;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import static com.baidu.location.g.j.R;

public class ImageUtils {

    private static final String TAG = "ImageUtils";
    static DisplayImageOptions.Builder optionsBuilder;

    /**
     * 压缩等级
     */
    public static enum COMPRESS {
        LOSSLESS(0),HQUALITY(0), MQUALITY(0), LQUALITY(0);

        int level;

        COMPRESS(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }

    /**
     * 初始化枚举对象COMPRESS
     *
     * @param context
     */
    public static void init(Context context) {

        optionsBuilder = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.edition5_loading)
//                .showImageForEmptyUri(R.drawable.empty_img)
//                .showImageOnFail(R.drawable.empty_img)
                .cacheInMemory(true)//缓存在内存中
                .cacheOnDisk(true)//缓存在磁盘中
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY);

        ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(context)
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))//最近最少使用的内存会被删除
                //.memoryCacheSize(10 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(optionsBuilder.build())
                .tasksProcessingOrder(QueueProcessingType.LIFO);

        ImageLoader.getInstance().init(configBuilder.build());


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        //默认的高质量压缩将图片压缩为屏幕大小的一半,由于图片加载框架是按RGB_565进行加载位图，所以每个像素占用2字节
        int imageSize = widthPixels * heightPixels * 2;
        COMPRESS.LOSSLESS.setLevel(imageSize);
        COMPRESS.HQUALITY.setLevel(imageSize / (int) Math.pow(2, 1));
        COMPRESS.MQUALITY.setLevel(imageSize / (int) Math.pow(2, 2));
        COMPRESS.LQUALITY.setLevel(imageSize / (int) Math.pow(2, 3));
    }

    /**
     * byte[] → Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap BytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 将bitmap转换压缩成byte Bitmap → byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] BitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Drawable → Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * bitmap类型转换为drawable类型
     *
     * @return
     */
    public static Drawable BitmapToDrawable(Bitmap b, Resources resource) {

        BitmapDrawable bd = new BitmapDrawable(resource, b);
        // 设置图片边界
        bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());
        return bd;
    }

    /**
     * 将资源文件转换成bitmap
     *
     * @param r     resources
     * @param resID 图片的resid
     * @return
     */
    public static Bitmap ResToBiamap(Resources r, int resID) {
        InputStream is = r.openRawResource(resID);
        BitmapDrawable bmpDraw = new BitmapDrawable(r, is);
        Bitmap bmp = bmpDraw.getBitmap();
        return bmp;
    }

    /**
     * 将base64编码的字符串转为bitmap
     *
     * @param str
     * @return bitmap
     */
    public static Bitmap stringToBitmap(String str) {
        Bitmap bitmap = null;
        byte[] bitmapArray = Base64.decode(str, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                bitmapArray.length);
        return bitmap;
    }

    /**
     * 将图片由base64编码转为string
     *
     * @param b bitmap
     * @return 编码有的string
     */
    public static String bitmapToString(Bitmap b) {
        String str = null;
        byte[] bytes = BitmapToBytes(b);
        str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    /**
     * 根据传递的uri 获取图片
     *
     * @param cr contentResolver
     * @param u  图片的Uri
     * @return 获取到的bitmap
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Deprecated
    public static Bitmap getBitmapByUri(ContentResolver cr, Uri u)
            throws FileNotFoundException, IOException {
        Bitmap b = MediaStore.Images.Media.getBitmap(cr, u);
        return b;
    }

    /**
     * 将指定的uri转换为bitmap
     *
     * @param uri
     * @return bitmap
     */
    @Deprecated
    public static Bitmap decodeUriAsBitmap(ContentResolver cr, Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream is = cr.openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * @param cr    contentResolver
     * @param uri   图片的Uri
     * @param sizeW 图片最大的size宽度
     * @param sizeH 图片的最大高度
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Bitmap getThumbnailByUri(ContentResolver cr, Uri uri,
                                           int sizeW, int sizeH) throws FileNotFoundException, IOException {
        InputStream input = cr.openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;// optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1)
                || (onlyBoundsOptions.outHeight == -1))
            return null;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = mathSampleSize(onlyBoundsOptions.outWidth,
                onlyBoundsOptions.outHeight, sizeW, sizeH);
        bitmapOptions.inDither = true;// optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        input = cr.openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.ceil(ratio));
        if (k == 0)
            return 1;
        else
            return k;
    }

    /**
     * 图片按质量压缩,形成文件形式
     *
     * @param image 要压缩的bitmap
     * @return
     */
    public static File compressBitmapToFile(Bitmap image, String filePath) {

        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 80;
        while (baos.toByteArray().length / 1024 > 300 && options >= 10) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(f);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 图片按质量压缩,形成文件形式
     *
     * @param image
     * @param f
     * @return
     */
    public static File compressBitmapToFile(Bitmap image, File f) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 80;
        while (baos.toByteArray().length / 1024 > 300 && options >= 10) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(f);
            out.write(baos.toByteArray());
            baos.reset();
            baos.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return f;
    }

    /**
     * 将字节流转为bitmap,并按比例压缩
     *
     * @param b 包含图片数据的字节流
     * @return 经过压缩后的bitmap
     */
    public static Bitmap compByteToBitmap(byte[] b) {
        ByteArrayInputStream isBm = new ByteArrayInputStream(b);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float ww = 480f;// 这里设置宽度为480f
        float hh = 800f;// 这里设置高度为800f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        int be1 = 1;
        if (w < h) {// 如果宽度大的话根据宽度固定大小缩放, 如果高度高的话根据宽度固定大小缩放
            be = (int) Math.ceil(w / ww);
            be1 = (int) Math.ceil(h / hh);
        } else if (w >= h) {
            be = (int) Math.ceil(h / ww);
            be1 = (int) Math.ceil(w / hh);
        }
        be = (be1 > be) ? be1 : be;
        if (be <= 0)
            be = 1;
//        MyDebugUtils.i(TAG, "缩放比例" + be);
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        ByteArrayInputStream isBm1 = new ByteArrayInputStream(b);
        bitmap = BitmapFactory.decodeStream(isBm1, null, newOpts);
        return bitmap;
    }

    /**
     * 将图片从sd中读取出来,返回的图片大小按照 120*1280计算
     *
     * @param filePath 图片文件的file的地址 ;complete path name for the file to be decoded
     * @return
     */
    public static Bitmap compFilePathToBitmap(String filePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了 表示只读边不读内容
        option.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeFile(filePath, option);
        option.inJustDecodeBounds = false;
        int w = option.outWidth;
        int h = option.outHeight;
        Log.i(TAG, "图片的宽:" + w + "图片的高" + h);
        // 现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        int ww = 720;
        int hh = 1280;
        int be = mathSampleSize(w, h, ww, hh);
        Log.i(TAG, "缩放比例" + be);
        option.inSampleSize = be;// 设置缩放比例
        option.inPurgeable = true;
        option.inInputShareable = true;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        b = BitmapFactory.decodeFile(filePath, option);
        return b;
    }

    /**
     * 将图片从sd中读取出来,返回的图片大小按照传递的大小计算
     *
     * @param filePath 图片文件的file的地址 (complete path name for the file to be decoded)
     * @param ww       最大的宽度
     * @param hh       最大的高度
     * @return 处理后的bitmap
     */
    public static Bitmap compFilePathToBitmap(String filePath, int ww, int hh) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了 表示只读边不读内容
        option.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeFile(filePath, option);
        option.inJustDecodeBounds = false;
        int w = option.outWidth;
        int h = option.outHeight;
        Log.i(TAG, "图片的宽:" + w + "图片的高" + h);
        int be = mathSampleSize(w, h, ww, hh);
        Log.i(TAG, "缩放比例" + be);
        option.inSampleSize = be;// 设置缩放比例
        option.inPurgeable = true;
        option.inInputShareable = true;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        b = BitmapFactory.decodeFile(filePath, option);
        return b;
    }

    /**
     * 图片的缩放比例
     *
     * @param oriW 图片的原始宽度
     * @param oriH 图片的原始高度
     * @param w    指定的宽度
     * @param h    指定的高度
     * @return 最大的比例
     */
    private static int mathSampleSize(int oriW, int oriH, int w, int h) {
        double i = oriW * 1.0 / w;
        double j = oriH * 1.0 / h;
        double z = i > j ? i : j;
        return getPowerOfTwoForSampleRatio(z);
    }

//    /**
//     * 根据图片的content Uri 获取图片的本地路径
//     *
//     * @param uri 图片的URI
//     * @return 图片的真实路径
//     */
//    public static String getImagePathFromURI(Uri uri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = MyApplication.getInstance().getApplicationContext().getContentResolver().query(uri, proj, null, null, null);
//        if (cursor == null)
//            return "";
//        int actual_image_column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(actual_image_column_index);
//    }


    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }


    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


    /**
     * 显示图片
     *
     * @param imageUrl   图片的路径(网络，本地)
     * @param imageAware 用来显示图片的View
     */
    public static void displayImage(String imageUrl, ImageView imageAware) {
        displayImage(imageUrl, imageAware, false);//默认加载不再进行质量压缩
    }

    /**
     * 显示图片
     *
     * @param imageUrl   图片的路径(网络，本地)
     * @param imageAware 用来显示图片的View
     * @param ignore     是否忽略判断wifi
     */
    public static void displayImage(String imageUrl, ImageView imageAware, boolean ignore) {
        displayImage(imageUrl, imageAware, COMPRESS.MQUALITY, 100, ignore);
    }

    /**
     * @param imageUrl   图片的路径(网络，本地)
     * @param imageAware 用来显示图片的View
     * @param compress   压缩等级
     * @param zoom       再次进行质量压缩(取值0-100)
     */
    public static void displayImage(String imageUrl, ImageView imageAware, COMPRESS compress, int zoom) {
        displayImage(imageUrl, imageAware, compress, zoom, false);
    }

    /**
     * @param imageUrl   图片的路径(网络，本地)
     * @param imageAware 用来显示图片的View
     * @param ignore     是否忽略判断wifi
     * @param compress   压缩等级
     * @param zoom       再次进行质量压缩(取值0-100)
     */
    public static void displayImage(String imageUrl, ImageView imageAware, COMPRESS compress, int zoom, boolean ignore) {
        displayImage(imageUrl, imageAware, null, compress, zoom, ignore);
    }

    /**
     * @param imageUrl   图片的路径(网络，本地)
     * @param imageAware 用来显示图片的View
     * @param listener   图片加载的监听器
     */
    public static void displayImage(String imageUrl, ImageView imageAware, ImageLoadingListener listener) {
        displayImage(imageUrl, imageAware, listener, false);
    }

    /**
     * @param imageUrl   图片的路径(网络，本地)
     * @param imageAware 用来显示图片的View
     * @param ignore     是否忽略判断wifi
     * @param listener   图片加载的监听器
     */
    public static void displayImage(String imageUrl, ImageView imageAware, ImageLoadingListener listener, boolean ignore) {
        displayImage(imageUrl, imageAware, listener, COMPRESS.MQUALITY, 100, ignore);
    }

    /**
     * @param imageUrl        图片的路径(网络，本地)
     * @param imageAware      用来显示图片的View
     * @param loadingListener 图片显示的监听
     * @param compress        压缩等级
     * @param zoom            再次进行质量压缩(取值0-100)
     */
    public static void displayImage(String imageUrl, ImageView imageAware, ImageLoadingListener loadingListener, COMPRESS compress, int zoom) {
        displayImage(imageUrl, imageAware, loadingListener, compress, zoom, false);
    }


    /**
     * @param imageUrl        图片的路径(网络，本地)
     * @param imageAware      用来显示的View
     * @param loadingListener 图片显示的监听
     * @param compress        压缩等级
     * @param zoom            再次进行质量压缩(取值0-100)
     * @param ignore          是否忽略判断wifi
     */

    static Map debugHash = new Hashtable<String, Integer>();

    public static void displayImage(String imageUrl, ImageView imageAware, ImageLoadingListener loadingListener, final COMPRESS compress, final int zoom, boolean ignore) {
        String loadUrl = imageUrl;
        if (!ignore)
            checkImage:
                    {
                        if (CommonUtils.isNetWorkConnected(MyApplication.getInstance()) && !CommonUtils.isWifiConnected(MyApplication.getInstance()) && isWifiLoadImage()) {
                            loadUrl = null;
                            //从内存中去找这个url有没有缓存，有缓存就去加载
                            String memoryKey = generateKey(imageUrl, (ImageAware) (new ImageViewAware(imageAware)));
                            Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(memoryKey);
                            if (bitmap != null) {
                                bitmap = null;//将bitmap释放掉
                                loadUrl = imageUrl;
                                break checkImage;
                            }

                            File image = ImageLoader.getInstance().getDiskCache().get(imageUrl);
                            boolean isImageCachedOnDisk = image != null && image.exists();
                            image = null;//将image释放掉
                            if (isImageCachedOnDisk) {
                                loadUrl = imageUrl;
                                break checkImage;
                            }
                        }
                    }


       // if( imageAware.isShown()) {
            ImageLoader.getInstance().displayImage(loadUrl, imageAware, optionsBuilder.preProcessor(new BitmapProcessor() {
                @Override
                public Bitmap process(Bitmap bitmap) {
                    /**
                     * 根据传入的图片压缩等级，以及倍数进行质量压缩
                     */
                    return bitmap != null ? compressBitmapSizeByScale(bitmap, compress.getLevel() * zoom / 100) : null;
                }
            }).build(), loadingListener);

//           Integer counter = (Integer) debugHash.get(loadUrl);
//            if(counter==null) counter=0;
//           debugHash.put(loadUrl, ++counter);
//            MyDebugUtils.d("img", loadUrl +" "+counter);



//        }else{
//            try{
//                throw new Exception("image is Not Visible, skip to load.");
//            }catch (Exception e){
//                MyDebugUtils.d("img", loadUrl,e );
//            }
//
//        }
    }


    public static void setListenerOnShow(final String url, final ImageView image){
        setListenerOnShow(url, image, COMPRESS.MQUALITY, 100);
    }

    public static void setListenerOnShow(final String url, final ImageView image, final COMPRESS compress, final int zoom){
      image.postDelayed(new Runnable() {
          @Override
          public void run() {
              if(image.isShown())
                displayImage(url, image, compress, zoom);
          }
      }, 100);


    }

    /**
     * 质量压缩
     *
     * @param image 要压缩的图片
     * @param size  图片大小
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image, long size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        do {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 5;// 每次都减少5
        } while (baos.toByteArray().length > size && options >= 5);// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        Bitmap temp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
        baos.reset();
        return temp;
    }

    /**
     * 尺寸压缩
     * @param image 要压缩的图片
     * @param size  图片大小
     * @return
     */
    public static Bitmap compressBitmapSize(Bitmap image, long size){
        //获得当前bitmap的宽高
        int width = image.getWidth();
        int height = image.getHeight();
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int inSampleSize=1;
        Log.i(TAG,"原始bitmap的宽度:"+width);
        Log.i(TAG,"原始bitmap的高度:"+height);
        Log.i(TAG,"目标图片的大小:"+size);
        do{
            width/=inSampleSize;
            height/=inSampleSize;
            inSampleSize++;
        }while (width*height*2>size);
        Log.i(TAG,"压缩后图片的大小:"+width*height*2);
//        MyDebugUtils.i(TAG, "缩放比例" + inSampleSize);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = inSampleSize>1?inSampleSize-1:1;// 设置缩放比例
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        byte[] datas=BitmapToBytes(image);
        image.recycle();
        return BitmapFactory.decodeByteArray(datas,0,datas.length,options);
    }

    /**
     * 尺寸压缩
     * @param image 要压缩的图片
     * @param size  图片大小
     * @return
     */
    public static Bitmap compressBitmapSizeByScale(Bitmap image, long size){
        //获得当前bitmap的宽高
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int width=imageWidth;

        float imgaeRatio= (imageHeight/(imageWidth*1.0f));//宽高比
        for(;width>0 && width*(width*imgaeRatio)*2>size;width--);//跳出循环的width就是所需要的width
        float scale=width*1f/imageWidth;//缩放比

        Matrix matrix=new Matrix();
        // 缩放图片动作 缩放比例
        matrix.postScale(scale,scale);
        // 创建一个新的Bitmap 从原始图像剪切图像
        Bitmap tempBitmap = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix,true);
        return tempBitmap;
    }

    /**
     * 判断是否开启了wifi下加载图片
     *
     * @return
     */
    public static boolean isWifiLoadImage() {
      //  SharedPreferences imagePrefrences = MyApplication.getInstance().getImageOptionsSharedPreferences();
       // return imagePrefrences.getBoolean("wifiLoadImage", false);
        return MyApplication.getInstance().isOnlyWifiLoadImage();
    }


    /**
     * 上下文,因为没有给configuration设置memoryCacheExtraOptions,所以可以调用者方法生成key
     *
     * @return
     */
    static ImageSize getMaxImageSize() {
        DisplayMetrics displayMetrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new ImageSize(width, height);
    }

    /**
     *
     */
    static String generateKey(String imageUrl, ImageAware imageAware) {
        ImageSize imageSize = ImageSizeUtils.defineTargetSizeForView(imageAware, getMaxImageSize());
        return MemoryCacheUtils.generateKey(imageUrl, imageSize);
    }


}
