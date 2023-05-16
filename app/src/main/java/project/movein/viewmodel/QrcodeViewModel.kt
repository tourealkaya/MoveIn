package project.movein.viewmodel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.Socket

class QrcodeViewModel : ViewModel() {

    private val TAG = "QrcodeViewModel"
    private val serverAddress = "192.168.39.203"
    private val serverPort = 8080
    private val _barcodeText = MutableLiveData<String>()
    val barcodeText: LiveData<String> = _barcodeText

    fun setBarcodeText(text: String) {
        _barcodeText.value = text
    }

    fun sendData(
        message: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                val socket = Socket(serverAddress, serverPort)
                val dataOutputStream = DataOutputStream(socket.getOutputStream())
                dataOutputStream.writeUTF(message)
                dataOutputStream.flush()
                Log.d(TAG, "Data sent to server: $message")

                val bufferSize = 1024 // taille du buffer pour stocker la réponse
                val buffer = ByteArray(bufferSize)
                val inputStream = socket.getInputStream()
                var bytesRead: Int

                // Lire la réponse byte par byte dans un tableau de bytes
                bytesRead = inputStream.read(buffer)
                val response = String(buffer, 0, bytesRead)

                // Traiter la réponse du serveur
                Log.d(TAG, "Received response from server: $response")
                onSuccess(response)

                socket.close()

            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }.start()
    }


}

/*fun sendData(message: String, onSuccess: () -> Unit, onError: (String?) -> Unit) {
    Thread {
        try {
            val socket = Socket(serverAddress, serverPort)
            val dataOutputStream = DataOutputStream(socket.getOutputStream())
            dataOutputStream.writeUTF(message)
            dataOutputStream.flush()
            Log.d(TAG, "Data sent to server: $message")

            // Lire les données en temps réel à partir du serveur
            val bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val response = bufferedReader.readLine()

            // Traiter la réponse du serveur
            Log.d(TAG, "Received response from server: $response")
            // Mettre à jour la valeur LiveData avec la réponse du serveur
            setBarcodeText(response)

            socket.close()

            onSuccess()

        } catch (e: Exception) {
            val errorMessage = e.message
            Log.e(TAG, "Error: $errorMessage")
            onError(errorMessage)
        }
    }.start()
}*/