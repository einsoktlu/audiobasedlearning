package com.tlu.audiobasedlearning

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.media3.common.MimeTypes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.alansdk.button.AlanButton
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.Serializable
import java.net.URI

class LibraryActivity : AppCompatActivity() {
    private val getContent = registerForActivityResult(CustomContract()) { uri: Uri? ->
        // handle uri
        if (uri == null) return@registerForActivityResult

        val filename = Helpers.resolveFileNameFromUri(contentResolver, uri)
        val fileNameWithUri = FilenameWithUri(filename!!, uri.toString())
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        writeToLibraryFile(fileNameWithUri)
        val fileObjects = readLibraryFile()

        recyclerView.adapter = CustomAdapter(fileObjects)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var libraryFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        ActivityBase.currentActivity = this

        libraryFile = File(applicationContext.filesDir, getString(R.string.library_list_filename))

        recyclerView = findViewById(R.id.reclist)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (libraryFile.exists()) {
            updateLibraryFile()
            recyclerView.adapter = CustomAdapter(readLibraryFile())
        }

        findViewById<Button>(R.id.openFileButton).setOnClickListener {
            getContent.launch(arrayOf(MimeTypes.AUDIO_MPEG))
        }

        findViewById<Button>(R.id.clearLibraryButton).setOnClickListener {
            if (libraryFile.exists()) libraryFile.delete()
            recyclerView.adapter = null
            Toast.makeText(it.context, "Library cleared!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRestart() {
        super.onRestart()
        ActivityBase.currentActivity = this
        val mainAlanButton: AlanButton = ActivityBase.mainActivity.findViewById(R.id.alan_button)
        AlanAI.setVisualState(mainAlanButton, getString(R.string.library_screen))
    }

    private fun readLibraryFile(): ArrayList<FilenameWithUri> {
        val jsonStrings = libraryFile.bufferedReader().useLines { lines ->
            lines.toList().toTypedArray()
        }

        val fileObjects: ArrayList<FilenameWithUri> = ArrayList()
        val mapper = jacksonObjectMapper()

        jsonStrings.forEach { json ->
            fileObjects.add(mapper.readValue(json, FilenameWithUri::class.java))
        }

        Log.d("LibraryFile", "Read: $fileObjects")

        return fileObjects
    }

    private fun updateLibraryFile() {
        val jsonStrings = libraryFile.bufferedReader().useLines { lines ->
            lines.toList().toTypedArray()
        }

        Log.d("LibraryFile", "updateLibraryFile()")


        val fileObjects: ArrayList<FilenameWithUri> = ArrayList()
        val mapper = jacksonObjectMapper()

        jsonStrings.forEach { json ->
            Log.d("LibraryFile", "jsonStrings: $json")
            val fileObject = mapper.readValue(json, FilenameWithUri::class.java)

            Log.d("LibraryFile", "fileObject: $fileObject")

            if (checkIfContentUriExists(Uri.parse(fileObject.fileUri))) fileObjects.add(fileObject)
        }

        libraryFile.delete()
        libraryFile.createNewFile()
        fileObjects.forEach { fileObject ->
            val json = mapper.writeValueAsString(fileObject)
            libraryFile.appendBytes("$json\n".toByteArray())
        }
    }

    private fun writeToLibraryFile(fileObject: FilenameWithUri) {
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(fileObject)
        Log.d("LibraryFile", "Write: $json")

        libraryFile.appendBytes("$json\n".toByteArray())
    }

    private fun checkIfContentUriExists(uri: Uri): Boolean {
        val cur = contentResolver.query(uri, null, null, null, null)
        val result = cur?.moveToFirst() ?: false

        cur?.close()

        return result
    }
}

class CustomAdapter(private val dataSet: ArrayList<FilenameWithUri>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.textViewRowItem)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].fileName

        viewHolder.textView.setOnClickListener {
            val intent = Intent(it.context, MediaPlayerActivity::class.java)
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.data = Uri.parse(dataSet[position].fileUri)

            AlanAI.setVisualState(ActivityBase.mainActivity.findViewById(R.id.alan_button),
                ActivityBase.currentActivity.getString(R.string.mediaplayer_screen))

            ActivityBase.currentActivity.startActivity(intent)
            //ActivityBase.currentActivity.finish()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}

data class FilenameWithUri(val fileName: String, val fileUri: String)

class CustomContract : ActivityResultContracts.OpenDocument() {
    override fun createIntent(context: Context, input: Array<String>): Intent {
        val intent = super.createIntent(context, input)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        return intent
    }
}