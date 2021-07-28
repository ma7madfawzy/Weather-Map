package com.app.weather.presentation.splash

import android.graphics.Color
import com.app.weather.databinding.FragmentSplashBinding
import com.mikhaellopez.rxanimation.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.rxkotlin.addTo

class AnimationHandler(private val binding: FragmentSplashBinding) {
    private var disposable = CompositeDisposable()

    fun startSplashAnimation(onTerminateAction: Action) =
        RxAnimation.sequentially(
            RxAnimation.together(
                binding.imageViewBottomDrawable.translationY(500f),
                binding.imageViewEllipse.fadeOut(0L),
                binding.imageViewBottomDrawable.fadeOut(0L),
                binding.imageViewBigCloud.translationX(-500F, 0L),
                binding.imageViewSmallCloud.translationX(500f, 0L),
                binding.imageViewBottomDrawableShadow.translationY(500f),
                binding.imageViewMainCloud.fadeOut(0L),
                binding.imageViewBottomDrawableShadow.fadeOut(0L)
            ),

            RxAnimation.together(
                binding.imageViewBottomDrawable.fadeIn(1000L),
                binding.imageViewBottomDrawable.translationY(-1f),
                binding.imageViewBottomDrawableShadow.fadeIn(1250L),
                binding.imageViewBottomDrawableShadow.translationY(-1f)
            ),

            RxAnimation.together(
                binding.imageViewEllipse.fadeIn(1000L),
                binding.imageViewEllipse.translationY(-50F, 1000L)
            ),

            RxAnimation.together(
                binding.imageViewBigCloud.translationX(-15f, 1000L),
                binding.imageViewSmallCloud.translationX(25f, 1000L)
            ),

            binding.imageViewMainCloud.fadeIn(500L),
        ).doOnTerminate(onTerminateAction).subscribe().addTo(disposable)

    fun endSplashAnimation(onTerminateAction: Action) =
        RxAnimation.sequentially(
            RxAnimation.together(
                binding.imageViewBottomDrawable.fadeOut(300L),
                binding.imageViewBottomDrawable.translationY(100f),
                binding.imageViewBottomDrawableShadow.fadeOut(300L),
                binding.imageViewBottomDrawableShadow.translationY(100f)
            ),

            RxAnimation.together(
                binding.imageViewEllipse.fadeOut(300L),
                binding.imageViewEllipse.translationY(500F, 300L)
            ),

            RxAnimation.together(
                binding.imageViewBigCloud.translationX(500f, 300L),
                binding.imageViewSmallCloud.translationX(-500f, 300L)
            ),

            binding.imageViewMainCloud.fadeOut(300L),
            binding.rootView.backgroundColor(
                Color.parseColor("#5D50FE"),
                Color.parseColor("#FFFFFF"),
                duration = 750L
            )
        ).doOnTerminate(onTerminateAction).subscribe().addTo(disposable)

    fun destroy() {
        disposable.clear()
    }

}