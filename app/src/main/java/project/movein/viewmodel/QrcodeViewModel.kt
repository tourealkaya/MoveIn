package project.movein.viewmodel
import android.app.Application
import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import java.util.concurrent.ExecutionException
import javax.inject.Inject


class QrcodeViewModel (application: Application):AndroidViewModel(application){
    private val cameraProviderLiveData: MutableLiveData<ProcessCameraProvider> = MutableLiveData()
    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())
            cameraProviderFuture.addListener(
                {
                    cameraProviderLiveData.setValue(cameraProviderFuture.get())
                },
                ContextCompat.getMainExecutor(getApplication())
            )
            return cameraProviderLiveData
        }
}



