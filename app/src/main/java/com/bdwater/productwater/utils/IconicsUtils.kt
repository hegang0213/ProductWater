package com.bdwater.productwater.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import com.bdwater.productwater.R
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon

/**
 * Created by hegang on 2018/7/4.
 */
object IconicsUtil {
    fun getSmallIcon(context: Context, icon: IIcon): Drawable {
        return getIcon(context, icon, context.resources.getDimension(R.dimen.icon_size).toInt())
    }

    fun getNormalIcon(context: Context, icon: IIcon): Drawable {
        return getIcon(context, icon, context.resources.getDimension(R.dimen.icon_size_normal).toInt(), 2)
    }

    fun getBigIcon(context: Context, icon: IIcon): Drawable {
        return getIcon(context, icon, context.resources.getDimension(R.dimen.icon_size_big).toInt())
    }

    @JvmOverloads
    fun getIcon(context: Context, icon: IIcon, sizeDp: Int, paddingDp: Int = 0, color: Int = -9999): Drawable {
        var drawable = IconicsDrawable(context, icon)
        if (sizeDp != -1)
            drawable = drawable.sizeDp(sizeDp)
        drawable = drawable.paddingDp(paddingDp)
        if (color != -9999)
            drawable = drawable.color(color)
        return drawable
    }

    fun getBitmap(context: Context, icon: IIcon, sizeDp: Int, @ColorInt color: Int): Bitmap {
        val drawable = getNormalIcon(context, icon)
        (drawable as IconicsDrawable).color(color)
        val bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight())
        drawable.draw(canvas)

        val width = bitmap.width
        val height = bitmap.height
        // 计算缩放比例.
        val scaleWidth = sizeDp.toFloat() / width
        val scaleHeight = sizeDp.toFloat() / height
        // 取得想要缩放的matrix参数.
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }
}

