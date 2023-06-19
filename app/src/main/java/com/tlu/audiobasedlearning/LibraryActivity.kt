package com.tlu.audiobasedlearning

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MimeTypes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.alansdk.button.AlanButton

class LibraryActivity : AppCompatActivity() {
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // handle uri
        val filename = Helpers.resolveFileNameFromUri(contentResolver, uri)
        recyclerView.adapter = CustomAdapter(arrayOf(filename!!))

        val intent = Intent(this@LibraryActivity, MediaPlayerActivity::class.java)
        intent.putExtra("file_uri", uri.toString())
        AlanAI.setVisualState(ActivityBase.mainActivity.findViewById(R.id.alan_button), getString(R.string.mediaplayer_screen))
        startActivity(intent)
        this@LibraryActivity.finish()
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        ActivityBase.currentActivity = this

        recyclerView = findViewById(R.id.reclist)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomAdapter(arrayOf("cocktails.mp3", "mojito.mp3", "viiul.mp3"))

        findViewById<Button>(R.id.openFileButton).setOnClickListener {
            getContent.launch(MimeTypes.AUDIO_MPEG)
        }
    }

    override fun onRestart() {
        super.onRestart()
        ActivityBase.currentActivity = this
        val mainAlanButton: AlanButton = ActivityBase.mainActivity.findViewById(R.id.alan_button)
        AlanAI.setVisualState(mainAlanButton, getString(R.string.library_screen))
    }
}

class CustomAdapter(private val dataSet: Array<String>) :
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
        viewHolder.textView.text = dataSet[position]

        viewHolder.textView.setOnClickListener {
            Toast.makeText(it.context, viewHolder.textView.text, Toast.LENGTH_LONG).show()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
