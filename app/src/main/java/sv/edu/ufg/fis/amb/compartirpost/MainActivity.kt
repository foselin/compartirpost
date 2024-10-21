package sv.edu.ufg.fis.amb.compartirpost



import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnTakePhoto: Button
    private lateinit var btnSavePhoto: Button
    private lateinit var btnSharePhoto: Button
    private lateinit var btnRetakePhoto: Button

    private var photoUri: Uri? = null
    private var photoBitmap: Bitmap? = null
    private var isPhotoSaved = false

    // Registro para permisos de almacenamiento
    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) savePhotoToGallery()
        else Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
    }

    // Registro para permisos de cámara
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openCamera()
        else Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    // Registro para tomar una foto
    private val takePictureLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                photoBitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
                imageView.setImageBitmap(photoBitmap)
                isPhotoSaved = false // La foto aún no se ha guardado
            } else {
                Toast.makeText(this, "Error al capturar la foto", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        btnTakePhoto = findViewById(R.id.btn_take_photo)
        btnSavePhoto = findViewById(R.id.btn_save_photo)
        btnSharePhoto = findViewById(R.id.btn_share_photo)
        btnRetakePhoto = findViewById(R.id.btn_retake_photo)

        btnTakePhoto.setOnClickListener { requestCameraPermission() }
        btnSavePhoto.setOnClickListener { requestStoragePermission() }
        btnSharePhoto.setOnClickListener { sharePhoto() }
        btnRetakePhoto.setOnClickListener { imageView.setImageResource(0) }
    }

    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun requestStoragePermission() {
        storagePermissionLauncher.launch(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun openCamera() {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File.createTempFile("IMG_$timestamp", ".jpg", cacheDir)
        photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)

        takePictureLauncher.launch(photoUri)
    }

    private fun savePhotoToGallery() {
        if (photoBitmap != null) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "CapturedPhoto_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraDemo")
            }

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    photoBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    Toast.makeText(this, "Foto guardada en galería", Toast.LENGTH_SHORT).show()
                    isPhotoSaved = true // Marcamos la foto como guardada
                }
            } else {
                Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No hay foto para guardar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharePhoto() {
        if (isPhotoSaved && photoUri != null) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, photoUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Permiso para compartir
            }

            // Verificamos que haya apps disponibles para compartir
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(shareIntent, "Compartir imagen"))
            } else {
                Toast.makeText(this, "No hay aplicaciones disponibles para compartir", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Primero guarda la foto antes de compartir", Toast.LENGTH_SHORT).show()
        }
    }
}
