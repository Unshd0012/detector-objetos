package com.unshd.detectionsobjects.core.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetectionAnalyzer(private val overlay: ObjectOverlayView) : ImageAnalysis.Analyzer {
    private val detector: ObjectDetector = ObjectDetection.getClient(
        ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)
            detector.process(inputImage)
                .addOnSuccessListener { detectedObjects ->
                    if (detectedObjects.isNotEmpty()) {
                        Log.i("ObjectDetection", "Detected objects: ${detectedObjects.get(0).boundingBox}")
                        Log.i("ObjectDetection", "Detected objects: ${detectedObjects.get(0).labels}")
                        Log.i("ObjectDetection", "Detected objects: ${detectedObjects.get(0).trackingId}")

                    }
                  overlay.post {
                        overlay.updateObjects(detectedObjects, inputImage.width, inputImage.height, rotationDegrees)

                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ObjectDetection", "Detection error", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}