package project.movein.backend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.DataOutputStream
import java.net.Socket

class SendReceiveData {
    private val TAG = "BACKEND"
    private val serverAddress = "192.168.107.203"
    private val serverPort = 9997

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
                //  Log.d(TAG, "Data sent to server: $message")
                val bufferSize = 2048 // taille du buffer pour stocker la réponse
                val buffer = ByteArray(bufferSize)
                val inputStream = socket.getInputStream()
                // Lire la réponse byte par byte dans un tableau de bytes
                val bytesRead: Int = inputStream.read(buffer)
                val response = String(buffer, 0, bytesRead)
                // Traiter la réponse du serveur
                //    Log.d(TAG, "Received response from server: $response")
                onSuccess(response)
                socket.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }.start()
    }

}