package com.mikyou.pretty.ui.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView

/**
 * Class Name : PrettyImageView
 * User Name: mikyou
 * Date: create on 2018/1/24.
 * Support Function:
 *                         1、支持圆角矩形图片，圆形图片
 *                         2、支持border的显示以及宽度和颜色的定制化
 *                         3、支持圆角矩形四个角的角度任意定制化,包括四个角，每个角的X,Y方向上大小定制化
 *                         4、支持圆形右上角圆点定制化
 *
 */
class PrettyImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defAttrStyle: Int = 0)
	: ImageView(context, attributeSet, defAttrStyle) {

	enum class ShapeType {
		SHAPE_CIRCLE,
		SHAPE_ROUND
	}

	//defAttr var
	private var mShapeType: ShapeType = ShapeType.SHAPE_CIRCLE
		set(value) {
			field = value
			invalidate()
		}
	private var mBorderWidth: Float = 20f
		set(value) {
			field = value
			invalidate()
		}
	private var mBorderColor: Int = Color.parseColor("#ff9900")
		set(value) {
			field = value
			invalidate()
		}

	private var mLeftTopRadiusX: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mLeftTopRadiusY: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mRightTopRadiusX: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mRightTopRadiusY: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mLeftBottomRadiusX: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mLeftBottomRadiusY: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mRightBottomRadiusX: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mRightBottomRadiusY: Float = 0f
		set(value) {
			field = value
			invalidate()
		}
	private var mShowBorder: Boolean = true
		set(value) {
			field = value
			invalidate()
		}
	private var mShowCircleDot: Boolean = false
		set(value) {
			field = value
			invalidate()
		}
	private var mCircleDotColor: Int = Color.RED
		set(value) {
			field = value
			invalidate()
		}

	private var mCircleDotRadius: Float = 20f
		set(value) {
			field = value
			invalidate()
		}

	//drawTools var
	private lateinit var mShapePath: Path
	private lateinit var mBorderPath: Path
	private lateinit var mBitmapPaint: Paint
	private lateinit var mBorderPaint: Paint
	private lateinit var mCircleDotPaint: Paint
	private lateinit var mMatrix: Matrix

	//temp var
	private var mWidth: Int = 200//View的宽度
	private var mHeight: Int = 200//View的高度
	private var mRadius: Float = 100f//圆的半径

	init {
		initAttrs(context, attributeSet, defAttrStyle)//获取自定义属性的值
		initDrawTools()//初始化绘制工具
	}

	private fun initAttrs(context: Context, attributeSet: AttributeSet?, defAttrStyle: Int) {
		val array = context.obtainStyledAttributes(attributeSet, R.styleable.PrettyImageView, defAttrStyle, 0)
		(0..array.indexCount)
				.asSequence()
				.map { array.getIndex(it) }
				.forEach {
					when (it) {
						R.styleable.PrettyImageView_shape_type ->
							mShapeType = when {
								array.getInt(it, 0) == 0 -> ShapeType.SHAPE_CIRCLE
								array.getInt(it, 0) == 1 -> ShapeType.SHAPE_ROUND
								else -> ShapeType.SHAPE_CIRCLE
							}
						R.styleable.PrettyImageView_border_width ->
							mBorderWidth = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics))
						R.styleable.PrettyImageView_border_color ->
							mBorderColor = array.getColor(it, Color.parseColor("#ff0000"))
						R.styleable.PrettyImageView_left_top_radiusX ->
							mLeftTopRadiusX = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_left_top_radiusY ->
							mLeftTopRadiusY = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_left_bottom_radiusX ->
							mLeftBottomRadiusX = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_left_bottom_radiusY ->
							mLeftBottomRadiusY = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_right_bottom_radiusX ->
							mRightBottomRadiusX = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_right_bottom_radiusY ->
							mRightBottomRadiusY = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_right_top_radiusX ->
							mRightTopRadiusX = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_right_top_radiusY ->
							mRightTopRadiusY = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
						R.styleable.PrettyImageView_show_border ->
							mShowBorder = array.getBoolean(it, false)
						R.styleable.PrettyImageView_show_circle_dot ->
							mShowCircleDot = array.getBoolean(it, false)
						R.styleable.PrettyImageView_circle_dot_color ->
							mCircleDotColor = array.getColor(it, Color.parseColor("#ff0000"))
						R.styleable.PrettyImageView_circle_dot_radius ->
							mCircleDotRadius = array.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics))
					}
				}
		array.recycle()
	}

	private fun initDrawTools() {
		mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			//最终绘制图片的画笔,需要设置BitmapShader着色器，从而实现把图片绘制在不同形状图形上
			style = Paint.Style.FILL
		}
		mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			//绘制边框画笔
			style = Paint.Style.STROKE
			color = mBorderColor
			strokeCap = Paint.Cap.ROUND
			strokeWidth = mBorderWidth
		}
		mCircleDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			//绘制右上角圆点画笔
			style = Paint.Style.FILL
			color = mCircleDotColor
		}
		mShapePath = Path()//描述形状轮廓的path路径
		mBorderPath = Path()//描述图片边框轮廓的path路径
		mMatrix = Matrix()//用于缩放图片的矩阵
		scaleType = ScaleType.CENTER_CROP
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {//View的测量
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		if (mShapeType == ShapeType.SHAPE_CIRCLE) {
			mWidth = Math.min(measuredWidth, measuredHeight)
			mRadius = mWidth / 2.0f
			setMeasuredDimension(mWidth, mWidth)
		} else {
			mWidth = measuredWidth
			mHeight = measuredHeight
			setMeasuredDimension(mWidth, mHeight)
		}
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {//确定了最终View的尺寸
		super.onSizeChanged(w, h, oldw, oldh)
		mBorderPath.reset()
		mShapePath.reset()
		when (mShapeType) {
			ShapeType.SHAPE_ROUND -> {
				mWidth = w
				mHeight = h
				buildRoundPath()
			}
			ShapeType.SHAPE_CIRCLE -> {
				buildCirclePath()
			}
		}
	}

	private fun buildCirclePath() {//构建圆形类型的Path路径
		if (!mShowBorder) {//绘制不带边框的圆形实际上只需要把一个圆形扔进path即可
			mShapePath.addCircle(mRadius, mRadius, mRadius, Path.Direction.CW)
		} else {//绘制带边框的圆形需要把内部圆形和外部圆形边框都要扔进path
			mShapePath.addCircle(mRadius, mRadius, mRadius - mBorderWidth, Path.Direction.CW)
			mBorderPath.addCircle(mRadius, mRadius, mRadius - mBorderWidth / 2.0f, Path.Direction.CW)
		}
	}

	private fun buildRoundPath() {//构建圆角类型的Path路径
		if (!mShowBorder) {//绘制不带边框的圆角实际上只需要把一个圆角矩形扔进path即可
			floatArrayOf(mLeftTopRadiusX, mLeftTopRadiusY,
					mRightTopRadiusX, mRightTopRadiusY,
					mRightBottomRadiusX, mRightBottomRadiusY,
					mLeftBottomRadiusX, mLeftBottomRadiusY).run {
				mShapePath.addRoundRect(RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat()), this, Path.Direction.CW)
			}

		} else {//绘制带边框的圆角实际上只需要把一个圆角矩形和一个圆角矩形的变量都扔进path即可
			floatArrayOf(mLeftTopRadiusX - mBorderWidth / 2.0f, mLeftTopRadiusY - mBorderWidth / 2.0f,
					mRightTopRadiusX - mBorderWidth / 2.0f, mRightTopRadiusY - mBorderWidth / 2.0f,
					mRightBottomRadiusX - mBorderWidth / 2.0f, mRightBottomRadiusY - mBorderWidth / 2.0f,
					mLeftBottomRadiusX - mBorderWidth / 2.0f, mLeftBottomRadiusY - mBorderWidth / 2.0f).run {
				mBorderPath.addRoundRect(RectF(mBorderWidth / 2.0f, mBorderWidth / 2.0f, mWidth.toFloat() - mBorderWidth / 2.0f, mHeight.toFloat() - mBorderWidth / 2.0f), this, Path.Direction.CW)
			}

			floatArrayOf(mLeftTopRadiusX - mBorderWidth, mLeftTopRadiusY - mBorderWidth,
					mRightTopRadiusX - mBorderWidth, mRightTopRadiusY - mBorderWidth,
					mRightBottomRadiusX - mBorderWidth, mRightBottomRadiusY - mBorderWidth,
					mLeftBottomRadiusX - mBorderWidth, mLeftBottomRadiusY - mBorderWidth).run {
				mShapePath.addRoundRect(RectF(mBorderWidth, mBorderWidth, mWidth.toFloat() - mBorderWidth, mHeight.toFloat() - mBorderWidth),
						this, Path.Direction.CW)
			}

		}
	}

	override fun onDraw(canvas: Canvas?) {//由于经过以上根据不同逻辑构建了boderPath和shapePath,path中已经储存相应的形状，现在只需要把相应shapePath中形状用带BitmapShader画笔绘制出来,boderPath用普通画笔绘制出来即可
		drawable ?: return
		mBitmapPaint.shader = getBitmapShader()//获得相应的BitmapShader着色器对象
		when (mShapeType) {
			ShapeType.SHAPE_CIRCLE -> {
				if (mShowBorder) {
					canvas?.drawPath(mBorderPath, mBorderPaint)//绘制圆形图片边框path
				}
				canvas?.drawPath(mShapePath, mBitmapPaint)//绘制圆形图片形状path
				if (mShowCircleDot) {
					drawCircleDot(canvas)//绘制圆形图片右上角圆点
				}
			}
			ShapeType.SHAPE_ROUND -> {
				if (mShowBorder) {
					canvas?.drawPath(mBorderPath, mBorderPaint)//绘制圆角图片边框path
				}
				canvas?.drawPath(mShapePath, mBitmapPaint)//绘制圆角图片形状path
			}
		}

	}

	private fun drawCircleDot(canvas: Canvas?) {
		canvas?.run {
			drawCircle((mRadius + mRadius * (Math.sqrt(2.0) / 2.0f)).toFloat(), (mRadius - mRadius * (Math.sqrt(2.0) / 2.0f)).toFloat(), mCircleDotRadius, mCircleDotPaint)
		}
	}

	private fun getBitmapShader(): BitmapShader {
		val bitmap = drawableToBitmap(drawable)
		return BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
			var scale = 1.0f
			if (mShapeType == ShapeType.SHAPE_CIRCLE) {
				scale = (mWidth * 1.0f / Math.min(bitmap.width, bitmap.height))
			} else if (mShapeType == ShapeType.SHAPE_ROUND) {
				// 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
				if (!(width == bitmap.width && width == bitmap.height)) {
					scale = Math.max(width * 1.0f / bitmap.width, height * 1.0f / bitmap.height)
				}
			}
			// shader的变换矩阵，我们这里主要用于放大或者缩小
			mMatrix.setScale(scale, scale)
			setLocalMatrix(mMatrix)
		}
	}

	private fun drawableToBitmap(drawable: Drawable): Bitmap {
		if (drawable is BitmapDrawable) {
			return drawable.bitmap
		}
		return Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
			drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
			drawable.draw(Canvas(this@apply))
		}
	}

	companion object {
		private const val STATE_INSTANCE = "state_instance"
		private const val STATE_INSTANCE_SHAPE_TYPE = "state_shape_type"
		private const val STATE_INSTANCE_BORDER_WIDTH = "state_border_width"
		private const val STATE_INSTANCE_BORDER_COLOR = "state_border_color"
		private const val STATE_INSTANCE_RADIUS_LEFT_TOP_X = "state_radius_left_top_x"
		private const val STATE_INSTANCE_RADIUS_LEFT_TOP_Y = "state_radius_left_top_y"
		private const val STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X = "state_radius_left_bottom_x"
		private const val STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y = "state_radius_left_bottom_y"
		private const val STATE_INSTANCE_RADIUS_RIGHT_TOP_X = "state_radius_right_top_x"
		private const val STATE_INSTANCE_RADIUS_RIGHT_TOP_Y = "state_radius_right_top_y"
		private const val STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X = "state_radius_right_bottom_x"
		private const val STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y = "state_radius_right_bottom_y"
		private const val STATE_INSTANCE_RADIUS = "state_radius"
		private const val STATE_INSTANCE_SHOW_BORDER = "state_radius_show_border"
	}

	//View State Save
	override fun onSaveInstanceState(): Parcelable = Bundle().apply {
		putParcelable(STATE_INSTANCE, super.onSaveInstanceState())
		putInt(STATE_INSTANCE_SHAPE_TYPE, when (mShapeType) {
			ShapeType.SHAPE_CIRCLE -> 0
			ShapeType.SHAPE_ROUND -> 1
		})
		putFloat(STATE_INSTANCE_BORDER_WIDTH, mBorderWidth)
		putInt(STATE_INSTANCE_BORDER_COLOR, mBorderColor)
		putFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_X, mLeftTopRadiusX)
		putFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_Y, mLeftTopRadiusY)
		putFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X, mLeftBottomRadiusX)
		putFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y, mLeftBottomRadiusY)
		putFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_X, mRightTopRadiusX)
		putFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_Y, mRightTopRadiusY)
		putFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X, mRightBottomRadiusX)
		putFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y, mRightBottomRadiusY)
		putFloat(STATE_INSTANCE_RADIUS, mRadius)
		putBoolean(STATE_INSTANCE_SHOW_BORDER, mShowBorder)
	}

	//View State Restore
	override fun onRestoreInstanceState(state: Parcelable?) {
		if (state !is Bundle) {
			super.onRestoreInstanceState(state)
			return
		}

		with(state) {
			super.onRestoreInstanceState(getParcelable(STATE_INSTANCE))
			mShapeType = when {
				getInt(STATE_INSTANCE_SHAPE_TYPE) == 0 -> ShapeType.SHAPE_CIRCLE
				getInt(STATE_INSTANCE_SHAPE_TYPE) == 1 -> ShapeType.SHAPE_ROUND
				else -> ShapeType.SHAPE_CIRCLE
			}
			mBorderWidth = getFloat(STATE_INSTANCE_BORDER_WIDTH)
			mBorderColor = getInt(STATE_INSTANCE_BORDER_COLOR)
			mLeftTopRadiusX = getFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_X)
			mLeftTopRadiusY = getFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_Y)
			mLeftBottomRadiusX = getFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X)
			mLeftBottomRadiusY = getFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y)
			mRightTopRadiusX = getFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_X)
			mRightTopRadiusY = getFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_Y)
			mRightBottomRadiusX = getFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X)
			mRightBottomRadiusY = getFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y)
			mRadius = getFloat(STATE_INSTANCE_RADIUS)
			mShowBorder = getBoolean(STATE_INSTANCE_SHOW_BORDER)
		}
	}
}